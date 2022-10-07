package mobarena.Wave;

import mobarena.config.ArenaModel;
import mobarena.config.RecurrentWave;
import mobarena.config.SingleWave;

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

    public void addCustomWaves(ArenaModel arenaName) {
            for (SingleWave wave1 : arenaName.getSingleWaves()) {
                var singleWave = new Wave(true, wave1.getType(), wave1.getWave());
                if (!arenaName.getMonsters().isEmpty()) {
                    singleWave.setMobs(wave1.getMonsters());
                }
                waves.add(singleWave);
            }

            for (RecurrentWave wave1 : arenaName.getRecurrentWaves()) {
                var recurrentWave = new Wave(wave1.getFrequency(), 1, wave1.getPriority(), wave1.getType(), wave1.getWave());
                if (!arenaName.getMonsters().isEmpty()) {
                    recurrentWave.setMobs(wave1.getMonsters());
                }
                waves.add(recurrentWave);
            }
    }

    public void decrementWaveFrequencies() {
        for (Wave w: waves) {
            if (w.getFrequency() > 0) {
                w.decrementFrequency();
            }
        }

    }

    public void pickWave() {
        ArrayList<Wave> possibleWaves = new ArrayList<>();

        int minPriority = 1;

        for (Wave w: waves) {
            if (w.isSingle() && w.getStartWave() == currentWave) {
                setWave(w);
                return;
            }



            if (w.getFrequency() <= 0 && currentWave >= w.getStartWave()) {
                if (w.getPriority() >= minPriority) {
                possibleWaves.add(w);
                minPriority = w.getPriority();
                }

            }
        }

        for (Wave w: possibleWaves) {
            if (w.getPriority() < minPriority) {
                possibleWaves.remove(w);
            }
        }

        int index = new Random().nextInt(possibleWaves.size());

        setWave(possibleWaves.get(index));
        getWaves().get(index).resetFrequency();
        getWave().resetFrequency();
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
