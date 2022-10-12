package mobarena;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mobarena.database.RewardModel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.HashMap;

public class RewardManager {

    private HashMap<ServerPlayerEntity, Integer> wavesSurvived = new HashMap<>();

    private ArrayList<RewardModel> rewards = new ArrayList<>();

    public void addPlayer(ServerPlayerEntity p) {
        wavesSurvived.put(p, 0);
    }

    public void incrementPlayerWave(ServerPlayerEntity p) {
        wavesSurvived.put(p, wavesSurvived.get(p) + 1);
    }

    public int getWavesSurvived(ServerPlayerEntity p) {
        return wavesSurvived.get(p);
    }

    //save the possible rewards in memory
    public void setRewards(String arenaName) {
        rewards = MobArena.database.getRewardItemStacks(arenaName);
    }

    //call this when a player leaves arena or dies
    public void grantRewards(ServerPlayerEntity p) {
        //create itemstacks
        for (RewardModel reward : rewards) {
            int rewardFormula =  getWavesSurvived(p) / reward.wave();
            int rewardCount = (int) Math.floor(rewardFormula);
            for (int i = 0; i < rewardCount; i++) {
                var data = reward.itemStackNbt();
                ItemStack itemStack;
                try {
                    itemStack = ItemStack.fromNbt(StringNbtReader.parse(data));
                    p.getInventory().offerOrDrop(itemStack);
                } catch (CommandSyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
