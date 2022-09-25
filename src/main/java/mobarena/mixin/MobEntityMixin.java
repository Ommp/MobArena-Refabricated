package mobarena.mixin;

import mobarena.ArenaManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin {
    @Inject(method = "isAffectedByDaylight", at = @At("RETURN"), cancellable = true)
    private void disableAffectedByDaylight(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (ArenaManager.getMobFromAnyArena().containsKey(entity.getUuidAsString())) {
            cir.setReturnValue(false);
        }
    }
}
