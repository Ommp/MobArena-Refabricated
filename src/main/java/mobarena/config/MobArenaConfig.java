package mobarena.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MobArenaConfig {

    private static MobArenaConfig INSTANCE=null;


    public boolean globalEnabled = true;

    public JsonObject arenasJsonObject;

    public class ArenaContainer {
        public List<ArenaTemplate> arenasList;
    }

    public class ArenaTemplate {
        public String name;

        public int dimensionId;
        public boolean arenaEnabled = true;

        public List<Double> arenaWarp;
        public List<Double> lobbyWarp;
        public List<Double> exitWarp;
        public List<Double> specWarp;

        public List<Float> arenaWarpYawPitch, lobbyWarpYawPitch, exitWarpPitch, specWarpYawPitch;

        public List<Integer> p1, p2, l1, l2;
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