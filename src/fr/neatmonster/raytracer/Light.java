package fr.neatmonster.raytracer;

import fr.neatmonster.raytracer.util.Vector;

public abstract class Light {
    public static class DirectionalLight extends Light {
        private final Vector direction;

        public DirectionalLight(final Vector direction, final Vector color,
                final float intensity) {
            super(color, intensity);
            this.direction = direction;
        }

        public Vector getDirection() {
            return direction;
        }
    }

    public static class PointLight extends Light {
        private final Vector position;
        private final float  constant;
        private final float  linear;
        private final float  exponent;

        public PointLight(final Vector position, final Vector color,
                final float intensity, final float constant, final float linear,
                final float exponent) {
            super(color, intensity);
            this.position = position;
            this.constant = constant;
            this.linear = linear;
            this.exponent = exponent;
        }

        public float getConstant() {
            return constant;
        }

        public float getExponent() {
            return exponent;
        }

        public float getLinear() {
            return linear;
        }

        public Vector getPosition() {
            return position;
        }
    }

    private final Vector color;
    private final float  intensity;

    private Light(final Vector color, final float intensity) {
        this.color = color;
        this.intensity = intensity;
    }

    public Vector getColor() {
        return color;
    }

    public float getIntensity() {
        return intensity;
    }
}
