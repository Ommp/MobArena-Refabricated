package mobarena.steps;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;

public class SetGameMode extends PlayerStep{
    private GameMode mode;

    private SetGameMode(ServerPlayerEntity player){
        super(player);
    }

    @Override
    public void run() {
        mode = player.interactionManager.getGameMode();

        player.setGameMode(GameMode.SURVIVAL);
    }

    @Override
    public void undo() {
        player.setGameMode(mode);
    }

    static StepFactory create() {
        return SetGameMode::new;
    }
}
