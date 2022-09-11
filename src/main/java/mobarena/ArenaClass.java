package mobarena;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ArenaClass {

    private transient String name;

    @SerializedName("items")
    private ArrayList<ArenaItem> items = new ArrayList<>();

    public ArenaClass(String name, ArrayList<ArenaItem> items) {
        this.name = name;
        this.items = items;
    }

    public ArenaClass() {
    }

    public String getName() {
        return name;
    }

    public ArrayList<ArenaItem> getItems() {
        return items;
    }
    public ArenaItem getItem(int index) {
        return items.get(index);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addItems(ArenaItem item) {
        this.items.add(item);
    }
}
