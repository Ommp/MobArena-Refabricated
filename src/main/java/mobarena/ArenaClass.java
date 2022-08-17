package mobarena;

import java.util.ArrayList;

public class ArenaClass {
    private String name, helmet, chest, legs, boots;
    private ArrayList<String> items;

    public ArenaClass(String name, String helmet, String chest, String legs, String boots) {
        this.name = name;
        this.helmet = helmet;
        this.chest = chest;
        this.legs = legs;
        this.boots = boots;
    }

    public String getName() {
        return name;
    }
}