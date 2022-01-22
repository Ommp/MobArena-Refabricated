package mobarena.steps;

import mobarena.Arena;
import mobarena.MobArena;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PlayerSpecArena {
    public static StepFactory create(Arena arena){

        List<StepFactory> factories = new ArrayList<>();

        if (arena.getRegion().getExitWarp() != null) {
            factories.add(Defer.it(MoveToExit.create(arena)));
        }
        factories.add(MoveToLobby.create(arena));
        factories.add(SetGameMode.create());
        factories.add(SetHealth.create());

        return PlayerMultiStep.create(factories, (Logger) MobArena.LOGGER);

    }
}
