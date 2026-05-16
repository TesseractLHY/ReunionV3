package com.corrodinggames.rts.appFramework;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import cn.tesseract.union.hook.ExtractMapHook;
import com.corrodinggames.rts.R;
import com.corrodinggames.rts.game.units.custom.logicBooleans.VariableScope;
import com.corrodinggames.rts.gameFramework.class_1061;
import com.corrodinggames.rts.gameFramework.class_775;
import com.corrodinggames.rts.gameFramework.class_866;
import com.corrodinggames.rts.gameFramework.e.class_899;
import java.util.ArrayList;
import java.util.Collections;

/* JADX INFO: loaded from: classes.dex */
public class LoadLevelActivity extends class_1 {
    public static final int LOADING_DIALOG = 0;
    public static final String currentSavePath = "/SD/rustedWarfare/saves/";
    public class_5 gameView;
    public String[] levels;
    public TextView messageInfo;
    public Button modsImportMod;
    public ProgressDialog progressDialog;
    public final Handler uiHandler = new Handler();
    public Runnable fileAddedCallback = new class_70(this);
    public Handler refreshLevelsHandler = new Handler();
    public Runnable refreshLevelsRunnable = new class_72(this);

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
        setup(false);
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

    public static String[] getGameSaves() {
        String[] strArrMethod_2168 = class_899.method_2168(currentSavePath, false);
        if (strArrMethod_2168 == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (String str : strArrMethod_2168) {
            if (!str.endsWith(".map") && !str.endsWith(".tmp")) {
                arrayList.add(str);
            }
        }
        Collections.sort(arrayList, new class_74());
        return (String[]) arrayList.toArray(new String[0]);
    }

    public static String convertDataFileNameForDisplay(String str) {
        class_899.method_2192(str);
        if (str.contains("/")) {
            str = str.substring(str.lastIndexOf("/") + 1);
        }
        if (str.endsWith(".rwsave")) {
            str = str.replace(".rwsave", VariableScope.nullOrMissingString);
        }
        if (str.endsWith(".replay")) {
            return str.replace(".replay", VariableScope.nullOrMissingString);
        }
        return str;
    }

    public void setup(boolean z) {
        class_1061.method_3037(this);
        findViewById(R.id.levelButtonBack).setOnClickListener(new class_62(this));
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.levelHolder);
        linearLayout.removeAllViews();
        if (!class_84.method_137(this)) {
            finish();
            return;
        }
        this.levels = getGameSaves();
        if (z) {
            class_84.method_132(this);
        }
        String storageInfoAndWarnings = SettingsActivity.getStorageInfoAndWarnings("/saves");
        if (storageInfoAndWarnings != null) {
            this.messageInfo.setVisibility(0);
            this.messageInfo.setText(storageInfoAndWarnings);
        } else {
            this.messageInfo.setVisibility(8);
            this.messageInfo.setText(VariableScope.nullOrMissingString);
        }
        if (this.levels == null) {
            this.levels = new String[0];
        }
        int i = 0;
        while (true) {
            String[] strArr = this.levels;
            if (i >= strArr.length) {
                break;
            }
            String str = strArr[i];
            Button button = new Button(getBaseContext());
            button.setId(i);
            button.setTag(str);
            class_866.method_2048(getApplicationContext()).method_2047(currentSavePath.concat(String.valueOf(str)));
            String strConvertDataFileNameForDisplay = convertDataFileNameForDisplay(str);
            button.setBackgroundResource(R.drawable.btn_dropdown);
            button.setText(strConvertDataFileNameForDisplay);
            registerForContextMenu(button);
            button.setOnClickListener(new class_63(this));
            button.setTypeface(Typeface.DEFAULT_BOLD);
            button.setPadding(0, 16, 0, 16);
            button.setTextColor(-1);
            linearLayout.addView(button);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(button.getLayoutParams());
            layoutParams.setMargins(0, 2, 0, 2);
            button.setLayoutParams(layoutParams);
            i++;
        }
        ((TextView) findViewById(R.id.LevelTextTop)).setText("Select Game Save");
        ProgressDialog progressDialog = this.progressDialog;
        if (progressDialog != null && progressDialog.isShowing()) {
            try {
                dismissDialog(0);
            } catch (IllegalArgumentException e) {
                class_1061.method_3010("dismissDialog failed", e);
            }
        }
    }

    @Override // android.app.Activity, android.view.View.OnCreateContextMenuListener
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        super.onCreateContextMenu(contextMenu, view, contextMenuInfo);
        contextMenu.setHeaderTitle(((Button) view).getText());
        contextMenu.add(0, view.getId(), 0, "Share");
        contextMenu.add(1, view.getId(), 0, "Rename");
        contextMenu.add(2, view.getId(), 0, "Delete");
        String[] strArr = this.levels;
        if (strArr != null && strArr.length > 0) {
            MenuItem menuItemAdd = contextMenu.add(3, view.getId(), 0, "Storage: ".concat(String.valueOf(class_899.method_2193(strArr[view.getId()]))));
            if (menuItemAdd != null) {
                menuItemAdd.setEnabled(false);
            }
        }
    }

    @Override // android.app.Activity
    public boolean onContextItemSelected(MenuItem menuItem) {
        String str = this.levels[menuItem.getItemId()];
        if (menuItem.getGroupId() == 0) {
            shareLevel(str);
            return false;
        }
        if (menuItem.getGroupId() == 1) {
            renameLevel(str);
            return false;
        }
        if (menuItem.getGroupId() == 2) {
            deleteLevel(str);
            return false;
        }
        return false;
    }

    public void shareLevel(String str) {
        class_1061.method_3076();
        class_84.method_109(this, class_775.method_1783(str, false));
    }

    public void renameLevel(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String strMethod_2194 = class_899.method_2194(str);
        builder.setTitle("Rename - ".concat(String.valueOf(strMethod_2194)));
        builder.setMessage("Enter a new name for this save");
        EditText editText = new EditText(this);
        editText.setText(strMethod_2194);
        builder.setView(editText);
        builder.setPositiveButton("Ok", new class_65(this, editText, str));
        builder.setNegativeButton("Cancel", new class_66(this));
        builder.show();
    }

    public void deleteLevel(String str) {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Are you sure?").setMessage("Delete saved game '" + str + "'?").setPositiveButton("Delete", new class_68(this, str)).setNegativeButton("Keep", new class_67(this)).show();
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

    public void loadLevel(Context context, String str) {
        showDialog(0);
        class_73 class_73Var = new class_73(this);
        class_73Var.field_252 = context;
        class_73Var.field_251 = str;
        new Thread(class_73Var).start();
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTitle("Load");
        if (class_84.method_127(this, true)) {
            setContentView(R.layout.level_select);
            class_84.method_120(getWindow().getDecorView().findViewById(android.R.id.content));
            ((Spinner) findViewById(R.id.aiDifficulty)).setVisibility(8);
            this.gameView = class_84.method_125(this);
            this.messageInfo = (TextView) findViewById(R.id.messageInfo);
            Button button = (Button) findViewById(R.id.modsImportMod);
            this.modsImportMod = button;
            button.setVisibility(0);
            this.modsImportMod.setOnClickListener(new class_69(this));
            setup(true);
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

    public void refresh() {
        this.refreshLevelsHandler.post(this.refreshLevelsRunnable);
    }
}
