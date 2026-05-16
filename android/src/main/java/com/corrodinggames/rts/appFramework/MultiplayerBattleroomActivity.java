package com.corrodinggames.rts.appFramework;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import cn.tesseract.union.hook.PlatformHook;
import cn.tesseract.union.hook.SpawnPositionHook;
import com.corrodinggames.rts.R;
import com.corrodinggames.rts.game.class_324;
import com.corrodinggames.rts.game.units.class_426;
import com.corrodinggames.rts.game.units.custom.logicBooleans.VariableScope;
import com.corrodinggames.rts.gameFramework.class_1061;
import com.corrodinggames.rts.gameFramework.class_768;
import com.corrodinggames.rts.gameFramework.e.class_899;
import com.corrodinggames.rts.gameFramework.h.class_988;
import com.corrodinggames.rts.gameFramework.j.class_1001;
import com.corrodinggames.rts.gameFramework.j.class_1011;
import com.corrodinggames.rts.gameFramework.j.class_1016;
import com.corrodinggames.rts.gameFramework.j.class_1047;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.fory.shaded.org.codehaus.janino.Descriptor;

/* JADX INFO: loaded from: classes.dex */
public class MultiplayerBattleroomActivity extends class_1 {
    public static final int REQUEST_ENABLE_BT_CLIENT = 2;
    public static final int REQUEST_ENABLE_BT_SERVER = 1;
    public static AlertDialog currentAskPasswordAlert;
    public static class_1011 currentAskPasswordCallBack;
    public static MultiplayerBattleroomActivity lastLoaded;
    public static boolean missedStartGame = false;
    public Button addAI;
    public Button changeTeam;
    public TextView chatLog;
    public LinearLayout chatLogWrap;
    public EditText chatMessage;
    public String[] currentDropdownRawArray;
    public Button gameOptions;
    public TextView gameSummary;
    public boolean hadProxyControl;
    public ScrollView mainScrollView;
    public Spinner mapDropdown;
    public LinearLayout mapLayout;
    public ImageView mapThumbnail;
    public ExpandedListView networkPlayerList;
    public TableLayout playerListTable;
    public ArrayAdapter playersAdapter;
    public CheckBox readyCheckbox;
    public Button startBluetoothButton;
    public Button startNetButton;
    public TextView status_info;
    public Spinner typeDropdown;
    public LinearLayout typeLayout;
    public boolean onCreateFinished = false;
    public final Handler uiHandler = new Handler();
    public boolean activityVisible = true;
    public String mapThumbnailLastLoaded = VariableScope.nullOrMissingString;
    public int currentDropdownMapType = -1;
    public Handler handler = new class_132(this);
    public Runnable updateRunnable = new class_134(this);
    public ArrayList activeTextViews = new ArrayList();
    public ArrayList deletedTextViews = new ArrayList();
    public Runnable startGameRunnable = new class_146(this);

    public void checkForDelayedAskPassword() {
        $invoke$special$checkForDelayedAskPassword();
    }

    public void refreshMapThumbnail() {
        $invoke$special$refreshMapThumbnail();
    }

    public void refreshServerInfo() {
        $invoke$special$refreshServerInfo();
    }

    public void setMapDropdownFromServer() {
        $invoke$special$setMapDropdownFromServer();
    }

    public void setupPlayerColorDropDown(Spinner spinner, boolean z, boolean z2, class_324 class_324Var) {
        PlatformHook.setupPlayerColorDropDown(this, spinner, z, z2, class_324Var);
    }

    public void setupSpawnPositionDropDown(Spinner spinner, boolean z) {
        SpawnPositionHook.setupSpawnPositionDropDown(this, spinner, z);
    }

    public void setupTeamAllyDropDown(Spinner spinner, boolean z) {
        SpawnPositionHook.setupTeamAllyDropDown(this, spinner, z);
    }

    public void showPlayerEditPopup(class_324 class_324Var) {
        $invoke$special$showPlayerEditPopup(class_324Var);
    }

    public void updateControlVisibility() {
        $invoke$special$updateControlVisibility();
    }

    public void updatePlayerList() {
        $invoke$special$updatePlayerList();
    }

    public static boolean isActivityVisible() {
        MultiplayerBattleroomActivity multiplayerBattleroomActivity = lastLoaded;
        if (multiplayerBattleroomActivity == null) {
            return false;
        }
        return multiplayerBattleroomActivity.activityVisible;
    }

    @Override // android.app.Activity
    public void onPause() {
        this.activityVisible = false;
        class_1061.method_3076().field_6352.method_2701();
        super.onPause();
    }

    @Override // com.corrodinggames.rts.appFramework.class_1, android.app.Activity
    public void onResume() {
        this.activityVisible = true;
        class_1061 class_1061VarMethod_3037 = class_1061.method_3037(this);
        class_1061VarMethod_3037.field_6352.method_2702();
        if (class_1061VarMethod_3037.field_6352 != null && class_1061VarMethod_3037.field_6352.field_5898) {
            class_1061.method_3031("MultiplayerBattleroomActivity:onResume: gameHasBeenStarted");
            if (missedStartGame) {
                class_1061.method_3031("MultiplayerBattleroomActivity:onResume: missed start game, calling now");
                startGame();
                missedStartGame = false;
            }
            finish();
        }
        if (class_1061VarMethod_3037.field_6352 != null && !class_1061VarMethod_3037.field_6352.field_5850) {
            finish();
        }
        this.hadProxyControl = false;
        $invoke$special$refreshServerInfo();
        missedStartGame = false;
        class_84.method_115(this, false);
        $invoke$special$checkForDelayedAskPassword();
        reshowAskPassword();
        super.onResume();
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (class_84.method_127(this, false)) {
            class_1061 class_1061VarMethod_3037 = class_1061.method_3037(this);
            setContentView(R.layout.multiplayer_battleroom);
            class_84.method_120(getWindow().getDecorView().findViewById(android.R.id.content));
            getWindow().setBackgroundDrawable(null);
            this.mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);
            this.networkPlayerList = (ExpandedListView) findViewById(R.id.networkPlayerList);
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
            this.playersAdapter = arrayAdapter;
            this.networkPlayerList.setAdapter((ListAdapter) arrayAdapter);
            this.playerListTable = (TableLayout) findViewById(R.id.battleroom_playerTable);
            $invoke$special$updatePlayerList();
            this.onCreateFinished = false;
            lastLoaded = this;
            this.status_info = (TextView) findViewById(R.id.battleroom_status_info);
            ImageView imageView = (ImageView) findViewById(R.id.battleroom_thumbnail);
            this.mapThumbnail = imageView;
            imageView.setVisibility(8);
            this.typeDropdown = (Spinner) findViewById(R.id.battleroom_type);
            ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, class_1016.method_2840());
            arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.typeDropdown.setAdapter((SpinnerAdapter) arrayAdapter2);
            this.typeDropdown.setSelection(class_1061VarMethod_3037.field_6352.field_5874.field_6012.ordinal());
            this.typeDropdown.setOnItemSelectedListener(new class_129(this));
            this.mapDropdown = (Spinner) findViewById(R.id.battleroom_map);
            updateMapDropdown(true);
            this.mapDropdown.setOnItemSelectedListener(new class_145(this));
            $invoke$special$setMapDropdownFromServer();
            this.gameSummary = (TextView) findViewById(R.id.battleroom_game_summary);
            this.mapLayout = (LinearLayout) findViewById(R.id.battleroom_mapLayout);
            this.typeLayout = (LinearLayout) findViewById(R.id.battleroom_typeLayout);
            this.chatLogWrap = (LinearLayout) findViewById(R.id.chatLogWrap);
            this.changeTeam = (Button) findViewById(R.id.battleroom_changeTeam);
            this.gameOptions = (Button) findViewById(R.id.battleroom_otherGameOptions);
            this.addAI = (Button) findViewById(R.id.battleroom_addAI);
            this.startNetButton = (Button) findViewById(R.id.battleroom_startNetButton);
            this.startBluetoothButton = (Button) findViewById(R.id.battleroom_startBluetoothButton);
            $invoke$special$updateControlVisibility();
            this.startBluetoothButton.setOnClickListener(new class_156(this));
            this.changeTeam.setOnClickListener(new class_157(this));
            this.gameOptions.setOnClickListener(new class_158(this));
            this.addAI.setOnClickListener(new class_161(this));
            this.startNetButton.setOnClickListener(new class_162(this));
            CheckBox checkBox = (CheckBox) findViewById(R.id.battleroom_ready);
            this.readyCheckbox = checkBox;
            checkBox.setOnCheckedChangeListener(new class_163(this));
            TextView textView = (TextView) findViewById(R.id.chatLog);
            this.chatLog = textView;
            textView.setText(Html.fromHtml(class_1061VarMethod_3037.field_6352.field_5878.method_2698()));
            this.chatMessage = (EditText) findViewById(R.id.battleroom_text);
            ((Button) findViewById(R.id.battleroom_send)).setOnClickListener(new class_164(this));
            this.chatMessage.setOnKeyListener(new class_130(this));
            $invoke$special$refreshServerInfo();
            getWindow().setSoftInputMode(2);
            this.onCreateFinished = true;
        }
    }

    public void updateMapDropdown(boolean z) {
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        class_1061.method_3043("updateMapDropdown firstRun:".concat(String.valueOf(z)));
        int selectedItemPosition = this.typeDropdown.getSelectedItemPosition();
        int i = 0;
        if (!class_1061VarMethod_3076.field_6352.field_5851) {
            selectedItemPosition = 0;
        }
        if (this.currentDropdownMapType == selectedItemPosition) {
            class_1061.method_3043("updateMapDropdown: Same type: " + selectedItemPosition + " already selected");
            return;
        }
        this.currentDropdownMapType = selectedItemPosition;
        this.currentDropdownRawArray = null;
        ArrayList arrayList = new ArrayList();
        if (selectedItemPosition == 0) {
            String[] strArrMethod_2168 = class_899.method_2168(LevelGroupSelectActivity.skirmishLevelsDir, true);
            this.currentDropdownRawArray = strArrMethod_2168;
            Arrays.sort(strArrMethod_2168);
            String[] strArr = this.currentDropdownRawArray;
            int length = strArr.length;
            while (i < length) {
                arrayList.add(LevelSelectActivity.convertLevelFileNameForDisplay(strArr[i]));
                i++;
            }
        } else if (selectedItemPosition == 1) {
            this.currentDropdownRawArray = class_899.method_2168(LevelGroupSelectActivity.customLevelsDir, true);
            String[] strArrMethod_2661 = class_1061VarMethod_3076.field_6354.method_2661(this.currentDropdownRawArray, LevelGroupSelectActivity.customLevelsDir);
            this.currentDropdownRawArray = strArrMethod_2661;
            if (strArrMethod_2661 == null) {
                if (!class_84.method_129(this)) {
                    class_1061VarMethod_3076.method_3056("Permission not yet granted to read storage");
                } else {
                    class_1061VarMethod_3076.method_3056("Could not find folder: /SD/rustedWarfare/maps");
                }
                this.currentDropdownRawArray = new String[0];
            }
            Arrays.sort(this.currentDropdownRawArray);
            String[] strArr2 = this.currentDropdownRawArray;
            int length2 = strArr2.length;
            while (i < length2) {
                arrayList.add(LevelSelectActivity.convertLevelFileNameForDisplay(strArr2[i]));
                i++;
            }
        } else if (selectedItemPosition == 2) {
            String[] gameSaves = LoadLevelActivity.getGameSaves();
            this.currentDropdownRawArray = gameSaves;
            if (gameSaves == null) {
                if (!class_84.method_129(this)) {
                    class_1061VarMethod_3076.method_3056("Permission not yet granted to read storage");
                } else {
                    class_1061VarMethod_3076.method_3056("Could not find a save folder on storage");
                }
                this.currentDropdownRawArray = new String[0];
            }
            String[] strArr3 = this.currentDropdownRawArray;
            int length3 = strArr3.length;
            while (i < length3) {
                arrayList.add(LevelSelectActivity.convertLevelFileNameForDisplay(strArr3[i]));
                i++;
            }
        } else {
            throw new RuntimeException("Unknown typeIndex:".concat(String.valueOf(selectedItemPosition)));
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mapDropdown.setAdapter((SpinnerAdapter) arrayAdapter);
        if (z) {
            $invoke$special$setMapDropdownFromServer();
        }
        gameInfoChanged();
        class_1061.method_3043("updateMapDropdown end");
    }

    public String getMapDropdownSelected() {
        int selectedItemPosition = this.mapDropdown.getSelectedItemPosition();
        if (selectedItemPosition == -1) {
            return null;
        }
        return this.currentDropdownRawArray[selectedItemPosition];
    }

    public void readInterfaceIntoNetworkSettings() {
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if (class_1061VarMethod_3076.field_6352.field_5851) {
            String mapDropdownSelected = getMapDropdownSelected();
            if (mapDropdownSelected == null) {
                mapDropdownSelected = "<No Map>";
            }
            if (class_1061VarMethod_3076.field_6352.field_5874.field_6013 == null || !class_1061VarMethod_3076.field_6352.field_5874.field_6013.equals(mapDropdownSelected)) {
                class_1061.method_3043("Changing map to:".concat(String.valueOf(mapDropdownSelected)));
            }
            class_1061VarMethod_3076.field_6352.field_5874.field_6013 = mapDropdownSelected;
            int selectedItemPosition = this.typeDropdown.getSelectedItemPosition();
            class_1061VarMethod_3076.field_6352.field_5874.field_6012 = class_1016.values()[selectedItemPosition];
        }
    }

    public void gameInfoChanged() {
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if (class_1061VarMethod_3076.field_6352.field_5851) {
            readInterfaceIntoNetworkSettings();
            if (class_1061VarMethod_3076.field_6352.field_5977) {
                class_1047.method_2949();
            }
            class_1061VarMethod_3076.field_6352.method_2821();
            $invoke$special$refreshMapThumbnail();
        }
    }

    public void sendChat() {
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        String string = this.chatMessage.getText().toString();
        if (!string.trim().equals(VariableScope.nullOrMissingString)) {
            class_1061VarMethod_3076.field_6352.method_2816(string);
        }
        this.chatMessage.setText(VariableScope.nullOrMissingString);
    }

    public static void refreshChatLog() {
        MultiplayerBattleroomActivity multiplayerBattleroomActivity = lastLoaded;
        if (multiplayerBattleroomActivity != null) {
            lastLoaded.uiHandler.post(new class_131(multiplayerBattleroomActivity));
        }
    }

    public static void addMessageToChatLog(String str) {
        MultiplayerBattleroomActivity multiplayerBattleroomActivity = lastLoaded;
        if (multiplayerBattleroomActivity != null) {
            Message messageObtainMessage = multiplayerBattleroomActivity.handler.obtainMessage();
            messageObtainMessage.getData().putString("text", str);
            multiplayerBattleroomActivity.handler.sendMessage(messageObtainMessage);
        }
    }

    public void addMessageToChatLogInternal(String str) {
        refreshChatLogDirect();
    }

    public void refreshChatLogDirect() {
        if (!this.onCreateFinished) {
            class_1061.method_3031("addMessageToChatLogInternal: !onCreateFinished");
            return;
        }
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        Spanned spannedFromHtml = Html.fromHtml(class_1061VarMethod_3076.field_6352.field_5878.method_2698());
        TextView textView = this.chatLog;
        if (textView == null) {
            throw new RuntimeException("chatLog==null");
        }
        if (spannedFromHtml == null) {
            throw new RuntimeException("chatLogHTML==null");
        }
        try {
            textView.clearFocus();
            this.chatLog.setTextKeepState(spannedFromHtml);
        } catch (NullPointerException e) {
            class_1061.method_3010("chatLog.setText error", e);
            class_1061VarMethod_3076.method_3056("chatLog.setText error");
        }
    }

    public static void closeIfOpen(String str, String str2) {
        MultiplayerBattleroomActivity multiplayerBattleroomActivity = lastLoaded;
        if (multiplayerBattleroomActivity != null) {
            lastLoaded.uiHandler.post(new class_133(multiplayerBattleroomActivity, str2));
        }
    }

    public static void updateUI() {
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if (class_1061VarMethod_3076.field_6352 != null) {
            class_1061VarMethod_3076.field_6352.method_2823();
        }
        if (!class_1061.field_6304) {
            if (class_1061VarMethod_3076.field_6352 == null || !class_1061VarMethod_3076.field_6352.field_5898) {
                MultiplayerBattleroomActivity multiplayerBattleroomActivity = lastLoaded;
                if (multiplayerBattleroomActivity != null) {
                    multiplayerBattleroomActivity.uiHandler.post(multiplayerBattleroomActivity.updateRunnable);
                } else {
                    class_1061.method_3031("MultiplayerBattleroomActivity:updateUI() lastLoaded==null");
                }
            }
        }
    }

    private /* synthetic */ void $invoke$special$checkForDelayedAskPassword() {
        synchronized (this) {
            class_1061 class_1061VarMethod_3037 = class_1061.method_3037(this);
            if (class_1061VarMethod_3037.field_6352.field_5849) {
                askPasswordInternal(class_1001.field_5933);
                class_1061VarMethod_3037.field_6352.field_5849 = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void $invoke$special$updateControlVisibility() {
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if (class_1061VarMethod_3076.field_6352.field_5851 || class_1061VarMethod_3076.field_6352.field_5856) {
            this.mapLayout.setVisibility(0);
            if (!class_1061VarMethod_3076.field_6352.field_5856) {
                this.typeLayout.setVisibility(0);
            } else {
                this.typeLayout.setVisibility(8);
            }
            this.changeTeam.setVisibility(8);
            this.gameOptions.setVisibility(0);
            this.addAI.setVisibility(0);
            this.startNetButton.setVisibility(0);
            this.gameSummary.setVisibility(8);
        } else {
            this.mapLayout.setVisibility(8);
            this.typeLayout.setVisibility(8);
            if (class_1061VarMethod_3076.field_6352.field_5874.field_6024) {
                this.changeTeam.setVisibility(8);
            } else {
                this.changeTeam.setVisibility(0);
            }
            this.gameOptions.setVisibility(8);
            this.addAI.setVisibility(8);
            this.startNetButton.setVisibility(8);
            this.gameSummary.setVisibility(0);
        }
        if (class_1061VarMethod_3076.field_6352.field_5854) {
            this.chatLogWrap.setVisibility(8);
        } else {
            this.chatLogWrap.setVisibility(0);
        }
        if (!class_1001.field_5899) {
            this.startBluetoothButton.setVisibility(8);
        }
    }

    private /* synthetic */ void $invoke$special$setMapDropdownFromServer() {
        class_1061.method_3043("Battleroom: setMapDropdownFromServer");
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if (class_1061VarMethod_3076.field_6352 == null) {
            class_1061.method_3043("setMapDropDownFromServer: game.network");
            return;
        }
        String[] strArr = this.currentDropdownRawArray;
        String str = class_1061VarMethod_3076.field_6352.field_5874.field_6013;
        if (str == null) {
            class_1061.method_3043("setMapDropDownFromServer: currentMap==null");
            return;
        }
        String lowerCase = class_899.method_2194(str).replaceAll("\\.tmx$", VariableScope.nullOrMissingString).replaceAll("\\\\", "/").toLowerCase();
        class_1061.method_3043("Battleroom: setMapDropdownFromServer: ".concat(String.valueOf(lowerCase)));
        boolean z = false;
        int i = 0;
        while (true) {
            if (i >= strArr.length) {
                break;
            }
            String lowerCase2 = strArr[i];
            if (lowerCase2 != null) {
                lowerCase2 = class_899.method_2194(lowerCase2).replaceAll("\\.tmx$", VariableScope.nullOrMissingString).replaceAll("\\\\", "/").toLowerCase();
            }
            if (!"/".concat(String.valueOf(lowerCase)).endsWith("/".concat(String.valueOf(lowerCase2)))) {
                i++;
            } else {
                class_1061.method_3043("Found map in dropdown index:" + i + " map:" + lowerCase2);
                this.mapDropdown.setSelection(i);
                z = true;
                break;
            }
        }
        if (!z) {
            class_1061.method_3043("Could not find map in dropdown: ".concat(String.valueOf(lowerCase)));
        }
    }

    private /* synthetic */ void $invoke$special$refreshMapThumbnail() {
        Bitmap bitmapMethod_136;
        class_1061.method_3076();
        String strMethod_2705 = class_1001.method_2705();
        this.mapThumbnail.setVisibility(8);
        if (strMethod_2705 != null && (bitmapMethod_136 = class_84.method_136(strMethod_2705)) != null) {
            if (!this.mapThumbnailLastLoaded.equals(strMethod_2705)) {
                this.mapThumbnailLastLoaded = strMethod_2705;
                this.mapThumbnail.setImageBitmap(bitmapMethod_136);
            }
            this.mapThumbnail.setVisibility(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:43:0x013a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public /* synthetic */ void $invoke$special$refreshServerInfo() {
        /*
            Method dump skipped, instruction units count: 1049
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.corrodinggames.rts.appFramework.MultiplayerBattleroomActivity.$invoke$special$refreshServerInfo():void");
    }

    public void markAllActiveCellsDeleted() {
        for (int size = this.activeTextViews.size() - 1; size >= 0; size--) {
            class_169 class_169Var = (class_169) this.activeTextViews.get(size);
            class_169Var.field_484.removeView(class_169Var.field_483);
            class_169Var.field_484 = null;
        }
        this.deletedTextViews.addAll(this.activeTextViews);
        this.activeTextViews.clear();
    }

    public TextView addCell(TableRow tableRow, String str, int i) {
        TextView textView;
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        int size = this.deletedTextViews.size() - 1;
        while (true) {
            if (size < 0) {
                textView = null;
                break;
            }
            class_169 class_169Var = (class_169) this.deletedTextViews.get(size);
            if (class_169Var.field_482 != i) {
                size--;
            } else {
                textView = class_169Var.field_483;
                this.deletedTextViews.remove(size);
                break;
            }
        }
        if (textView == null) {
            textView = new TextView(tableRow.getContext());
            textView.setBackgroundResource(R.drawable.cell_shape);
            textView.setTextAppearance(tableRow.getContext(), android.R.attr.textAppearanceMedium);
            textView.setPadding(class_1061VarMethod_3076.method_2986(5.0f), class_1061VarMethod_3076.method_2986(5.0f), class_1061VarMethod_3076.method_2986(5.0f), class_1061VarMethod_3076.method_2986(5.0f));
        }
        class_169 class_169Var2 = new class_169(this);
        class_169Var2.field_483 = textView;
        class_169Var2.field_482 = i;
        class_169Var2.field_484 = tableRow;
        this.activeTextViews.add(class_169Var2);
        textView.setText(str);
        tableRow.addView(textView);
        return textView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void $invoke$special$showPlayerEditPopup(class_324 class_324Var) {
        String str;
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if (class_1061VarMethod_3076.field_6352.method_2706() || (class_1061VarMethod_3076.field_6352.field_5848 == class_324Var && !class_1061VarMethod_3076.field_6352.field_5874.field_6024)) {
            if (class_324Var.field_1468 != null) {
                str = class_324Var.field_1468;
            } else {
                str = "unnamed";
            }
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.multiplayer_battleroom_playerpopup);
            class_84.method_120(dialog.getWindow().getDecorView().findViewById(android.R.id.content));
            dialog.setTitle("Player: ".concat(String.valueOf(str)));
            dialog.getWindow().setSoftInputMode(2);
            Spinner spinner = (Spinner) dialog.findViewById(R.id.teamId);
            Spinner spinner2 = (Spinner) dialog.findViewById(R.id.teamAllyGroup);
            LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.teamAllyGroupWrap);
            LinearLayout linearLayout2 = (LinearLayout) dialog.findViewById(R.id.aiDifficultyWrap);
            LinearLayout linearLayout3 = (LinearLayout) dialog.findViewById(R.id.playerOverridesSection);
            Spinner spinner3 = (Spinner) dialog.findViewById(R.id.aiDifficulty);
            Spinner spinner4 = (Spinner) dialog.findViewById(R.id.startingUnits);
            Spinner spinner5 = (Spinner) dialog.findViewById(R.id.playerColor);
            setupSpawnPositionDropDown(spinner, false);
            if (class_324Var.method_464()) {
                class_166.method_154(spinner, "-3");
            } else {
                class_166.method_154(spinner, new StringBuilder().append(class_324Var.field_1457).toString());
            }
            setupTeamAllyDropDown(spinner2, true);
            if (class_324Var.field_1457 % 2 != class_324Var.field_1464) {
                class_166.method_154(spinner2, new StringBuilder().append(class_324Var.field_1464 + 1).toString());
            } else {
                class_166.method_154(spinner2, "0");
            }
            setupAIDifficultyDropDown(spinner3, true);
            if (class_324Var.field_1398 == null) {
                class_166.method_154(spinner3, "-99");
            } else {
                class_166.method_154(spinner3, new StringBuilder().append(class_324Var.field_1398).toString());
            }
            setupStartingUnitsDropDown(spinner4, true);
            if (class_324Var.field_1399 == null) {
                class_166.method_154(spinner4, "-99");
            } else {
                class_166.method_154(spinner4, new StringBuilder().append(class_324Var.field_1399).toString());
            }
            setupPlayerColorDropDown(spinner5, true, true, class_324Var);
            if (class_324Var.field_1401 == null) {
                class_166.method_154(spinner5, "-99");
            } else {
                class_166.method_154(spinner5, new StringBuilder().append(class_324Var.field_1401).toString());
            }
            if (!class_1061VarMethod_3076.field_6352.field_5851) {
                linearLayout3.setVisibility(8);
            }
            if (!class_324Var.field_1469) {
                linearLayout2.setVisibility(8);
            }
            linearLayout.setVisibility(0);
            if (!class_1061VarMethod_3076.field_6352.method_2706() && class_1061VarMethod_3076.field_6352.field_5874.field_6025) {
                linearLayout.setVisibility(8);
            }
            Button button = (Button) dialog.findViewById(R.id.battleroom_playerpopup_give);
            if (class_1061VarMethod_3076.field_6352.field_5856 && class_1061VarMethod_3076.field_6352.field_5848 != class_324Var) {
                button.setVisibility(0);
            } else {
                button.setVisibility(8);
            }
            button.setOnClickListener(new class_135(this, dialog, str, class_324Var));
            ((Button) dialog.findViewById(R.id.battleroom_playerpopup_cancel)).setOnClickListener(new class_139(this, dialog));
            ((Button) dialog.findViewById(R.id.battleroom_playerpopup_apply)).setOnClickListener(new class_140(this, spinner3, class_324Var, spinner4, spinner5, spinner, spinner2, dialog));
            Button button2 = (Button) dialog.findViewById(R.id.battleroom_playerpopup_kick);
            if (class_1061VarMethod_3076.field_6352.field_5848 == class_324Var) {
                button2.setVisibility(8);
            } else {
                button2.setVisibility(0);
            }
            button2.setOnClickListener(new class_141(this, dialog, class_324Var, str));
            dialog.show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void $invoke$special$updatePlayerList() {
        String str;
        String strValueOf;
        int iMethod_520;
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        for (int childCount = this.playerListTable.getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = this.playerListTable.getChildAt(childCount);
            if (childAt.getId() == -1) {
                this.playerListTable.removeView(childAt);
            }
        }
        markAllActiveCellsDeleted();
        for (class_324 class_324Var : class_324.method_485()) {
            if (class_324Var != null) {
                if (class_324Var.field_1468 == null) {
                    str = "unnamed";
                } else {
                    str = class_324Var.field_1468;
                }
                int iMethod_539 = class_324Var.method_539();
                if (iMethod_539 == -99) {
                    strValueOf = "HOST";
                } else if (class_324Var.field_1469) {
                    strValueOf = "-";
                } else if (iMethod_539 == -1) {
                    strValueOf = "N/A";
                } else if (iMethod_539 == -2) {
                    strValueOf = "-";
                } else if (class_324Var.field_1417 == 1) {
                    strValueOf = iMethod_539 + " (HOST)";
                } else {
                    strValueOf = String.valueOf(iMethod_539);
                }
                TableRow tableRow = new TableRow(this);
                tableRow.setBackgroundResource(android.R.drawable.list_selector_background);
                tableRow.setClickable(true);
                tableRow.setOnClickListener(new class_144(this, class_324Var));
                TextView textViewAddCell = addCell(tableRow, str, 1);
                if (class_324Var == class_1061VarMethod_3076.field_6352.field_5848) {
                    textViewAddCell.setTypeface(null, 1);
                } else {
                    textViewAddCell.setTypeface(null, 0);
                }
                if (class_324Var.field_1401 != null) {
                    textViewAddCell.setTextColor(class_324.method_522(class_324Var.field_1401.intValue()));
                } else {
                    textViewAddCell.setTextColor(-1);
                }
                String string = Integer.toString(class_324Var.field_1457 + 1);
                boolean zMethod_464 = class_324Var.method_464();
                if (zMethod_464) {
                    string = Descriptor.SHORT;
                }
                if (!zMethod_464 && class_324Var.field_1399 != null && class_324Var.field_1399.intValue() != class_1061VarMethod_3076.field_6352.field_5874.field_6018) {
                    string = string + " - " + class_1001.method_2774(class_324Var.field_1399.intValue());
                }
                TextView textViewAddCell2 = addCell(tableRow, string, 2);
                if (class_324Var.field_1464 == -3) {
                    iMethod_520 = class_324.method_522(-3);
                } else {
                    iMethod_520 = class_324.method_520(class_324Var.field_1457);
                }
                textViewAddCell2.setTextColor(iMethod_520);
                textViewAddCell2.setTypeface(null, 1);
                TextView textViewAddCell3 = addCell(tableRow, class_324.method_467(class_324Var.field_1464), 3);
                textViewAddCell3.setTypeface(null, 1);
                textViewAddCell3.setTextColor(class_324.method_522(class_324Var.field_1464));
                addCell(tableRow, strValueOf, 4);
                this.playerListTable.addView(tableRow);
                ViewGroup.LayoutParams layoutParams = tableRow.getLayoutParams();
                layoutParams.width = -1;
                tableRow.setLayoutParams(layoutParams);
            }
        }
    }

    public static void startGame() {
        MultiplayerBattleroomActivity multiplayerBattleroomActivity = lastLoaded;
        if (multiplayerBattleroomActivity != null) {
            multiplayerBattleroomActivity.uiHandler.post(multiplayerBattleroomActivity.startGameRunnable);
            missedStartGame = false;
        } else {
            class_1061.method_3031("MultiplayerBattleroomActivity:startGame() lastLoaded==null");
            class_1061.method_2969();
            missedStartGame = true;
        }
    }

    public static void warnIfTeamsUneven() {
        class_168 class_168Var = new class_168("Starting unit count");
        class_168 class_168Var2 = new class_168("Total unit HP");
        class_168 class_168Var3 = new class_168("Team Credits");
        for (class_324 class_324Var : class_324.method_499()) {
            class_426[] class_426VarArr = class_426.field_1908.field_7339;
            int size = class_426.field_1908.size();
            int i = 0;
            int i2 = 0;
            for (int i3 = 0; i3 < size; i3++) {
                class_426 class_426Var = class_426VarArr[i3];
                if (class_426Var.field_1927 == class_324Var) {
                    i++;
                    i2 = (int) (i2 + class_426Var.field_1983);
                }
            }
            if (i != 0) {
                class_168Var.method_157(class_324Var, i);
                class_168Var2.method_157(class_324Var, i2);
                class_168Var3.method_157(class_324Var, (int) class_324Var.field_1461);
            }
        }
        if (!class_168Var.method_156()) {
            class_168Var2.method_156();
        }
        class_168Var3.method_156();
    }

    public static void startGameCommon() {
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        class_1061VarMethod_3076.field_6471 = null;
        if (class_1061VarMethod_3076.field_6352.field_5874.field_6012 == class_1016.field_6031) {
            if (!class_1061VarMethod_3076.field_6352.field_5851) {
                class_1061VarMethod_3076.field_6355.method_1780(class_1061VarMethod_3076.field_6352.field_5876, true, false);
                class_1061VarMethod_3076.field_6347.field_5600.method_2596(null, "Note: Game was started from a saved game.");
            } else {
                class_1061VarMethod_3076.field_6355.method_1787(class_1061VarMethod_3076.field_6352.field_5874.field_6013, true);
            }
            warnIfTeamsUneven();
            return;
        }
        if (class_1061VarMethod_3076.field_6352.field_5874.field_6012 == class_1016.field_6030) {
            if (!class_1061VarMethod_3076.field_6352.field_5851) {
                class_1061VarMethod_3076.field_6470 = VariableScope.nullOrMissingString;
                class_1061VarMethod_3076.field_6471 = class_1061VarMethod_3076.field_6352.field_5877;
                class_1061VarMethod_3076.method_3013(true, class_768.field_4200);
                class_1061VarMethod_3076.field_6347.field_5600.method_2596(null, "Note: Game was started from a custom map on server.");
            } else {
                class_1061VarMethod_3076.field_6470 = class_1061VarMethod_3076.field_6352.field_5875;
                class_1061VarMethod_3076.method_3013(true, class_768.field_4200);
            }
            warnIfTeamsUneven();
            return;
        }
        class_1061VarMethod_3076.field_6470 = class_1061VarMethod_3076.field_6352.field_5875;
        class_1061VarMethod_3076.method_3013(true, class_768.field_4200);
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        if (class_1061.method_3076().field_6352.field_5854) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(android.R.drawable.ic_dialog_info);
            builder.setTitle(class_988.method_2636("menus.ingame.multiplayerClose.title", new Object[0]));
            builder.setMessage("What would you like to do?");
            builder.setPositiveButton(class_988.method_2636("menus.ingame.exitGame", new Object[0]), new class_147(this));
            builder.setNegativeButton(class_988.method_2636("menus.ingame.multiplayerClose.stayButton", new Object[0]), new class_148(this));
            builder.show();
            return;
        }
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setIcon(android.R.drawable.ic_dialog_info);
        builder2.setTitle(class_988.method_2636("menus.ingame.multiplayerClose.title", new Object[0]));
        builder2.setMessage(class_988.method_2636("menus.ingame.multiplayerClose.message", new Object[0]));
        builder2.setPositiveButton(class_988.method_2636("menus.ingame.multiplayerClose.disconnectButton", new Object[0]), new class_149(this));
        builder2.setNeutralButton(class_988.method_2636("menus.ingame.multiplayerClose.minimizeButton", new Object[0]), new class_150(this));
        builder2.setNegativeButton(class_988.method_2636("menus.ingame.multiplayerClose.stayButton", new Object[0]), new class_151(this));
        builder2.show();
    }

    public static void reshowAskPassword() {
        class_1011 class_1011Var = currentAskPasswordCallBack;
        if (class_1011Var == null) {
            return;
        }
        Context context = class_1061.method_3076().field_6316;
        AlertDialog alertDialog = currentAskPasswordAlert;
        if (alertDialog != null && alertDialog.getContext() == context) {
            class_1061.method_3043("reshowAskPassword: skipping, same context");
        } else {
            askPasswordInternal(class_1011Var);
        }
    }

    public static void askPasswordInternal(class_1011 class_1011Var) {
        String str;
        String strMethod_2638;
        AlertDialog.Builder builder = new AlertDialog.Builder(class_1061.method_3076().field_6316);
        if (class_1011Var.field_6001 == null) {
            str = "Password Required";
            strMethod_2638 = "This server requires a password to join";
        } else {
            strMethod_2638 = class_988.method_2638(class_1011Var.field_6001);
            str = "Server Question";
        }
        if (class_1011Var.field_6004 != null) {
            str = class_1011Var.field_6004;
        }
        builder.setTitle(str);
        builder.setMessage(strMethod_2638);
        EditText editText = new EditText(builder.getContext());
        builder.setView(editText);
        if (class_1011Var.field_6001 != null) {
            editText.setHint("Enter text...");
        } else {
            editText.setHint("Enter password...");
        }
        builder.setPositiveButton(class_1011Var.field_6005 != null ? class_1011Var.field_6005 : "Submit", new class_152(editText, class_1011Var));
        builder.setNegativeButton(class_1011Var.field_6006 != null ? class_1011Var.field_6006 : "Disconnect", new class_153(class_1011Var));
        builder.setOnCancelListener(new class_154(class_1011Var));
        AlertDialog alertDialog = currentAskPasswordAlert;
        if (alertDialog != null) {
            try {
                alertDialog.dismiss();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        AlertDialog alertDialogShow = builder.show();
        currentAskPasswordCallBack = class_1011Var;
        currentAskPasswordAlert = alertDialogShow;
        editText.requestFocus();
    }

    public void startBluetoothServerSetup() {
        class_1061.method_3076();
        if (class_1001.method_2703() != null) {
            Intent intent = new Intent("android.bluetooth.adapter.action.REQUEST_DISCOVERABLE");
            intent.putExtra("android.bluetooth.adapter.extra.DISCOVERABLE_DURATION", 900);
            startActivityForResult(intent, 1);
        }
    }

    @Override // android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        class_1061.method_3043("bluetooth: onActivityResult");
        if (i == 1) {
            if (i2 != 0) {
                startBluetoothServerReady();
            }
        } else if (i != 2) {
            super.onActivityResult(i, i2, intent);
        }
    }

    public void startBluetoothServerReady() {
        class_1061.method_3076().method_3056(class_988.method_2636("menus.battleroom.message.bluetoothReady", new Object[0]));
        class_1061.method_3043("bluetooth: startBluetoothServerReady");
    }

    public void findBluetoothServer() {
        class_1061.method_3043("bluetooth: findBluetoothServer");
        class_1061.method_3076();
        if (class_1001.method_2703() != null) {
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 2);
        }
    }

    public void findBluetoothServer2() {
        class_1061.method_3043("bluetooth: findBluetoothServer2");
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        BluetoothAdapter bluetoothAdapterMethod_2703 = class_1001.method_2703();
        if (bluetoothAdapterMethod_2703 != null) {
            String str = VariableScope.nullOrMissingString;
            for (BluetoothDevice bluetoothDevice : bluetoothAdapterMethod_2703.getBondedDevices()) {
                str = str + "\nFound device: " + bluetoothDevice.getName() + " Add: " + bluetoothDevice.getAddress();
            }
            class_1061.method_3043(str);
            class_1061VarMethod_3076.method_3032("devices", str);
        }
    }

    public void scrollToChat() {
        int[] iArr = new int[2];
        this.chatLog.getLocationInWindow(iArr);
        this.mainScrollView.post(new class_155(this, iArr[1]));
    }

    public void setupStartingUnitsDropDown(Spinner spinner, boolean z) {
        class_1061.method_3076();
        ArrayList arrayList = new ArrayList();
        if (z) {
            arrayList.add(new class_167("-99", class_988.method_2636("menus.settings.option.default", new Object[0]), null));
        }
        for (Integer num : class_1001.method_2783()) {
            arrayList.add(new class_167(num.toString(), class_1001.method_2774(num.intValue())));
        }
        class_166 class_166Var = new class_166(this, arrayList);
        class_166Var.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter((SpinnerAdapter) class_166Var);
    }

    public void setupAIDifficultyDropDown(Spinner spinner, boolean z) {
        ArrayList arrayList = new ArrayList();
        if (z) {
            arrayList.add(new class_167("-99", class_988.method_2636("menus.settings.option.default", new Object[0]), null));
        }
        for (int i = -2; i <= 3; i++) {
            arrayList.add(new class_167(String.valueOf(i), class_988.method_2636("menus.settings.option.ai.".concat(String.valueOf(i)), new Object[0]), null));
        }
        class_166 class_166Var = new class_166(this, arrayList);
        class_166Var.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter((SpinnerAdapter) class_166Var);
    }
}
