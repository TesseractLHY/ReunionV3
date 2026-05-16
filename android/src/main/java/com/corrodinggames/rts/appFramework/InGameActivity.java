package com.corrodinggames.rts.appFramework;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.corrodinggames.rts.R;
import com.corrodinggames.rts.appFramework.android.AndroidSAF;
import com.corrodinggames.rts.game.units.custom.logicBooleans.VariableScope;
import com.corrodinggames.rts.gameFramework.class_1061;
import com.corrodinggames.rts.gameFramework.class_907;
import com.corrodinggames.rts.gameFramework.h.class_988;
import com.corrodinggames.rts.gameFramework.j.class_1001;
import com.corrodinggames.rts.gameFramework.o.class_1282;

/* JADX INFO: loaded from: classes.dex */
public class InGameActivity extends class_1 {
    public static final int DISCONNECT_ID = 10;
    public static final int EXIT_GAME_ID = 15;
    public static final int FULL_SAVE_ID = 12;
    public static final int HIDE_INTERFACE_ID = 22;
    public static final int LIST_PLAYERS_ID = 14;
    public static final int LOOK_ID = 4;
    public static final int MODE_ID = 6;
    public static final int PICKTILE_ID = 1;
    public static final int QUICK_LOAD_ID = 8;
    public static final int QUICK_SAVE_ID = 7;
    public static final int RECORD_ID = 9;
    public static final int RESTART_ID = 5;
    public static final int SAVE_MAP_ID = 18;
    public static final int SAVING_DIALOG = 0;
    public static final int SEND_MESSAGE_ID = 13;
    public static final int SEND_TEAM_MESSAGE_ID = 16;
    public static final int SETTINGS_ID = 2;
    public static final int SHOW_BATTLE_ROOM = 21;
    public static final int SHOW_BRIEFING_ID = 11;
    public static final int SHOW_LEADERBOARD_ID = 23;
    public static final int SHOW_MAIN_MENU = 20;
    public static final int SKIP_ID = 3;
    public static final int STEAM_REINVITE_ID = 17;
    public static final int SURRENDER_ID = 19;
    public class_5 gameViewCommon;
    public ProgressDialog progressDialog;
    public final Handler uiHandler = new Handler(Looper.getMainLooper());
    public boolean test = true;

    public void makeExportMapPopup(String str) {
        $invoke$special$makeExportMapPopup(str);
    }

    public void makeSaveGamePopup(String str) {
        $invoke$special$makeSaveGamePopup(str);
    }

    public void makeSendMessagePopup(boolean z) {
        $invoke$special$makeSendMessagePopup(z);
    }

    public void openMarketLinkInternal() {
        $invoke$special$openMarketLinkInternal();
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (class_1061.method_3076() == null) {
            startActivity(new Intent(getBaseContext(), (Class<?>) IntroScreen.class));
            finish();
        }
        if (class_84.method_127(this, true)) {
            setContentView(R.layout.main);
            getWindow().setBackgroundDrawable(null);
            class_5 class_5VarMethod_125 = class_84.method_125(this);
            this.gameViewCommon = class_5VarMethod_125;
            class_5VarMethod_125.setInGameActivity(this);
        }
    }

    @Override // android.app.Activity
    public void finish() {
        class_1061.method_3043("IngameActivity: finish");
        super.finish();
        class_84.method_133(this, true);
    }

    @Override // android.app.Activity
    public void onStart() {
        super.onStart();
        Log.e(AndroidSAF.TAG, "Ingame:onStart");
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if (class_1061VarMethod_3076 != null) {
            class_1061VarMethod_3076.method_3001(this.gameViewCommon);
        }
    }

    @Override // android.app.Activity
    public void onStop() {
        super.onStop();
        Log.e(AndroidSAF.TAG, "Ingame:onStop");
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if (class_1061VarMethod_3076 != null) {
            class_1061VarMethod_3076.method_2996(this, this.gameViewCommon);
        }
    }

    @Override // android.app.Activity
    public void onPause() {
        Log.e(AndroidSAF.TAG, "Ingame:onPause");
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if (class_1061VarMethod_3076 != null) {
            class_1061VarMethod_3076.method_3030(this.gameViewCommon);
        }
        this.gameViewCommon.onParentPause();
        super.onPause();
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (z) {
            class_84.method_116(this, false, true);
        }
        this.gameViewCommon.onParentWindowFocusChanged(z);
    }

    @Override // com.corrodinggames.rts.appFramework.class_1, android.app.Activity
    public void onResume() {
        Log.e(AndroidSAF.TAG, "Ingame:onResume");
        super.onResume();
        class_1061 class_1061VarMethod_3037 = class_1061.method_3037(this);
        if (class_1061VarMethod_3037 != null) {
            class_5 class_5VarMethod_108 = class_84.method_108(this, this.gameViewCommon);
            this.gameViewCommon = class_5VarMethod_108;
            class_5VarMethod_108.setInGameActivity(this);
            class_1061VarMethod_3037.method_2997(this, this.gameViewCommon, false);
        }
        class_84.method_116(this, false, true);
    }

    @Override // android.app.Activity
    public void onDestroy() {
        Log.e(AndroidSAF.TAG, "InGameActivity:onDestroy");
        class_1061.method_3076();
        super.onDestroy();
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        menu.add(0, 12, 0, class_988.method_2636("menus.ingame.save", new Object[0])).setIcon(android.R.drawable.ic_menu_save);
        if (class_1061VarMethod_3076.field_6376 && !class_1061.field_6309) {
            menu.add(0, 18, 0, class_988.method_2636("menus.ingame.exportMap", new Object[0])).setIcon(android.R.drawable.ic_menu_save);
        }
        menu.add(0, 2, 0, class_988.method_2636("menus.ingame.settings", new Object[0])).setIcon(android.R.drawable.ic_menu_preferences);
        class_1061VarMethod_3076.method_2963();
        if (class_1061VarMethod_3076.field_6356 != null && class_1061VarMethod_3076.field_6356.method_2036()) {
            menu.add(0, 22, 0, class_988.method_2636("menus.ingame.hideInterface", new Object[0])).setIcon(android.R.drawable.ic_menu_send);
        }
        if (class_1061VarMethod_3076.method_2963()) {
            menu.add(0, 13, 0, class_988.method_2636("menus.ingame.chat", new Object[0])).setIcon(android.R.drawable.ic_menu_send);
            menu.add(0, 14, 0, class_988.method_2636("menus.ingame.players", new Object[0])).setIcon(android.R.drawable.ic_menu_sort_by_size);
            if (class_1061VarMethod_3076.field_6352.field_5851) {
                class_1282.method_3463();
            }
            if (!(class_1061VarMethod_3076.field_6373 != null && class_1061VarMethod_3076.field_6373.field_1407) && !class_1061VarMethod_3076.field_6475) {
                menu.add(0, 19, 0, class_988.method_2636("menus.ingame.surrender", new Object[0])).setIcon(android.R.drawable.ic_lock_power_off);
            }
            if (!class_1061VarMethod_3076.field_6352.field_5851) {
                menu.add(0, 10, 0, class_988.method_2636("menus.ingame.disconnect", new Object[0])).setIcon(android.R.drawable.ic_lock_power_off);
            } else {
                menu.add(0, 10, 0, class_988.method_2636("menus.ingame.exitGame", new Object[0])).setIcon(android.R.drawable.ic_lock_power_off);
            }
        } else {
            if (class_1061VarMethod_3076.field_6411 != null && class_1061VarMethod_3076.field_6411.field_7102 != null) {
                menu.add(0, 11, 0, class_988.method_2636("menus.ingame.briefing", new Object[0])).setIcon(android.R.drawable.ic_dialog_info);
            }
            menu.add(0, 15, 0, class_988.method_2636("menus.ingame.exitGame", new Object[0])).setIcon(android.R.drawable.ic_lock_power_off);
        }
        if (class_1061VarMethod_3076 != null && class_1061VarMethod_3076.field_6345.allowGameRecording) {
            if (!class_1061VarMethod_3076.field_6369) {
                menu.add(0, 9, 0, "Start Recording");
            } else {
                menu.add(0, 9, 0, "Stop Recording");
            }
        }
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        selectMenuOptionInternal(menuItem.getItemId());
        return super.onOptionsItemSelected(menuItem);
    }

    public void selectMenuOption(int i) {
        class_1061.method_3043("outer selectMenuOption: ".concat(String.valueOf(i)));
        this.uiHandler.post(new class_14(this, i));
    }

    public void selectMenuOptionInternal(int i) {
        switch (i) {
            case 2:
                startActivityForResult(new Intent(getBaseContext(), (Class<?>) SettingsActivity.class), 0);
                break;
            case 3:
                new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Skip?").setMessage("Are you sure you want to skip this level?").setPositiveButton("Yes", new class_27(this)).setNegativeButton("No", (DialogInterface.OnClickListener) null).show();
                break;
            case 4:
                class_1061.method_3076().field_6414 = class_1061.method_3076().field_6414 ? false : true;
                break;
            case 5:
                new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Restart?").setMessage("Are you sure you want to restart this level?").setPositiveButton("Yes", new class_28(this)).setNegativeButton("No", (DialogInterface.OnClickListener) null).show();
                break;
            case 6:
                class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
                class_1061VarMethod_3076.field_6366 = class_1061VarMethod_3076.field_6366 ? false : true;
                break;
            case 9:
                class_1061 class_1061VarMethod_30762 = class_1061.method_3076();
                if (!class_1061VarMethod_30762.field_6369) {
                    class_1061VarMethod_30762.field_6369 = true;
                } else {
                    class_1061VarMethod_30762.field_6369 = false;
                }
                break;
            case 10:
                class_1061 class_1061VarMethod_30763 = class_1061.method_3076();
                String strMethod_2636 = class_988.method_2636("menus.ingame.multiplayerClose.titleDisconnect", new Object[0]);
                String strMethod_26362 = class_988.method_2636("menus.ingame.multiplayerClose.messageDisconnect", new Object[0]);
                String strMethod_26363 = class_988.method_2636("menus.ingame.multiplayerClose.disconnectButton", new Object[0]);
                if (class_1061VarMethod_30763.field_6352.field_5851) {
                    strMethod_2636 = class_988.method_2636("menus.ingame.multiplayerClose.title", new Object[0]);
                    strMethod_26362 = class_988.method_2636("menus.ingame.multiplayerClose.messageEndGame", new Object[0]);
                    strMethod_26363 = class_988.method_2636("menus.ingame.exitGame", new Object[0]);
                }
                AlertDialog.Builder negativeButton = new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(strMethod_2636).setMessage(strMethod_26362).setPositiveButton(strMethod_26363, new class_32(this)).setNegativeButton(class_988.method_2636("menus.common.back", new Object[0]), (DialogInterface.OnClickListener) null);
                if (class_1061VarMethod_30763.field_6352.field_5851) {
                    negativeButton.setNeutralButton(class_988.method_2636("menus.ingame.multiplayerClose.returnToBattleroom", new Object[0]), new class_33(this));
                }
                negativeButton.show();
                break;
            case 11:
                class_1061 class_1061VarMethod_30764 = class_1061.method_3076();
                if (class_1061VarMethod_30764.field_6411 != null && class_1061VarMethod_30764.field_6411.field_7102 != null) {
                    class_1061VarMethod_30764.method_3006("Briefing", class_1061VarMethod_30764.field_6411.field_7102);
                    break;
                }
                break;
            case 12:
                class_29 class_29Var = new class_29(this, this);
                if (!class_84.method_111(this, class_29Var)) {
                    class_29Var.run();
                }
                break;
            case 13:
                $invoke$special$makeSendMessagePopup(false);
                break;
            case 14:
                if (class_1061.method_3076().field_6352 != null) {
                    class_1001.method_2813();
                }
                break;
            case 15:
                new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit?").setMessage("Are you sure you want to exit this game?").setPositiveButton("Yes", new class_34(this)).setNegativeButton("No", (DialogInterface.OnClickListener) null).show();
                break;
            case 16:
                $invoke$special$makeSendMessagePopup(true);
                break;
            case 18:
                if (class_84.method_137(this)) {
                    $invoke$special$makeExportMapPopup(null);
                }
                break;
            case 19:
                new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Disconnect?").setMessage("Are you sure you want to surrender this game?").setPositiveButton("Surrender", new class_31(this)).setNegativeButton("No", (DialogInterface.OnClickListener) null).show();
                break;
            case 20:
                finish();
                break;
            case 21:
                finish();
                MultiplayerBattleroomActivity.updateUI();
                MultiplayerBattleroomActivity.refreshChatLog();
                break;
            case 22:
                class_1061 class_1061VarMethod_30765 = class_1061.method_3076();
                class_1061VarMethod_30765.field_6401 = true;
                class_1061VarMethod_30765.field_6347.field_5613 = false;
                break;
            case 23:
                class_1061.method_3043("TODO display leaderboard settings");
                break;
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 82) {
            return super.onKeyDown(i, keyEvent);
        }
        if (i == 25) {
            return super.onKeyDown(i, keyEvent);
        }
        if (i == 24) {
            return super.onKeyDown(i, keyEvent);
        }
        if (i == 84) {
            class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
            if (class_1061VarMethod_3076.field_6402 == 1.0f) {
                class_1061VarMethod_3076.field_6402 = 1.5f;
            } else if (class_1061VarMethod_3076.field_6402 == 1.5f) {
                class_1061VarMethod_3076.field_6402 = 0.75f;
            } else {
                class_1061VarMethod_3076.field_6402 = 1.0f;
            }
        }
        if (i == 4) {
            if (keyEvent.getSource() == 8194) {
                class_1061.method_3043("KEYCODE_BACK from mouse");
                class_1061 class_1061VarMethod_30762 = class_1061.method_3076();
                this.gameViewCommon.getCurrTouchPoint().method_148(class_1061VarMethod_30762.field_6450, class_1061VarMethod_30762.field_6451, true);
                return true;
            }
            onBackPressed();
        }
        return class_1061.method_3076().method_2992(i, keyEvent);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i == 4 && keyEvent.getSource() == 8194) {
            class_1061.method_3043("onKeyUp from mouse: KEYCODE_BACK");
            class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
            this.gameViewCommon.getCurrTouchPoint().method_148(class_1061VarMethod_3076.field_6450, class_1061VarMethod_3076.field_6451, false);
        }
        if (i == 82) {
            return super.onKeyDown(i, keyEvent);
        }
        return class_1061.method_3076().method_3027(i, keyEvent);
    }

    @Override // android.app.Activity
    public boolean onTrackballEvent(MotionEvent motionEvent) {
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        class_1061VarMethod_3076.field_6466 += motionEvent.getX();
        class_1061VarMethod_3076.field_6467 += motionEvent.getY();
        return (motionEvent.getAction() == 0 || motionEvent.getAction() == 1) ? false : true;
    }

    private /* synthetic */ void $invoke$special$makeSendMessagePopup(boolean z) {
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (!z) {
            builder.setTitle("Send Message");
        } else {
            builder.setTitle("Send Team Message");
        }
        View viewInflate = LayoutInflater.from(this).inflate(R.layout.alert_chat, (ViewGroup) null);
        builder.setView(viewInflate);
        TextView textView = (TextView) viewInflate.findViewById(R.id.chat_messages);
        EditText editText = (EditText) viewInflate.findViewById(R.id.chat_text);
        textView.setText(class_1061VarMethod_3076.field_6352.field_5878.method_2695());
        editText.setText(VariableScope.nullOrMissingString);
        editText.requestFocus();
        builder.setPositiveButton(z ? "Send Team" : "Send", new class_35(this, editText, z));
        builder.setNeutralButton("Send & Ping Map", new class_15(this, editText, z));
        builder.setNegativeButton("Cancel", new class_16(this));
        builder.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void $invoke$special$makeExportMapPopup(String str) {
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Export Map");
        builder.setMessage("Enter a name to export the map as");
        EditText editText = new EditText(this);
        if (str == null) {
            str = ("New " + class_1061VarMethod_3076.method_2980() + " (" + class_907.method_2272("d MMM yyyy").replace(".", VariableScope.nullOrMissingString) + " " + class_907.method_2272("HH.mm.ss") + ")").replace("  ", " ");
        }
        editText.setText(str);
        builder.setView(editText);
        builder.setPositiveButton("Ok", new class_17(this, editText, class_1061VarMethod_3076));
        builder.setNegativeButton("Cancel", new class_19(this));
        builder.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void $invoke$special$makeSaveGamePopup(String str) {
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save Game");
        builder.setMessage("Enter a name to save the game under");
        EditText editText = new EditText(this);
        if (str == null) {
            editText.setText(class_1061VarMethod_3076.method_2980() + " (" + class_907.method_2272("d MMM yyyy").replace(".", VariableScope.nullOrMissingString) + " " + class_907.method_2272("HH.mm.ss") + ")");
        } else {
            editText.setText(str);
        }
        builder.setView(editText);
        builder.setPositiveButton("Ok", new class_20(this, editText));
        builder.setNegativeButton("Cancel", new class_22(this));
        builder.show();
    }

    @Override // android.app.Activity
    public Dialog onCreateDialog(int i) {
        switch (i) {
            case 0:
                ProgressDialog progressDialog = new ProgressDialog(this);
                this.progressDialog = progressDialog;
                progressDialog.setProgressStyle(0);
                this.progressDialog.setMessage("Saving...");
                this.progressDialog.setCancelable(false);
                return this.progressDialog;
            default:
                return null;
        }
    }

    public void saveGame(String str) {
        showDialog(0);
        class_36 class_36Var = new class_36(this);
        class_36Var.field_196 = str;
        new Thread(class_36Var).start();
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        if (class_1061.method_3076().field_6352.field_5850) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(android.R.drawable.ic_dialog_info);
            builder.setTitle(class_988.method_2636("menus.ingame.multiplayerClose.title", new Object[0]));
            builder.setMessage(class_988.method_2636("menus.ingame.multiplayerClose.message", new Object[0]));
            builder.setPositiveButton(class_988.method_2636("menus.ingame.multiplayerClose.disconnectButton", new Object[0]), new class_23(this));
            builder.setNeutralButton(class_988.method_2636("menus.ingame.multiplayerClose.minimizeButton", new Object[0]), new class_24(this));
            builder.setNegativeButton(class_988.method_2636("menus.ingame.multiplayerClose.stayButton", new Object[0]), new class_25(this));
            builder.show();
            return;
        }
        finish();
    }

    public void openMarketLink() {
        this.uiHandler.post(new class_26(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void $invoke$special$openMarketLinkInternal() {
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.corrodinggames.rts")));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Failed to open Android Market", 0).show();
        }
    }

    public void showPCMainMenu() {
    }

    public void showLeaderboardSettingsWindow() {
    }
}
