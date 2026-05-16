package com.corrodinggames.rts.appFramework;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.corrodinggames.rts.appFramework.android.AndroidSAF;
import com.corrodinggames.rts.gameFramework.class_1061;
import com.corrodinggames.rts.gameFramework.m.class_1081;
import com.corrodinggames.rts.gameFramework.m.class_1239;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: loaded from: classes.dex */
public class GameViewThreaded extends SurfaceView implements SurfaceHolder.Callback, class_5, class_126 {
    public static final int BUFFER_SIZE = 1;
    public static int canvasBuffers_nextDraw;
    public Object canvasSync;
    public Context context;
    public Resources contextResources;
    public class_127 currTouchPoint;
    public Object drawNotifier;
    public class_11 drawThread;
    public int fullHeight;
    public int fullWidth;
    public Object gameThreadSync;
    public InGameActivity inGameActivity;
    public float lastDeltaSpeed;
    public class_125 multiTouchController;
    public volatile boolean paused;
    public class_1081 renderer;
    public volatile boolean surfaceExists;
    public volatile SurfaceHolder surfaceHolder;
    public Object updateNotifier;
    public static GameViewThreaded staticGameView = null;
    public static Object bufferChangeLock = new Object();
    public static class_10[] canvasBuffers = null;

    @Override
    public void onParentStop() {
    }

    @Override
    public void onParentStart() {
    }

    @Override
    public void onReplacedByAnotherView() {
    }

    @Override
    public void onParentPause() {
        class_1061.method_3048("GameViewThreaded:onParentPause start - " + hashCode());
        this.paused = true;
        class_11 class_11Var = this.drawThread;
        class_1061.method_3043("GameViewThreadedThread - start marking pause");
        synchronized (class_11Var.field_159) {
            class_11Var.field_158 = true;
        }
        class_1061.method_3043("GameViewThreadedThread - end marking pause");
        synchronized (this.gameThreadSync) {
            class_1061.method_3048("GameViewThreaded:onParentPause synchronized - " + hashCode());
        }
    }

    @Override
    public void onParentResume() {
        class_1061.method_3048("GameViewThreaded:onParentResume - " + hashCode());
        this.paused = false;
        class_11 class_11Var = this.drawThread;
        class_1061.method_3043("GameViewThreadedThread - wakeup");
        synchronized (class_11Var.field_159) {
            class_11Var.field_158 = false;
            class_11Var.field_159.notifyAll();
        }
    }

    @Override
    public void onParentWindowFocusChanged(boolean z) {
    }

    public static GameViewThreaded getMainView() {
        return staticGameView;
    }

    public GameViewThreaded(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.surfaceExists = false;
        this.gameThreadSync = new Object();
        this.canvasSync = new Object();
        this.lastDeltaSpeed = 1.0f;
        this.drawNotifier = new Object();
        this.updateNotifier = new Object();
        this.fullWidth = -1;
        this.fullHeight = -1;
        this.paused = false;
        Log.e(AndroidSAF.TAG, "GameView:GameView()");
        this.multiTouchController = new class_125(this);
        this.currTouchPoint = new class_127();
        init(context);
    }

    public void init(Context context) {
        if (canvasBuffers == null) {
            canvasBuffers = new class_10[1];
            for (int i = 0; i <= 0; i++) {
                canvasBuffers[0] = new class_10(this);
            }
        }
        if (staticGameView != null) {
            Log.e(AndroidSAF.TAG, "gameView is not null");
        }
        class_11 class_11Var = new class_11(this);
        this.drawThread = class_11Var;
        class_11Var.setPriority(8);
        this.drawThread.start();
        staticGameView = this;
        this.context = context;
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
        this.contextResources = context.getResources();
        class_1061.method_3037(context);
    }

    public void finalize() throws Throwable {
        Log.e(AndroidSAF.TAG, "GameView:finalize()");
        super.finalize();
    }

    public SurfaceHolder getSurfaceHolder() {
        return this.surfaceHolder;
    }

    public Resources getContextResources() {
        return this.contextResources;
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
        synchronized (this.canvasSync) {
            if (this.fullWidth != -1) {
                class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
                int i = this.fullWidth;
                int i2 = this.fullHeight;
                if (class_1061VarMethod_3076.field_6345.renderDoubleScale) {
                    i = this.fullWidth / 2;
                    i2 = this.fullHeight / 2;
                }
                staticGameView.getHolder().setFixedSize(i, i2);
                class_1061VarMethod_3076.method_2990(i, i2);
            }
            flushCanvas();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        class_1061.method_3048("GameView:surfaceCreated start - " + hashCode());
        this.surfaceHolder = surfaceHolder;
        this.surfaceHolder.addCallback(this);
        updateResolution();
        class_1061.method_3076().field_6331 = true;
        this.surfaceExists = true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        class_1061.method_3048("GameView:surfaceDestroyed start - " + hashCode());
        class_1061VarMethod_3076.field_6331 = false;
        this.surfaceExists = false;
        synchronized (this.drawNotifier) {
            this.drawNotifier.notifyAll();
        }
        synchronized (this.gameThreadSync) {
            class_1061.method_3048("GameEngine catch currentGameView.gameThreadSync - " + this.gameThreadSync.hashCode());
        }
        class_1061.method_3048("GameView:surfaceDestroyed finished - " + hashCode());
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
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
        return this.renderer;
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

    public class_10 getBufferFromCanvas(class_1239 class_1239Var) {
        for (int i = 0; i <= 0; i++) {
            class_10 class_10Var = canvasBuffers[0];
            if (class_10Var.field_152 == class_1239Var) {
                return class_10Var;
            }
        }
        throw new RuntimeException("unlockAndReturnCanvas: canvasBuffer==null");
    }

    public class_10 findFreeCanvasBuffer(boolean z) {
        if (z) {
            for (int i = 0; i <= 0; i++) {
                class_10 class_10Var = canvasBuffers[0];
                if (class_10Var.field_151 == z) {
                    return class_10Var;
                }
            }
        } else {
            class_10 class_10Var2 = canvasBuffers[canvasBuffers_nextDraw];
            if (!class_10Var2.field_151) {
                return class_10Var2;
            }
        }
        return null;
    }

    @Override
    public void drawStarting(float f, int i) {
    }

    public boolean lockLock(ReentrantLock reentrantLock) {
        while (!reentrantLock.tryLock(250L, TimeUnit.MILLISECONDS)) {
            try {
                class_1061.method_3043("getLock: timeout getting lock");
                class_1061.method_2969();
                return false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public void flushCanvas() {
        class_1061.method_3043("GameViewNonSurface: flushCanvas");
        for (int i = 0; i <= 0; i++) {
            class_10 class_10Var = canvasBuffers[0];
            if (lockLock(class_10Var.field_153)) {
                try {
                    class_10Var.field_152.method_3230();
                    class_10Var.field_152.field_6697 = false;
                } finally {
                    class_10Var.field_153.unlock();
                }
            } else {
                class_1061.method_3043("flushCanvas: Failed to get Canvas Lock");
            }
            class_10Var.field_151 = true;
        }
    }

    @Override
    public void drawCompleted(float f, int i) {
        this.lastDeltaSpeed = f;
        Context context = getContext();
        if (!(context instanceof Activity)) {
            class_1061.method_3043("Warning context is:" + context.getClass());
        }
    }

    public String getStats() {
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 <= 0; i3++) {
            if (canvasBuffers[0].field_151) {
                i2++;
            } else {
                i++;
            }
        }
        return "drawnBuffers:" + i + " blackBuffers:" + i2;
    }

    @Override
    public class_1239 getNewCanvasLock(boolean z) {
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if (this.paused || !class_1061VarMethod_3076.method_3082()) {
            return null;
        }
        class_10 class_10VarFindFreeCanvasBuffer = findFreeCanvasBuffer(z);
        if (class_10VarFindFreeCanvasBuffer == null) {
            while (true) {
                if (class_10VarFindFreeCanvasBuffer != null && class_10VarFindFreeCanvasBuffer.field_151 == z) {
                    break;
                }
                if (this.paused || !class_1061VarMethod_3076.method_3082()) {
                    break;
                }
                if (z) {
                    try {
                        synchronized (this.drawNotifier) {
                            class_10VarFindFreeCanvasBuffer = findFreeCanvasBuffer(z);
                            if (class_10VarFindFreeCanvasBuffer == null || class_10VarFindFreeCanvasBuffer.field_151 != z) {
                                this.drawNotifier.wait(200L);
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    synchronized (this.updateNotifier) {
                        class_10VarFindFreeCanvasBuffer = findFreeCanvasBuffer(z);
                        if (class_10VarFindFreeCanvasBuffer == null || class_10VarFindFreeCanvasBuffer.field_151 != z) {
                            this.updateNotifier.wait(500L);
                        }
                    }
                }
            }
            class_1061.method_3043("getNewCanvasLock: paused while getting lock");
            class_1061.method_2969();
            return null;
        }
        if (lockLock(class_10VarFindFreeCanvasBuffer.field_153)) {
            return class_10VarFindFreeCanvasBuffer.field_152;
        }
        if (!this.paused) {
            return null;
        }
        class_1061.method_3043("getNewCanvasLock: paused while failing to lock");
        class_1061.method_2969();
        return null;
    }

    @Override
    public void unlockAndReturnCanvas(class_1239 class_1239Var, boolean z) {
        synchronized (bufferChangeLock) {
            class_10 bufferFromCanvas = getBufferFromCanvas(class_1239Var);
            bufferFromCanvas.field_153.unlock();
            if (z) {
                bufferFromCanvas.field_151 = false;
                canvasBuffers_nextDraw = bufferFromCanvas.field_154;
            } else {
                bufferFromCanvas.field_151 = true;
            }
        }
        synchronized (this.updateNotifier) {
            this.updateNotifier.notifyAll();
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
