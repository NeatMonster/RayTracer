package fr.neatmonster.raytracer;

import fr.neatmonster.raytracer.util.Vector;

public class Triangle extends Primitive {
    private final Vector edgeA;
    private final Vector edgeB;
    private final Vector norm;

    public Triangle(final Vector vert0, final Vector vert1,
            final Vector vert2) {
        super(vert0);
        edgeA = vert1.sub(vert0);
        edgeB = vert2.sub(vert0);
        norm = edgeA.cross(edgeB).norm();
    }

    @Override
    public Intersection intersect(final Ray r) {
        final Vector p = r.getDir().cross(edgeB);
        final float d = edgeA.dot(p);
        if (d > -Main.PRECISION && d < Main.PRECISION)
            return null;

        final Vector t = r.getPos().sub(getPos());
        final float u = t.dot(p) / d;
        if (u < 0f || u > 1f)
            return null;

        final Vector q = t.cross(edgeA);
        final float v = r.getDir().dot(q) / d;
        if (v < 0f || u + v > 1f)
            return null;

        final float dist = edgeB.dot(q) / d;
        if (dist < Main.PRECISION)
            return null;

        return new Intersection(r.getPos().add(r.getDir().mul(t)), norm, dist);
    }
}
