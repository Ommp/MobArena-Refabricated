package mobarena.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GlobalConfig {

    @Expose
    @SerializedName("prefix")
    public String prefix = "@a [Mobarena]";

    @Expose
    @SerializedName("enabled")
    public boolean enabled = true;

    public GlobalConfig() {
    }
}
