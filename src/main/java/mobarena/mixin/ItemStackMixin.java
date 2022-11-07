package mobarena.mixin;

import mobarena.ArenaManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract Item getItem();

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void inject(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {

        var pos = context.getBlockPos();

        if (!Registry.ITEM.getId(this.getItem()).toString().equals("minecraft:flint_and_steel") && !Registry.ITEM.getId(this.getItem()).toString().equals("minecraft:tnt") && !Registry.ITEM.getId(this.getItem()).toString().matches("^minecraft:.*egg$")) {

            for (var arena : ArenaManager.arenas.values()) {
                if (arena.getArenaRegion().isInsideRegion(pos) && arena.getIsProtected()) {
                    cir.setReturnValue(ActionResult.PASS);
                }
            }

        }
    }
}
