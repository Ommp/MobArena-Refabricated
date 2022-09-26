package mobarena.mixin;

import mobarena.ArenaManager;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/entity/mob/SpiderEntity$AttackGoal")
public abstract class SpiderEntityMixin extends MeleeAttackGoal {

    public SpiderEntityMixin(PathAwareEntity mob, double speed) {
        super(mob, speed, false);
    }

    @Inject(method = "shouldContinue", at = @At("HEAD"), cancellable = true)
    private void should(CallbackInfoReturnable<Boolean> cir) {
        if (ArenaManager.getMobFromAnyArena().containsKey(this.mob.getUuidAsString())) {
            cir.setReturnValue(true);
        }
    }

}
