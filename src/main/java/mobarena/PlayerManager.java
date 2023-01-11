package mobarena;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mobarena.database.PlayerInventoryModel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
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
            if (!Objects.equals(Registries.ITEM.getId(inventory.getStack(i).getItem()).toString(), "minecraft:air")) {

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
            PlayerInventoryModel inventory = MobArena.database.getPlayerItems(p.getUuidAsString());

            for (ArenaItem item: inventory.getItems()) {
                try {
                    ItemStack itemStack = ItemStack.fromNbt(StringNbtReader.parse(item.getData()));
                    int slot = item.getSlot();
                    p.getInventory().insertStack(slot, itemStack);
                } catch (CommandSyntaxException e) {
                    throw new RuntimeException(e);
                }
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
