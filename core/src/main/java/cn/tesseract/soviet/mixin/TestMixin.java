package cn.tesseract.soviet.mixin;


import com.corrodinggames.rts.game.GameTeam;
import com.corrodinggames.rts.game.Player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GameTeam.class)
public abstract class TestMixin extends Player {
    @Overwrite
    public void updateTeam(float f) {
        System.out.println("&" + f);
        super.updateTeam(f);
    }
}
