package mobarena;

import com.electronwill.nightconfig.core.file.FileConfig;
import mobarena.config.LoadsConfigFile;
import mobarena.items.GuiItem;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class MobArena implements ModInitializer {

	private ArenaMaster arenaMaster;

	private Throwable lastFailureCause;
	private FileConfig config;
	private LoadsConfigFile loadsConfigFile;

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

	public void saveConfig() {
		getConfig().save();
	}

	public FileConfig getConfig() {
		if (config == null) {
			reloadConfig();
		}
		return config;
	}

	public void reloadConfig() {
		if (loadsConfigFile == null) {
			loadsConfigFile = new LoadsConfigFile(this);
		}
		config = loadsConfigFile.load();
	}

	public void reload(){
		reloadConfig();
		reloadArenaMaster();
	}

	private void reloadArenaMaster(){
		arenaMaster.getArenas().forEach(Arena::endArena);
	}

	public static Item GUI_ITEM = new GuiItem(new Item.Settings().group(ItemGroup.MISC));

    public static final String MOD_ID = "mobarena";
    public static final Logger LOGGER = LogManager.getLogger("mobarena");


	public static void log(Level level, String message) {
		final String logPrefix = "[MobArena]: ";
		LOGGER.log(level, logPrefix.concat(message));
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Initialised MobArena Mod for Minecraft v1.16");

		Registry.register(Registry.ITEM, new Identifier("mobarena", "gui_item"), GUI_ITEM);

		setup();

		loadsConfigFile = new LoadsConfigFile(this);
		loadsConfigFile.load();
	}


}
