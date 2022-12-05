package mobarena.config;

import java.util.HashMap;

public class Reinforcement {

    private int wave;

    //classes and their items
    private final HashMap<String, String> items = new HashMap<>();

    private String className;

    private boolean recurrent;

    public int getWave() {
        return wave;
    }

    public HashMap<String, String> getItem() {
        return items;
    }

    public String getClassName() {
        return className;
    }

    public boolean isRecurrent() {
        return recurrent;
    }
}
