package mobarena;

import mobarena.commands.EditCommand;
import mobarena.config.MobArenaConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MobArena implements ModInitializer {

	private ArenaMaster arenaMaster;

	private Throwable lastFailureCause;

	public static final MobArenaConfig config = new MobArenaConfig();

	private void setup() {
		setupArenaMaster();
//		setup other things...
	}

	private void setupArenaMaster() {
		arenaMaster = new ArenaMasterImpl(this);
	}

	public ArenaMaster getArenaMaster() {
		return arenaMaster;
	}

	public Throwable getLastFailureCause() {
		return lastFailureCause;
	}

	private void reloadArenaMaster(){
		arenaMaster.getArenas().forEach(Arena::endArena);
	}

    public static final String MOD_ID = "mobarena";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static void log(Level level, String message) {
		final String logPrefix = "[MobArena]: ";
		LOGGER.log(level, logPrefix.concat(message));
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Initialised MobArena Mod for Minecraft v1.16");

		setup();

		config.loadFile();
		arenaMaster.initialize();

		CommandRegistrationCallback.EVENT.register(EditCommand::register);
	}
}
