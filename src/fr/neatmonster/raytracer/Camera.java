package fr.neatmonster.raytracer;

import fr.neatmonster.raytracer.util.Quaternion;
import fr.neatmonster.raytracer.util.Vector;

public class Camera {
    private Vector     pos;
    private Quaternion rot;
    private Vector     right;
    private Vector     up;
    private Vector     forward;

    public Camera() {
        rot = new Quaternion(0f, 0f, 1f, 0f);
        reset();
    }

    public Vector getForward() {
        return forward;
    }

    public Vector getPos() {
        return pos;
    }

    public Vector getRight() {
        return right;
    }

    public Quaternion getRot() {
        return rot;
    }

    public Vector getUp() {
        return up;
    }

    public void reset() {
        pos = new Vector(0f, 1f, 0f);
        right = new Vector(-1f, 0f, 0f);
        up = new Vector(0f, 1f, 0f);
        forward = new Vector(0f, 0f, -1f);
    }

    public void setForward(final Vector forward) {
        this.forward = forward;
    }

    public void setPos(final Vector pos) {
        this.pos = pos;
    }

    public void setRight(final Vector right) {
        this.right = right;
    }

    public void setRot(final Quaternion rot) {
        this.rot = rot;
    }

    public void setUp(final Vector up) {
        this.up = up;
    }
}
