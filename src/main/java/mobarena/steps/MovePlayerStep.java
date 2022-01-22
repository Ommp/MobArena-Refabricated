package mobarena.steps;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.function.Supplier;

abstract class MovePlayerStep extends PlayerStep{
    private final Supplier<BlockPos> destination;

    private BlockPos blockPos;

    MovePlayerStep(ServerPlayerEntity player, Supplier<BlockPos> destination) {
        super(player);
        this.destination = destination;
    }

    @Override
    public void run(){
        blockPos = player.getBlockPos();
    }

    @Override
    public void undo(){
        player.teleport(blockPos.getX(),blockPos.getY(), blockPos.getZ());
    }
}
