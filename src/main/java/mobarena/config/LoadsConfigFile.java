package mobarena.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import mobarena.MobArena;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LoadsConfigFile {

    private final MobArena mod;
    private final String configDir = FabricLoader.getInstance().getConfigDir().toString() + "/mobarena/";

    public LoadsConfigFile(MobArena mod){
        this.mod =  mod;
    }

    public FileConfig load() {
        return loadConfiguration();
    }

    public FileConfig loadConfiguration(){

        // if config doesn't exist in folder, create default
        File file = new File(configDir, "config.yml");
        if (!file.exists()) {
            MobArena.LOGGER.log(Level.INFO, "No config file found. Creating default...");
            try {
                this.saveDefaultConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileConfig configFile = FileConfig.of(configDir + "config.yml");
        MobArena.LOGGER.info(configDir);
        configFile.load();
        return configFile;
    }

    public void saveDefaultConfig() throws IOException {
//        File file = new File(folder, "config.yml");
        Files.createDirectories(Paths.get(configDir));
        FileConfig config = FileConfig.of(configDir + "config.yml");
        MobArena.LOGGER.info(configDir);
        config.set("key", "value");
        config.set("number", 123);

        Config subConfig = Config.inMemory();
        subConfig.set("subKey", "value");
        subConfig.set("subConfig", subConfig);
        config.save();
    }


}
