package cn.tesseract.soviet.mixin;


import com.corrodinggames.rts.java.Main;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public class TestMixin {
    @Inject(method = "main", at = @At("HEAD"))
    private static void main(String[] args, CallbackInfo ci) {
        ci.cancel();
        Main.field_8827 = "Mindustry";
    }
}
