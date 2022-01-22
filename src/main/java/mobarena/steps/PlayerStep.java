package mobarena.steps;

import net.minecraft.server.network.ServerPlayerEntity;

abstract class PlayerStep implements Step{
    protected final ServerPlayerEntity player;

    protected PlayerStep(ServerPlayerEntity player) {
        this.player = player;
    }

    @Override
    public String toString() {
        String step = getClass().getSimpleName();
        String target = player.getName().asString();
        return step + "(" + target + ")";
    }
}
