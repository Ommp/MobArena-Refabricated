package mobarena.mixin;

import mobarena.MobArena;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin{

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void injected(DamageSource source, CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (!livingEntity.getWorld().isClient) {
        MobArena.arenaManager.tellArenaMobDeath(livingEntity.getUuidAsString());
        }
    }
}