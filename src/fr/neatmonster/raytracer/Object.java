package fr.neatmonster.raytracer;

import java.util.ArrayList;
import java.util.List;

import fr.neatmonster.raytracer.primitives.Plane;
import fr.neatmonster.raytracer.primitives.Sphere;
import fr.neatmonster.raytracer.util.Vector;

public class Object {
    private final List<Primitive> prims = new ArrayList<Primitive>();
    private final Material        mat;

    public Object(final Vector pos, final float rad, final Material mat) {
        prims.add(new Sphere(pos, rad));
        this.mat = mat;
    }

    public Object(final Vector pos, final Vector norm, final Material mat) {
        prims.add(new Plane(pos, norm));
        this.mat = mat;
    }

    public Material getMat() {
        return mat;
    }

    public List<Primitive> getPrims() {
        return prims;
    }
}
