package mobarena.config;

import java.util.ArrayList;
import java.util.HashMap;

public class Reinforcement {

    private int wave;

    //classes and their items
    private final HashMap<String, ArrayList<String>> items = new HashMap<>();

    private boolean recurrent = true;

    public int getWave() {
        return wave;
    }

    public HashMap<String, ArrayList<String>> getClassItems() {
        return items;
    }

    public boolean isRecurrentCanBeUsed(int wave) {
        return recurrent && wave > getWave() && wave % getWave() == 0;
    }

    public void setWave(int wave) {
        this.wave = wave;
    }

    public Reinforcement() {
    }

    public Reinforcement(int wave, boolean recurrent) {
        this.wave = wave;
        this.recurrent = recurrent;
    }
}
