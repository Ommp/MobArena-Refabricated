package mobarena;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.HashMap;

public class Scoreboard {

    private final HashMap<ServerPlayerEntity, Integer> wavesSurvived = new HashMap<>();
    private final HashMap<ServerPlayerEntity, Integer> playerKills = new HashMap<>();
    private final HashMap<ServerPlayerEntity, Float> playerDamage = new HashMap<>();

    public void addPlayer(ServerPlayerEntity p) {
        wavesSurvived.put(p, 0);
        playerKills.put(p, 0);
        playerDamage.put(p, 0f);
    }

    public void incrementPlayerWave(ArrayList<ServerPlayerEntity> players) {
        for (var p: players) {
            wavesSurvived.put(p, wavesSurvived.get(p) + 1);
        }
    }

    public int getWavesSurvived(ServerPlayerEntity p) {
        return wavesSurvived.get(p);
    }

    public HashMap<ServerPlayerEntity, Integer> getPlayerKills() {
        return playerKills;
    }

    public HashMap<ServerPlayerEntity, Float> getPlayerDamage() {
        return playerDamage;
    }

    public void increasePlayerKillCount(ServerPlayerEntity p) {
        if (playerKills.containsKey(p)) {
            int num = playerKills.get(p);
            playerKills.put(p, num+1);
        }
        else {
            playerKills.put(p, 1);
        }
    }
    public void increasePlayerDamage(ServerPlayerEntity p, float damage) {
        if (playerDamage.containsKey(p)) {
            var num = playerDamage.get(p);
            playerDamage.put(p, num+damage);
        }
        else {
            playerDamage.put(p, damage);
        }
    }
}
