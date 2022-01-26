package mobarena;

import com.google.gson.JsonObject;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import mobarena.config.MobArenaConfig;
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

	public static MobArenaConfig config;

	public static JsonObject arenaData;

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

		AutoConfig.register(MobArenaConfig.class, GsonConfigSerializer::new);
		MobArenaConfig config = AutoConfig.getConfigHolder(MobArenaConfig.class).getConfig();
	}
}
