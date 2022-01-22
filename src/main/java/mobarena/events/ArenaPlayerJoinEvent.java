package mobarena.events;

import mobarena.Arena;
import net.minecraft.server.network.ServerPlayerEntity;

public class ArenaPlayerJoinEvent {

    private ServerPlayerEntity player;
    private Arena arena;

    public ArenaPlayerJoinEvent(ServerPlayerEntity player, Arena arena) {
        this.player = player;
        this.arena =  arena;
    }

    public ServerPlayerEntity getPlayer() {
        return player;
    }

    public Arena getArena() {
        return arena;
    }


}
