package com.corrodinggames.rts.appFramework;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.corrodinggames.rts.R;
import com.corrodinggames.rts.appFramework.android.AndroidSAF;
import com.corrodinggames.rts.game.units.custom.class_472;
import com.corrodinggames.rts.game.units.custom.logicBooleans.VariableScope;
import com.corrodinggames.rts.gameFramework.class_1061;
import com.corrodinggames.rts.gameFramework.class_907;
import com.corrodinggames.rts.gameFramework.e.class_899;
import com.corrodinggames.rts.gameFramework.i.class_991;
import com.corrodinggames.rts.gameFramework.i.class_992;
import com.corrodinggames.rts.gameFramework.i.class_994;
import com.corrodinggames.rts.gameFramework.utility.a.class_1290;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class ModsActivity extends class_1 {
    public static final int FILE_SELECT_CODE = 5;
    public static final int LOADING_DIALOG = 0;
    public static final int OPEN_DIRECTORY_REQUEST_CODE = 6;
    public static final String progressDialogBaseMessage = "Loading selected mod data...";
    public static boolean refreshButtonBlocked = false;
    public static Object refreshLock = new Object();
    public ProgressDialog progressDialog;
    public final Handler uiHandler = new Handler();
    public class_246 linkModFolder = new class_115(this);
    public Runnable fileAddedCallback = new class_116(this);

    public void showFileChooserForImport() {
        $invoke$special$showFileChooserForImport();
    }

    public void showFolderChooserForModLink() {
        $invoke$special$showFolderChooserForModLink();
    }

    @Override // android.app.Activity
    public void finish() {
        super.finish();
        class_84.method_133(this, false);
    }

    @Override // com.corrodinggames.rts.appFramework.class_1, android.app.Activity
    public void onResume() {
        super.onResume();
        setup(false);
        class_1061.method_3076();
        class_84.method_115(this, false);
    }

    @Override // android.app.Activity
    public void onPause() {
        super.onPause();
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTitle("Mods");
        if (class_84.method_127(this, false)) {
            setContentView(R.layout.mods);
            class_84.method_120(getWindow().getDecorView().findViewById(android.R.id.content));
            setup(true);
        }
    }

    public void setup(boolean z) {
        String str;
        String str2;
        class_1061 class_1061VarMethod_3037 = class_1061.method_3037(this);
        if (!class_84.method_135(this)) {
            finish();
            return;
        }
        if (z) {
            class_84.method_132(this);
        }
        ((TextView) findViewById(R.id.messageInfo)).setVisibility(8);
        TextView textView = (TextView) findViewById(R.id.modExtraTextInfo);
        if (class_1061VarMethod_3037.field_6345.storageType != 0) {
            str = VariableScope.nullOrMissingString;
        } else {
            str = "Note: Using internal game storage only (Switch to external storage in game settings)";
        }
        if (class_991.field_5779 != null) {
            str = str + "\n" + class_991.field_5779;
        }
        textView.setText(str);
        ArrayList arrayList = new ArrayList();
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.modsContainer);
        class_991 class_991Var = class_1061VarMethod_3037.field_6354;
        linearLayout.removeAllViews();
        for (class_992 class_992Var : class_991Var.method_2677()) {
            LinearLayout linearLayout2 = new LinearLayout(getBaseContext());
            linearLayout2.setOrientation(1);
            linearLayout2.setGravity(17);
            linearLayout2.setBackgroundColor(Color.argb(30, 0, 0, 0));
            linearLayout2.setPadding(2, 7, 2, 7);
            LinearLayout linearLayout3 = new LinearLayout(getBaseContext());
            linearLayout3.setOrientation(1);
            linearLayout3.setGravity(17);
            linearLayout3.setBackgroundColor(Color.argb(40, 255, 255, 255));
            linearLayout3.setMinimumHeight(2);
            linearLayout2.addView(linearLayout3);
            class_1061.method_3043("found mod: " + class_992Var.field_5807 + " - " + (!class_992Var.field_5810));
            CheckBox checkBox = new CheckBox(getBaseContext());
            checkBox.setChecked(!class_992Var.field_5810);
            checkBox.setText(class_992Var.method_2679());
            checkBox.setTextSize(1, 22.0f);
            checkBox.setTextColor(-1);
            linearLayout2.addView(checkBox);
            checkBox.setTag(class_992Var.field_5809);
            linearLayout2.setTag(class_992Var.field_5809);
            registerForContextMenu(checkBox);
            registerForContextMenu(linearLayout2);
            class_120 class_120Var = new class_120(this);
            class_120Var.field_324 = checkBox;
            class_120Var.field_325 = class_992Var;
            arrayList.add(class_120Var);
            if (class_992Var.field_5823 != null) {
                StringBuilder sb = new StringBuilder("  ");
                if (class_992Var.field_5823 == null) {
                    str2 = VariableScope.nullOrMissingString;
                } else {
                    str2 = VariableScope.nullOrMissingString + class_992Var.field_5823;
                }
                String str3 = "RAM:" + class_992Var.method_2692();
                if (class_992Var.field_5816 != null) {
                    str3 = str3 + " Storage: slow external unpacked";
                }
                if (class_1061.method_2982() && class_992Var.field_5819 != null && class_899.method_2165(class_992Var.field_5819) && !class_992Var.field_5812) {
                    str3 = str3 + " Warning: slow external storage";
                }
                String string = sb.append(str2 + "\n (" + str3 + ")").toString();
                TextView textView2 = new TextView(getBaseContext());
                textView2.setText(string);
                textView2.setTextSize(1, 14.0f);
                textView2.setTextColor(-1);
                linearLayout2.addView(textView2);
            }
            if (class_992Var.field_5800 != null) {
                TextView textView3 = new TextView(getBaseContext());
                textView3.setText(class_992Var.field_5800);
                textView3.setTextSize(1, 14.0f);
                textView3.setTextColor(Color.argb(255, 219, 143, 143));
                textView3.setBackgroundColor(Color.argb(108, 0, 0, 0));
                linearLayout2.addView(textView3);
            }
            String strMethod_2689 = class_992Var.method_2689();
            if (strMethod_2689 != null) {
                TextView textView4 = new TextView(getBaseContext());
                textView4.setText(strMethod_2689);
                textView4.setTextSize(1, 14.0f);
                textView4.setTextColor(-16711936);
                linearLayout2.addView(textView4);
            }
            linearLayout.addView(linearLayout2);
        }
        ((Button) findViewById(R.id.modsClose)).setOnClickListener(new class_99(this));
        ((Button) findViewById(R.id.modsSave)).setOnClickListener(new class_107(this, arrayList));
        ((Button) findViewById(R.id.modsCreateFolder)).setOnClickListener(new class_108(this));
        ((Button) findViewById(R.id.modsImportMod)).setOnClickListener(new class_109(this));
        ((Button) findViewById(R.id.modsLinkModFolder)).setOnClickListener(new class_110(this));
        ((Button) findViewById(R.id.modsReload)).setOnClickListener(new class_114(this, arrayList));
        ProgressDialog progressDialog = this.progressDialog;
        if (progressDialog != null && progressDialog.isShowing()) {
            try {
                dismissDialog(0);
            } catch (IllegalArgumentException e) {
                class_1061.method_3010("dismissDialog failed", e);
            }
        }
    }

    public boolean modsSave(ArrayList arrayList, boolean z) {
        class_1061.method_3043("Saving mods");
        Iterator it = arrayList.iterator();
        while (true) {
            boolean z2 = false;
            if (!it.hasNext()) {
                break;
            }
            class_120 class_120Var = (class_120) it.next();
            class_992 class_992Var = class_120Var.field_325;
            class_992Var.field_5810 = !class_120Var.field_324.isChecked();
            StringBuilder sbAppend = new StringBuilder("mod: ").append(class_992Var.field_5807).append(" - ");
            if (!class_992Var.field_5810) {
                z2 = true;
            }
            class_1061.method_3043(sbAppend.append(z2).toString());
        }
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        class_1061VarMethod_3076.field_6354.method_2666();
        class_1061VarMethod_3076.field_6345.save();
        int iMethod_2649 = class_1061VarMethod_3076.field_6354.method_2649();
        if (class_1061VarMethod_3076.field_6352.field_5850) {
            class_1061.method_3043("savesMods: in network game");
            class_1061VarMethod_3076.method_3056("You are currently in a network game, mods changes will be remembered and applied on next game");
        } else if (class_472.method_1148(true)) {
            if (iMethod_2649 == 0) {
                int iMethod_2662 = class_1061VarMethod_3076.field_6354.method_2662();
                if (z) {
                    class_1061VarMethod_3076.method_3056("Mod changes saved. " + iMethod_2662 + " selected mods will be used in the next game.");
                }
            } else {
                if (!z) {
                    return false;
                }
                class_1061VarMethod_3076.method_3056("Mod changes saved. But " + iMethod_2649 + " mods are not loaded. Click 'Reload Mod Data' or restart the game to use these mods.");
                return false;
            }
        } else {
            if (!z) {
                return false;
            }
            class_1061VarMethod_3076.method_3056("Mods errors found");
            return false;
        }
        return true;
    }

    public void createAndShowFolder(String str) {
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        String strMethod_2178 = class_899.method_2178(str);
        if (!strMethod_2178.endsWith(File.separator)) {
            new StringBuilder().append(strMethod_2178).append(File.separator);
        }
        String strMethod_2176 = class_899.method_2176(strMethod_2178);
        File file = new File(strMethod_2178);
        if (!class_899.method_2186(file.getAbsolutePath())) {
            if (class_899.method_2190(file.getAbsolutePath())) {
                if (class_899.method_2186(file.getAbsolutePath()) && class_899.method_2180(file.getAbsolutePath())) {
                    if (!class_1061VarMethod_3076.field_6345.externalSAFWorking) {
                        class_1061VarMethod_3076.method_3056("Created folder: " + strMethod_2176 + ". (Note: " + ("Failed to read: " + class_1061VarMethod_3076.field_6345.externalSAFPathShown + " - Folder might have moved or permission expired. Please setup again under in-game settings.") + ")");
                        return;
                    }
                    class_1061VarMethod_3076.method_3056("Created folder: ".concat(String.valueOf(strMethod_2176)));
                    return;
                }
                class_1061VarMethod_3076.method_3056("Error creating folder: ".concat(String.valueOf(strMethod_2176)));
                return;
            }
            class_1061VarMethod_3076.method_3056("Failed to create: ".concat(String.valueOf(strMethod_2176)));
            return;
        }
        if (class_899.method_2180(file.getAbsolutePath())) {
            if (!class_1061VarMethod_3076.field_6345.externalSAFWorking) {
                class_1061VarMethod_3076.method_3056("Note: " + ("Failed to read: " + class_1061VarMethod_3076.field_6345.externalSAFPathShown + " - Folder might have moved or permission expired. Please setup again under in-game settings.") + ". Folder already created: " + strMethod_2176);
                return;
            }
            class_1061VarMethod_3076.method_3056("Folder already created: ".concat(String.valueOf(strMethod_2176)));
            return;
        }
        class_1061VarMethod_3076.method_3056("Mod path exist but is not a directory: ".concat(String.valueOf(strMethod_2176)));
    }

    @Override // android.app.Activity
    public Dialog onCreateDialog(int i) {
        switch (i) {
            case 0:
                ProgressDialog progressDialog = new ProgressDialog(this);
                this.progressDialog = progressDialog;
                progressDialog.setProgressStyle(0);
                this.progressDialog.setMessage(progressDialogBaseMessage);
                this.progressDialog.setCancelable(false);
                return this.progressDialog;
            default:
                return null;
        }
    }

    public void refreshModsInBackground() {
        showDialog(0);
        new Thread(new class_122(this)).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void $invoke$special$showFolderChooserForModLink() {
        class_84.method_107(this, 6, false, "Select a Rusted Warfare Mod Folder to Import", null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void $invoke$special$showFileChooserForImport() {
        class_84.method_138(this);
    }

    @Override // android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        switch (i) {
            case 5:
                onFileSelectResult(this, i, i2, intent, this.fileAddedCallback);
                break;
            case 6:
                class_84.method_106(this, i, i2, intent, this.linkModFolder);
                break;
            default:
                super.onActivityResult(i, i2, intent);
                break;
        }
    }

    public static void addSafModUriString(String str) throws IOException {
        addSafModUri(Uri.parse(str));
    }

    public static void addSafModUri(Uri uri) throws IOException {
        String strSubstring;
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        Context contextMethod_104 = class_84.method_104();
        AndroidSAF androidSAF = AndroidSAF.getInstance();
        Uri uriBuildDocumentUriUsingTree = androidSAF.buildDocumentUriUsingTree(uri);
        Iterator it = androidSAF.listByName(contextMethod_104, uriBuildDocumentUriUsingTree).iterator();
        boolean z = false;
        while (it.hasNext()) {
            if ("mod-info.txt".equals((String) it.next())) {
                z = true;
            }
        }
        if (!z) {
            throw new IOException("No 'mod-info.txt' file found in this directory. Please add this first.");
        }
        String name = androidSAF.getName(contextMethod_104, uriBuildDocumentUriUsingTree);
        class_1061.method_3043("Filename: ".concat(String.valueOf(name)));
        String strMethod_3476 = class_1290.method_3476(uri);
        String strMethod_3477 = class_1290.method_3477(uri, false);
        if (strMethod_3477 == null) {
            throw new IOException("Failed to link folder: " + strMethod_3476 + " - Check permissions.");
        }
        class_1061.method_3043("safVirualPath: ".concat(String.valueOf(strMethod_3477)));
        String strMethod_2328 = class_907.method_2328(uri.toString());
        if (name.contains("/")) {
            strSubstring = name.substring(name.lastIndexOf("/") + 1);
        } else {
            strSubstring = name;
        }
        class_992 class_992VarMethod_2658 = class_1061VarMethod_3076.field_6354.method_2658(strSubstring, name, strMethod_3477, strMethod_2328, true, false);
        class_992VarMethod_2658.field_5815 = true;
        class_992VarMethod_2658.field_5816 = uri.toString();
        class_992VarMethod_2658.field_5817 = "Path: ".concat(String.valueOf(strMethod_3476));
    }

    public static class_119 getFileData(Activity activity, Uri uri) {
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if (uri != null) {
            class_119 class_119Var = new class_119();
            class_119Var.field_322 = uri;
            try {
                class_1061.method_3043("File Uri: " + uri.toString());
                class_1061.method_3043("mimeType: ".concat(String.valueOf(activity.getContentResolver().getType(uri))));
                Cursor cursorQuery = activity.getContentResolver().query(uri, null, null, null, null);
                if (cursorQuery != null) {
                    int columnIndex = cursorQuery.getColumnIndex("_display_name");
                    int columnIndex2 = cursorQuery.getColumnIndex("_size");
                    if (cursorQuery.moveToFirst()) {
                        String string = cursorQuery.getString(columnIndex);
                        long j = cursorQuery.getLong(columnIndex2);
                        class_1061.method_3043("fileName: ".concat(String.valueOf(string)));
                        class_1061.method_3043("fileSize: ".concat(String.valueOf(j)));
                        if (string != null) {
                            class_119Var.field_319 = string;
                            class_119Var.field_320 = j;
                            class_994 class_994Var = class_994.unknown;
                            if (string != null) {
                                if (string.toLowerCase(Locale.ROOT).endsWith(".zip") || string.toLowerCase(Locale.ROOT).endsWith(".rwmod")) {
                                    class_994Var = class_994.mod;
                                }
                                if (string.toLowerCase(Locale.ROOT).endsWith(".tmx")) {
                                    class_994Var = class_994.map;
                                }
                                if (string.toLowerCase(Locale.ROOT).endsWith(".replay")) {
                                    class_994Var = class_994.replay;
                                }
                                if (string.toLowerCase(Locale.ROOT).endsWith(".rwsave")) {
                                    class_994Var = class_994.save;
                                }
                                if (string.endsWith("_map.png")) {
                                    class_994Var = class_994.mapThumbnail;
                                }
                            }
                            class_119Var.field_321 = class_994Var;
                            return class_119Var;
                        }
                        class_1061VarMethod_3076.method_3056("Cannot import - Did not receive a filename");
                        return null;
                    }
                    class_1061VarMethod_3076.method_3056("Cannot import - Did not receive a filename data");
                    return null;
                }
                class_1061VarMethod_3076.method_3056("Cannot import - Did not receive a file");
                return null;
            } catch (SecurityException e) {
                e.printStackTrace();
                class_1061VarMethod_3076.method_3056("Cannot read file - File permission error: " + e.getMessage());
                return null;
            }
        }
        class_1061VarMethod_3076.method_3056("Cannot import - no file");
        return null;
    }

    public static boolean importFileData(Activity activity, class_119 class_119Var, Integer num) {
        String strMethod_2652;
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        Uri uri = class_119Var.field_322;
        if (class_119Var.field_322 != null) {
            if (class_119Var.field_321 != class_994.unknown) {
                try {
                    FileDescriptor fileDescriptor = activity.getContentResolver().openFileDescriptor(uri, "r").getFileDescriptor();
                    String str = class_119Var.field_319;
                    class_994 class_994Var = class_119Var.field_321;
                    class_1061.method_3043("Importing " + str + "..");
                    if (num == null) {
                        strMethod_2652 = class_991.method_2652(class_994Var, str, class_899.field_5109);
                    } else {
                        strMethod_2652 = class_991.method_2652(class_994Var, str, class_899.method_2160(num.intValue()));
                    }
                    if (strMethod_2652 == null) {
                        class_1061.method_3043("saveTargetFullPathFinal==null, not writing");
                        return false;
                    }
                    FileInputStream fileInputStream = new FileInputStream(fileDescriptor);
                    try {
                        File file = new File(strMethod_2652);
                        File file2 = new File(strMethod_2652 + ".tmp");
                        try {
                            try {
                                OutputStream outputStreamMethod_2164 = class_899.method_2164(file2, false);
                                try {
                                    class_907.method_2295(fileInputStream, outputStreamMethod_2164);
                                    fileInputStream.close();
                                    if (class_1061.method_2982() && class_899.method_2186(file.getAbsolutePath())) {
                                        class_1061.method_3043("File already exists: " + file.getAbsolutePath());
                                        if (!class_899.method_2170(file)) {
                                            class_1061.method_3043("Failed to delete existing file");
                                        }
                                    }
                                    class_1061.method_3043("Finished writing file, renaming to final filename");
                                    if (class_899.method_2171(file2, file)) {
                                        class_1061.method_3043("File '" + str + "' imported");
                                        return true;
                                    }
                                    class_1061.method_3043("Failed to rename to final file");
                                    throw new IOException("Failed to rename to final file. Check file permissions of storage.");
                                } finally {
                                    if (outputStreamMethod_2164 != null) {
                                        outputStreamMethod_2164.close();
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                if (class_899.method_2186(file2.getAbsolutePath())) {
                                    class_1061.method_3043("writeSteamToFileAtomic: Removing temp file");
                                    class_899.method_2170(file2);
                                }
                                throw new IOException(e);
                            }
                        } catch (Throwable th) {
                            fileInputStream.close();
                            throw th;
                        }
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        class_1061VarMethod_3076.method_3056("Error importing mod file: " + e2.getMessage() + " (Hint: check permissions and disk space)");
                        return false;
                    }
                } catch (SecurityException e3) {
                    e3.printStackTrace();
                    class_1061VarMethod_3076.method_3056("Permission error importing mod file: " + e3.getMessage());
                    return false;
                } catch (Exception e4) {
                    e4.printStackTrace();
                    class_1061VarMethod_3076.method_3056("Failed to import file: " + e4.getMessage());
                    return false;
                }
            }
            class_1061VarMethod_3076.method_3056("Cannot import file: " + class_119Var.field_319 + " - Expected a .zip, .rwmod or .tmx file");
            return false;
        }
        class_1061VarMethod_3076.method_3056("Cannot import - no file");
        return false;
    }

    public static void askBeforeImportingFiles(Activity activity, ArrayList arrayList, Runnable runnable) {
        String str;
        String strConcat;
        boolean z;
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if (arrayList.size() == 0) {
            class_1061VarMethod_3076.method_3056("Cannot import no files");
            return;
        }
        class_994 class_994Var = class_994.unknown;
        Iterator it = arrayList.iterator();
        int i = 0;
        while (it.hasNext()) {
            class_119 class_119Var = (class_119) it.next();
            if (class_119Var.field_321 == class_994.mapThumbnail) {
                if (class_119Var.field_319.endsWith("_map.png")) {
                    String str2 = class_119Var.field_319.substring(0, class_119Var.field_319.length() - 8) + ".tmx";
                    Iterator it2 = arrayList.iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            z = false;
                            break;
                        } else if (((class_119) it2.next()).field_319.equals(str2)) {
                            z = true;
                            break;
                        }
                    }
                    if (z) {
                        class_1061.method_3031("Found matching map for thumbnail: " + class_119Var.field_319);
                    } else {
                        class_119Var.field_323 = true;
                        class_1061.method_3031("Failed to find matching map for thumbnail: " + class_119Var.field_319);
                        i++;
                    }
                } else {
                    class_119Var.field_323 = true;
                    class_1061.method_3031("Unknown map thumbnail name: " + class_119Var.field_319);
                }
            }
            if (class_119Var.field_321 == class_994.unknown) {
                class_119Var.field_323 = true;
            }
        }
        Iterator it3 = arrayList.iterator();
        int i2 = 0;
        int i3 = 0;
        while (it3.hasNext()) {
            class_119 class_119Var2 = (class_119) it3.next();
            if (!class_119Var2.field_323) {
                i2++;
            } else if (class_119Var2.field_321 != class_994.mapThumbnail) {
                i3++;
            }
        }
        if (arrayList.size() == 1) {
            class_119 class_119Var3 = (class_119) arrayList.get(0);
            if (class_119Var3.field_321 == class_994.unknown) {
                class_1061VarMethod_3076.method_3056("Cannot import file: " + class_119Var3.field_319 + " - Expected a .zip, .rwmod or .tmx files");
                return;
            }
            if (class_119Var3.field_321 == class_994.mapThumbnail) {
                class_1061VarMethod_3076.method_3032("Cannot import thumbnail: " + class_119Var3.field_319, "When importing a thumbnail also include it's map");
                return;
            }
            String str3 = class_119Var3.field_319;
            class_994Var = class_119Var3.field_321;
            String str4 = class_994Var == class_994.mod ? "Import mod?" : "Import";
            if (class_994Var == class_994.map) {
                str4 = "Import map?";
            }
            if (class_994Var == class_994.replay) {
                str4 = "Import replay?";
            }
            if (class_994Var == class_994.save) {
                str4 = "Import save?";
            }
            String str5 = str4;
            strConcat = "Are you sure you want to import: ".concat(String.valueOf(str3));
            str = str5;
        } else {
            str = "Import " + i2 + " files?";
            strConcat = "Are you sure you want to import " + i2 + " files.";
            if (arrayList.size() > 0) {
                Iterator it4 = arrayList.iterator();
                class_994 class_994Var2 = null;
                class_994 class_994Var3 = null;
                while (true) {
                    if (!it4.hasNext()) {
                        class_994Var2 = class_994Var3;
                        break;
                    }
                    class_119 class_119Var4 = (class_119) it4.next();
                    if (!class_119Var4.field_323) {
                        if (class_994Var3 == null) {
                            class_994Var3 = class_119Var4.field_321;
                        } else if (class_994Var3 != class_119Var4.field_321) {
                            break;
                        }
                    }
                }
                if (class_994Var2 != null) {
                    class_994Var = class_994Var2;
                }
            }
            if (i3 != 0) {
                strConcat = strConcat + "\n" + i3 + " files were skipped due to being an unknown type";
            }
            if (i != 0) {
                strConcat = strConcat + "\n" + i + " thumbnails were skipped due to not including a matching map";
            }
        }
        if (i2 == 0) {
            class_1061VarMethod_3076.method_3056("Cannot import any selected files - Expected a .zip, .rwmod or .tmx files");
            return;
        }
        if (!class_1061.method_2982() || class_994Var != class_994.mod || (class_1061VarMethod_3076.field_6345.storageType != 2 && class_1061VarMethod_3076.field_6345.storageType != 1)) {
            class_84.method_114(activity, str, strConcat, "Import", new class_101(activity, arrayList, runnable), null, null);
        } else {
            class_84.method_114(activity, str, strConcat + "\n\nInternal storage is recommended for faster mod loading (but is deleted by OS on uninstall)", "Import (external)", new class_100(activity, arrayList, runnable), "Import (internal)", new class_118(activity, arrayList, runnable));
        }
    }

    public static void importingFilesBackground(Activity activity, ArrayList arrayList, Runnable runnable, Integer num) {
        ModsActivity modsActivity;
        if (activity instanceof ModsActivity) {
            modsActivity = (ModsActivity) activity;
        } else {
            modsActivity = null;
        }
        if (modsActivity != null) {
            modsActivity.showDialog(0);
        }
        class_1061.method_3076().field_6441 = null;
        class_102 class_102Var = new class_102(modsActivity, activity, arrayList, runnable, num);
        if (modsActivity != null) {
            new Thread(class_102Var).start();
        } else {
            class_102Var.run();
        }
    }

    public static void importingFiles(Activity activity, ArrayList arrayList, Runnable runnable, Integer num) {
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        Iterator it = arrayList.iterator();
        int i = 0;
        while (it.hasNext()) {
            class_119 class_119Var = (class_119) it.next();
            if (!class_119Var.field_323) {
                try {
                    if (importFileData(activity, class_119Var, num)) {
                        i++;
                    }
                } catch (IOException e) {
                    class_1061.method_3076().method_3056("Error importing file: " + class_119Var + " - " + e.getMessage() + " (Hint: check permissions and disk space)");
                    e.printStackTrace();
                    return;
                }
            }
        }
        if (i > 0) {
            if (arrayList.size() == 1) {
                class_1061VarMethod_3076.method_3056("File '" + ((class_119) arrayList.get(0)).field_319 + "' imported");
            } else {
                class_1061VarMethod_3076.method_3056(i + " files imported");
            }
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    public static void onFileSelectResult(Activity activity, int i, int i2, Intent intent, Runnable runnable) {
        if (i2 == -1) {
            try {
                ClipData clipData = intent.getClipData();
                ArrayList arrayList = new ArrayList();
                if (clipData != null) {
                    for (int i3 = 0; i3 < clipData.getItemCount(); i3++) {
                        class_119 fileData = getFileData(activity, clipData.getItemAt(i3).getUri());
                        if (fileData != null) {
                            arrayList.add(fileData);
                        } else {
                            return;
                        }
                    }
                } else {
                    class_119 fileData2 = getFileData(activity, intent.getData());
                    if (fileData2 != null) {
                        arrayList.add(fileData2);
                    } else {
                        return;
                    }
                }
                askBeforeImportingFiles(activity, arrayList, runnable);
                return;
            } catch (IOException e) {
                class_1061.method_3076().method_3056("Error importing file: " + e.getMessage() + " (Hint: check permissions and disk space)");
                e.printStackTrace();
                return;
            }
        }
        class_1061.method_3043("FILE_SELECT_CODE resultCode:".concat(String.valueOf(i2)));
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x006e  */
    @Override // android.app.Activity, android.view.View.OnCreateContextMenuListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void onCreateContextMenu(android.view.ContextMenu r5, android.view.View r6, android.view.ContextMenu.ContextMenuInfo r7) {
        /*
            Method dump skipped, instruction units count: 243
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.corrodinggames.rts.appFramework.ModsActivity.onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu$ContextMenuInfo):void");
    }

    @Override // android.app.Activity
    public boolean onContextItemSelected(MenuItem menuItem) {
        class_992 class_992Var;
        class_991 class_991Var = class_1061.method_3076().field_6354;
        int itemId = menuItem.getItemId();
        Iterator it = class_991Var.field_5783.iterator();
        while (true) {
            if (it.hasNext()) {
                class_992Var = (class_992) it.next();
                if (class_992Var.field_5794 == itemId) {
                    break;
                }
            } else {
                class_992Var = null;
                break;
            }
        }
        if (class_992Var != null) {
            if (menuItem.getGroupId() == 0) {
                shareMod(class_992Var);
            } else if (menuItem.getGroupId() == 2) {
                deleteMod(class_992Var);
            } else if (menuItem.getGroupId() == 3) {
                unlinkMod(class_992Var);
            }
            return false;
        }
        class_1061.method_3076().method_3056("Mod not found");
        return true;
    }

    public void shareMod(class_992 class_992Var) {
        class_1061.method_3076();
        class_84.method_109(this, new File(class_992Var.method_2686()));
    }

    public void deleteMod(class_992 class_992Var) {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Are you sure?").setMessage("Delete mod: '" + class_992Var.method_2684() + "'?").setPositiveButton("Delete", new class_104(this, class_992Var)).setNegativeButton("Keep", new class_103(this)).show();
    }

    public void unlinkMod(class_992 class_992Var) {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Are you sure?").setMessage("Unlink mod: '" + class_992Var.method_2684() + "'? Mod files will remain unchanged.").setPositiveButton("Unlink", new class_106(this, class_992Var)).setNegativeButton("Cancel", new class_105(this)).show();
    }
}
