package mobarena.Wave;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Wave {

    //reset to the original frequency after a wave has been used
    private int startFrequency;

    //a frequency of 1 means there's a chance the wave will appear when waves are picked
    private int frequency;
    private int priority = 1;

    private boolean isSingle = false;

    private HashMap<String, Integer> mobs = new HashMap<>();

    private WaveType type;

    private int mobAmount;

    //for custom recurrent waves
    private int startWave = 1;

    private boolean isFixed = false;

    public void useStandardMobs() {
        mobs.put("minecraft:zombie", 5);
        mobs.put("minecraft:husk", 2);
        mobs.put("minecraft:skeleton", 3);
        mobs.put("minecraft:pillager", 1);
    }

    public void useSwarmMobs() {
        mobs.put("minecraft:wolf", 8);
        mobs.put("minecraft:husk", 3);
        mobs.put("minecraft:zombie", 4);
        mobs.put("minecraft:spider", 1);
        mobs.put("minecraft:cave_spider", 2);
    }

    public void useBossMobs() {
        mobs.put("minecraft:zombie", 5);
        mobs.put("minecraft:husk", 10);
        mobs.put("minecraft:blaze", 3);
    }

    public void addDefaultCustomMobs(ArrayList<String> monsters) {
        for (String monster : monsters) {
            mobs.put(monster, 1);
        }
    }

    public void useDefaultMobs() {
        switch (type) {
            case DEFAULT -> useStandardMobs();
            case SWARM -> useSwarmMobs();
            case BOSS -> useBossMobs();
        }
    }

    public void addMob(String mob, int num) {
        mobs.put(mob, num);
    }
    public void setMobs(HashMap<String, Integer> mobs) {
            this.mobs = mobs;
    }

    public HashMap<String, Integer> updateUnfixedMobs() {
        if (!isFixed) {
            ArrayList<String> unfixedMobs = new ArrayList<>();

            for (var str: mobs.keySet()) {
                for (int i = 0; i < mobs.get(str); i++) {
                    unfixedMobs.add(str);
                }
            }

            HashMap<String, Integer> mobs = new HashMap<>();
            System.out.println(getMobAmount() + " in updateUnfixedMobs()");
            for (int i = 0; i < getMobAmount(); i++) {
            int index = new Random().nextInt(unfixedMobs.size());

                if(mobs.containsKey(unfixedMobs.get(index))) {
                    var num = mobs.get(unfixedMobs.get(index));
                    mobs.put(unfixedMobs.get(index), num+1);
                } else {
                    mobs.put(unfixedMobs.get(index), 1);
                }

            }
            return mobs;

        }
        return mobs;
    }

    public void calculateMobs(int currentWave, int playerCount) {
        if (this.isFixed) {
            return;
        }
        int num = 1;

        switch (type) {
            case DEFAULT -> num = currentWave + (3*playerCount);
            case SWARM -> num = currentWave + (playerCount*6);
        }

        this.mobAmount = num;
    }

    //default wave constructor
    public Wave(WaveType type, int startFrequency, int frequency, boolean isFixed) {
        this.type = type;
        this.startFrequency = startFrequency;
        this.frequency = frequency;
        this.isFixed = isFixed;
    }

    //single wave constructor
    public Wave(boolean isSingle, WaveType type, int startWave, boolean isFixed) {
        this.isSingle = isSingle;
        this.type = type;
        this.startWave = startWave;
        this.isFixed = isFixed;
    }

    //recurrent wave constructor
    public Wave(int startFrequency, int frequency, int priority, WaveType type, int startWave, boolean isFixed) {
        this.startFrequency = startFrequency;
        this.frequency = frequency;
        this.priority = priority;
        this.type = type;
        this.startWave = startWave;
        this.isFixed = isFixed;
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

    public int getStartWave() {
        return startWave;
    }

    public void resetMobAmount() {
        mobAmount = 0;
    }

    public void incrementMobAmount() {
        mobAmount++;
    }
}
