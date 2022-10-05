package mobarena.Wave;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Wave {

    //reset to the original frequency after a wave has been used
    private final int startFrequency;

    //a frequency of 1 means there's a chance the wave will appear when waves are picked
    private int frequency;
    private int priority;

    private boolean isSingle = false;

    private HashMap<String, Integer> mobs = new HashMap<>();

    private ArrayList<String> defaultMobs = new ArrayList<>();

    private WaveType type;

    private int mobAmount;

    //for custom recurrent waves
    private int startWave;

    public void useStandardMobs() {
        defaultMobs.clear();
        defaultMobs.add("minecraft:zombie");
        defaultMobs.add("minecraft:husk");
        defaultMobs.add("minecraft:spider");
        defaultMobs.add("minecraft:cave_spider");
        defaultMobs.add("minecraft:skeleton");
        defaultMobs.add("minecraft:pillager");
    }

    public void useSwarmMobs() {
        defaultMobs.clear();
        defaultMobs.add("minecraft:zombie");
        defaultMobs.add("minecraft:husk");
        defaultMobs.add("minecraft:spider");
        defaultMobs.add("minecraft:cave_spider");
    }

    public void useBossMobs() {
        defaultMobs.clear();
        defaultMobs.add("minecraft:zombie");
        defaultMobs.add("minecraft:husk");
        defaultMobs.add("minecraft:blaze");
        defaultMobs.add("minecraft:iron_golem");
    }

    public void useCustomMobs() {

    }

    public void addDefaultMobs() {
        if (type.equals(WaveType.DEFAULT)) {
            useStandardMobs();
        }
        else if (type.equals(WaveType.SWARM)) {
            useSwarmMobs();
        }
        else if (type.equals(WaveType.BOSS)) {
            useBossMobs();
        }
        for (int i = 0; i < mobAmount; i++) {
            int index = new Random().nextInt(defaultMobs.size());

            if(mobs.containsKey(defaultMobs.get(index))) {
                var num = mobs.get(defaultMobs.get(index));
                mobs.put(defaultMobs.get(index), num+1);
            }
            else {
                mobs.put(defaultMobs.get(index), 1);
            }
        }
        for (String str: mobs.keySet()) {
            System.out.println(str + " amount: " + mobs.get(str));
        }
    }

    public void addMob(String mob, int num) {
        mobs.put(mob, num);
    }
    public void setMobs(HashMap<String, Integer> mobs) {
        this.mobs = mobs;
    }


    public void calculateMobs(int currentWave, int playerCount) {
        int num = 1;
        if (type.equals(WaveType.DEFAULT)) {
            num = currentWave + (3*playerCount);
        }
        else if(type.equals(WaveType.SWARM)) {
            num = currentWave + (playerCount*6);
        }
        mobAmount = num;
    }

    public Wave(WaveType type, int startFrequency, int frequency) {
        this.type = type;
        this.startFrequency = startFrequency;
        this.frequency = frequency;
    }

    public void resetFrequency() {
        setFrequency(getStartFrequency());
    }


    public int getStartFrequency() {
        return startFrequency;
    }

    public int getFrequency() {
        return frequency;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public HashMap<String, Integer> getMobs() {
        return mobs;
    }

    public ArrayList<String> getDefaultMobs() {
        return defaultMobs;
    }

    public WaveType getType() {
        return type;
    }

    public int getMobAmount() {
        return mobAmount;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
    public void decrementFrequency() {
        this.frequency--;
    }
}
