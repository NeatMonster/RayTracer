package fr.neatmonster.raytracer;

import fr.neatmonster.raytracer.util.Vector;

public abstract class Material {
    public static class CookTorranceMaterial extends Material {
        private final float roughness;
        private final float fresnel;
        private final float density;

        public CookTorranceMaterial(final Vector ambient, final Vector diffuse,
                final Vector specular, final float roughness,
                final float fresnel, final float density,
                final float reflectivity, final float refractivity,
                final float ior) {
            super(ambient, diffuse, specular, reflectivity, refractivity, ior);
            this.roughness = roughness;
            this.fresnel = fresnel;
            this.density = density;
        }

        public float getDensity() {
            return density;
        }

        public float getFresnel() {
            return fresnel;
        }

        public float getRoughness() {
            return roughness;
        }
    }

    public static class PhongMaterial extends Material {
        private final float shininess;

        public PhongMaterial(final Vector ambient, final Vector diffuse,
                final Vector specular, final float shininess,
                final float reflectivity, final float refractivity,
                final float ior) {
            super(ambient, diffuse, specular, reflectivity, refractivity, ior);
            this.shininess = shininess;
        }

        public float getShininess() {
            return shininess;
        }
    }

    public static final Material METAL = new CookTorranceMaterial(
            new Vector(0.01f), new Vector(0f), new Vector(1f), 0.1f, 1f, 0.5f,
            1f, 0f, 0f);

    public static final Material GLASS = new CookTorranceMaterial(
            new Vector(0.01f), new Vector(0f), new Vector(1f), 0.1f, 1f, 0.5f,
            0f, 1f, 1.52f);

    private final Vector ambient;
    private final Vector diffuse;
    private final Vector specular;

    private final float reflectivity;
    private final float refractivity;
    private final float ior;

    public Material(final Vector ambient, final Vector diffuse,
            final Vector specular, final float reflectivity,
            final float refractivity, final float ior) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;

        this.reflectivity = reflectivity;
        this.refractivity = refractivity;
        this.ior = ior;
    }

    public Vector getAmbient() {
        return ambient;
    }

    public Vector getDiffuse() {
        return diffuse;
    }

    public float getIOR() {
        return ior;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public float getRefractivity() {
        return refractivity;
    }

    public Vector getSpecular() {
        return specular;
    }
}
