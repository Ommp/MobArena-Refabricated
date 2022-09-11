package mobarena.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import mobarena.ArenaClass;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.util.HashMap;

public class ArenaClassConfig {

    private transient final File configFile = new File(FabricLoader.getInstance().getConfigDir().toString()+ "/mobarena/classes.json");
    private transient final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @SerializedName("Classes")
    private final HashMap<String, ArenaClass> arenaClasses = new HashMap<>();

    public void addClass(ArenaClass arenaClass){
        arenaClasses.put(arenaClass.getName(), arenaClass);
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
        ArenaClassConfig config = gson.fromJson(reader, ArenaClassConfig.class);
        arenaClasses.putAll(config.arenaClasses);
    }
    public void save() {
        try {
            FileWriter fileWriter = new FileWriter(configFile);
            gson.toJson(this, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HashMap<String, ArenaClass> getArenaClasses() {
        return arenaClasses;
    }
}
