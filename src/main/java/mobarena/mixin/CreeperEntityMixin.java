package mobarena.mixin;

import mobarena.ArenaManager;
import net.minecraft.entity.mob.CreeperEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin {
    CreeperEntity entity = (CreeperEntity) (Object) this;

    @Inject(method = "explode", at = @At("HEAD"))
    private void addCreeperDeath(CallbackInfo ci) {
        if (!entity.getWorld().isClient) {
            ArenaManager.handleCreeperExplosion(entity.getUuidAsString());
        }
    }
}
