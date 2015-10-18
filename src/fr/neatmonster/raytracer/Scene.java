package fr.neatmonster.raytracer;

import java.util.ArrayList;
import java.util.List;

import fr.neatmonster.raytracer.Light.PointLight;
import fr.neatmonster.raytracer.Material.CookTorranceMaterial;
import fr.neatmonster.raytracer.util.Vector;

public class Scene {
    public static Material makeDiffuse(final float r, final float g,
            final float b) {
        return new CookTorranceMaterial(
                new Vector(0.01f * r, 0.01f * g, 0.01f * b),
                new Vector(r, g, b), new Vector(r, g, b), 0.375f, 0.5f, 0.9f,
                0f, 0f, 0f);
    }

    private final List<Object> objs = new ArrayList<Object>();

    private final List<Light> lights = new ArrayList<Light>();

    public Scene() {
        // Planes
        objs.add(new Object(new Vector(0f, 0f, 0f), new Vector(0f, 1f, 0f),
                makeDiffuse(1f, 0f, 0f)));
        objs.add(new Object(new Vector(0f, 5f, 0f), new Vector(0f, -1f, 0f),
                makeDiffuse(0f, 1f, 0f)));
        objs.add(new Object(new Vector(0f, 0f, -10f), new Vector(0f, 0f, 1f),
                makeDiffuse(0f, 0f, 1f)));
        objs.add(new Object(new Vector(4f, 0f, 0f), new Vector(-1f, 0f, 0f),
                makeDiffuse(1f, 1f, 0f)));
        objs.add(new Object(new Vector(-4f, 0f, 0f), new Vector(1f, 0f, 0f),
                makeDiffuse(0f, 1f, 1f)));

        // Spheres
        objs.add(new Object(new Vector(1f, 0.75f, -4f), 0.75f, Material.METAL));
        objs.add(
                new Object(new Vector(-1f, 0.75f, -5f), 0.75f, Material.GLASS));

        // Lights
        lights.add(new PointLight(new Vector(0f, 4f, -5f), new Vector(1f), 1f,
                0f, 0f, 0.1f));
    }

    public List<Light> getLights() {
        return lights;
    }

    public List<Object> getObjs() {
        return objs;
    }
}
