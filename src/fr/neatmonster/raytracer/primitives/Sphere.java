package fr.neatmonster.raytracer.primitives;

import fr.neatmonster.raytracer.Intersection;
import fr.neatmonster.raytracer.Main;
import fr.neatmonster.raytracer.Primitive;
import fr.neatmonster.raytracer.Ray;
import fr.neatmonster.raytracer.util.Vector;

public class Sphere extends Primitive {
    private final float rad;

    public Sphere(final Vector pos, final float rad) {
        super(pos);
        this.rad = rad;
    }

    @Override
    public Intersection intersect(final Ray r) {
        final Vector l = getPos().sub(r.getPos());

        final float b = l.dot(r.getDir());
        float d = b * b - l.dot(l) + rad * rad;
        if (d < 0f)
            return null;
        d = (float) Math.sqrt(d);

        float t = b - d;
        if (t < Main.PRECISION)
            t = b + d;
        if (t < Main.PRECISION)
            return null;

        final Vector v = r.getPos().add(r.getDir().mul(t));
        return new Intersection(v, v.sub(getPos()).div(rad), t);
    }
}
