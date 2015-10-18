package fr.neatmonster.raytracer.primitives;

import fr.neatmonster.raytracer.Intersection;
import fr.neatmonster.raytracer.Main;
import fr.neatmonster.raytracer.Primitive;
import fr.neatmonster.raytracer.Ray;
import fr.neatmonster.raytracer.util.Vector;

public class Plane extends Primitive {
    private final Vector norm;

    public Plane(final Vector pos, final Vector norm) {
        super(pos);
        this.norm = norm;
    }

    @Override
    public Intersection intersect(final Ray r) {
        final float t = getPos().sub(r.getPos()).dot(norm)
                / r.getDir().dot(norm);
        if (t < Main.PRECISION)
            return null;

        return new Intersection(r.getPos().add(r.getDir().mul(t)), norm, t);
    }
}
