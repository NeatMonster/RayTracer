package fr.neatmonster.raytracer;

public class Main {
    public static final int   WIDTH        = 1280;
    public static final int   HEIGHT       = 720;
    public static final int   RECURSION    = 3;
    public static final int   ANTIALIASING = 4;
    public static final int   SHADOW_RAYS  = 4;
    public static final float SHADOW_RANGE = 0.25f;
    public static final float PRECISION    = 0.001f;

    public static void main(final String[] args) {
        new Engine(new Display()).start();
    }
}
