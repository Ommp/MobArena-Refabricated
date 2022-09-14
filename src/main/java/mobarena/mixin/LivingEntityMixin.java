package mobarena.mixin;

import mobarena.ArenaManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin{
    LivingEntity livingEntity = (LivingEntity) (Object) this;

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void injected(DamageSource source, CallbackInfo ci) {
        if (!livingEntity.getWorld().isClient) {
        ArenaManager.handleMobDeath(livingEntity.getUuidAsString());
        }
    }
    @Inject(method = "drop", at = @At("HEAD"), cancellable = true)
    private void disableDrop(DamageSource source, CallbackInfo ci) {
        //cancel dropping items if the mob belongs to an arena
        if (ArenaManager.getMobToArena().containsKey(livingEntity.getUuidAsString())) {
            ci.cancel();
        }
    }
}