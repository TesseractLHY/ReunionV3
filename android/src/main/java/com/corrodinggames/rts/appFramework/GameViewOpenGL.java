package com.corrodinggames.rts.appFramework;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.corrodinggames.rts.appFramework.android.AndroidSAF;
import com.corrodinggames.rts.gameFramework.b.class_807;
import com.corrodinggames.rts.gameFramework.b.class_813;
import com.corrodinggames.rts.gameFramework.b.class_828;
import com.corrodinggames.rts.gameFramework.b.class_840;
import com.corrodinggames.rts.gameFramework.class_1061;
import com.corrodinggames.rts.gameFramework.class_907;
import com.corrodinggames.rts.gameFramework.m.class_1081;
import com.corrodinggames.rts.gameFramework.m.class_1148;
import com.corrodinggames.rts.gameFramework.m.class_1236;
import com.corrodinggames.rts.gameFramework.m.class_1239;
import com.corrodinggames.rts.gameFramework.m.class_1241;
import com.corrodinggames.rts.gameFramework.m.class_1242;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantLock;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;

/* JADX INFO: loaded from: classes.dex */
public class GameViewOpenGL extends GLSurfaceViewShared implements class_5, class_126, class_261 {
    public static final int EGL_CONTEXT_CLIENT_VERSION_VALUE = 2;
    public static class_9 renderManagerThread = null;
    public static final boolean retainGlContext = true;
    public static class_828 retainedCanvas;
    public class_1236 canvasDirectGL;
    public class_1242 canvasProxy;
    public Context context;
    public Resources contextResources;
    public class_127 currTouchPoint;
    public Object drawDone;
    public volatile boolean drawPending;
    public int drawTimeouts;
    public GL10 field_129;
    public int fullHeight;
    public int fullWidth;
    public Object gameThreadSync;
    public boolean hasCanvasRendered;
    public InGameActivity inGameActivity;
    public boolean isActive;
    public Method lockHardwareCanvasMethod;
    public boolean loggedDrawTimeout;
    public class_828 mCanvas;
    public class_125 multiTouchController;
    public volatile boolean paused;
    public class_1081 renderer;
    public volatile boolean surfaceExists;
    public SurfaceHolder surfaceHolderOnLock;
    public static int numberOfNonRenderedCanvas = 0;
    public static Object renderManagerLock = new Object();
    public static boolean requestRenderQueued = false;
    public static Object makeActiveLock = new Object();
    public static EGLContext retainedGlContext = null;
    public static GameViewOpenGL lastHeldSurfaceView = null;

    public void requestRenderNonBlocking() {
        requestRender();
    }

    public String getStats() {
        return "NO STATS";
    }

    @Override
    public void onParentStop() {
    }

    @Override
    public void onParentStart() {
    }

    @Override
    public void onReplacedByAnotherView() {
        this.paused = true;
        synchronized (this.drawDone) {
            this.drawDone.notifyAll();
        }
    }

    @Override
    public void onParentPause() {
        class_1061.method_3048("GameView:onParentPause start - " + hashCode());
        synchronized (this.gameThreadSync) {
            class_1061.method_3048("GameView:onParentPause synchronized - " + hashCode());
        }
    }

    @Override
    public void onParentResume() {
        class_1061.method_3048("GameView:onParentResume - " + hashCode());
        this.paused = false;
        makeActive();
    }

    @Override
    public void onParentWindowFocusChanged(boolean z) {
    }

    public GameViewOpenGL(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.surfaceExists = false;
        this.gameThreadSync = new Object();
        this.fullWidth = -1;
        this.fullHeight = -1;
        this.hasCanvasRendered = false;
        this.paused = false;
        this.canvasProxy = new class_1242();
        this.drawDone = new Object();
        this.drawTimeouts = 0;
        this.loggedDrawTimeout = false;
        this.canvasDirectGL = new class_1236();
        this.isActive = true;
        Log.e(AndroidSAF.TAG, "GameView:GameViewOpenGL()");
        this.multiTouchController = new class_125(this);
        this.currTouchPoint = new class_127();
        init(context);
    }

    public void init(Context context) {
        initGL();
        this.context = context;
        this.contextResources = context.getResources();
        class_1061.method_3037(context);
    }

    @Override
    public void finalize() throws Throwable {
        Log.e(AndroidSAF.TAG, "GameView:finalize()");
        super.finalize();
    }

    public Resources getContextResources() {
        return this.contextResources;
    }

    @Override
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        synchronized (this.gameThreadSync) {
            class_1061.method_3048("GameEngine onSizeChanged currentGameView.gameThreadSync - " + this.gameThreadSync.hashCode());
        }
        super.onSizeChanged(i, i2, i3, i4);
        class_1061.method_3043("GameViewOpenGL onSizeChanged: " + i + ", " + i2);
        this.fullWidth = i;
        this.fullHeight = i2;
        updateResolution();
        if (lastHeldSurfaceView == this && this.mCanvas != null) {
            class_1061.method_3043("GameViewOpenGL mCanvas.setSize: " + i + ", " + i2);
            this.mCanvas.method_1909(i, i2);
        }
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
            getHolder().setFixedSize(i, i2);
            class_1061VarMethod_3076.method_2990(i, i2);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        class_1061.method_3048("GameView:surfaceCreated start - " + hashCode());
        updateResolution();
        class_1061.method_3076().field_6331 = true;
        this.surfaceExists = true;
        super.surfaceCreated(surfaceHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        class_1061.method_3048("GameView:surfaceDestroyed start - " + hashCode());
        if (lastHeldSurfaceView == this) {
            class_1061VarMethod_3076.field_6331 = false;
        }
        this.surfaceExists = false;
        synchronized (this.gameThreadSync) {
            class_1061.method_3048("GameEngine catch currentGameView.gameThreadSync - " + this.gameThreadSync.hashCode());
        }
        class_1061.method_3048("GameView:surfaceDestroyed finished - " + hashCode());
        this.drawPending = false;
        super.surfaceDestroyed(surfaceHolder);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        motionEvent.getSource();
        motionEvent.getSource();
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
        class_1239 class_1241Var;
        synchronized (this.drawDone) {
            if (this.drawPending) {
                try {
                    this.drawDone.wait(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (this.drawPending) {
                int i = this.drawTimeouts + 1;
                this.drawTimeouts = i;
                if (i > 3 && !this.loggedDrawTimeout) {
                    class_1061.method_3043("getNewCanvasLock - Timing out - surfaceExists:" + this.surfaceExists);
                    if (this.surfaceExists && isSurfaceBadHack()) {
                        class_1061.method_3043("Detected bad surface, removing all retained opengl context");
                        clearRetainedGLContext();
                    }
                }
                class_1241Var = new class_1241();
            } else {
                this.drawTimeouts = 0;
                this.canvasProxy.method_3230();
                class_1241Var = this.canvasProxy;
            }
        }
        return class_1241Var;
    }

    @Override
    public void unlockAndReturnCanvas(class_1239 class_1239Var, boolean z) {
        synchronized (this.drawDone) {
            this.drawPending = true;
        }
        requestRenderNonBlocking();
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

    public static void clearRetainedGLContext() {
        retainedGlContext = null;
        retainedCanvas = null;
    }

    public void makeActive() {
        synchronized (makeActiveLock) {
            GameViewOpenGL gameViewOpenGL = lastHeldSurfaceView;
            if (gameViewOpenGL != null && gameViewOpenGL != this) {
                gameViewOpenGL.isActive = false;
                gameViewOpenGL.onPause();
            }
            lastHeldSurfaceView = this;
            if (!this.isActive) {
                this.isActive = true;
                onResume();
            }
        }
    }

    public void initGL() {
        setZOrderOnTop(false);
        setEGLContextClientVersion(2);
        setEGLContextFactory(new class_8(this));
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        setPreserveEGLContextOnPause(true);
        getHolder().setFormat(-3);
        setRenderer(this);
        setRenderMode(0);
    }

    @Override // com.corrodinggames.rts.appFramework.class_261
    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        class_1061.method_3043("GameViewOpenGL onSurfaceCreated");
        if (retainedCanvas == null) {
            retainedCanvas = new class_828();
        }
        this.mCanvas = retainedCanvas;
    }

    @Override // com.corrodinggames.rts.appFramework.class_261
    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        class_1061.method_3043("GameViewOpenGL onSurfaceChanged");
        if (lastHeldSurfaceView == this) {
            this.mCanvas.method_1909(i, i2);
        } else {
            class_1061.method_3043("GameViewOpenGL onSurfaceChanged - not lastHeldSurfaceView");
        }
    }

    @Override // com.corrodinggames.rts.appFramework.class_261
    public void onDrawFrame(GL10 gl10) {
        this.field_129 = gl10;
        this.mCanvas.method_1918();
        onGLDraw(this.mCanvas);
        this.hasCanvasRendered = true;
    }

    public void onGLDraw(class_828 class_828Var) {
        class_1061.method_3076();
        if (this.drawPending) {
            synchronized (this.canvasDirectGL.field_6904) {
                class_1236 class_1236Var = this.canvasDirectGL;
                class_1236Var.field_6902 = class_828Var;
                class_1236Var.field_6903 = (class_840) class_1236Var.field_6902.method_1917();
                class_1236 class_1236Var2 = this.canvasDirectGL;
                class_840 class_840Var = (class_840) class_1236Var2.field_6902.method_1917();
                class_840Var.method_1982();
                GLES20.glViewport(0, 0, class_840Var.field_4649, class_840Var.field_4650);
                class_840Var.field_4613 = true;
                class_840Var.field_4664.field_4678 = null;
                if (class_840Var.field_4626 != null) {
                    class_813 class_813Var = class_840Var.field_4626;
                    class_813Var.field_4505++;
                    if (class_813Var.field_4504 && class_813Var.field_4505 > 600) {
                        class_813Var.field_4506 = true;
                        class_813Var.field_4502.clear();
                    }
                }
                if (class_1236.field_6908) {
                    class_1236.field_6908 = false;
                    class_1236Var2.field_6902.method_1919();
                }
                class_1242 class_1242Var = this.canvasProxy;
                class_1242Var.field_6927 = this.canvasDirectGL;
                class_1148[] class_1148VarArr = class_1242Var.field_6937.field_6685;
                int i = class_1242Var.field_6938;
                for (int i2 = 0; i2 < i; i2++) {
                    class_1148 class_1148Var = class_1148VarArr[i2];
                    class_1148Var.field_6677.method_3410(class_1148Var.field_6683.field_6927, class_1148Var);
                }
                this.canvasProxy.method_3230();
                class_1236 class_1236Var3 = this.canvasDirectGL;
                class_840 class_840Var2 = class_1236Var3.field_6903;
                if (class_1236.field_6907) {
                    class_840Var2.method_1942("GL - #tex: " + class_840.field_4606 + " tex size:" + class_907.method_2317(class_840.field_4607), 70.0f, 90.0f, class_840Var2.field_4605);
                }
                class_1236Var3.field_6903.method_1979(null);
                class_840Var2.method_1952();
                class_840Var2.method_1979(null);
                if (class_840Var2.field_4655.field_4462 != 0 || class_840Var2.field_4656.field_4462 != 0) {
                    synchronized (class_840Var2.field_4655) {
                        class_807 class_807Var = class_840Var2.field_4655;
                        if (class_840Var2.field_4655.field_4462 > 0) {
                            class_840.field_4657.method_1851(class_807Var.field_4462, class_807Var.field_4461);
                            class_807Var.method_1854();
                        }
                        class_807 class_807Var2 = class_840Var2.field_4656;
                        if (class_807Var2.field_4462 > 0) {
                            class_840.field_4657.method_1853(class_807Var2.field_4462, class_807Var2.field_4461);
                            class_807Var2.method_1854();
                        }
                    }
                }
                if (class_840Var2.field_4626 != null) {
                    class_840Var2.field_4626.method_1870();
                }
                if (class_1236.field_6908) {
                    class_1236.field_6908 = false;
                    class_1236Var3.field_6902.method_1919();
                }
                if (class_840Var2.field_4665 != 0) {
                    class_1061.method_3043("endFrame: currentTransformIndex=" + class_840Var2.field_4665);
                }
                class_1236Var3.field_6906++;
                if (class_1236Var3.field_6906 > 60) {
                    class_1236Var3.field_6906 = 0;
                    class_1236Var3.field_6902.method_1921();
                    class_1236Var3.field_6902.method_1920();
                }
                this.canvasProxy.field_6939 = false;
            }
            this.drawPending = false;
            synchronized (this.drawDone) {
                this.drawDone.notifyAll();
            }
        }
    }
}
