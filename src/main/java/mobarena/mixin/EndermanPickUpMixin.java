package mobarena.mixin;

import mobarena.ArenaManager;
import net.minecraft.entity.mob.EndermanEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/entity/mob/EndermanEntity$PickUpBlockGoal")
public class EndermanPickUpMixin {

    @Shadow @Final private EndermanEntity enderman;

    @Inject(method = "canStart", at = @At("HEAD"), cancellable = true)
    private void inject(CallbackInfoReturnable<Boolean> cir) {
        for (var arena : ArenaManager.arenas.values()) {
            if (arena.getArenaRegion().isInsideRegion(enderman.getBlockPos()) && arena.getIsProtected()) {
                cir.setReturnValue(false);
            }
        }
    }
}
