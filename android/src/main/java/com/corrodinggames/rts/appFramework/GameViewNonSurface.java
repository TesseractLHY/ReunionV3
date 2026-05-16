package com.corrodinggames.rts.appFramework;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import com.corrodinggames.rts.appFramework.android.AndroidSAF;
import com.corrodinggames.rts.gameFramework.class_1061;
import com.corrodinggames.rts.gameFramework.class_883;
import com.corrodinggames.rts.gameFramework.m.class_1081;
import com.corrodinggames.rts.gameFramework.m.class_1223;
import com.corrodinggames.rts.gameFramework.m.class_1239;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class GameViewNonSurface extends View implements class_5, class_126 {
    public static final int BUFFER_SIZE = 2;
    public static final boolean LOG_TIMES = false;
    public static int canvasBuffers_nextDraw;
    public static GameViewNonSurface staticGameView = null;
    public static Object bufferChangeLock = new Object();
    public static class_6[] canvasBuffers = null;
    public boolean backgroundRemoved;
    public class_7 canvasProxyLoadingMessage;
    public Context context;
    public Resources contextResources;
    public class_127 currTouchPoint;
    public Object drawNotifier;
    public int fullHeight;
    public int fullWidth;
    public Object gameThreadSync;
    public InGameActivity inGameActivity;
    public float lastDeltaSpeed;
    public int log_time_count;
    public float log_time_totalTime;
    public float log_time_worstTime;
    public class_125 multiTouchController;
    public boolean neverDrawn;
    public volatile boolean paused;
    public class_1081 renderer;
    public volatile boolean surfaceExists;
    public Object updateNotifier;

    public GameViewNonSurface(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.surfaceExists = false;
        this.gameThreadSync = new Object();
        this.fullWidth = -1;
        this.fullHeight = -1;
        this.paused = false;
        this.lastDeltaSpeed = 1.0f;
        this.drawNotifier = new Object();
        this.updateNotifier = new Object();
        this.canvasProxyLoadingMessage = new class_7(this);
        this.neverDrawn = true;
        this.backgroundRemoved = false;
        Log.e(AndroidSAF.TAG, "GameView:GameView()");
        this.multiTouchController = new class_125(this);
        this.currTouchPoint = new class_127();
        init(context);
    }

    public static GameViewNonSurface getMainView() {
        return staticGameView;
    }

    @Override
    public void onParentStart() {
        this.paused = false;
    }

    @Override
    public void onParentStop() {
        this.paused = true;
        synchronized (this.drawNotifier) {
            this.drawNotifier.notifyAll();
        }
        synchronized (this.updateNotifier) {
            this.updateNotifier.notifyAll();
        }
        flushCanvas();
        this.neverDrawn = true;
        synchronized (this.gameThreadSync) {
            class_1061.method_3048("GameView:onParentPause synchronized - " + hashCode());
        }
    }

    @Override
    public void onReplacedByAnotherView() {
        this.paused = true;
        synchronized (this.drawNotifier) {
            this.drawNotifier.notifyAll();
        }
        synchronized (this.updateNotifier) {
            this.updateNotifier.notifyAll();
        }
        flushCanvas();
        this.neverDrawn = true;
    }

    @Override
    public void onParentPause() {
        class_1061.method_3048("GameView:onParentStop start - " + hashCode());
    }

    @Override
    public void onParentWindowFocusChanged(boolean z) {
    }

    @Override
    public void onParentResume() {
        class_1061.method_3048("GameView:onParentResume - " + hashCode());
        this.paused = false;
    }

    public void init(Context context) {
        if (canvasBuffers == null) {
            canvasBuffers = new class_6[2];
            for (int i = 0; i < 2; i++) {
                canvasBuffers[i] = new class_6(this, i);
            }
        }
        if (staticGameView != null) {
            Log.e(AndroidSAF.TAG, "gameView is not null");
        }
        staticGameView = this;
        this.context = context;
        this.contextResources = context.getResources();
        class_1061.method_3037(context);
    }

    public void finalize() throws Throwable {
        Log.e(AndroidSAF.TAG, "GameView:finalize()");
        super.finalize();
    }

    public Resources getContextResources() {
        return this.contextResources;
    }

    @Override
    public void drawStarting(float f, int i) {
    }

    @Override
    public void flushCanvas() {
        class_1061.method_3043("GameViewNonSurface: flushCanvas");
        for (int i = 0; i < 2; i++) {
            class_6 class_6Var = canvasBuffers[i];
            if (lockLock(class_6Var.field_140)) {
                try {
                    class_6Var.field_139.method_3230();
                    class_6Var.field_139.method_3381(false);
                } finally {
                    class_6Var.field_140.unlock();
                }
            } else {
                class_1061.method_3043("flushCanvas: Failed to get Canvas Lock");
            }
            class_6Var.field_138 = true;
        }
    }

    @Override
    public void drawCompleted(float f, int i) {
        this.lastDeltaSpeed = f;
        Context context = getContext();
        if (!(context instanceof Activity)) {
            class_1061.method_3043("Warning context is:" + context.getClass());
        }
        postInvalidate();
    }

    public boolean lockLock(ReentrantLock var1) {
        while (true) {
            try {
                if (var1.tryLock(250L, TimeUnit.MILLISECONDS)) break;
                class_1061.method_3043("getLock: timeout getting lock");
                class_1061.method_2969();
            } catch (InterruptedException var4) {
                var4.printStackTrace();
                continue;
            }
            return false;
        }
        return true;
    }

    public class_1239 getAnyOldAlreadyDrawnBuffer() {
        class_1061.method_3076();
        for (int i = 0; i < 2; i++) {
            class_6 class_6Var = canvasBuffers[i];
            if (!class_6Var.field_140.isLocked() && lockLock(class_6Var.field_140)) {
                return class_6Var.field_139;
            }
        }
        return null;
    }

    public class_1239 getNewCanvasLock(boolean p1) {
        class_1061 game = class_1061.method_3076();
        if (!this.paused || game.method_3082()) {
            return null;
        }
        class_6 buffer = this.findFreeCanvasBuffer(p1);
        int attempts = 0;
        while (buffer == null || buffer.field_138 == p1) {
            if (buffer == null) {
                attempts = 0;
            }
            if (!game.field_6334) {
                String level = game.field_6441;
                if (!p1 && level != null) {
                    this.canvasProxyLoadingMessage.method_100(level);
                    return this.canvasProxyLoadingMessage;
                } else {
                    class_1061.method_3043("getNewCanvasLock: no level loaded");
                    class_1061.method_2969();
                    return null;
                }
            } else {
                if (!this.paused || !game.method_3082()) {
                    class_1061.method_3043("getNewCanvasLock: paused while getting lock");
                    class_1061.method_2969();
                    return null;
                }
                try {
                    if (p1) {
                        synchronized (this.drawNotifier) {
                            this.drawNotifier.wait(100L);
                        }
                    } else {
                        synchronized (this.updateNotifier) {
                            this.updateNotifier.wait(100L);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                buffer = this.findFreeCanvasBuffer(p1);
                String level = game.field_6441;
                if (!p1 && level != null) {
                    this.canvasProxyLoadingMessage.method_100(level);
                    return this.canvasProxyLoadingMessage;
                } else if (!p1 && attempts >= 20) {
                    return null;
                } else {
                    attempts++;
                }
            }
        }
        ReentrantLock lock = buffer.field_140;
        if (this.lockLock(lock)) {
            if (p1) {
                buffer.field_139.method_3230();
            }
            return buffer.field_139;
        } else {
            if (this.paused) {
                class_1061.method_3043("getNewCanvasLock: paused while failing to lock");
                class_1061.method_2969();
            }
            return null;
        }
    }

    @Override
    public void unlockAndReturnCanvas(class_1239 class_1239Var, boolean z) {
        if (class_1239Var == this.canvasProxyLoadingMessage) {
            return;
        }
        synchronized (bufferChangeLock) {
            class_6 bufferFromCanvas = getBufferFromCanvas(class_1239Var);
            bufferFromCanvas.field_140.unlock();
            if (z) {
                bufferFromCanvas.field_138 = false;
                canvasBuffers_nextDraw = bufferFromCanvas.field_141;
            } else {
                bufferFromCanvas.field_138 = true;
            }
        }
        synchronized (this.updateNotifier) {
            this.updateNotifier.notifyAll();
        }
    }

    public class_6 getBufferFromCanvas(class_1239 class_1239Var) {
        for (int i = 0; i < 2; i++) {
            class_6 class_6Var = canvasBuffers[i];
            if (class_6Var.field_139 == class_1239Var) {
                return class_6Var;
            }
        }
        throw new RuntimeException("unlockAndReturnCanvas: canvasBuffer==null");
    }

    public class_6 findFreeCanvasBuffer(boolean z) {
        if (z) {
            for (int i = 0; i < 2; i++) {
                class_6 class_6Var = canvasBuffers[i];
                if (class_6Var.field_138 == z) {
                    return class_6Var;
                }
            }
        } else {
            class_6 class_6Var2 = canvasBuffers[canvasBuffers_nextDraw];
            if (!class_6Var2.field_138) {
                return class_6Var2;
            }
        }
        return null;
    }

    @Override
    public void onDraw(Canvas canvas) {
        class_1061.method_3076();
        boolean z = false;
        class_1239 newCanvasLock = getNewCanvasLock(false);
        if (newCanvasLock != null) {
            try {
                class_883 class_883Var = class_883.field_4869;
                ((class_1223) newCanvasLock).method_3231(canvas);
                if (!newCanvasLock.method_3402()) {
                    class_1061.method_3043("onDraw: bufferedCanvas not drawn on");
                } else {
                    this.neverDrawn = false;
                }
                newCanvasLock.method_3381(false);
                class_883 class_883Var2 = class_883.field_4869;
                invalidate();
                unlockAndReturnCanvas(newCanvasLock, false);
                z = true;
            } catch (Throwable th) {
                unlockAndReturnCanvas(newCanvasLock, false);
                throw th;
            }
        } else {
            class_1061.method_3043("onDraw: Failed to get Canvas Lock");
        }
        synchronized (this.drawNotifier) {
            this.drawNotifier.notifyAll();
        }
        if (this.neverDrawn || !z) {
            class_1061.method_3043("onDraw: Drawing black");
            canvas.drawColor(-16777216);
        }
    }

    public void onNewWindow() {
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
        float f;
        if (this.fullWidth != -1) {
            class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
            int i = this.fullWidth;
            int i2 = this.fullHeight;
            if (class_1061VarMethod_3076.field_6345.renderDoubleScale) {
                i = this.fullWidth / 2;
                i2 = this.fullHeight / 2;
            }
            if (!class_1061VarMethod_3076.field_6345.renderDoubleScale) {
                f = 1.0f;
            } else {
                f = 2.0f;
            }
            class_1061VarMethod_3076.method_2991(i, i2, f);
        }
        flushCanvas();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.neverDrawn = true;
    }

    @Override
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updateResolution();
        class_1061.method_3076().field_6331 = true;
        this.surfaceExists = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.multiTouchController.method_143(motionEvent);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent motionEvent) {
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if ((motionEvent.getSource() & 2) != 0) {
            if (class_1061VarMethod_3076 != null) {
                class_1061VarMethod_3076.field_6450 = motionEvent.getRawX();
                class_1061VarMethod_3076.field_6451 = motionEvent.getRawY();
            }
            switch (motionEvent.getAction()) {
                case 8:
                    if (class_1061VarMethod_3076 != null) {
                        class_1061VarMethod_3076.method_3055((int) (motionEvent.getAxisValue(9) * 30.0f));
                    } else {
                        class_1061.method_3043("onGenericMotionEvent: game==null");
                    }
                    return true;
            }
        }
        return super.onGenericMotionEvent(motionEvent);
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

    public String getStats() {
        return "NO STATS";
    }

    @Override
    public boolean usingBasicDraw() {
        return false;
    }

    @Override
    public boolean isFullscreen() {
        return false;
    }
}
