package com.corrodinggames.rts.appFramework;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.corrodinggames.rts.R;
import com.corrodinggames.rts.game.a.class_263;
import com.corrodinggames.rts.game.class_313;
import com.corrodinggames.rts.game.class_324;
import com.corrodinggames.rts.game.units.custom.logicBooleans.VariableScope;
import com.corrodinggames.rts.gameFramework.class_1061;
import com.corrodinggames.rts.gameFramework.class_768;
import com.corrodinggames.rts.gameFramework.class_866;
import com.corrodinggames.rts.gameFramework.e.class_899;
import com.corrodinggames.rts.gameFramework.h.class_988;
import com.corrodinggames.rts.gameFramework.i.class_992;
import com.corrodinggames.rts.gameFramework.j.class_1001;
import com.corrodinggames.rts.gameFramework.m.class_1189;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public class LevelSelectActivity extends class_1 {
    public static final int LOADING_DIALOG = 0;
    public static class_1189 missingMapThumb;
    public static class_1189 toolargethumb;
    public Spinner aiCountSpinner;
    public Spinner aiDifficulty;
    public String currentMapPath;
    public boolean custom;
    public Spinner gameModeSpinner;
    public class_5 gameView;
    public String lastContextMenu;
    public ArrayList levelPaths;
    public ArrayList levelViews;
    public String[] levels;
    public Button modsImportMod;
    public ProgressDialog progressDialog;
    public boolean skirmish;
    public final Handler uiHandler = new Handler();
    public class_59 levelAdapter = new class_59(this);
    public boolean wasHidden = true;
    public Runnable fileAddedCallback = new class_55(this);
    public Handler refreshLevelsHandler = new Handler();
    public Runnable refreshLevelsRunnable = new class_47(this);

    public void showFileChooserForImport() {
        $invoke$special$showFileChooserForImport();
    }

    @Override // android.app.Activity
    public void finish() {
        super.finish();
        class_84.method_133(this, true);
    }

    @Override // com.corrodinggames.rts.appFramework.class_1, android.app.Activity
    public void onResume() {
        super.onResume();
        setup();
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if (class_1061VarMethod_3076 != null) {
            class_5 class_5VarMethod_108 = class_84.method_108(this, this.gameView);
            this.gameView = class_5VarMethod_108;
            class_1061VarMethod_3076.method_2997(this, class_5VarMethod_108, true);
        }
        class_84.method_115(this, true);
    }

    @Override // android.app.Activity
    public void onStart() {
        super.onStart();
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if (class_1061VarMethod_3076 != null) {
            class_1061VarMethod_3076.method_3001(this.gameView);
        }
    }

    @Override // android.app.Activity
    public void onStop() {
        this.wasHidden = true;
        super.onStop();
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if (class_1061VarMethod_3076 != null) {
            class_1061VarMethod_3076.method_2996(this, this.gameView);
        }
    }

    @Override // android.app.Activity
    public void onPause() {
        super.onPause();
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if (class_1061VarMethod_3076 != null) {
            class_1061VarMethod_3076.method_3030(this.gameView);
        }
    }

    public static String convertFilePathToFileName(String str) {
        if (str == null) {
            return null;
        }
        if (str.contains("/MOD|")) {
            return str.substring(str.indexOf("/MOD|"));
        }
        if (str.contains("/NEW_PATH|")) {
            return str.substring(str.indexOf("/NEW_PATH|"));
        }
        return str.split("/")[r2.length - 1];
    }

    public static boolean isAvailableInDemo(String str, String str2) {
        Matcher matcher = Pattern.compile(".*\\[(.*)\\].*").matcher(str);
        if (matcher.matches()) {
            if ((matcher.group(1).toLowerCase(Locale.ENGLISH) + "|").contains("demo|")) {
                return true;
            }
        }
        return class_899.method_2186(new StringBuilder().append(str2.replace(".tmx", VariableScope.nullOrMissingString)).append("_demo").toString());
    }

    public static String convertLevelFileNameForDisplay(String str) {
        return class_84.method_131(str);
    }

    public static boolean isCustomMapOrFolder(String str) {
        if (str != null && str.contains("/SD/")) {
            return true;
        }
        return false;
    }

    public void setup() {
        String string;
        this.wasHidden = false;
        class_1061 class_1061VarMethod_3037 = class_1061.method_3037(this);
        findViewById(R.id.levelButtonBack).setOnClickListener(new class_45(this));
        if (getIntent() != null && getIntent().getExtras() != null) {
            string = getIntent().getExtras().getString(SettingsActivity.intentMode);
        } else {
            string = null;
        }
        if (string == null) {
            class_1061.method_3043("LevelSelectActivity: mode==null, running this.finish()");
            finish();
            return;
        }
        boolean zIsCustomMapOrFolder = isCustomMapOrFolder(string);
        if (!"maps/survival".equals(string)) {
            this.aiDifficulty.setVisibility(0);
        } else {
            this.aiDifficulty.setVisibility(8);
        }
        if (zIsCustomMapOrFolder) {
            this.modsImportMod.setVisibility(0);
            TextView textView = (TextView) findViewById(R.id.messageInfo);
            String storageInfoAndWarnings = SettingsActivity.getStorageInfoAndWarnings("/maps");
            if (storageInfoAndWarnings != null) {
                textView.setVisibility(0);
                textView.setText(storageInfoAndWarnings);
            } else {
                textView.setVisibility(8);
                textView.setText(VariableScope.nullOrMissingString);
            }
        } else {
            this.modsImportMod.setVisibility(8);
        }
        this.aiDifficulty.setSelection(class_1061VarMethod_3037.field_6345.aiDifficulty + 2);
        this.skirmish = LevelGroupSelectActivity.skirmishLevelsDir.equals(string);
        this.custom = LevelGroupSelectActivity.customLevelsDir.equals(string);
        if (this.skirmish) {
            this.aiCountSpinner.setVisibility(0);
            this.gameModeSpinner.setVisibility(0);
            this.aiCountSpinner.setSelection(3);
        }
        GridView gridView = (GridView) findViewById(R.id.levelHolder);
        if (isCustomMapOrFolder(string) && !class_84.method_135(this)) {
            finish();
            return;
        }
        this.levels = class_899.method_2168(string, true);
        String[] strArrMethod_2661 = class_1061VarMethod_3037.field_6354.method_2661(this.levels, string);
        this.levels = strArrMethod_2661;
        if (this.custom && (strArrMethod_2661 == null || strArrMethod_2661.length == 0)) {
            class_84.method_132(this);
        }
        if (this.levels == null) {
            this.levels = new String[0];
        }
        this.currentMapPath = string + "/";
        this.levelViews = new ArrayList();
        this.levelPaths = new ArrayList();
        for (String str : this.levels) {
            this.levelPaths.add(str);
            this.levelViews.add(null);
        }
        gridView.setAdapter((ListAdapter) this.levelAdapter);
        gridView.setOnItemClickListener(new class_48(this));
        if (zIsCustomMapOrFolder) {
            registerForContextMenu(gridView);
        }
        ProgressDialog progressDialog = this.progressDialog;
        if (progressDialog != null && progressDialog.isShowing()) {
            try {
                dismissDialog(0);
            } catch (IllegalArgumentException e) {
                class_1061.method_3010("dismissDialog failed", e);
            }
        }
    }

    public void levelOnclickHandler(String str) {
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        String strMethod_3060 = class_1061.method_3060(str);
        class_988.method_2632(convertLevelFileNameForDisplay(strMethod_3060));
        boolean zIsAvailableInDemo = isAvailableInDemo(strMethod_3060, this.currentMapPath + strMethod_3060);
        if (class_1061VarMethod_3076.field_6321 && !zIsAvailableInDemo) {
            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Full version only").setMessage("Sorry this level is not available in the demo version.").setPositiveButton("Ok", new class_49(this)).show();
        } else if (class_1061VarMethod_3076 == null || !class_1061VarMethod_3076.field_6334 || class_1061VarMethod_3076.field_6335) {
            startNewLevel(this, str, null);
        } else {
            class_84.method_118(this, new class_61(this, this, str), new class_50(this));
        }
    }

    public void setupViewForLevel(View view, String str) {
        Button button = (Button) view.findViewById(R.id.reservedNamedId1);
        DynamicImageView dynamicImageView = (DynamicImageView) view.findViewById(R.id.reservedNamedId2);
        String strMethod_2632 = class_988.method_2632(convertLevelFileNameForDisplay(str));
        boolean zIsAvailableInDemo = isAvailableInDemo(str, this.currentMapPath + str);
        button.setTag(this.currentMapPath + str);
        view.setTag(this.currentMapPath + str);
        class_866.method_2048(getApplicationContext()).method_2047(this.currentMapPath + str);
        String strValueOf = String.valueOf(strMethod_2632);
        if (class_1061.method_3076().field_6321 && !zIsAvailableInDemo) {
            strValueOf = "[LOCKED] ".concat(String.valueOf(strValueOf));
            if (dynamicImageView != null) {
                dynamicImageView.setColorFilter(new LightingColorFilter(Color.argb(255, 60, 60, 60), 0));
            }
        }
        button.setText(strValueOf);
    }

    public View createViewForLevel(String str) {
        Drawable bitmapDrawable;
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        LinearLayout linearLayout = new LinearLayout(getBaseContext());
        linearLayout.setGravity(17);
        LinearLayout linearLayout2 = new LinearLayout(getBaseContext());
        linearLayout2.setOrientation(1);
        linearLayout2.setGravity(17);
        linearLayout2.setBackgroundColor(Color.argb(50, 0, 0, 0));
        linearLayout2.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        linearLayout2.setPadding(2, 4, 2, 2);
        linearLayout2.setMinimumHeight(class_1061VarMethod_3076.method_2986(170.0f));
        Button button = new Button(getBaseContext(), null, R.style.LevelButton);
        InputStream inputStreamMethod_134 = class_84.method_134(this.currentMapPath + str);
        if (inputStreamMethod_134 != null) {
            bitmapDrawable = Drawable.createFromStream(inputStreamMethod_134, null);
            try {
                inputStreamMethod_134.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            bitmapDrawable = null;
        }
        if (bitmapDrawable != null && (bitmapDrawable instanceof BitmapDrawable)) {
            BitmapDrawable bitmapDrawable2 = (BitmapDrawable) bitmapDrawable;
            Bitmap bitmap = bitmapDrawable2.getBitmap();
            if (bitmapDrawable2.getBitmap() != null && (bitmap.getWidth() > 500 || bitmap.getHeight() > 500)) {
                class_1061.method_3043("Map thumbnail is too large. Size:(" + bitmap.getWidth() + "," + bitmap.getHeight() + ") (max:500 pixels)");
                if (toolargethumb == null) {
                    toolargethumb = class_1061.method_3076().field_6342.method_3241(R.drawable.error_toolargethumb);
                }
                bitmapDrawable = new BitmapDrawable(getResources(), toolargethumb.method_3209());
            }
        }
        if (bitmapDrawable == null && this.custom) {
            if (missingMapThumb == null) {
                missingMapThumb = class_1061.method_3076().field_6342.method_3241(R.drawable.error_missingmap);
            }
            bitmapDrawable = new BitmapDrawable(getResources(), missingMapThumb.method_3209());
        }
        if (bitmapDrawable != null) {
            DynamicImageView dynamicImageView = new DynamicImageView(getBaseContext());
            dynamicImageView.setId(R.id.reservedNamedId2);
            dynamicImageView.setMaxFixedHeight(class_1061VarMethod_3076.method_2986(150.0f));
            dynamicImageView.setImageDrawable(bitmapDrawable);
            dynamicImageView.setPadding(3, 1, 3, 1);
            linearLayout2.addView(dynamicImageView);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dynamicImageView.getLayoutParams());
            layoutParams.width = -1;
            dynamicImageView.setLayoutParams(layoutParams);
        }
        button.setId(R.id.reservedNamedId1);
        button.setTypeface(Typeface.DEFAULT_BOLD);
        button.setPadding(3, 5, 3, 5);
        button.setTextSize(18.0f);
        button.setTextColor(-1);
        button.setShadowLayer(1.0f, 1.0f, 1.0f, Color.argb(127, 0, 0, 0));
        button.setGravity(17);
        button.setBackgroundDrawable(null);
        linearLayout2.addView(button);
        setupViewForLevel(linearLayout2, str);
        linearLayout.addView(linearLayout2);
        return linearLayout;
    }

    @Override // android.app.Activity
    public Dialog onCreateDialog(int i) {
        switch (i) {
            case 0:
                ProgressDialog progressDialog = new ProgressDialog(this);
                this.progressDialog = progressDialog;
                progressDialog.setProgressStyle(0);
                this.progressDialog.setMessage("Loading...");
                this.progressDialog.setCancelable(false);
                return this.progressDialog;
            default:
                return null;
        }
    }

    public void startNewLevel(Context context, String str, Boolean bool) {
        class_1061 class_1061VarMethod_3037 = class_1061.method_3037(context);
        class_1061VarMethod_3037.field_6345.aiDifficulty = this.aiDifficulty.getSelectedItemPosition() - 2;
        class_1061VarMethod_3037.field_6345.save();
        if ((this.skirmish || this.custom) && bool == null) {
            int numberOfAIsSelected = getNumberOfAIsSelected();
            int numberOfAIsAlliesSelected = getNumberOfAIsAlliesSelected();
            boolean teamedUpSelecteed = getTeamedUpSelecteed();
            String strConvertLevelFileNameForDisplay = convertLevelFileNameForDisplay(convertFilePathToFileName(str));
            String str2 = VariableScope.nullOrMissingString + "Difficulty: " + class_1001.method_2760(class_1061VarMethod_3037.field_6345.aiDifficulty) + "\n";
            if (!this.custom) {
                str2 = str2 + "Number of AIs: " + numberOfAIsSelected + " (" + numberOfAIsAlliesSelected + " allies)\n";
            }
            String str3 = str2 + "Map: " + strConvertLevelFileNameForDisplay + "\n";
            class_992 class_992VarMethod_2671 = class_1061VarMethod_3037.field_6354.method_2671(str);
            if (class_992VarMethod_2671 != null) {
                str3 = str3 + "From mod file: " + class_992VarMethod_2671.method_2684() + "\n";
            } else if (isCustomMapOrFolder(str)) {
                str3 = str3 + "From storage location: " + class_899.method_2193(str) + "\n";
            }
            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Start?").setMessage(str3).setPositiveButton("Start game", new class_53(this, context, str)).setNegativeButton("Advanced setup", new class_51(this, class_1061VarMethod_3037, str, numberOfAIsSelected, numberOfAIsAlliesSelected, teamedUpSelecteed)).show();
            return;
        }
        showDialog(0);
        class_60 class_60Var = new class_60(this);
        class_60Var.field_232 = context;
        class_60Var.field_231 = str;
        new Thread(class_60Var).start();
    }

    public int getNumberOfAIsSelected() {
        if (this.custom) {
            return 4;
        }
        return this.aiCountSpinner.getSelectedItemPosition() + 1;
    }

    public int getNumberOfAIsAlliesSelected() {
        int selectedItemPosition = this.gameModeSpinner.getSelectedItemPosition();
        if (selectedItemPosition <= 0 || selectedItemPosition > 3) {
            return 0;
        }
        return selectedItemPosition;
    }

    public boolean getTeamedUpSelecteed() {
        return this.gameModeSpinner.getSelectedItemPosition() != 4;
    }

    public static boolean isMapSkirmish(String str) {
        if (str.contains("skirmish/")) {
            return true;
        }
        return false;
    }

    public static boolean isMapCustom(String str) {
        if (str.contains("SD/")) {
            return true;
        }
        return false;
    }

    public static void loadSinglePlayerMapRaw(String str, boolean z, int i, int i2, boolean z2, boolean z3) {
        int i3;
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        class_1061VarMethod_3076.field_6347.method_2551();
        if (z || z3) {
            class_1061VarMethod_3076.method_2961();
            synchronized (class_1061VarMethod_3076) {
                class_1061VarMethod_3076.field_6471 = null;
                class_1061VarMethod_3076.field_6470 = str;
                int i4 = class_324.field_1448 - 1;
                int iMethod_123 = class_84.method_123(str);
                class_1061.method_3043("Max teams on map: " + str + " = " + iMethod_123);
                if (iMethod_123 > 0 && (i3 = iMethod_123 - 1) < i4) {
                    i4 = i3;
                }
                class_324.method_543();
                class_1061VarMethod_3076.field_6373 = new class_313(0);
                class_1061VarMethod_3076.field_6373.field_1468 = "Player";
                int i5 = 0;
                int i6 = 0;
                while (i5 <= 1) {
                    for (int i7 = 1; i7 <= i4; i7++) {
                        boolean z4 = i7 % 2 == 0 || i5 == 1;
                        if (i6 < i2 && z4 && class_324.method_526(i7) == null) {
                            class_263 class_263Var = new class_263(i7);
                            class_263Var.field_1468 = "AI";
                            class_263Var.field_1464 = 0;
                            i6++;
                        }
                    }
                    i5++;
                }
                class_1061.method_3043("Allies: " + i6 + "/" + i2);
                int i8 = 0;
                int i9 = 0;
                while (i8 <= 1) {
                    for (int i10 = 1; i10 <= i4; i10++) {
                        boolean z5 = i10 % 2 == 1 || i8 == 1;
                        if (!z2) {
                            z5 = true;
                        }
                        if (i9 < i - i2 && z5 && class_324.method_526(i10) == null) {
                            class_263 class_263Var2 = new class_263(i10);
                            class_263Var2.field_1468 = "AI";
                            i9++;
                            if (z2) {
                                class_263Var2.field_1464 = 1;
                            }
                        }
                    }
                    i8++;
                }
                class_1061VarMethod_3076.field_6352.method_2700();
                if (!z3) {
                    class_1061VarMethod_3076.method_3013(false, class_768.field_4200);
                }
            }
            return;
        }
        class_1061VarMethod_3076.method_2961();
        synchronized (class_1061VarMethod_3076) {
            class_1061VarMethod_3076.field_6471 = null;
            class_1061VarMethod_3076.field_6470 = str;
        }
        if (!z3) {
            class_1061VarMethod_3076.method_3013(true, class_768.field_4200);
        }
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTitle("Levels");
        if (class_84.method_127(this, true)) {
            setContentView(R.layout.level_select_grid);
            class_84.method_120(getWindow().getDecorView().findViewById(android.R.id.content));
            this.aiDifficulty = (Spinner) findViewById(R.id.aiDifficulty);
            this.aiCountSpinner = (Spinner) findViewById(R.id.aiCount);
            this.gameModeSpinner = (Spinner) findViewById(R.id.gameMode);
            this.modsImportMod = (Button) findViewById(R.id.modsImportMod);
            this.gameView = class_84.method_125(this);
            this.modsImportMod.setOnClickListener(new class_54(this));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void $invoke$special$showFileChooserForImport() {
        class_84.method_138(this);
    }

    @Override // android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        switch (i) {
            case 5:
                ModsActivity.onFileSelectResult(this, i, i2, intent, this.fileAddedCallback);
                break;
            default:
                super.onActivityResult(i, i2, intent);
                break;
        }
    }

    @Override // android.app.Activity, android.view.View.OnCreateContextMenuListener
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        class_992 class_992VarMethod_2671;
        super.onCreateContextMenu(contextMenu, view, contextMenuInfo);
        View view2 = ((AdapterView.AdapterContextMenuInfo) contextMenuInfo).targetView;
        String str = (String) view2.getTag();
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        String strConvertLevelFileNameForDisplay = convertLevelFileNameForDisplay(str);
        if (str != null) {
            class_992VarMethod_2671 = class_1061VarMethod_3076.field_6354.method_2671(str);
        } else {
            class_992VarMethod_2671 = null;
        }
        this.lastContextMenu = str;
        contextMenu.setHeaderTitle(strConvertLevelFileNameForDisplay);
        MenuItem menuItemAdd = contextMenu.add(0, view2.getId(), 0, "Export");
        if (class_992VarMethod_2671 != null) {
            menuItemAdd.setTitle("Export (Standalone maps only)");
            menuItemAdd.setEnabled(false);
        }
        MenuItem menuItemAdd2 = contextMenu.add(2, view2.getId(), 0, "Delete");
        if (class_992VarMethod_2671 != null) {
            menuItemAdd2.setTitle("Delete (Standalone maps only)");
            menuItemAdd2.setEnabled(false);
        }
        if (class_992VarMethod_2671 != null) {
            contextMenu.add(4, view2.getId(), 0, "From Mod: " + class_992VarMethod_2671.method_2682()).setEnabled(false);
        }
        if (class_992VarMethod_2671 == null && this.custom) {
            MenuItem menuItemAdd3 = contextMenu.add(3, view.getId(), 0, "Storage: ".concat(String.valueOf(class_899.method_2193(str))));
            if (menuItemAdd3 != null) {
                menuItemAdd3.setEnabled(false);
            }
        }
    }

    @Override // android.app.Activity
    public boolean onContextItemSelected(MenuItem menuItem) {
        String str = this.lastContextMenu;
        if (str == null) {
            class_1061.method_3043("onContextItemSelected: level==null");
            if (menuItem != null) {
                class_1061.method_3043("onContextItemSelected: item.getGroupId():" + menuItem.getGroupId());
                return false;
            }
            return false;
        }
        if (menuItem.getGroupId() == 0) {
            shareLevel(str);
            return false;
        }
        if (menuItem.getGroupId() == 2) {
            deleteLevel(str);
            return false;
        }
        return false;
    }

    public void shareLevel(String str) {
        if (str == null) {
            class_1061.method_3043("shareLevel: level==null");
        } else {
            class_1061.method_3076();
            class_84.method_109(this, new File(class_899.method_2178(str)));
        }
    }

    public void deleteLevel(String str) {
        String strSubstring;
        if (str == null) {
            class_1061.method_3043("deleteLevel: level==null");
            return;
        }
        String strMethod_2178 = class_899.method_2178(str);
        String strConvertLevelFileNameForDisplay = convertLevelFileNameForDisplay(str);
        if (strMethod_2178.toLowerCase(Locale.ROOT).endsWith(".tmx")) {
            strSubstring = strMethod_2178.substring(0, strMethod_2178.length() - 4);
        } else {
            strSubstring = strMethod_2178;
        }
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Are you sure?").setMessage("Delete map '" + strConvertLevelFileNameForDisplay + "'?").setPositiveButton("Delete", new class_46(this, strMethod_2178, strSubstring + "_map.png")).setNegativeButton("Keep", new class_58(this)).show();
    }

    public void refresh() {
        this.refreshLevelsHandler.post(this.refreshLevelsRunnable);
    }
}
