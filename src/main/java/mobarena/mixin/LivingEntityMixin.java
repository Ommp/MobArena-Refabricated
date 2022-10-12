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
    @Inject(method = "dropLoot", at = @At("HEAD"), cancellable = true)
    private void disableDrop(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
        //cancel dropping items if the mob belongs to an arena
        if (ArenaManager.getMobFromAnyArena().containsKey(livingEntity.getUuidAsString())) {
            ci.cancel();
        }
    }
    @Inject(method = "dropInventory", at = @At("HEAD"), cancellable = true)
    private void disableDrop(CallbackInfo ci) {
        //cancel dropping items if the mob belongs to an arena
        if (ArenaManager.getMobFromAnyArena().containsKey(livingEntity.getUuidAsString())) {
            ci.cancel();
        }
    }

    @Inject(method = "dropXp", at = @At("HEAD"), cancellable = true)
    private void disableXP(CallbackInfo ci) {
        if (ArenaManager.getMobFromAnyArena().containsKey(livingEntity.getUuidAsString())) {
            if (!ArenaManager.getArena(ArenaManager.getArenaFromMob(livingEntity.getUuidAsString())).isXPAllowed()) {
                ci.cancel();
            }
        }
    }
}