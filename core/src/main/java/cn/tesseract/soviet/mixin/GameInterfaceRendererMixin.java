package cn.tesseract.soviet.mixin;

import com.corrodinggames.rts.game.units.BaseUnit;
import com.corrodinggames.rts.game.units.actions.SelectUnitTypeAction;
import com.corrodinggames.rts.gameFramework.ui.GameInterfaceRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(GameInterfaceRenderer.class)
public class GameInterfaceRendererMixin {
    @Inject(method = "method_2365", at = @At(
            value = "INVOKE",
            target = "Ljava/util/ArrayList;add(ILjava/lang/Object;)V",
            ordinal = 1,
            shift = At.Shift.AFTER
    ), cancellable = true)
    public void method_2365(BaseUnit var1, ArrayList<BaseUnit> var2, CallbackInfoReturnable<ArrayList<SelectUnitTypeAction>> cir) {
        cir.setReturnValue(new ArrayList<>());
    }
}
