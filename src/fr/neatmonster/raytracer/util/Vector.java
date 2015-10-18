package fr.neatmonster.raytracer.util;

public class Vector {
    private final float x;
    private final float y;
    private final float z;

    public Vector() {
        this(0f);
    }

    public Vector(final float f) {
        this(f, f, f);
    }

    public Vector(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector add(final Vector v) {
        return new Vector(x + v.x, y + v.y, z + v.z);
    }

    public Vector clamp(final float min, final float max) {
        return new Vector(Math.max(min, Math.min(max, x)),
                Math.max(min, Math.min(max, y)),
                Math.max(min, Math.min(max, z)));
    }

    public Vector cross(final Vector v) {
        return new Vector(y * v.z - v.y * z, z * v.x - v.z * x,
                x * v.y - v.x * y);
    }

    public Vector div(final float f) {
        return new Vector(x / f, y / f, z / f);
    }

    public float dot(final Vector v) {
        return x * v.x + y * v.y + z * v.z;
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

    public float len() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vector mul(final float f) {
        return new Vector(x * f, y * f, z * f);
    }

    public Vector mul(final Quaternion q) {
        final Quaternion w = q.mul(this).mul(q.conj());
        return new Vector(w.getX(), w.getY(), w.getZ());
    }

    public Vector mul(final Vector v) {
        return new Vector(x * v.x, y * v.y, z * v.z);
    }

    public Vector neg() {
        return new Vector(-x, -y, -z);
    }

    public Vector norm() {
        final float len = len();
        return new Vector(x / len, y / len, z / len);
    }

    public Vector reflect(final Vector N) {
        return sub(N.mul(dot(N)).mul(2f));
    }

    public Vector refract(final Vector N, final float n, final float NdotI,
            float cosT) {
        cosT = (float) Math.sqrt(1f - cosT);
        if (cosT < 1f)
            return mul(n).add(N.mul(n * NdotI - cosT));
        else
            return reflect(N);
    }

    public Vector sub(final Vector v) {
        return new Vector(x - v.x, y - v.y, z - v.z);
    }

    @Override
    public String toString() {
        return String.format("Vector[%.5f, %.5f, %.5f]", x, y, z);
    }
}
