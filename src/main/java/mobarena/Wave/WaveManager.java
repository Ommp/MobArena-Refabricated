package mobarena.Wave;

import java.util.ArrayList;
import java.util.Random;

public class WaveManager {

    private int currentWave = 1;
    private int finalWave;

    private ArrayList<Wave> waves = new ArrayList<>();

    private Wave wave;



    public void incrementWave() {
        currentWave++;
    }

    public boolean isFinalWave() {
        return finalWave == currentWave;
    }

    public void setWave(Wave wave) {
        this.wave = wave;
    }

    public void addDefaultWaves() {
        waves.add(new Wave(WaveType.DEFAULT, 1, 1));
        waves.add(new Wave(WaveType.SWARM, 3, 3));
        waves.add(new Wave(WaveType.BOSS, 4, 4));
    }

    public void decrementWaveFrequencies() {
        for (Wave w: waves) {
            if (w.getFrequency() > 1) {
                w.decrementFrequency();
            }
        }

    }

    public void pickWave() {
        ArrayList<Wave> possibleWaves = new ArrayList<>();
        for (Wave w: waves) {
            if (w.getFrequency() == 1 && w.isSingle()) {
                setWave(w);
                break;
            }

            if (w.getFrequency() == 1) {
                possibleWaves.add(w);
            }
        }

        int index = new Random().nextInt(possibleWaves.size());

        setWave(possibleWaves.get(index));
        getWave().resetFrequency();
        getWaves().get(index).resetFrequency();
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public int getFinalWave() {
        return finalWave;
    }

    public ArrayList<Wave> getWaves() {
        return waves;
    }

    public Wave getWave() {
        return wave;
    }
}
