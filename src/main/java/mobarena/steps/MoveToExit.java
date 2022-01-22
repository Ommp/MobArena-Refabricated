package mobarena.steps;

import mobarena.Arena;
import net.minecraft.server.network.ServerPlayerEntity;

public class MoveToExit extends MovePlayerStep{
    private MoveToExit(ServerPlayerEntity player, Arena arena){
        super(player, () -> arena.getRegion().getExitWarp());
    }

    static StepFactory create(Arena arena) {
        return player -> new MoveToExit(player, arena);
    }
}
