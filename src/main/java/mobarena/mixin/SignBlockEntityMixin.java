package mobarena.mixin;

import mobarena.MobArena;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SignBlockEntity.class)
public abstract class SignBlockEntityMixin {

    @Shadow @Final private Text[] texts;

    @Shadow private boolean filterText;

    @Shadow public abstract Text getTextOnRow(int row, boolean filtered);

    @Inject(method = "onActivate", at = @At("HEAD"))
    public void useOnBlock(ServerPlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        String text1 = this.getTextOnRow(0, false).getString();
        String text2 = this.getTextOnRow(1, false).getString();
        MobArena.arenaManager.handleSignEvent(text1, text2, player);
    }

}