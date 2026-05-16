package com.corrodinggames.rts.appFramework;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes.dex */
public class GLSurfaceViewShared extends SurfaceView implements SurfaceHolder.Callback2 {
    public static final int CONFIG_CHECK_GL_ERROR = 1;
    public static final int DEBUG_CHECK_GL_ERROR = 1;
    public static final int DEBUG_LOG_GL_CALLS = 2;
    public static final int EGL14_EGL_OPENGL_ES2_BIT = 4;
    public static final int EGLExt_EGL_OPENGL_ES3_BIT_KHR = 64;
    public static final boolean LOG_ATTACH_DETACH = false;
    public static final boolean LOG_EGL = false;
    public static final boolean LOG_PAUSE_RESUME = false;
    public static final boolean LOG_RENDERER = false;
    public static final boolean LOG_RENDERER_DRAW_FRAME = false;
    public static final boolean LOG_SURFACE = false;
    public static final boolean LOG_THREADS = false;
    public static final int RENDERMODE_CONTINUOUSLY = 1;
    public static final int RENDERMODE_WHEN_DIRTY = 0;
    public static final String TAG = "GLSurfaceView";
    public static final long Trace_TRACE_TAG_VIEW = 8;
    public static final class_258 sGLThreadManager = new class_258(0);
    public int mDebugFlags;
    public boolean mDetached;
    public class_253 mEGLConfigChooser;
    public int mEGLContextClientVersion;
    public class_254 mEGLContextFactory;
    public class_255 mEGLWindowSurfaceFactory;
    public class_257 mGLThread;
    public class_259 mGLWrapper;
    public boolean mPreserveEGLContextOnPause;
    public class_261 mRenderer;
    public final WeakReference mThisWeakRef;

    public void checkRenderThreadState() {
        $invoke$special$checkRenderThreadState();
    }


    public GLSurfaceViewShared(Context context) {
        super(context);
        this.mThisWeakRef = new WeakReference(this);
        init();
    }

    public GLSurfaceViewShared(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mThisWeakRef = new WeakReference(this);
        init();
    }

    public void finalize() throws Throwable {
        try {
            class_257 class_257Var = this.mGLThread;
            if (class_257Var != null) {
                class_257Var.method_178();
            }
        } finally {
            super.finalize();
        }
    }

    private /* synthetic */ void init() {
        getHolder().addCallback(this);
    }

    public void setGLWrapper(class_259 class_259Var) {
        this.mGLWrapper = class_259Var;
    }

    public void setDebugFlags(int i) {
        this.mDebugFlags = i;
    }

    public int getDebugFlags() {
        return this.mDebugFlags;
    }

    public void setPreserveEGLContextOnPause(boolean z) {
        this.mPreserveEGLContextOnPause = z;
    }

    public boolean getPreserveEGLContextOnPause() {
        return this.mPreserveEGLContextOnPause;
    }

    public void setRenderer(class_261 class_261Var) {
        $invoke$special$checkRenderThreadState();
        if (this.mEGLConfigChooser == null) {
            this.mEGLConfigChooser = new class_4(this, true);
        }
        byte b = 0;
        if (this.mEGLContextFactory == null) {
            this.mEGLContextFactory = new class_251(this, b);
        }
        if (this.mEGLWindowSurfaceFactory == null) {
            this.mEGLWindowSurfaceFactory = new class_252((byte) 0);
        }
        this.mRenderer = class_261Var;
        class_257 class_257Var = new class_257(this.mThisWeakRef);
        this.mGLThread = class_257Var;
        class_257Var.start();
    }

    public void setEGLContextFactory(class_254 class_254Var) {
        $invoke$special$checkRenderThreadState();
        this.mEGLContextFactory = class_254Var;
    }

    public void setEGLWindowSurfaceFactory(class_255 class_255Var) {
        $invoke$special$checkRenderThreadState();
        this.mEGLWindowSurfaceFactory = class_255Var;
    }

    public void setEGLConfigChooser(class_253 class_253Var) {
        $invoke$special$checkRenderThreadState();
        this.mEGLConfigChooser = class_253Var;
    }

    public void setEGLConfigChooser(boolean z) {
        setEGLConfigChooser(new class_4(this, z));
    }

    public void setEGLConfigChooser(int i, int i2, int i3, int i4, int i5, int i6) {
        setEGLConfigChooser(new class_250(this, i, i2, i3, i4, i5, i6));
    }

    public void setEGLContextClientVersion(int i) {
        $invoke$special$checkRenderThreadState();
        this.mEGLContextClientVersion = i;
    }

    public void setRenderMode(int i) {
        this.mGLThread.method_175(i);
    }

    public int getRenderMode() {
        return this.mGLThread.method_177();
    }

    public boolean isSurfaceBadHack() {
        class_257 class_257Var = this.mGLThread;
        if (class_257Var == null) {
            return false;
        }
        return class_257Var.field_621;
    }

    public void requestRender() {
        class_257 class_257Var = this.mGLThread;
        synchronized (sGLThreadManager) {
            class_257Var.field_628 = true;
            sGLThreadManager.notifyAll();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        class_257 class_257Var = this.mGLThread;
        synchronized (sGLThreadManager) {
            class_257Var.field_620 = true;
            class_257Var.field_625 = false;
            sGLThreadManager.notifyAll();
            while (class_257Var.field_622 && !class_257Var.field_625 && !class_257Var.field_617) {
                try {
                    sGLThreadManager.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        class_257 class_257Var = this.mGLThread;
        synchronized (sGLThreadManager) {
            class_257Var.field_620 = false;
            sGLThreadManager.notifyAll();
            while (!class_257Var.field_622 && !class_257Var.field_617) {
                try {
                    sGLThreadManager.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        class_257 class_257Var = this.mGLThread;
        synchronized (sGLThreadManager) {
            class_257Var.field_626 = i2;
            class_257Var.field_627 = i3;
            class_257Var.field_631 = true;
            class_257Var.field_628 = true;
            class_257Var.field_629 = false;
            if (Thread.currentThread() == class_257Var) {
                return;
            }
            sGLThreadManager.notifyAll();
            while (!class_257Var.field_617 && !class_257Var.field_619 && !class_257Var.field_629) {
                if (!(class_257Var.field_623 && class_257Var.field_624 && class_257Var.method_174())) {
                    break;
                }
                try {
                    sGLThreadManager.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Override2
    @Deprecated
    public void surfaceRedrawNeeded(SurfaceHolder surfaceHolder) {
    }

    public void onPause() {
        class_257 class_257Var = this.mGLThread;
        synchronized (sGLThreadManager) {
            class_257Var.field_618 = true;
            sGLThreadManager.notifyAll();
            while (!class_257Var.field_617 && !class_257Var.field_619) {
                try {
                    sGLThreadManager.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void onResume() {
        class_257 class_257Var = this.mGLThread;
        synchronized (sGLThreadManager) {
            class_257Var.field_618 = false;
            class_257Var.field_628 = true;
            class_257Var.field_629 = false;
            sGLThreadManager.notifyAll();
            while (!class_257Var.field_617 && class_257Var.field_619 && !class_257Var.field_629) {
                try {
                    sGLThreadManager.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void queueEvent(Runnable runnable) {
        class_257 class_257Var = this.mGLThread;
        if (runnable != null) {
            synchronized (sGLThreadManager) {
                class_257Var.field_630.add(runnable);
                sGLThreadManager.notifyAll();
            }
            return;
        }
        throw new IllegalArgumentException("r must not be null");
    }

    @Override // android.view.SurfaceView, android.view.View
    public void onAttachedToWindow() {
        int iMethod_177;
        super.onAttachedToWindow();
        if (this.mDetached && this.mRenderer != null) {
            class_257 class_257Var = this.mGLThread;
            if (class_257Var != null) {
                iMethod_177 = class_257Var.method_177();
            } else {
                iMethod_177 = 1;
            }
            class_257 class_257Var2 = new class_257(this.mThisWeakRef);
            this.mGLThread = class_257Var2;
            if (iMethod_177 != 1) {
                class_257Var2.method_175(iMethod_177);
            }
            this.mGLThread.start();
        }
        this.mDetached = false;
    }

    @Override // android.view.SurfaceView, android.view.View
    public void onDetachedFromWindow() {
        class_257 class_257Var = this.mGLThread;
        if (class_257Var != null) {
            class_257Var.method_178();
        }
        this.mDetached = true;
        super.onDetachedFromWindow();
    }

    private /* synthetic */ void $invoke$special$checkRenderThreadState() {
        if (this.mGLThread != null) {
            throw new IllegalStateException("setRenderer has already been called for this instance.");
        }
    }
}
