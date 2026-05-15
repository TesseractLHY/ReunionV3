package cn.tesseract.soviet.desktop;

import com.corrodinggames.rts.java.Main;

import cn.tesseract.crosshook.HookRegistry;


public class DesktopLauncher {
    public static void main(String[] args) {
        HookRegistry.instance.register(MainHook.class);
        Main.main(args);
    }
}
