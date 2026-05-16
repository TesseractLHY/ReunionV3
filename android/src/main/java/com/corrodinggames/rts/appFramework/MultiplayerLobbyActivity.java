package com.corrodinggames.rts.appFramework;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import cn.tesseract.union.hook.game.LobbyHook;
import com.corrodinggames.rts.R;
import com.corrodinggames.rts.game.units.custom.logicBooleans.VariableScope;
import com.corrodinggames.rts.gameFramework.class_1061;
import com.corrodinggames.rts.gameFramework.class_907;
import com.corrodinggames.rts.gameFramework.h.class_988;
import com.corrodinggames.rts.gameFramework.j.class_1001;
import com.corrodinggames.rts.gameFramework.j.class_1040;
import com.corrodinggames.rts.gameFramework.j.class_1047;
import com.corrodinggames.rts.gameFramework.utility.class_1323;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public class MultiplayerLobbyActivity extends class_1 {
    public static final int LOADING_DIALOG = 0;
    public static final int REQUEST_ENABLE_BT_CLIENT = 2;
    public static final int REQUEST_ENABLE_BT_SERVER = 1;
    public static MultiplayerLobbyActivity lastLoaded = null;
    public static final String normalServerCell = "nsc";
    public ExpandedListView foundServersList;
    public TableLayout gameListTable;
    public Button joinBluetoothButton;
    public EditText joinIpAddress;
    public ArrayAdapter lanServersAdapter;
    public ScrollView mainScrollView;
    public EditText networkPlayerName;
    public ProgressDialog progressDialog;
    public Button refreshButton;
    public boolean showLimitedRows;
    public String textRefreshButton;
    public String textRefreshingButton;
    public final Handler uiHandler = new Handler();
    public class_1323 activityRecycledTextViews = new class_1323();
    public Handler handler = new class_170(this);
    public Runnable refreshListCallback = new class_180(this);
    public Runnable refreshServerListRunnable = new class_174(this);

    public void addDebugText(String str) {
        Message messageObtainMessage = this.handler.obtainMessage();
        messageObtainMessage.getData().putString("text", str);
        this.handler.sendMessage(messageObtainMessage);
    }

    public void addDebugTextInternal(String str) {
    }

    @Override // android.app.Activity
    public void onPause() {
        super.onPause();
    }

    @Override // com.corrodinggames.rts.appFramework.class_1, android.app.Activity
    public void onResume() {
        class_1061 class_1061VarMethod_3037 = class_1061.method_3037(this);
        if (class_1061VarMethod_3037 != null) {
            class_1061VarMethod_3037.method_2995(this);
        }
        if (class_1061VarMethod_3037.method_2963()) {
            finish();
        }
        class_1047.method_2936(this.refreshListCallback);
        class_84.method_115(this, false);
        super.onResume();
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (class_84.method_127(this, false)) {
            class_1061 class_1061VarMethod_3037 = class_1061.method_3037(this);
            setContentView(R.layout.multiplayer_lobby);
            class_84.method_120(getWindow().getDecorView().findViewById(android.R.id.content));
            this.textRefreshButton = class_988.method_2636("menus.lobby.button.refresh", new Object[0]);
            this.textRefreshingButton = class_988.method_2636("menus.lobby.button.refreshing", new Object[0]);
            getWindow().setBackgroundDrawable(null);
            for (int i = 0; i < 10; i++) {
                for (int i2 = 0; i2 < 6; i2++) {
                    this.activityRecycledTextViews.add(getCellTextView(this, null));
                }
            }
            if (class_1061VarMethod_3037.field_6345.saveMultiplayerReplays && !class_84.method_129(this)) {
                new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Cannot enable replays").setMessage("You have requested replays but file write permission is required to save them. Do you want to enable it now?").setPositiveButton("Ok", new class_181(this)).setNegativeButton("No", new class_182(this)).show();
            }
            this.foundServersList = (ExpandedListView) findViewById(R.id.foundServersList);
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, new ArrayList());
            this.lanServersAdapter = arrayAdapter;
            this.foundServersList.setAdapter((ListAdapter) arrayAdapter);
            this.foundServersList.setOnItemClickListener(new class_183(this));
            this.joinBluetoothButton = (Button) findViewById(R.id.battleroom_joinBluetoothButton);
            if (!class_1001.field_5899) {
                this.joinBluetoothButton.setVisibility(8);
            }
            this.joinBluetoothButton.setOnClickListener(new class_184(this));
            refreshServerList();
            class_1001.method_2788("network: load lobby");
            this.networkPlayerName = (EditText) findViewById(R.id.networkPlayerName);
            if (class_1061VarMethod_3037.field_6345.lastNetworkPlayerName == null) {
                this.networkPlayerName.setText("Unnamed" + class_907.method_2249(0, 999));
            } else {
                this.networkPlayerName.setText(class_1061VarMethod_3037.field_6352.method_2749(class_1061VarMethod_3037.field_6345.lastNetworkPlayerName));
            }
            this.joinIpAddress = (EditText) findViewById(R.id.joinIpAddress);
            if (class_1061VarMethod_3037.field_6345.lastNetworkIP != null) {
                this.joinIpAddress.setText(class_1061VarMethod_3037.field_6345.lastNetworkIP);
            }
            ((Button) findViewById(R.id.hostButton)).setOnClickListener(new class_185(this));
            ((Button) findViewById(R.id.watchReplayButton)).setOnClickListener(new class_188(this));
            ((Button) findViewById(R.id.joinButton)).setOnClickListener(new class_189(this));
            ScrollView scrollView = (ScrollView) findViewById(R.id.mainScrollView);
            this.mainScrollView = scrollView;
            ViewTreeObserver viewTreeObserver = scrollView.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                this.showLimitedRows = true;
                viewTreeObserver.addOnScrollChangedListener(new class_171(this, class_1061VarMethod_3037));
            } else {
                this.showLimitedRows = false;
            }
            Button button = (Button) findViewById(R.id.refreshServersButton);
            this.refreshButton = button;
            button.setOnClickListener(new class_172(this, class_1061VarMethod_3037));
            this.gameListTable = (TableLayout) findViewById(R.id.gameListTable);
            getWindow().setSoftInputMode(2);
            addDebugText("ready..");
            lastLoaded = this;
        }
    }

    public void joinServerFromList(class_1040 class_1040Var, String str) {
        class_1061.method_3076().field_6352.field_5961 = class_1040Var.field_6201;
        joinServer(str, false);
    }

    public void joinServer(String str) {
        class_1061.method_3076().field_6352.field_5961 = null;
        joinServer(str, false);
    }

    public void joinServer(String str, boolean z) {
        class_1061.method_3076().field_6352.method_2749(this.networkPlayerName.getText().toString());
        if (str != null && !str.trim().equals(VariableScope.nullOrMissingString)) {
            showDialog(0);
            class_190 class_190Var = new class_190(this, str);
            class_190Var.field_514 = z;
            new Thread(class_190Var).start();
        }
    }

    public static void refreshServerList() {
        MultiplayerLobbyActivity multiplayerLobbyActivity = lastLoaded;
        if (multiplayerLobbyActivity != null) {
            multiplayerLobbyActivity.uiHandler.post(multiplayerLobbyActivity.refreshServerListRunnable);
        }
    }

    public static ArrayList getSortedDiscoveredServers() {
        ArrayList arrayList;
        synchronized (class_1047.field_6255) {
            class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
            arrayList = new ArrayList();
            Iterator it = class_1061VarMethod_3076.field_6352.field_5947.iterator();
            while (it.hasNext()) {
                arrayList.add((class_1040) it.next());
            }
            Collections.sort(arrayList, new class_173());
        }
        return LobbyHook.getSortedDiscoveredServers(null, arrayList);
    }

    public static TextView getCellTextView(Context context, class_1323 class_1323Var) {
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if (class_1323Var != null && class_1323Var.size() > 0) {
            return (TextView) class_1323Var.method_3655();
        }
        TextView textView = new TextView(context);
        textView.setBackgroundResource(R.drawable.cell_shape);
        textView.setTextAppearance(context, android.R.attr.textAppearanceMedium);
        textView.setPadding(class_1061VarMethod_3076.method_2986(5.0f), class_1061VarMethod_3076.method_2986(5.0f), class_1061VarMethod_3076.method_2986(5.0f), class_1061VarMethod_3076.method_2986(5.0f));
        textView.setTag(normalServerCell);
        return textView;
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0040  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static android.widget.TextView addCell(com.corrodinggames.rts.gameFramework.j.class_1040 r11, android.widget.TableRow r12, java.lang.String r13, com.corrodinggames.rts.gameFramework.utility.class_1323 r14) {
        /*
            com.corrodinggames.rts.gameFramework.class_1061 r0 = com.corrodinggames.rts.gameFramework.class_1061.method_3076()
            if (r13 != 0) goto Ld
            java.lang.String r13 = "cellText==null"
            com.corrodinggames.rts.gameFramework.class_1061.method_3053(r13)
            java.lang.String r13 = ""
        Ld:
            r1 = 1
            r2 = 0
            if (r11 == 0) goto L2d
            boolean r3 = r11.field_6223
            if (r3 == 0) goto L2d
            java.lang.String r3 = "chat"
            java.lang.String r4 = r11.field_6218
            boolean r3 = r3.equalsIgnoreCase(r4)
            if (r3 == 0) goto L21
            r3 = 1
            goto L22
        L21:
            r3 = 0
        L22:
            boolean r4 = r11.method_2913()
            if (r4 == 0) goto L2b
            r4 = 1
            r5 = 1
            goto L30
        L2b:
            r4 = 1
            goto L2f
        L2d:
            r3 = 0
            r4 = 0
        L2f:
            r5 = 0
        L30:
            android.content.Context r6 = r12.getContext()
            android.widget.TextView r14 = getCellTextView(r6, r14)
            r6 = 255(0xff, float:3.57E-43)
            int r7 = android.graphics.Color.argb(r6, r6, r6, r6)
            if (r11 == 0) goto Lb4
            r8 = 35
            if (r4 == 0) goto L57
            if (r3 != 0) goto L4b
            if (r5 == 0) goto L49
            goto L4b
        L49:
            r4 = 1
            goto L72
        L4b:
            r4 = 236(0xec, float:3.31E-43)
            r7 = 249(0xf9, float:3.49E-43)
            r9 = 152(0x98, float:2.13E-43)
            int r7 = android.graphics.Color.argb(r6, r9, r4, r7)
            r4 = 1
            goto L72
        L57:
            boolean r4 = r11.field_6207
            if (r4 == 0) goto L62
            r4 = 240(0xf0, float:3.36E-43)
            int r7 = android.graphics.Color.argb(r6, r4, r4, r4)
            goto L63
        L62:
        L63:
            boolean r4 = r11.field_6200
            if (r4 == 0) goto L70
            r4 = 229(0xe5, float:3.21E-43)
            r7 = 149(0x95, float:2.09E-43)
            int r7 = android.graphics.Color.argb(r6, r4, r7, r8)
            goto L71
        L70:
        L71:
            r4 = 0
        L72:
            int r9 = r12.getChildCount()
            r10 = 3
            if (r9 != r10) goto L95
            com.corrodinggames.rts.gameFramework.class_1061 r9 = com.corrodinggames.rts.gameFramework.class_1061.method_3076()
            com.corrodinggames.rts.gameFramework.j.class_1001 r9 = r9.field_6352
            java.lang.String r9 = r9.field_5961
            if (r9 == 0) goto L8c
            java.lang.String r10 = r11.field_6201
            boolean r9 = r9.equals(r10)
            if (r9 == 0) goto L8c
            r2 = 1
        L8c:
            if (r2 == 0) goto L95
            r2 = 200(0xc8, float:2.8E-43)
            int r2 = android.graphics.Color.argb(r6, r8, r8, r2)
            r7 = r2
        L95:
            if (r3 != 0) goto Lb2
            if (r5 != 0) goto Lb2
            int r2 = r12.getChildCount()
            r3 = 4
            if (r2 != r3) goto Lb2
            int r0 = r0.method_3012(r1)
            int r11 = r11.field_6211
            if (r0 == r11) goto Lb2
            r11 = 155(0x9b, float:2.17E-43)
            r0 = 147(0x93, float:2.06E-43)
            int r7 = android.graphics.Color.argb(r6, r11, r0, r0)
            r2 = r4
            goto Lb5
        Lb2:
            r2 = r4
            goto Lb5
        Lb4:
        Lb5:
            r11 = 0
            r14.setTypeface(r11, r2)
            r14.setTextColor(r7)
            r14.setText(r13)
            r12.addView(r14)
            return r14
        */
        throw new UnsupportedOperationException("Method not decompiled: com.corrodinggames.rts.appFramework.MultiplayerLobbyActivity.addCell(com.corrodinggames.rts.gameFramework.j.class_1040, android.widget.TableRow, java.lang.String, com.corrodinggames.rts.gameFramework.utility.class_1323):android.widget.TextView");
    }

    @Override // android.app.Activity
    public Dialog onCreateDialog(int i) {
        switch (i) {
            case 0:
                ProgressDialog progressDialog = new ProgressDialog(this);
                this.progressDialog = progressDialog;
                progressDialog.setProgressStyle(0);
                this.progressDialog.setMessage("Connecting...");
                this.progressDialog.setCancelable(false);
                return this.progressDialog;
            default:
                return null;
        }
    }

    @Override // android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        class_1061.method_3043("bluetooth: onActivityResult");
        if (i != 1) {
            if (i == 2) {
                if (i2 != 0) {
                    findBluetoothServer2();
                    return;
                }
                return;
            }
            super.onActivityResult(i, i2, intent);
        }
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
}
