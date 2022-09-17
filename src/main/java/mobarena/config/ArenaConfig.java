package mobarena.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.util.HashMap;

public class ArenaConfig {
    private transient final File configFile = new File(FabricLoader.getInstance().getConfigDir().toString()+ "/mobarena/arenaconfigs.json");

    private transient final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    @SerializedName("Arenas")
    private HashMap<String, ArenaModel> arenas = new HashMap<>();

    public void save() {
        try {
            FileWriter writer = new FileWriter(configFile);
            gson.toJson(this, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load() {
        if (!configFile.exists()) {
            save();
        }

        FileReader reader;
        try {
            reader = new FileReader(configFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ArenaConfig config = gson.fromJson(reader, ArenaConfig.class);
        if (!arenas.isEmpty()) {
            arenas.clear();
        }
        arenas.putAll(config.arenas);
    }

    public ArenaModel getArenaConfig(String name) {
        return arenas.get(name);
    }

    public boolean configExists(String name) {
        return arenas.containsKey(name);
    }

}
