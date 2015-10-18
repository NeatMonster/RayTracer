package fr.neatmonster.raytracer.util;

public class Quaternion {
    private final float w;
    private final float x;
    private final float y;
    private final float z;

    public Quaternion(final float w, final float x, final float y,
            final float z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Quaternion conj() {
        return new Quaternion(w, -x, -y, -z);
    }

    public Vector getForward() {
        return new Vector(0f, 0f, 1f).mul(this);
    }

    public Vector getRight() {
        return new Vector(1f, 0f, 0f).mul(this);
    }

    public Vector getUp() {
        return new Vector(0f, 1f, 0f).mul(this);
    }

    public float getW() {
        return w;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public Quaternion mul(final Quaternion q) {
        return new Quaternion(w * q.w - x * q.x - y * q.y - z * q.z,
                w * q.x + x * q.w + y * q.z - z * q.y,
                w * q.y - x * q.z + y * q.w + z * q.x,
                w * q.z + x * q.y - y * q.x + z * q.w);
    }

    public Quaternion mul(final Vector v) {
        return new Quaternion(-x * v.getX() - y * v.getY() - z * v.getZ(),
                w * v.getX() + y * v.getZ() - z * v.getY(),
                w * v.getY() + z * v.getX() - x * v.getZ(),
                w * v.getZ() + x * v.getY() - y * v.getX());
    }

    @Override
    public String toString() {
        return String.format("Quaternion[%.5f, %.5f, %.5f, %.5f]", w, x, y, z);
    }
}
