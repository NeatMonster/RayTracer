package fr.neatmonster.raytracer;

import fr.neatmonster.raytracer.util.Vector;

public class Ray {
    private final Vector pos;
    private final Vector dir;
    private final float  ior;

    public Ray(final Vector pos, final Vector dir) {
        this(pos, dir, 1f);
    }

    public Ray(final Vector pos, final Vector dir, final float ior) {
        this.pos = pos;
        this.dir = dir.norm();
        this.ior = ior;
    }

    public Vector getDir() {
        return dir;
    }

    public float getIOR() {
        return ior;
    }

    public Vector getPos() {
        return pos;
    }
}
