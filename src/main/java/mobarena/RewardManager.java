package mobarena;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mobarena.database.RewardModel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;

public class RewardManager {
    private ArrayList<RewardModel> rewards = new ArrayList<>();


    //save the possible rewards in memory
    public void setRewards(String arenaName) {
        rewards = MobArena.database.getRewardItemStacks(arenaName);
    }

    //call this when a player leaves arena or dies
    public void grantRewards(ServerPlayerEntity p, int wavesSurvived) {
        //create itemstacks
        for (RewardModel reward : rewards) {
            int rewardFormula =  wavesSurvived / reward.wave();
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
