package mobarena;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameMode;

import java.util.HashMap;
import java.util.Objects;

public class PlayerManager {

    private static final HashMap<ServerPlayerEntity, GameMode> gamemodes = new HashMap<>();

    public static void savePlayerInventory(ServerPlayerEntity p) {
        try {
        var UUID = p.getUuidAsString();
        var inventory = p.getInventory();
        MobArena.database.addPlayer(UUID);

        for (int i = 0; i < inventory.size(); i++) {
            if (!Objects.equals(Registry.ITEM.getId(inventory.getStack(i).getItem()).toString(), "minecraft:air")) {

                var stackEncoded = ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, inventory.getStack(i)).get().orThrow().toString();
                MobArena.database.addPlayerInventoryItemStack(UUID, stackEncoded, i);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void retrieveItems(ServerPlayerEntity p) {
        try {
            var inventory = MobArena.database.getPlayerItems(p.getUuidAsString());

            for (int i = 0; i < inventory.getItems().size(); i++) {
                var data = inventory.getItems().get(i).getData();
                ItemStack itemStack;
                try {
                    itemStack = ItemStack.fromNbt(StringNbtReader.parse(data));
                } catch (CommandSyntaxException e) {
                    throw new RuntimeException(e);
                }
                var slot = inventory.getItems().get(i).slot;

                p.getInventory().insertStack(slot, itemStack);
            }
            MobArena.database.deleteStoredPlayerItems(p.getUuidAsString());

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public static void clearInventory(ServerPlayerEntity p) {
        for (int i = 0; i < p.getInventory().size(); i++) {
            p.getInventory().getStack(i).setCount(0);
        }
    }

    public static void restoreVitals(ServerPlayerEntity p) {
        p.setHealth(p.defaultMaxHealth);
        p.getHungerManager().setFoodLevel(20);
        p.clearStatusEffects();
    }

    public static void restoreGameMode(ServerPlayerEntity p) {
        p.changeGameMode(gamemodes.get(p));
        gamemodes.remove(p);
    }

    public static void setGameMode(ServerPlayerEntity p, GameMode gameMode) {
        gamemodes.put(p, p.interactionManager.getGameMode());
        p.changeGameMode(gameMode);
    }
}
