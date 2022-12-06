package mobarena.config;

import java.util.ArrayList;
import java.util.HashMap;

public class Reinforcement {

    private int wave = 5;

    //classes and their items
    private final HashMap<String, ArrayList<String>> items = new HashMap<>();

    private boolean recurrent = true;

    public int getWave() {
        return wave;
    }

    public HashMap<String, ArrayList<String>> getClassItems() {
        return items;
    }

    public boolean isRecurrent() {
        return recurrent;
    }

    public void setWave(int wave) {
        this.wave = wave;
    }

    public void setRecurrent(boolean recurrent) {
        this.recurrent = recurrent;
    }
}
