package mobarena.steps;

import mobarena.Arena;
import net.minecraft.server.network.ServerPlayerEntity;

class MoveToSpec extends MovePlayerStep{
    private MoveToSpec(ServerPlayerEntity player, Arena arena){
        super(player, () -> arena.getRegion().getSpecWarp());
    }

    static StepFactory create(Arena arena){
        return player -> new MoveToSpec(player, arena);
    }
}
