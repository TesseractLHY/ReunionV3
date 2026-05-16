package com.corrodinggames.rts.appFramework;

import android.app.Activity;
import android.os.Bundle;
import com.corrodinggames.rts.gameFramework.class_1061;

public class ClosingActivity extends Activity {
    public void onResume() {
        super.onResume();
        finish();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if (class_1061VarMethod_3076 != null && class_1061VarMethod_3076.field_6352 != null) {
            class_1061VarMethod_3076.field_6352.method_2699();
        }
        finish();
    }
}
