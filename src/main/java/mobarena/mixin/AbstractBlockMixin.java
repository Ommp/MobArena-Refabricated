package mobarena.mixin;

import mobarena.ArenaManager;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {

    @Inject(method = "onStateReplaced", at = @At("HEAD"))
    private void inject(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved, CallbackInfo ci) {

        for (var arena : ArenaManager.arenas.values()) {
            if (arena.isRunning()) {
                if (arena.getArenaRegion().isInsideRegion(pos)) {
                    arena.addBlockState(pos, state);
                }
            }
        }
    }
}
