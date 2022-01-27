package mobarena.config;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MobArenaConfig {

    private static MobArenaConfig INSTANCE=null;

    public boolean globalEnabled = true;

//        public String name;
//
//        public int dimensionId;
//        public boolean arenaEnabled = true;
//
//        public List<Double> arenaWarp;
//        public List<Double> lobbyWarp;
//        public List<Double> exitWarp;
//        public List<Double> specWarp;
//
//        public List<Float> arenaWarpYawPitch, lobbyWarpYawPitch, exitWarpPitch, specWarpYawPitch;
//
//        public List<Integer> p1, p2, l1, l2;


    public JsonObject ArenaDataTemplate(String name, int dimensionId,
                                        /*boolean arenaEnabled,*/
                                        JsonObject arenaWarp, JsonObject lobbyWarp, JsonObject exitWarp, JsonObject specWarp,
                                        JsonObject arenaWarpYawPitch,  JsonObject lobbyWarpYawPitch,  JsonObject exitWarpPitch,  JsonObject specWarpYawPitch,
                                        JsonObject p1, JsonObject p2, JsonObject l1, JsonObject l2) {

        JsonObject arena = new JsonObject();
        arena.add("dimensionId", new JsonPrimitive(dimensionId));
        arena.add("arenaEnabled", new JsonPrimitive(true));

        arena.add("arenaWarp", arenaWarp);
        arena.add("lobbyWarp", lobbyWarp);
        arena.add("exitWarp", exitWarp);
        arena.add("specWarp", specWarp);

        arena.add("arenaWarpYawPitch", arenaWarpYawPitch);
        arena.add("lobbyWarpYawPitch", lobbyWarpYawPitch);
        arena.add("exitWarpPitch", exitWarpPitch);
        arena.add("specWarpYawPitch", specWarpYawPitch);

        arena.add("p1", p1);
        arena.add("p2", p2);
        arena.add("l1", l1);
        arena.add("l2", l2);

        JsonObject list = new JsonObject();
        list.add(name, arena);

        return list;
    }

    //use this when player runs create command
    public JsonObject ArenaDataTemplate(String name, int dimensionId) {

        JsonObject arena = new JsonObject();
        arena.add("dimensionId", new JsonPrimitive(dimensionId));
        arena.add("arenaEnabled", new JsonPrimitive(true));

        arena.add("arenaWarp", new JsonPrimitive(""));
        arena.add("lobbyWarp", new JsonPrimitive(""));
        arena.add("exitWarp", new JsonPrimitive(""));
        arena.add("specWarp", new JsonPrimitive(""));

        arena.add("arenaWarpYawPitch", new JsonPrimitive(""));
        arena.add("lobbyWarpYawPitch", new JsonPrimitive(""));
        arena.add("exitWarpPitch", new JsonPrimitive(""));
        arena.add("specWarpYawPitch", new JsonPrimitive(""));

        arena.add("p1", new JsonPrimitive(""));
        arena.add("p2", new JsonPrimitive(""));
        arena.add("l1", new JsonPrimitive(""));
        arena.add("l2", new JsonPrimitive(""));

        JsonObject list = new JsonObject();
        list.add(name, arena);

        return list;
    }

    public static void loadConfig() {
        INSTANCE = new MobArenaConfig();
        Gson gson = new Gson();
        File configFile = new File(FabricLoader.getInstance().getConfigDir().toString(), "mobarena.json");

        try (FileReader reader = new FileReader(configFile)) {
            INSTANCE = gson.fromJson(reader, MobArenaConfig.class);
            System.out.println("Config: " + INSTANCE);

            try (FileWriter writer = new FileWriter(configFile)) {
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(INSTANCE));
                System.out.println("Config updated");
            } catch (IOException e2) {
                System.out.println("Failed to update config file");
                }

            System.out.println("Config loaded");
            }
            catch (IOException e) {
                System.out.println("No config found, generating one...");
                INSTANCE = new MobArenaConfig();

                try (FileWriter writer = new FileWriter(configFile)) {
                    writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(INSTANCE));
                } catch (IOException e2) {
                    System.out.println("Failed to generate config file!");
                }
            }
    }

    public static void saveConfig() {

    }

    public static MobArenaConfig getInstance() {
        if (INSTANCE == null) {
            loadConfig();
        }
        return INSTANCE;
    }
}