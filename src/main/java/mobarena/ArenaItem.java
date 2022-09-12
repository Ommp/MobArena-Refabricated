package mobarena;

public class ArenaItem {
    String data;
    int slot;

    public ArenaItem(String data, int slot) {
        this.data = data;
        this.slot = slot;
    }

    public String getData() {
        return data;
    }
    public int getSlot() {
        return slot;
    }
}
