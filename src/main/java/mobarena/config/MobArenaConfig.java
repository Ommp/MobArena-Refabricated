package mobarena.config;

import com.google.gson.JsonObject;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import mobarena.MobArena;

import java.util.List;

@Config(name=MobArena.MOD_ID)
public class MobArenaConfig implements ConfigData {

    @ConfigEntry.Gui.CollapsibleObject
    public GlobalSettings globalSettings = new GlobalSettings();

    @ConfigEntry.Gui.CollapsibleObject
    public ArenaList arenaList = new ArenaList();

    public static class ArenaList {

        @ConfigEntry.Gui.CollapsibleObject
        Arena arena = new Arena();
    }

    public static class Arena {
        public String name;
        public int dimensionId;
        public boolean enabled = true;

        public List<Double> arenaWarp;
        public List<Double> lobbyWarp;
        public List<Double> exitWarp;
        public List<Double> specWarp;

        public List<Float> arenaWarpYawPitch, lobbyWarpYawPitch, exitWarpPitch, specWarpYawPitch;

        public List<Integer> p1, p2, l1, l2;
    }

    public static class GlobalSettings {
        public boolean enabled = true;
    }
}