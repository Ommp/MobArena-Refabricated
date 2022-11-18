package mobarena.mixin;

import mobarena.ArenaManager;
import net.minecraft.block.FireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireBlock.class)
public class FireBlockMixin {

    @Inject(method = "areBlocksAroundFlammable", at = @At("HEAD"), cancellable = true)
    private void inject(BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        for (var arena : ArenaManager.arenas.values()) {
            if (arena.getArenaRegion().isInsideRegion(pos) && arena.getIsProtected()) {
                cir.setReturnValue(false);
            }
        }
    }
}
