package mobarena.mixin;

import mobarena.ArenaManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DamageTracker.class)
public abstract class DamageSourceMixin {

    @Shadow public abstract LivingEntity getEntity();

    @Inject(method = "onDamage", at = @At("RETURN"))
    private void inject(DamageSource damageSource, float originalHealth, float damage, CallbackInfo ci) {
        if (damageSource.getSource() instanceof ServerPlayerEntity p) {
            if (ArenaManager.isPlayerActive(p)) {
                ArenaManager.getArenaFromPlayer(p).getScoreboard().increasePlayerDamage(p, originalHealth-getEntity().getHealth());
            }
        }
    }
}
