package com.corrodinggames.rts.appFramework;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.tesseract.union.hook.ExtractMapHook;
import com.corrodinggames.rts.R;
import com.corrodinggames.rts.gameFramework.class_1061;
import com.corrodinggames.rts.gameFramework.class_866;
import com.corrodinggames.rts.gameFramework.e.class_899;
import java.util.ArrayList;
import java.util.Collections;

/* JADX INFO: loaded from: classes.dex */
public class ReplaySelectActivity extends class_1 {
    public static final int LOADING_DIALOG = 0;
    public static final String currentReplayPath = "/SD/rustedWarfare/replays/";
    public class_5 gameView;
    public ProgressDialog progressDialog;
    public String[] replays;
    public Handler refreshLevelsHandler = new Handler();
    public Runnable refreshLevelsRunnable = new class_207(this);
    public Runnable resumeActivityRunnable = new class_198(this);

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
        String[] strArrMethod_2184 = class_899.method_2184(currentReplayPath);
        if (strArrMethod_2184 == null) {
            class_1061.method_3043("failed to find replay folder");
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (String str : strArrMethod_2184) {
            if (!str.endsWith(".map")) {
                arrayList.add(str);
            }
        }
        Collections.sort(arrayList, new class_209());
        return (String[]) arrayList.toArray(new String[0]);
    }

    public void setup(boolean z) {
        class_1061.method_3037(this);
        if (!class_84.method_137(this)) {
            finish();
            return;
        }
        findViewById(R.id.levelButtonBack).setOnClickListener(new class_197(this));
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.replayHolder);
        linearLayout.removeAllViews();
        if (!class_84.method_137(this)) {
            finish();
            return;
        }
        String[] gameSaves = getGameSaves();
        this.replays = gameSaves;
        if (gameSaves == null) {
            this.replays = new String[0];
        }
        String[] strArr = this.replays;
        if (strArr == null || strArr.length == 0) {
            class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
            if (z && class_1061VarMethod_3076.field_6345.saveMultiplayerReplays) {
                class_84.method_132(this);
            }
        }
        int i = 0;
        while (true) {
            String[] strArr2 = this.replays;
            if (i >= strArr2.length) {
                break;
            }
            String str = strArr2[i];
            Button button = new Button(getBaseContext());
            button.setId(i);
            button.setTag(str);
            class_866.method_2048(getApplicationContext()).method_2047(currentReplayPath.concat(String.valueOf(str)));
            String strConvertDataFileNameForDisplay = LoadLevelActivity.convertDataFileNameForDisplay(class_899.method_2194(String.valueOf(str)));
            button.setBackgroundResource(R.drawable.btn_dropdown);
            button.setText(strConvertDataFileNameForDisplay);
            button.setTextColor(-1);
            registerForContextMenu(button);
            button.setOnClickListener(new class_199(this));
            button.setTypeface(Typeface.DEFAULT_BOLD);
            button.setPadding(0, 16, 0, 16);
            linearLayout.addView(button);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(button.getLayoutParams());
            layoutParams.setMargins(0, 2, 0, 2);
            button.setLayoutParams(layoutParams);
            i++;
        }
        ((TextView) findViewById(R.id.LevelTextTop)).setText("Select Replay");
        ProgressDialog progressDialog = this.progressDialog;
        if (progressDialog != null && progressDialog.isShowing()) {
            dismissDialog(0);
        }
    }

    @Override // android.app.Activity, android.view.View.OnCreateContextMenuListener
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        ExtractMapHook.onCreateContextMenu(this, contextMenu, view, contextMenuInfo);
        super.onCreateContextMenu(contextMenu, view, contextMenuInfo);
        contextMenu.setHeaderTitle(((Button) view).getText());
        contextMenu.add(0, view.getId(), 0, "Share");
        contextMenu.add(1, view.getId(), 0, "Rename");
        contextMenu.add(2, view.getId(), 0, "Delete");
        String[] strArr = this.replays;
        if (strArr != null && strArr.length > 0) {
            MenuItem menuItemAdd = contextMenu.add(3, view.getId(), 0, "Storage: ".concat(String.valueOf(class_899.method_2193(strArr[view.getId()]))));
            if (menuItemAdd != null) {
                menuItemAdd.setEnabled(false);
            }
        }
    }

    @Override // android.app.Activity
    public boolean onContextItemSelected(MenuItem menuItem) {
        ExtractMapHook.onContextItemSelected(this, menuItem);
        String str = this.replays[menuItem.getItemId()];
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
        class_84.method_109(this, class_1061.method_3076().field_6356.method_2025(str, false));
    }

    public void renameLevel(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String strMethod_2194 = class_899.method_2194(str);
        builder.setTitle("Rename - ".concat(String.valueOf(strMethod_2194)));
        builder.setMessage("Enter a new name for this replay");
        EditText editText = new EditText(this);
        editText.setText(strMethod_2194);
        builder.setView(editText);
        builder.setPositiveButton("Ok", new class_201(this, editText, str));
        builder.setNegativeButton("Cancel", new class_202(this));
        builder.show();
    }

    public void deleteLevel(String str) {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Are you sure?").setMessage("Delete replay '" + class_899.method_2194(String.valueOf(str)) + "' from " + class_899.method_2193(str) + " storage?").setPositiveButton("Delete", new class_204(this, str)).setNegativeButton("Keep", new class_203(this)).show();
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

    public void loadReplay(Context context, String str) {
        showDialog(0);
        class_208 class_208Var = new class_208(this);
        class_208Var.field_539 = context;
        class_208Var.field_538 = str;
        new Thread(class_208Var).start();
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTitle("Load Replay");
        if (class_84.method_127(this, true)) {
            setContentView(R.layout.replay_select);
            this.gameView = class_84.method_125(this);
            setup(true);
            if (!class_1061.method_3076().field_6345.saveMultiplayerReplays) {
                new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Recording off").setMessage("Multiplayer replay recordings are not turned on, enable them?").setPositiveButton("Yes", new class_206(this)).setNegativeButton("No", new class_205(this)).show();
            }
        }
    }

    public void refresh() {
        this.refreshLevelsHandler.post(this.refreshLevelsRunnable);
    }
}
