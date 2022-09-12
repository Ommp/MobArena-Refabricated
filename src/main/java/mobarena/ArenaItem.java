package mobarena;

import java.util.HashMap;

public class ArenaItem {
    String name;
    int count;
    int slot;

    //an item might have (multiple) enchantments, and enchantments have levels
    private HashMap<String, Integer> enchantments;

    public ArenaItem(String name, int count, int slot) {
        this.name = name;
        this.count = count;
        this.slot = slot;
    }
    public ArenaItem(String name, int count, int slot, HashMap<String, Integer> enchantments) {
        this.name = name;
        this.count = count;
        this.slot = slot;
        this.enchantments = enchantments;
    }

    public HashMap<String, Integer> getEnchantments() {
        return enchantments;
    }
    public boolean containsEnchantments() {
        if (enchantments != null) {
            return !enchantments.isEmpty();
        }
         return false;
    }

    public int getSlot() {
        return slot;
    }
}