package mobarena.mixin;

import mobarena.ArenaManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public abstract class WorldMixin {

    @Inject(method = "createExplosion", at = @At("HEAD"), cancellable = true)
    private void inject(Entity entity, DamageSource damageSource, ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, Explosion.DestructionType destructionType, CallbackInfoReturnable<Explosion> cir) {

        for (var arena: ArenaManager.arenas.values()) {
            if (arena.getArenaRegion().isInsideRegion(new BlockPos(x,y,z)) && arena.getIsProtected()) {

                    Explosion noharmExplosion = new Explosion(arena.getWorld(), entity, x, y, z, power, createFire, Explosion.DestructionType.NONE);
                    noharmExplosion.collectBlocksAndDamageEntities();
                    noharmExplosion.affectWorld(true);
                    cir.setReturnValue(noharmExplosion);

                    break;
                }
            }
        }
    }
