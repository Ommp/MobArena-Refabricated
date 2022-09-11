package mobarena;

import java.util.HashMap;

public class ArenaItem {
    String name;
    int count;

    //an item might have (multiple) enchantments, and enchantments have levels
    private HashMap<String, Integer> enchantments;

    public ArenaItem(String name, int count) {
        this.name = name;
        this.count = count;
    }
    public ArenaItem(String name, int count, HashMap<String, Integer> enchantments) {
        this.name = name;
        this.count = count;
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
}