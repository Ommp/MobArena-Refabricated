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


    public JsonObject ArenaDataTemplate(String name, int dimensionId, boolean arenaEnabled,
                                        List<Double> arenaWarp, List<Double> lobbyWarp, List<Double> exitWarp, List<Double> specWarp,
                                        List<Float> arenaWarpYawPitch,  List<Float> lobbyWarpYawPitch,  List<Float> exitWarpPitch,  List<Float> specWarpYawPitch,
                                        ArrayList<Integer> p1, List<Integer> p2, List<Integer> l1, List<Integer> l2) {

        JsonArray pointOne = new JsonArray();
        pointOne.addAll(arenaWarp);

//        JsonArray arenaWarpTest = new JsonArray();
//        arenaWarpTest.add(50);
//        arenaWarpTest.add(50.5);
//        arenaWarpTest.add(70.5);
//
//        arena.add("arenaWarp", arenaWarpTest);




        JsonObject arena = new JsonObject();
        arena.add("dimensionId", new JsonPrimitive(dimensionId));
        arena.add("arenaEnabled", new JsonPrimitive(true));

        arena.add("arenaWarp", new JsonPrimitive((Number) arenaWarp));
        arena.add("lobbyWarp", new JsonPrimitive((Number) lobbyWarp));
        arena.add("exitWarp", new JsonPrimitive((Number) exitWarp));
        arena.add("specWarp", new JsonPrimitive((Number) specWarp));

        arena.add("arenaWarpYawPitch", new JsonPrimitive((Number) arenaWarpYawPitch));
        arena.add("lobbyWarpYawPitch", new JsonPrimitive((Number) lobbyWarpYawPitch));
        arena.add("exitWarpPitch", new JsonPrimitive((Number) exitWarpPitch));
        arena.add("specWarpYawPitch", new JsonPrimitive((Number) specWarpYawPitch));

        arena.add("p1", new JsonArray(p1));
        arena.add("p2", new JsonPrimitive((Number) p2));
        arena.add("l1", new JsonPrimitive((Number) l1));
        arena.add("l2", new JsonPrimitive((Number) l2));

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