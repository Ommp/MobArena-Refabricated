package mobarena;

import mobarena.commands.setup.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class MobArena implements ModInitializer {

    public static final String MOD_ID = "mobarena";
    public static final Logger LOGGER = LogManager.getLogger("mobarena");


	public static void log(Level level, String message) {
		final String logPrefix = "[MobArena]: ";
		LOGGER.log(level, logPrefix.concat(message));
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Initalised MobArena Mod for Minecraft v1.16");

		//register commands

		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			ArenaAddCommand.register(dispatcher);
			DeleteArenaCommand.register(dispatcher);
		});
	}
	}
