package mobarena.steps;

import mobarena.Arena;
import net.minecraft.server.network.ServerPlayerEntity;

public class MoveToLobby extends MovePlayerStep{
    private MoveToLobby(ServerPlayerEntity player, Arena arena){
        super(player, () -> arena.getRegion().getLobbyWarp());
    }

    static StepFactory create(Arena arena){
        return player -> new MoveToLobby(player, arena);
    }
}
