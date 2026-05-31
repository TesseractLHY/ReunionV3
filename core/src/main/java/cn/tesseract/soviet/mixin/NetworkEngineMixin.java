package cn.tesseract.soviet.mixin;


import com.corrodinggames.rts.game.Player;
import com.corrodinggames.rts.gameFramework.network.NetworkEngine;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(NetworkEngine.class)
public abstract class NetworkEngineMixin {
    @Shadow
    public Player field_5848;

    @ModifyArg(method = "sendChatMessage", at = @At(value = "INVOKE", target = "Lcom/corrodinggames/rts/gameFramework/network/NetworkEngine;method_2741(Lcom/corrodinggames/rts/gameFramework/network/NetworkConnection;Lcom/corrodinggames/rts/game/Player;Ljava/lang/String;Ljava/lang/String;)V"), index = 2)
    private String modifyPlayerName1(String original) {
        return this.field_5848.name;
    }

    @ModifyArg(method = "sendChatMessage", at = @At(value = "INVOKE", target = "Lcom/corrodinggames/rts/gameFramework/network/NetworkEngine;method_2767(Lcom/corrodinggames/rts/gameFramework/network/NetworkConnection;Lcom/corrodinggames/rts/game/Player;Ljava/lang/String;Ljava/lang/String;)Z"), index = 2)
    private String modifyPlayerName2(String original) {
        return this.field_5848.name;
    }
}
