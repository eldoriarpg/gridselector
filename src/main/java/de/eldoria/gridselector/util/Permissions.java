package de.eldoria.gridselector.util;

public final class Permissions {
    private Permissions() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    private static final String BASE = "gridselector";

    private static String perm(String... perms) {
        return String.join(".", perms);
    }

    public static final String USE = perm(BASE, "use");
    private static final String GRID = perm(BASE, "grid");
    public static final String GRID_DEFINE = perm(GRID, "define");
    public static final String GRID_DRAW = perm(GRID, "draw");
    public static final String SAVE = perm(BASE, "save");
    public static final String SAVE_GLOBAL = perm(SAVE, "global");
}
