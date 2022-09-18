package mobarena;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mobarena.database.PlayerInventoryModel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.registry.Registry;

import java.util.Objects;

public class PlayerManager {

    public static void savePlayerInventory(ServerPlayerEntity p) {
        var UUID = p.getUuidAsString();
        var inventory = p.getInventory();
        MobArena.database.addPlayer(UUID);

        for (int i = 0; i < inventory.size(); i++) {
            if (!Objects.equals(Registry.ITEM.getId(inventory.getStack(i).getItem()).toString(), "minecraft:air")) {

                var stackEncoded = ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, inventory.getStack(i)).get().orThrow().toString();
                MobArena.database.addPlayerInventoryItemStack(UUID, stackEncoded, i);
            }
        }
        inventory.clear();
    }
    public static void retrieveItems(ServerPlayerEntity p) {
        p.getInventory().clear();
        var inventory = MobArena.database.getPlayerItems(p.getUuidAsString());

        for (PlayerInventoryModel playerInventoryModel : inventory) {
            var data = playerInventoryModel.itemStackNbt();
            ItemStack itemStack;
            try {
                itemStack = ItemStack.fromNbt(StringNbtReader.parse(data));
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
            var slot = playerInventoryModel.slot();

            p.getInventory().insertStack(slot, itemStack);
        }
        MobArena.database.deletePlayer(p.getUuidAsString());
    }

    public static void restoreVitals(ServerPlayerEntity p) {
        p.setHealth(p.defaultMaxHealth);
        p.getHungerManager().setFoodLevel(20);
        p.clearStatusEffects();
    }
}
