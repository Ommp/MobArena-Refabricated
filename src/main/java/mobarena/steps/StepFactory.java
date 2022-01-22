package mobarena.steps;

import net.minecraft.server.network.ServerPlayerEntity;

/**
 * StepFactories create closures over {@link ServerPlayerEntity Players} and return
 * {@link Step Steps} that operate on the given Player.
 */
public interface StepFactory {
    Step create(ServerPlayerEntity player);
}