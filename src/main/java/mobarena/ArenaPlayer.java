package mobarena;

import net.minecraft.server.network.ServerPlayerEntity;

public class ArenaPlayer {
    private ServerPlayerEntity player;
    private boolean isDead;

    public ArenaPlayer(ServerPlayerEntity player, Arena arena){
        this.player = player;
    }

    public ServerPlayerEntity getPlayer(){
        return player;
    }

    /**
     * Check if the player is "dead", i.e. died or not.
     * @return true, if the player is either a spectator or played and died, false otherwise
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * Set the player's death status.
     * @param value true, if the player is dead, false otherwise
     */
    public void setDead(boolean value) {
        isDead = value;
    }




}
