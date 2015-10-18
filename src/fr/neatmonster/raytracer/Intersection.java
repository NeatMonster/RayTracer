package fr.neatmonster.raytracer;

import fr.neatmonster.raytracer.util.Vector;

public class Intersection {
    private final Vector pos;
    private final Vector norm;
    private final float  dist;

    public Intersection(final Vector pos, final Vector norm, final float dist) {
        this.pos = pos;
        this.norm = norm;
        this.dist = dist;
    }

    public float getDist() {
        return dist;
    }

    public Vector getNorm() {
        return norm;
    }

    public Vector getPos() {
        return pos;
    }
}
