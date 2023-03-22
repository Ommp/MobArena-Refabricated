package mobarena.Wave;

import mobarena.config.ArenaModel;
import mobarena.config.RecurrentWave;
import mobarena.config.SingleWave;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class WaveManager {

    private int currentWave = 0;
    private int finalWave;

    private final ArrayList<Wave> waves = new ArrayList<>();

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
        waves.add(new Wave(WaveType.DEFAULT, 1, 1, false));
        waves.add(new Wave(WaveType.SWARM, 3, 3, false));
        waves.add(new Wave(WaveType.BOSS, 4, 4, false));
    }

    public void addCustomWaves(ArenaModel arenaName) {
            for (SingleWave wave1 : arenaName.getSingleWaves()) {
                var singleWave = new Wave(true, wave1.getType(), wave1.getWave(), wave1.isFixed());
                if (!arenaName.getMonsters().isEmpty()) {
                    singleWave.setMobs(wave1.getMonsters());
                }
                singleWave.resetMobAmount();
                singleWave.getMobs().forEach((key, value) -> {
                    for (int i = 0; i < value; i++) {
                        singleWave.incrementMobAmount();
                    }});
                waves.add(singleWave);
            }

            for (RecurrentWave wave1 : arenaName.getRecurrentWaves()) {
                var recurrentWave = new Wave(wave1.getFrequency(), 1, wave1.getPriority(), wave1.getType(), wave1.getWave(), wave1.isFixed());
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



            if (w.getFrequency() <= 0 && currentWave >= w.getStartWave() && !w.isSingle()) {
                if (w.getPriority() >= minPriority) {
                    possibleWaves.add(w);
                    minPriority = w.getPriority();
                }
            }
        }

        var noPriorityWaves = new ArrayList<Wave>();
        for (Wave w: possibleWaves) {
            if (w.getPriority() < minPriority) {
                noPriorityWaves.add(w);
            }
        }
        possibleWaves.removeAll(noPriorityWaves);

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

    public HashMap<String, Integer> prepareWaveReturnMobs(ArenaModel config, ArrayList<ServerPlayerEntity> arenaPlayers) {
        decrementWaveFrequencies();

        pickWave();
        getWave().calculateMobs(getCurrentWave(), arenaPlayers.size());

        if (getWave().getMobs().isEmpty()) {
            if (config.usesCustomSpawns()) {
                getWave().addDefaultCustomMobs(config.getMonsters());
            } else {
            getWave().useDefaultMobs();
            }
        }

        return wave.updateUnfixedMobs();
    }
}
