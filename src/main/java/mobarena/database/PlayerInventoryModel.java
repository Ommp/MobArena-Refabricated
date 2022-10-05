package mobarena.database;

import mobarena.ArenaItem;

import java.util.ArrayList;

public class PlayerInventoryModel {

    private String playerUUID;
    private ArrayList<ArenaItem> items = new ArrayList<>();

    public PlayerInventoryModel() {
    }

    public PlayerInventoryModel(String playerUUID, ArrayList<ArenaItem> items) {
        this.playerUUID = playerUUID;
        this.items = items;
    }

    public String getPlayerUUID() {
        return playerUUID;
    }

    public ArrayList<ArenaItem> getItems() {
        return items;
    }

    public void addItem(ArenaItem item) {
        items.add(item);
    }
}
