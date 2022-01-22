package mobarena.util;

public final class Slugs {

    /**
     * https://github.com/garbagemule/MobArena/blob/c143cc81c96afc40f6c42e3acca19f48adc8ed3c/src/main/java/com/garbagemule/MobArena/util/Slugs.java
     * Create a slug version
     * @param input
     * @return
     */
    public static String create(String input) {
        return input
                .toLowerCase()
                .replaceAll("[.,:;'\"]", "")
                .replaceAll("[<>(){}\\[\\]]", "")
                .replaceAll("[ _']", "-");
    }

}