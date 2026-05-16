package com.corrodinggames.rts.appFramework;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import cn.tesseract.union.packet.PacketType;
import com.corrodinggames.rts.R;
import com.corrodinggames.rts.appFramework.android.AndroidSAF;
import com.corrodinggames.rts.game.b.class_299;
import com.corrodinggames.rts.game.units.custom.logicBooleans.VariableScope;
import com.corrodinggames.rts.gameFramework.SettingsEngine;
import com.corrodinggames.rts.gameFramework.class_1061;
import com.corrodinggames.rts.gameFramework.class_907;
import com.corrodinggames.rts.gameFramework.e.class_899;
import com.corrodinggames.rts.gameFramework.e.class_900;
import com.corrodinggames.rts.gameFramework.e.class_901;
import com.corrodinggames.rts.gameFramework.e.class_905;
import com.corrodinggames.rts.gameFramework.h.class_988;
import com.corrodinggames.rts.gameFramework.j.class_1047;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class SettingsActivity extends class_1 {
    public static final int SETUP_EXTERNAL_FOLDER = 9;
    public static boolean debugWasSetOrAskedThisSession = false;
    public static final String intentMode = "mode";
    public Spinner aiDifficulty;
    public CheckBox allowGameRecording;
    public CheckBox autoSaveEnabled;
    public CheckBox batterySaving;
    public CheckBox classicInterface;
    public Button confKeys;
    public Button debugOptions;
    public CheckBox doubleClickToAttackMove;
    public CheckBox enableSounds;
    public TextView externalFolderInfo;
    public CheckBox forceEnglish;
    public SeekBar gameVolume;
    public TextView gameVolumeText;
    public CheckBox gestureZoom;
    public CheckBox highRefreshRate;
    public CheckBox immersiveFullScreen;
    public SeekBar interfaceVolume;
    public TextView interfaceVolumeText;
    public CheckBox keyboardSupport;
    public int locationActionOld;
    public int locationDpadOld;
    public CheckBox mouseSupport;
    public SeekBar musicVolume;
    public TextView musicVolumeText;
    public EditText networkPort;
    public CheckBox newRender;
    public CheckBox quickRally;
    public CheckBox renderBackground;
    public CheckBox renderClouds;
    public CheckBox renderDoubleScale;
    public CheckBox renderExtraLayers;
    public boolean replaysDisabledByPermission;
    public CheckBox replaysShowRecordedChat;
    public CheckBox saveMultiplayerReplays;
    public SeekBar scrollSpeed;
    public TextView scrollSpeedText;
    public CheckBox sendReports;
    public SettingsEngine settings;
    public Button setupExternalFolder;
    public boolean setupExternalFolderOnly;
    public CheckBox shaderEffects;
    public CheckBox showFps;
    public CheckBox showHp;
    public CheckBox showMapPingsOnBattlefield;
    public CheckBox showMapPingsOnMinimap;
    public CheckBox showPlayerChatInGame;
    public CheckBox showUnitGroups;
    public CheckBox showUnitIcons;
    public CheckBox showUnitWaypoints;
    public CheckBox showWarLogOnScreen;
    public CheckBox smartSelection;
    public View storageLayout;
    public Spinner storageType;
    public CheckBox teamShaders;
    public Spinner teamUnitCapHostedGame;
    public Spinner teamUnitCapSinglePlayer;
    public CheckBox udpInMultiplayer;
    public CheckBox unlockedScreenRotation;
    public CheckBox useCircleSelect;
    public CheckBox useMinimapAllyColors;
    public CheckBox zoomButton;
    public boolean saveChanges = true;
    public int[] unitCapOptions = {100, 250, 500, 1000, PacketType.PACKET_SERVER_DEBUG, 5000, 10000};
    public class_246 linkExternalFolder = new class_225(this);

    @Override // com.corrodinggames.rts.appFramework.class_1, android.app.Activity
    public void onResume() {
        super.onResume();
        class_84.method_115(this, false);
    }

    public void saveSettings() {
        this.settings.enableSounds = true;
        this.settings.musicVolume = this.musicVolume.getProgress() / 100.0f;
        this.settings.gameVolume = this.gameVolume.getProgress() / 100.0f;
        this.settings.interfaceVolume = this.interfaceVolume.getProgress() / 100.0f;
        this.settings.scrollSpeed = (this.scrollSpeed.getProgress() + 20.0f) / 100.0f;
        this.settings.batterySaving = this.batterySaving.isChecked();
        this.settings.highRefreshRate = this.highRefreshRate.isChecked();
        this.settings.unlockedScreenRotation = this.unlockedScreenRotation.isChecked();
        this.settings.renderBackground = this.renderBackground.isChecked();
        this.settings.renderExtraLayers = this.renderExtraLayers.isChecked();
        this.settings.immersiveFullScreen = this.immersiveFullScreen.isChecked();
        this.settings.renderDoubleScale = this.renderDoubleScale.isChecked();
        this.settings.renderClouds = this.renderClouds.isChecked();
        this.settings.showWarLogOnScreen = this.showWarLogOnScreen.isChecked();
        this.settings.classicInterface = this.classicInterface.isChecked();
        this.settings.showUnitWaypoints = this.showUnitWaypoints.isChecked();
        this.settings.useMinimapAllyColors = this.useMinimapAllyColors.isChecked();
        this.settings.showUnitGroups = this.showUnitGroups.isChecked();
        this.settings.allowGameRecording = this.allowGameRecording.isChecked();
        this.settings.showFps = this.showFps.isChecked();
        this.settings.newRender = this.newRender.isChecked();
        this.settings.shaderEffects = this.shaderEffects.isChecked();
        this.settings.teamShaders = this.teamShaders.isChecked();
        this.settings.sendReports = this.sendReports.isChecked();
        this.settings.showHp = this.showHp.isChecked();
        this.settings.showUnitIcons = this.showUnitIcons.isChecked();
        this.settings.gestureZoom = this.gestureZoom.isChecked();
        this.settings.useCircleSelect = this.useCircleSelect.isChecked();
        this.settings.smartSelection_v2 = this.smartSelection.isChecked();
        this.settings.quickRally = this.quickRally.isChecked();
        this.settings.doubleClickToAttackMove = this.doubleClickToAttackMove.isChecked();
        this.settings.showZoomButton = this.zoomButton.isChecked();
        this.settings.mouseSupport = this.mouseSupport.isChecked();
        this.settings.keyboardSupport = this.keyboardSupport.isChecked();
        this.settings.forceEnglish = this.forceEnglish.isChecked();
        this.settings.teamUnitCapSinglePlayer = getSpinnerByValue(this.teamUnitCapSinglePlayer, this.unitCapOptions);
        this.settings.teamUnitCapHostedGame = getSpinnerByValue(this.teamUnitCapHostedGame, this.unitCapOptions);
        if (!this.replaysDisabledByPermission || this.saveMultiplayerReplays.isChecked()) {
            this.settings.saveMultiplayerReplays = this.saveMultiplayerReplays.isChecked();
        }
        this.settings.replaysShowRecordedChat = this.replaysShowRecordedChat.isChecked();
        this.settings.udpInMultiplayer = this.udpInMultiplayer.isChecked();
        try {
            this.settings.networkPort = Integer.valueOf(this.networkPort.getText().toString()).intValue();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (this.settings.networkPort < 1024 || this.settings.networkPort > 65535) {
            class_1061.method_3043("networkPort out of range");
            this.settings.networkPort = PacketType.PACKET_STORAGE_DATA;
        }
        this.settings.showMapPingsOnBattlefield = this.showMapPingsOnBattlefield.isChecked();
        this.settings.showMapPingsOnMinimap = this.showMapPingsOnMinimap.isChecked();
        this.settings.showPlayerChatInGame = this.showPlayerChatInGame.isChecked();
        this.settings.autosaving = this.autoSaveEnabled.isChecked();
        this.settings.aiDifficulty = this.aiDifficulty.getSelectedItemPosition() - 2;
        saveStorageType();
        this.settings.save();
        class_988.method_2639();
        finish();
    }

    @Override // android.app.Activity
    public void onPause() {
        if (this.saveChanges && isFinishing()) {
            saveSettings();
        }
        super.onPause();
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTitle("Settings");
        if (class_84.method_127(this, false)) {
            class_1061.method_3037(this);
            setContentView(R.layout.settings);
            class_84.method_120(getWindow().getDecorView().findViewById(android.R.id.content));
            String string = null;
            getWindow().setBackgroundDrawable(null);
            this.settings = SettingsEngine.getInstance(getBaseContext());
            this.enableSounds = (CheckBox) findViewById(R.id.SettingsEnableSounds);
            this.musicVolumeText = (TextView) findViewById(R.id.musicVolumeText);
            this.musicVolume = (SeekBar) findViewById(R.id.musicVolume);
            this.gameVolumeText = (TextView) findViewById(R.id.gameVolumeText);
            this.gameVolume = (SeekBar) findViewById(R.id.gameVolume);
            this.interfaceVolumeText = (TextView) findViewById(R.id.interfaceVolumeText);
            this.interfaceVolume = (SeekBar) findViewById(R.id.interfaceVolume);
            this.scrollSpeedText = (TextView) findViewById(R.id.scrollSpeedText);
            this.scrollSpeed = (SeekBar) findViewById(R.id.scrollSpeed);
            this.batterySaving = (CheckBox) findViewById(R.id.settingsBatterySaving);
            this.highRefreshRate = (CheckBox) findViewById(R.id.settingsHighRefreshRate);
            this.unlockedScreenRotation = (CheckBox) findViewById(R.id.settingsUnlockedScreenRotation);
            this.renderBackground = (CheckBox) findViewById(R.id.settingsRenderBackground);
            this.renderExtraLayers = (CheckBox) findViewById(R.id.settingsRenderExtraLayers);
            this.immersiveFullScreen = (CheckBox) findViewById(R.id.settingsImmersiveFullScreen);
            this.renderDoubleScale = (CheckBox) findViewById(R.id.settingsRenderDoubleScale);
            this.renderClouds = (CheckBox) findViewById(R.id.settingsRenderClouds);
            this.showWarLogOnScreen = (CheckBox) findViewById(R.id.settingsShowWarLogOnScreen);
            this.classicInterface = (CheckBox) findViewById(R.id.settingsClassicInterface);
            this.useMinimapAllyColors = (CheckBox) findViewById(R.id.settingsUseMinimapAllyColors);
            this.showUnitWaypoints = (CheckBox) findViewById(R.id.settingsShowUnitWaypoints);
            this.showUnitGroups = (CheckBox) findViewById(R.id.settingsShowUnitGroups);
            this.gestureZoom = (CheckBox) findViewById(R.id.settingsGestureZoom);
            this.useCircleSelect = (CheckBox) findViewById(R.id.settingsUseCircleSelect);
            this.smartSelection = (CheckBox) findViewById(R.id.settingsSmartSelection);
            this.quickRally = (CheckBox) findViewById(R.id.settingsQuickRally);
            this.doubleClickToAttackMove = (CheckBox) findViewById(R.id.settingsDoubleClickToAttackMove);
            this.zoomButton = (CheckBox) findViewById(R.id.settingsZoomButton);
            this.mouseSupport = (CheckBox) findViewById(R.id.settingsMouseSupport);
            this.keyboardSupport = (CheckBox) findViewById(R.id.settingsKeyboardSupport);
            this.forceEnglish = (CheckBox) findViewById(R.id.settingsForceEnglish);
            this.teamUnitCapSinglePlayer = (Spinner) findViewById(R.id.teamUnitCapSinglePlayer);
            this.teamUnitCapHostedGame = (Spinner) findViewById(R.id.teamUnitCapHostedGame);
            this.saveMultiplayerReplays = (CheckBox) findViewById(R.id.settingsSaveMultiplayerReplays);
            this.replaysShowRecordedChat = (CheckBox) findViewById(R.id.settingsReplaysShowRecordedChat);
            this.allowGameRecording = (CheckBox) findViewById(R.id.settingsAllowGameRecording);
            this.showHp = (CheckBox) findViewById(R.id.settingsShowHp);
            this.showFps = (CheckBox) findViewById(R.id.settingsShowFps);
            this.newRender = (CheckBox) findViewById(R.id.settingsNewRender);
            this.shaderEffects = (CheckBox) findViewById(R.id.settingsShaderEffects);
            this.teamShaders = (CheckBox) findViewById(R.id.settingsTeamShaders);
            this.sendReports = (CheckBox) findViewById(R.id.settingsSendReports);
            this.showUnitIcons = (CheckBox) findViewById(R.id.settingsShowUnitIcons);
            this.debugOptions = (Button) findViewById(R.id.settingsDebugOptions);
            this.confKeys = (Button) findViewById(R.id.settingsConfKeys);
            this.aiDifficulty = (Spinner) findViewById(R.id.aiDifficulty);
            this.storageType = (Spinner) findViewById(R.id.storageType);
            this.networkPort = (EditText) findViewById(R.id.settingsNetworkPort);
            this.udpInMultiplayer = (CheckBox) findViewById(R.id.settingsUdpInMultiplayer);
            this.showMapPingsOnBattlefield = (CheckBox) findViewById(R.id.settingsShowMapPingsOnBattlefield);
            this.showMapPingsOnMinimap = (CheckBox) findViewById(R.id.settingsShowMapPingsOnMinimap);
            this.showPlayerChatInGame = (CheckBox) findViewById(R.id.settingsShowPlayerChatInGame);
            this.autoSaveEnabled = (CheckBox) findViewById(R.id.settingsAutoSaveEnabled);
            this.storageLayout = findViewById(R.id.settingsStorageLayout);
            this.setupExternalFolder = (Button) findViewById(R.id.settingsSetupExternalFolder);
            this.externalFolderInfo = (TextView) findViewById(R.id.settingsExternalFolderInfo);
            this.enableSounds.setChecked(this.settings.enableSounds);
            this.musicVolume.setProgress((int) (this.settings.musicVolume * 100.0f));
            this.musicVolumeText.setText(this.musicVolume.getProgress() + "%");
            this.gameVolume.setProgress((int) (this.settings.gameVolume * 100.0f));
            this.gameVolumeText.setText(this.gameVolume.getProgress() + "%");
            this.interfaceVolume.setProgress((int) (this.settings.interfaceVolume * 100.0f));
            this.interfaceVolumeText.setText(this.interfaceVolume.getProgress() + "%");
            this.scrollSpeed.setProgress((int) ((this.settings.scrollSpeed * 100.0f) - 20.0f));
            this.scrollSpeedText.setText((this.scrollSpeed.getProgress() + 20) + "%");
            this.batterySaving.setChecked(this.settings.batterySaving);
            this.highRefreshRate.setChecked(this.settings.highRefreshRate);
            this.unlockedScreenRotation.setChecked(this.settings.unlockedScreenRotation);
            this.renderBackground.setChecked(this.settings.renderBackground);
            this.renderExtraLayers.setChecked(this.settings.renderExtraLayers);
            this.immersiveFullScreen.setChecked(this.settings.immersiveFullScreen);
            this.renderDoubleScale.setChecked(this.settings.renderDoubleScale);
            this.renderClouds.setChecked(this.settings.renderClouds);
            this.showWarLogOnScreen.setChecked(this.settings.showWarLogOnScreen);
            this.classicInterface.setChecked(this.settings.classicInterface);
            this.showUnitWaypoints.setChecked(this.settings.showUnitWaypoints);
            this.useMinimapAllyColors.setChecked(this.settings.useMinimapAllyColors);
            this.showUnitGroups.setChecked(this.settings.showUnitGroups);
            this.allowGameRecording.setChecked(this.settings.allowGameRecording);
            this.allowGameRecording.setVisibility(8);
            this.showHp.setChecked(this.settings.showHp);
            this.showUnitIcons.setChecked(this.settings.showUnitIcons);
            this.gestureZoom.setChecked(this.settings.gestureZoom);
            this.useCircleSelect.setChecked(this.settings.useCircleSelect);
            this.smartSelection.setChecked(this.settings.smartSelection_v2);
            this.quickRally.setChecked(this.settings.quickRally);
            this.doubleClickToAttackMove.setChecked(this.settings.doubleClickToAttackMove);
            this.zoomButton.setChecked(this.settings.showZoomButton);
            this.mouseSupport.setChecked(this.settings.mouseSupport);
            this.keyboardSupport.setChecked(this.settings.keyboardSupport);
            this.forceEnglish.setChecked(this.settings.forceEnglish);
            setSpinnerByValue(this.teamUnitCapSinglePlayer, this.unitCapOptions, this.settings.teamUnitCapSinglePlayer, 1);
            setSpinnerByValue(this.teamUnitCapHostedGame, this.unitCapOptions, this.settings.teamUnitCapHostedGame, 1);
            this.saveMultiplayerReplays.setChecked(this.settings.saveMultiplayerReplays);
            this.replaysShowRecordedChat.setChecked(this.settings.replaysShowRecordedChat);
            if (!class_1061.field_6312) {
                this.saveMultiplayerReplays.setVisibility(8);
                this.replaysShowRecordedChat.setVisibility(8);
            }
            if (this.settings.saveMultiplayerReplays && !class_84.method_129(this)) {
                this.saveMultiplayerReplays.setChecked(false);
                this.replaysDisabledByPermission = true;
            }
            this.saveMultiplayerReplays.setOnCheckedChangeListener(new class_211(this));
            this.showFps.setChecked(this.settings.showFps);
            this.newRender.setChecked(this.settings.newRender);
            this.shaderEffects.setChecked(this.settings.shaderEffects);
            this.teamShaders.setChecked(this.settings.teamShaders);
            this.sendReports.setChecked(this.settings.sendReports);
            this.networkPort.setText(Integer.toString(this.settings.networkPort));
            this.udpInMultiplayer.setChecked(this.settings.udpInMultiplayer);
            this.showMapPingsOnBattlefield.setChecked(this.settings.showMapPingsOnBattlefield);
            this.showMapPingsOnMinimap.setChecked(this.settings.showMapPingsOnMinimap);
            this.showPlayerChatInGame.setChecked(this.settings.showPlayerChatInGame);
            this.autoSaveEnabled.setChecked(this.settings.autosaving);
            this.aiDifficulty.setSelection(this.settings.aiDifficulty + 2);
            class_900 class_900VarMethod_2173 = class_899.method_2173();
            this.setupExternalFolder.setOnClickListener(new class_228(this));
            if (!class_900VarMethod_2173.field_5114) {
                this.setupExternalFolder.setVisibility(8);
            }
            this.storageType.setOnItemSelectedListener(new class_229(this));
            updateStorageFields();
            this.musicVolume.setOnSeekBarChangeListener(new class_230(this));
            this.gameVolume.setOnSeekBarChangeListener(new class_231(this));
            this.interfaceVolume.setOnSeekBarChangeListener(new class_232(this));
            this.scrollSpeed.setOnSeekBarChangeListener(new class_233(this));
            ((Button) findViewById(R.id.settingsDone)).setOnClickListener(new class_234(this));
            ((Button) findViewById(R.id.settingsCancel)).setOnClickListener(new class_235(this));
            ((Button) findViewById(R.id.settingsCredits)).setOnClickListener(new class_214(this));
            Button button = (Button) findViewById(R.id.settingsMods);
            if (class_1061.method_3076().field_6321) {
                button.setVisibility(8);
            } else {
                button.setOnClickListener(new class_215(this));
            }
            this.confKeys.setOnClickListener(new class_216(this));
            this.debugOptions.setOnClickListener(new class_217(this));
            if (getIntent() != null && getIntent().getExtras() != null) {
                string = getIntent().getExtras().getString(intentMode);
            }
            if (string != null) {
                if (string.equals("setupExternalFolder")) {
                    this.setupExternalFolderOnly = true;
                    setupExternalSAFFolder();
                } else {
                    class_1061.method_3031("Unknown setup mode: ".concat(String.valueOf(string)));
                }
            }
            this.newRender.setOnCheckedChangeListener(new class_222(this));
            updateHiddenFields();
        }
    }

    public boolean allowExternalStorageType() {
        if (class_1061.method_2982() && !class_1061.method_3076().field_6345.externalSAFWorking) {
            return false;
        }
        return true;
    }

    public void saveStorageType() {
        this.settings.storageType = this.storageType.getSelectedItemPosition();
        class_899.method_2169();
    }

    public static String getStorageInfoAndWarnings(String str) {
        boolean z;
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if (class_1061VarMethod_3076.field_6345.externalSAFPathShown != null && class_1061VarMethod_3076.field_6345.externalSAFWorking) {
            z = true;
        } else {
            z = false;
        }
        if (!class_1061VarMethod_3076.field_6345.hasSelectedAStorageType) {
            return class_988.method_2636("menus.storage.notSetupInfo", new Object[0]);
        }
        if (z && class_1061VarMethod_3076.field_6345.storageType == 0) {
            return class_988.method_2636("menus.storage.noExternalRead", new Object[0]);
        }
        return getStorageExternalFolderInfo(str);
    }

    public static String getStorageExternalFolderInfo(String str) {
        String strMethod_2636;
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if (class_1061VarMethod_3076.field_6345.externalSAFPathShown == null) {
            strMethod_2636 = class_988.method_2636("menus.externalStorage.inactive", new Object[0]);
        } else if (!class_1061VarMethod_3076.field_6345.externalSAFWorking) {
            strMethod_2636 = class_988.method_2636("menus.externalStorage.failed", new Object[0]) + class_1061VarMethod_3076.field_6345.externalSAFPathShown + " (Please setup again)";
        } else {
            strMethod_2636 = class_988.method_2636("menus.externalStorage.active", new Object[0]) + class_1061VarMethod_3076.field_6345.externalSAFPathShown + str;
        }
        class_900 class_900VarMethod_2173 = class_899.method_2173();
        if (!class_900VarMethod_2173.field_5114) {
            if (class_900VarMethod_2173.field_5115) {
                strMethod_2636 = class_988.method_2636("menus.externalStorage.legacy", new Object[0]);
                if (Build.VERSION.SDK_INT >= 30) {
                    strMethod_2636 = strMethod_2636 + " (will have problems in Android 11 or higher!)";
                }
            } else {
                strMethod_2636 = class_988.method_2636("menus.externalStorage.disabled", new Object[0]);
            }
        }
        String strMethod_2159 = class_899.method_2159();
        if (strMethod_2159 != null) {
            return strMethod_2636 + " " + strMethod_2159;
        }
        return strMethod_2636;
    }

    public void updateStorageFields() {
        class_1061.method_3043("updateStorageFields()");
        this.storageType.setSelection(this.settings.storageType);
        class_1061.method_3076();
        this.externalFolderInfo.setText(getStorageExternalFolderInfo(VariableScope.nullOrMissingString));
    }

    public static String setDebugOption(String str) {
        String strConcat;
        boolean z;
        File file;
        File file2;
        Boolean bool;
        String str2;
        Boolean bool2;
        Boolean bool3;
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if (str == null) {
            str = VariableScope.nullOrMissingString;
        }
        String strTrim = str.toLowerCase(Locale.ROOT).replace("  ", " ").replace("\"", VariableScope.nullOrMissingString).replace("'", VariableScope.nullOrMissingString).trim();
        if (strTrim.equals("old map render")) {
            class_299.field_922 = true;
            strConcat = "Enabled old map rendering";
        } else {
            strConcat = null;
        }
        if (strTrim.equals("surface view2")) {
            class_84.field_270 = class_247.field_593;
            strConcat = "Enabled multi-threaded surface view";
        }
        if (strTrim.equals("nonsurface view")) {
            class_84.field_270 = class_247.field_594;
            strConcat = "Enabled non surface view";
        }
        if (strTrim.equals("surface view")) {
            class_84.field_270 = class_247.field_591;
            strConcat = "Enabled surface view";
        }
        if (strTrim.equals("opengl view")) {
            class_84.field_270 = class_247.field_595;
            strConcat = "Enabled opengl view";
        }
        if (strTrim.equals("watch memory")) {
            class_1061.field_6294 = true;
            strConcat = "Enabled memory watch";
        }
        boolean z2 = false;
        if (strTrim.equals("autosave off")) {
            class_1061VarMethod_3076.field_6345.autosaving = false;
            strConcat = "Disabled autosave";
        }
        if (strTrim.equals("autosave on")) {
            class_1061VarMethod_3076.field_6345.autosaving = true;
            strConcat = "Enabled autosave";
        }
        if (strTrim.equals("showhpchanges off")) {
            class_1061VarMethod_3076.field_6345.showHpChanges = false;
            strConcat = "Disabled showHpChanges";
        }
        if (strTrim.equals("showhpchanges on")) {
            class_1061VarMethod_3076.field_6345.showHpChanges = true;
            strConcat = "Enabled showHpChanges";
        }
        if (strTrim.startsWith("lang ")) {
            class_1061VarMethod_3076.field_6345.forceEnglish = false;
            class_988.field_5772 = strTrim.substring(5).trim();
            class_988.method_2639();
            strConcat = "Set language '" + class_988.field_5772 + "'";
        }
        if (strTrim.equals("version")) {
            strConcat = "Version" + class_1061VarMethod_3076.method_3065() + " " + class_1061VarMethod_3076.method_3012(true) + " - " + class_1061VarMethod_3076.method_3012(false);
            z = false;
        } else {
            z = true;
        }
        if (strTrim.equals("reset")) {
            class_1061.field_6294 = false;
            class_84.field_270 = class_84.field_269;
            class_299.field_922 = false;
            strConcat = "Reset debug options";
            z = false;
        }
        if (strTrim.equals("test crash") && class_1061VarMethod_3076.method_3063()) {
            throw new RuntimeException("test crash");
        }
        if (strTrim.equals("save logs") || strTrim.equals("share logs")) {
            boolean zStartsWith = strTrim.startsWith("share");
            String str3 = "savedLog-" + new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date()) + ".log";
            try {
                class_901 class_901VarMethod_2160 = class_899.field_5109;
                if (zStartsWith) {
                    class_901VarMethod_2160 = class_899.method_2160(0);
                    file = new File(class_901VarMethod_2160.method_2212("/SD/rustedWarfare/".concat(String.valueOf(str3))));
                    file.createNewFile();
                    file.deleteOnExit();
                } else {
                    file = new File(class_901VarMethod_2160.method_2212("/SD/rustedWarfare/".concat(String.valueOf(str3))));
                }
                PrintWriter printWriter = new PrintWriter(new BufferedOutputStream(class_901VarMethod_2160.method_2208(file.getAbsolutePath(), false)));
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("logcat -d").getInputStream()));
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    printWriter.append((CharSequence) line);
                    printWriter.append((CharSequence) "\n");
                }
                printWriter.close();
                String strConcat2 = "Saved game logs to: ".concat(String.valueOf(str3));
                if (zStartsWith) {
                    Context context = class_1061VarMethod_3076.field_6316;
                    if (context instanceof Activity) {
                        class_84.method_109((Activity) context, file);
                    }
                    strConcat = "Shared game logs";
                } else {
                    strConcat = strConcat2;
                }
                z = false;
            } catch (IOException e) {
                e.printStackTrace();
                strConcat = "Failed to save log: " + e.getMessage();
                z = false;
            }
        }
        if (strTrim.equals("save bad header") || strTrim.equals("share bad header")) {
            boolean zStartsWith2 = strTrim.startsWith("share");
            String str4 = class_1047.field_6256;
            if (str4 == null) {
                strConcat = "No bad header data has been recorded.";
                z = false;
            } else {
                String str5 = "header-" + new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date()) + ".log";
                try {
                    class_901 class_901VarMethod_21602 = class_899.field_5109;
                    if (zStartsWith2) {
                        class_901VarMethod_21602 = class_899.method_2160(0);
                        file2 = new File(class_901VarMethod_21602.method_2212("/SD/rustedWarfare/".concat(String.valueOf(str5))));
                        file2.createNewFile();
                        file2.deleteOnExit();
                    } else {
                        file2 = new File(class_901VarMethod_21602.method_2212("/SD/rustedWarfare/".concat(String.valueOf(str5))));
                    }
                    PrintWriter printWriter2 = new PrintWriter(new BufferedOutputStream(class_901VarMethod_21602.method_2208(file2.getAbsolutePath(), false)));
                    printWriter2.append((CharSequence) str4);
                    printWriter2.close();
                    String strConcat3 = "Saved bad header data to: ".concat(String.valueOf(str5));
                    if (zStartsWith2) {
                        Context context2 = class_1061VarMethod_3076.field_6316;
                        if (context2 instanceof Activity) {
                            class_84.method_109((Activity) context2, file2);
                        }
                        strConcat = "Shared bad header data";
                    } else {
                        strConcat = strConcat3;
                    }
                    z = false;
                } catch (IOException e2) {
                    e2.printStackTrace();
                    strConcat = "Failed to header log: " + e2.getMessage();
                    z = false;
                }
            }
        }
        if (strTrim.equals("cache list")) {
            strConcat = class_905.method_2224();
            z = false;
        }
        if (strTrim.equals("cache delete")) {
            strConcat = class_905.method_2231();
            z = false;
        }
        if (strTrim.equals("storage setup")) {
            Context context3 = class_1061VarMethod_3076.field_6316;
            if (context3 instanceof Activity) {
                class_84.method_112((Activity) context3, null, true);
                strConcat = "none";
                z = true;
            } else {
                strConcat = "Failed to show popup with context: ".concat(String.valueOf(context3));
                z = true;
            }
        }
        if (strTrim.equals("storage reset")) {
            class_1061VarMethod_3076.field_6345.externalSAFWorking = false;
            class_1061VarMethod_3076.field_6345.storageType = 0;
            class_1061VarMethod_3076.field_6345.hasSelectedAStorageType = false;
            class_1061VarMethod_3076.field_6345.externalSAFWorking = false;
            class_1061VarMethod_3076.field_6345.externalSAFLink = null;
            class_1061VarMethod_3076.field_6345.externalSAFPathShown = null;
            class_1061VarMethod_3076.field_6345.externalSAFPathExtra = null;
            class_899.method_2169();
            class_1061VarMethod_3076.field_6345.save();
            strConcat = "Reset storage settings";
            z = true;
        }
        if (strTrim.equals("network reset")) {
            class_1061VarMethod_3076.field_6345.networkClientId = null;
            class_1061VarMethod_3076.field_6345.networkServerId = null;
            class_1061VarMethod_3076.field_6345.save();
            strConcat = "Reset network settings";
            z = true;
        }
        if (strTrim.equals("saf locking")) {
            AndroidSAF.setGlobalLocking(true);
            strConcat = "SAF locking enabled";
            z = true;
        }
        if (strTrim.equals("saf force on")) {
            class_899.field_5110 = Boolean.TRUE;
            strConcat = "SAF access forced on";
            z = true;
        }
        if (strTrim.equals("saf force off")) {
            class_899.field_5110 = Boolean.FALSE;
            strConcat = Build.VERSION.SDK_INT >= 30 ? "SAF access forced off. Warning! You have Android 11 or higher. Scoped storage will likely block the app from reading and writing files." : "SAF access forced off. ";
            z = true;
        }
        if (strTrim.equals("opengl clear context")) {
            GameViewOpenGL.clearRetainedGLContext();
            strConcat = "opengl retained context cleared";
            z = false;
        }
        if (strTrim.equals("postprocessing")) {
            class_1061.field_6295 = true;
            strConcat = class_1061.method_3076().field_6345.newRender ? "postprocessing shaders on" : "opengl mode required for postprocessing";
        }
        if (strTrim.equals("team shaders") || strTrim.equals("team shaders on")) {
            strConcat = class_1061.method_3076().field_6345.newRender ? !class_1061.field_6296 ? "team shaders on" : "team shaders already on" : "opengl mode required for team shaders";
            class_1061.field_6296 = true;
        }
        if (strTrim.equals("team shaders off")) {
            strConcat = class_1061.field_6296 ? "team shaders off" : "team shaders already off";
            class_1061.field_6296 = false;
        }
        if (strTrim.equals("sound priority off")) {
            strConcat = !class_1061VarMethod_3076.field_6345.androidNoSoundPrioritiesDebug ? "sound priority now off" : "sound priority already off";
            class_1061VarMethod_3076.field_6345.androidNoSoundPrioritiesDebug = true;
            class_1061VarMethod_3076.field_6345.save();
        }
        if (strTrim.equals("sound priority on")) {
            strConcat = class_1061VarMethod_3076.field_6345.androidNoSoundPrioritiesDebug ? "sound priority now on" : "sound priority already on";
            class_1061VarMethod_3076.field_6345.androidNoSoundPrioritiesDebug = false;
            class_1061VarMethod_3076.field_6345.save();
        }
        if (strTrim.startsWith("display over cutout ")) {
            String lowerCase = strTrim.substring(20).trim().toLowerCase();
            if (lowerCase.equals("on")) {
                bool3 = Boolean.TRUE;
            } else if (lowerCase.equals("off")) {
                bool3 = Boolean.FALSE;
            } else {
                strConcat = "Unknown option - ".concat(String.valueOf(strTrim));
                bool3 = null;
            }
            if (bool3 != null) {
                strConcat = "displayOverCutout now " + (bool3.booleanValue() ? "on" : "off");
                class_1061VarMethod_3076.field_6345.displayOverCutout = bool3.booleanValue();
                class_1061VarMethod_3076.field_6345.save();
                z = false;
            }
        }
        if (strTrim.startsWith("use line width ")) {
            String lowerCase2 = strTrim.substring(15).trim().toLowerCase();
            if (lowerCase2.equals("on")) {
                bool2 = Boolean.TRUE;
            } else if (lowerCase2.equals("off")) {
                bool2 = Boolean.FALSE;
            } else {
                strConcat = "Unknown option - ".concat(String.valueOf(strTrim));
                bool2 = null;
            }
            if (bool2 != null) {
                strConcat = "renderWithLineWidth now " + (bool2.booleanValue() ? "on" : "off");
                class_1061VarMethod_3076.field_6345.renderWithLineWidth = bool2.booleanValue();
                class_1061VarMethod_3076.field_6345.save();
                z = false;
            }
        }
        if (strTrim.startsWith("digit grouping ")) {
            String lowerCase3 = strTrim.substring(15).trim().toLowerCase();
            if (lowerCase3.equals("on")) {
                bool = Boolean.TRUE;
            } else if (lowerCase3.equals("off")) {
                bool = Boolean.FALSE;
            } else {
                strConcat = "Unknown option - ".concat(String.valueOf(strTrim));
                bool = null;
            }
            if (bool != null) {
                boolean z3 = !bool.booleanValue();
                if (class_1061VarMethod_3076.field_6345.disableDigitGrouping == z3) {
                    str2 = "digit grouping already " + (bool.booleanValue() ? "on" : "off");
                } else {
                    str2 = "digit grouping now " + (bool.booleanValue() ? "on" : "off");
                }
                class_1061VarMethod_3076.field_6345.disableDigitGrouping = z3;
                class_1061VarMethod_3076.field_6345.save();
                strConcat = str2;
                z = false;
            }
        }
        if (strTrim.startsWith("ui scale ")) {
            String lowerCase4 = strTrim.substring(9).trim().toLowerCase();
            Float fMethod_2348 = class_907.method_2348(lowerCase4);
            if (fMethod_2348 == null) {
                strConcat = "Not float - ".concat(String.valueOf(lowerCase4));
                z2 = z;
            } else {
                if (fMethod_2348.floatValue() < 0.3f) {
                    fMethod_2348 = Float.valueOf(0.3f);
                }
                if (fMethod_2348.floatValue() > 4.0f) {
                    fMethod_2348 = Float.valueOf(4.0f);
                }
                strConcat = "ui scale now ".concat(String.valueOf(fMethod_2348));
                class_1061VarMethod_3076.field_6345.uiRenderScale = fMethod_2348.floatValue();
                class_1061VarMethod_3076.field_6345.save();
            }
        } else {
            z2 = z;
        }
        if (strTrim.startsWith("kick ban time ")) {
            String lowerCase5 = strTrim.substring(14).trim().toLowerCase();
            Float fMethod_23482 = class_907.method_2348(lowerCase5);
            if (fMethod_23482 == null) {
                strConcat = "Not float - ".concat(String.valueOf(lowerCase5));
            } else {
                int iFloatValue = (int) fMethod_23482.floatValue();
                String strConcat4 = "Ban time after kick now ".concat(String.valueOf(iFloatValue));
                class_1061VarMethod_3076.field_6345.banTimeInSecondsAfterKick = iFloatValue;
                class_1061VarMethod_3076.field_6345.save();
                strConcat = strConcat4;
            }
        }
        if (strTrim.equals("hash")) {
            strConcat = class_1061VarMethod_3076.field_6352.field_5945;
        }
        if (strConcat == null) {
            return "Unknown option - ".concat(String.valueOf(strTrim));
        }
        debugWasSetOrAskedThisSession = true;
        if (z2) {
            class_1061VarMethod_3076.field_6345.lastDebugOption = strTrim;
        } else {
            class_1061VarMethod_3076.field_6345.lastDebugOption = null;
        }
        class_1061VarMethod_3076.field_6345.save();
        return strConcat;
    }

    public static boolean askAboutLastDebugOption() {
        boolean z;
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        String str = class_1061VarMethod_3076.field_6345.lastDebugOption;
        if (!debugWasSetOrAskedThisSession && class_1061VarMethod_3076.field_6345.lastDebugOption != null && !class_1061VarMethod_3076.method_3035()) {
            class_1061VarMethod_3076.field_6345.lastDebugOption = null;
            AlertDialog.Builder builder = new AlertDialog.Builder(class_1061VarMethod_3076.field_6316);
            builder.setTitle("Debug");
            builder.setMessage("Re-enable last debug option: '" + str + "'?");
            builder.setPositiveButton("Yes", new class_223(str, class_1061VarMethod_3076));
            builder.setNegativeButton("No", new class_224());
            builder.show();
            z = true;
        } else {
            z = false;
        }
        debugWasSetOrAskedThisSession = true;
        return z;
    }

    public void setSpinnerByValue(Spinner spinner, int[] iArr, int i, int i2) {
        for (int i3 = 0; i3 < iArr.length; i3++) {
            if (iArr[i3] == i) {
                i2 = i3;
            }
        }
        spinner.setSelection(i2);
    }

    public int getSpinnerByValue(Spinner spinner, int[] iArr) {
        int selectedItemPosition = spinner.getSelectedItemPosition();
        if (selectedItemPosition < 0 || selectedItemPosition > iArr.length) {
            class_1061.method_3053("Spinner out of range: ".concat(String.valueOf(selectedItemPosition)));
            return iArr[0];
        }
        return iArr[selectedItemPosition];
    }

    public static void setupInternalFolder() {
    }

    public void setupExternalSAFFolder() {
        class_84.method_107(this, 9, true, "Select a Rusted Warfare Folder to use", Uri.parse("content://com.android.externalstorage.documents/document/primary%3A".concat(String.valueOf("rustedWarfare".replace("//", "%2F")))));
    }

    @Override // android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        switch (i) {
            case 9:
                if (!class_84.method_106(this, i, i2, intent, this.linkExternalFolder) && this.setupExternalFolderOnly) {
                    class_1061.method_3031("setupExternalFolderOnly");
                    finish();
                    break;
                }
                break;
            default:
                super.onActivityResult(i, i2, intent);
                break;
        }
    }

    public static void updatedLinkedExternalSAFFolder() {
    }

    public static double benchmarkSafFolder(String str) {
        return 0.0d;
    }

    public void updateHiddenFields() {
        int i = this.newRender.isChecked() ? 0 : 8;
        this.shaderEffects.setVisibility(i);
        this.teamShaders.setVisibility(i);
    }
}
