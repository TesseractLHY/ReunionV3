package com.corrodinggames.rts.appFramework;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.corrodinggames.rts.appFramework.android.AndroidSAF;
import com.corrodinggames.rts.gameFramework.class_1061;
import com.corrodinggames.rts.gameFramework.m.class_1081;
import com.corrodinggames.rts.gameFramework.m.class_1235;
import com.corrodinggames.rts.gameFramework.m.class_1239;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantLock;


public class GameView extends SurfaceView implements SurfaceHolder.Callback, class_5, class_126 {
    public class_127 currTouchPoint;
    public int fullHeight;
    public int fullWidth;
    public Object gameThreadSync;
    public InGameActivity inGameActivity;
    public Method lockHardwareCanvasMethod;
    public class_125 multiTouchController;
    public volatile boolean paused;
    public volatile boolean surfaceExists;
    public SurfaceHolder surfaceHolderOnLock;

    public String getStats() {
        return "NO STATS";
    }

    @Override
    public void onParentPause() {
        class_1061.method_3048("GameView:onParentPause start - " + hashCode());
        synchronized (this.gameThreadSync) {
            class_1061.method_3048("GameView:onParentPause synchronized - " + hashCode());
        }
    }

    @Override
    public void onParentWindowFocusChanged(boolean z) {
    }

    @Override
    public void onParentStart() {
        this.paused = false;
    }

    @Override
    public void onParentStop() {
        this.paused = true;
    }

    @Override
    public void onReplacedByAnotherView() {
        this.paused = true;
    }

    @Override
    public void onParentResume() {
        class_1061.method_3048("GameView:onParentResume - " + hashCode());
        this.paused = false;
    }

    public GameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.surfaceExists = false;
        this.gameThreadSync = new Object();
        this.fullWidth = -1;
        this.fullHeight = -1;
        this.paused = false;
        Log.e(AndroidSAF.TAG, "GameView:GameView()");
        this.multiTouchController = new class_125(this);
        this.currTouchPoint = new class_127();
        init(context);
    }

    public void init(Context context) {
        getHolder().addCallback(this);
        class_1061.method_3037(context);
    }

    public void finalize() throws Throwable {
        Log.e(AndroidSAF.TAG, "GameView:finalize()");
        super.finalize();
    }

    @Override
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.fullWidth = i;
        this.fullHeight = i2;
        updateResolution();
    }

    @Override
    public void updateResolution() {
        if (this.fullWidth != -1) {
            class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
            int i = this.fullWidth;
            int i2 = this.fullHeight;
            if (class_1061VarMethod_3076.field_6345.renderDoubleScale) {
                i = this.fullWidth / 2;
                i2 = this.fullHeight / 2;
            }
            if (this.surfaceExists) {
                getHolder().setFixedSize(i, i2);
            } else {
                class_1061.method_3048("updateResolution surfaceExists==false");
            }
            class_1061VarMethod_3076.method_2990(i, i2);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        class_1061.method_3048("GameView:surfaceCreated start - " + hashCode());
        class_1061.method_3076().field_6331 = true;
        this.surfaceExists = true;
        updateResolution();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        class_1061.method_3048("GameView:surfaceDestroyed start - " + hashCode());
        synchronized (this.gameThreadSync) {
            class_1061VarMethod_3076.field_6331 = false;
            this.surfaceExists = false;
            class_1061.method_3048("GameEngine catch currentGameView.gameThreadSync - " + this.gameThreadSync.hashCode());
            getHolder().getSurface().release();
        }
        class_1061.method_3048("GameView:surfaceDestroyed finished - " + hashCode());
        if (this.surfaceHolderOnLock != null) {
            class_1061.method_3048("GameView:surfaceDestroyed - Error lock is still open");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        class_1061.method_3043("onTouchEvent: Source:" + motionEvent.getSource());
        if (motionEvent.getSource() == 2) {
            class_1061.method_3043("onTouchEvent: InputDevice.SOURCE_CLASS_POINTER");
        }
        if (motionEvent.getSource() == 8194) {
            class_1061.method_3043("onTouchEvent: InputDevice.SOURCE_MOUSE");
        }
        return this.multiTouchController.method_143(motionEvent);
    }

    @Override
    public Object getDraggableObjectAtPoint(class_127 class_127Var) {
        return this;
    }

    @Override
    public void getPositionAndScale(Object obj, class_128 class_128Var) {
    }

    @Override
    public void selectObject(Object obj, class_127 class_127Var) {
        this.currTouchPoint.method_149(class_127Var);
    }

    @Override
    public boolean setPositionAndScale(Object obj, class_128 class_128Var, class_127 class_127Var) {
        this.currTouchPoint.method_149(class_127Var);
        return true;
    }

    @Override
    public void forceSurfaceUnlockWorkaround() {
        class_1061.method_3048("Forcing an unlock of surfaceview to avoid freeze - " + hashCode());
        try {
            Field declaredField = SurfaceView.class.getDeclaredField("mSurfaceLock");
            declaredField.setAccessible(true);
            ((ReentrantLock) declaredField.get(this)).unlock();
        } catch (Exception e) {
            class_1061.method_3048("Exception while forcing unlock - " + hashCode());
            e.printStackTrace();
        }
    }

    @Override
    public boolean getSurfaceExists() {
        return this.surfaceExists;
    }

    @Override
    public boolean getDirectSurfaceRendering() {
        return true;
    }

    @Override
    public class_1081 getRenderer() {
        return null;
    }

    @Override
    public boolean isPaused() {
        return this.paused;
    }

    @Override
    public Object getGameThreadSync() {
        return this.gameThreadSync;
    }

    @Override
    public InGameActivity getInGameActivity() {
        return this.inGameActivity;
    }

    @Override
    public void setInGameActivity(InGameActivity inGameActivity) {
        this.inGameActivity = inGameActivity;
    }

    @Override
    public class_127 getCurrTouchPoint() {
        return this.currTouchPoint;
    }

    @Override
    public void drawStarting(float f, int i) {
    }

    @Override
    public void drawCompleted(float f, int i) {
    }

    @Override
    public void flushCanvas() {
    }

    @Override
    public class_1239 getNewCanvasLock(boolean z) {
        Canvas canvasLockCanvas;
        if (!this.surfaceExists) {
            class_1061.method_3048("getNewCanvasLock: No surface ready");
            return null;
        }
        this.surfaceHolderOnLock = getHolder();
        if (Build.VERSION.SDK_INT >= 26) {
            if (this.lockHardwareCanvasMethod == null) {
                try {
                    this.lockHardwareCanvasMethod = SurfaceHolder.class.getMethod("lockHardwareCanvas", new Class[0]);
                } catch (NoSuchMethodException | SecurityException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                canvasLockCanvas = (Canvas) this.lockHardwareCanvasMethod.invoke(this.surfaceHolderOnLock, new Object[0]);
            } catch (Exception e3) {
                throw new RuntimeException(e3);
            }
        } else {
            canvasLockCanvas = this.surfaceHolderOnLock.lockCanvas();
        }
        if (canvasLockCanvas == null) {
            class_1061.method_3005("getNewCanvasLock: Error surfaceHolder.lockCanvas==null");
            return null;
        }
        return new class_1235(canvasLockCanvas);
    }

    @Override
    public void unlockAndReturnCanvas(class_1239 class_1239Var, boolean z) {
        try {
            this.surfaceHolderOnLock.unlockCanvasAndPost(((class_1235) class_1239Var).field_6900);
            this.surfaceHolderOnLock = null;
        } catch (Exception e) {
            throw new RuntimeException("surfaceExists=" + this.surfaceExists + ", source=" + (((class_1235) class_1239Var).field_6900 != null) + ", hash=" + hashCode(), e);
        }
    }

    @Override
    public boolean usingBasicDraw() {
        return false;
    }

    @Override
    public boolean isFullscreen() {
        return false;
    }

    public void onNewWindow() {
    }
}
