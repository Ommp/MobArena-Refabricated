package mobarena.mixin;

import mobarena.ArenaManager;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

    ServerPlayerEntity p = (ServerPlayerEntity) (Object) this;

    @Inject(method = "dropItem", at = @At("HEAD"), cancellable = true)
    private void cancelDropInLobby(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        if (ArenaManager.isPlayerActive(p)) {
            if (ArenaManager.getArenaFromPlayer(p).getLobbyPlayer(p)) {
                cir.setReturnValue(null);
            }
        }
    }

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    private void kickDisconnectedPlayer(CallbackInfo ci) {
        if (ArenaManager.isPlayerActive(p)) {
            ArenaManager.getArenaFromPlayer(p).leavePlayer(p);
            ArenaManager.removeActivePlayer(p);
        }
    }

}
