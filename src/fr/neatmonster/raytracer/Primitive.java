package fr.neatmonster.raytracer;

import fr.neatmonster.raytracer.util.Vector;

public abstract class Primitive {
    private final Vector pos;

    public Primitive(final Vector pos) {
        this.pos = pos;
    }

    public Vector getPos() {
        return pos;
    }

    public abstract Intersection intersect(final Ray r);
}
