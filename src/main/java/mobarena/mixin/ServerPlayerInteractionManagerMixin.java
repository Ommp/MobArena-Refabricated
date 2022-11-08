package mobarena.mixin;

import mobarena.ArenaManager;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin {


    @Shadow protected ServerWorld world;

    @Inject(method = "tryBreakBlock", at = @At("HEAD"), cancellable = true)
    private void inject(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {

        if (!Objects.equals(Registry.BLOCK.getId(world.getBlockState(pos).getBlock()).toString(), "minecraft:fire") && !Objects.equals(Registry.BLOCK.getId(world.getBlockState(pos).getBlock()).toString(), "minecraft:soul_fire") && !Objects.equals(Registry.BLOCK.getId(world.getBlockState(pos).getBlock()).toString(), "minecraft:tnt")) {

            for (var arena : ArenaManager.arenas.values()) {
                if (arena.getArenaRegion().isInsideRegion(pos) && arena.getIsProtected()) {
                    cir.setReturnValue(false);
                }
            }

        }
    }
}
