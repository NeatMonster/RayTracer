package fr.neatmonster.raytracer;

import java.util.Random;

import fr.neatmonster.raytracer.Light.DirectionalLight;
import fr.neatmonster.raytracer.Light.PointLight;
import fr.neatmonster.raytracer.Material.CookTorranceMaterial;
import fr.neatmonster.raytracer.Material.PhongMaterial;
import fr.neatmonster.raytracer.util.Vector;

public class Engine extends Thread {
    private final Display display;
    private final Camera  camera;
    private final Scene   scene;

    public Engine(final Display display) {
        this.display = display;
        camera = new Camera();
        scene = new Scene();
    }

    public Ray calculate(final float x, final float y) {
        final float ar = (float) Main.WIDTH / (float) Main.HEIGHT;
        final float xNorm = (x - Main.WIDTH * 0.5f) / Main.WIDTH * ar;
        final float yNorm = (Main.HEIGHT * 0.5f - y) / Main.HEIGHT;

        final Vector pos = camera.getPos();
        final Vector right = camera.getRight();
        final Vector up = camera.getUp();
        final Vector forward = camera.getForward();

        return new Ray(pos, right.mul(xNorm).add(up.mul(yNorm)).add(forward));
    }

    public void render() {
        final float aa = (float) Math.sqrt(Main.ANTIALIASING);
        for (int y = 0; y < Main.HEIGHT; y++)
            for (int x = 0; x < Main.WIDTH; x++) {
                Vector c = new Vector();
                for (float yAA = 0f; yAA < 1f; yAA += 1f / aa)
                    for (float xAA = 0f; xAA < 1f; xAA += 1f / aa)
                        c = c.add(trace(calculate(x + xAA, y + yAA), 0)
                                .mul(1f / (aa * aa)));
                display.drawPixel(x, y, c);
            }
        display.render();
    }

    public void rotate(final int dop) {
        final Vector focus = new Vector(1f, 0.75f, -4f);
        final Vector dir = focus.sub(camera.getPos());
        final float angle = (float) Math.PI / 360f
                - (float) Math.PI / 720f * dop;
        final float cos = (float) Math.cos(angle);
        final float sin = (float) Math.sin(angle);
        final float xNew = dir.getX() * cos - dir.getZ() * sin;
        final float zNew = dir.getX() * sin + dir.getZ() * cos;
        final Vector newDir = new Vector(xNew, dir.getY(), zNew);
        final Vector newPos = focus.sub(newDir);
        camera.setPos(newPos);
        final Vector r = camera.getRight();
        final float xrNew = r.getX() * cos - r.getZ() * sin;
        final float zrNew = r.getX() * sin + r.getZ() * cos;
        final Vector rNew = new Vector(xrNew, r.getY(), zrNew);
        camera.setRight(rNew);
        final Vector f = camera.getForward();
        final float xfNew = f.getX() * cos - f.getZ() * sin;
        final float zfNew = f.getX() * sin + f.getZ() * cos;
        final Vector fNew = new Vector(xfNew, f.getY(), zfNew);
        camera.setForward(fNew);
    }

    @Override
    public void run() {
        while (true)
            render();
    }

    public Vector trace(final Ray r, final int n) {
        if (n > Main.RECURSION)
            return new Vector();

        Object obj = null;
        Intersection x = null;
        float dist = Float.MAX_VALUE;

        for (final Object o : scene.getObjs())
            for (final Primitive p : o.getPrims()) {
                final Intersection xTmp = p.intersect(r);
                if (xTmp != null && xTmp.getDist() < dist) {
                    obj = o;
                    x = xTmp;
                    dist = x.getDist();
                }
            }

        if (x == null)
            return new Vector();

        Vector c = new Vector();
        for (final Light light : scene.getLights()) {
            c = c.add(traceLight(r, x, light, obj.getMat()));

            if (obj.getMat().getReflectivity() < 1f) {
                Ray rayShadow;
                float len = Float.MAX_VALUE;
                if (light instanceof DirectionalLight) {
                    final DirectionalLight dl = (DirectionalLight) light;
                    rayShadow = new Ray(x.getPos(), dl.getDirection().neg());
                } else if (light instanceof PointLight) {
                    final PointLight pl = (PointLight) light;
                    final Vector L = pl.getPosition().sub(x.getPos());
                    len = L.len();
                    rayShadow = new Ray(x.getPos(), L);
                } else
                    continue;
                //if (Main.SHADOW_RAYS == 1)
                //    c = c.mul(Math.min(1f, traceShadow(rayShadow, obj, len)
                //            + obj.getMat().getReflectivity()));
                //else {
                    final Vector X = rayShadow.getDir();
                    Vector U = new Vector(1, 0, 0);
                    if (U.dot(X) != 0)
                        U = new Vector(0, 1, 0);
                    final Vector Y = X.cross(U);
                    final Vector Z = X.cross(Y);
                    final Random rnd = new Random();
                    float weight = 0f;
                    for (int i = 0; i < Main.SHADOW_RAYS; i++) {
                        final float yOff = rnd.nextFloat() * Main.SHADOW_RANGE;
                        final float zOff = rnd.nextFloat() * Main.SHADOW_RANGE;
                        final Ray raySoftShadow = new Ray(rayShadow.getPos(),
                                rayShadow.getDir().add(Y.mul(yOff))
                                        .add(Z.mul(zOff)));
                        weight += traceShadow(raySoftShadow, obj, len)
                                / Main.SHADOW_RAYS;
                    }
                    c = c.mul(Math.min(1f,
                            weight + obj.getMat().getReflectivity()));
                //}
            }
        }

        if (obj.getMat().getReflectivity() > 0f) {
            final Ray rReflect = new Ray(x.getPos(),
                    r.getDir().reflect(x.getNorm()));
            c = c.add(
                    trace(rReflect, n + 1).mul(obj.getMat().getReflectivity()));
        }

        if (obj.getMat().getRefractivity() > 0f) {
            Vector N = x.getNorm();
            float NdotI = r.getDir().dot(N);

            float n1, n2;
            if (NdotI > 0f) {
                n1 = r.getIOR();
                n2 = obj.getMat().getIOR();
                N = N.neg();
            } else {
                n1 = obj.getMat().getIOR();
                n2 = r.getIOR();
                NdotI = -NdotI;
            }

            final float ior = n2 / n1;
            final float cosT = ior * ior * (1f - NdotI * NdotI);

            final Ray rRefract = new Ray(x.getPos(),
                    r.getDir().refract(N, ior, NdotI, cosT));
            c = c.add(
                    trace(rRefract, n + 1).mul(obj.getMat().getRefractivity()));
        }

        c = c.add(obj.getMat().getAmbient());

        return c.clamp(0f, 1f);
    }

    public Vector traceLight(final Ray r, final Intersection x, final Light l,
            final Material m) {
        float A = 1f;
        final Vector N = x.getNorm();
        Vector L, V, H;
        if (l instanceof DirectionalLight) {
            final DirectionalLight dl = (DirectionalLight) l;
            L = dl.getDirection().neg().norm();
            V = r.getDir().neg();
            H = V.add(L).norm();
        } else if (l instanceof PointLight) {
            final PointLight pl = (PointLight) l;
            L = pl.getPosition().sub(x.getPos());
            final float len = L.len();
            L = L.norm();
            V = r.getDir().neg();
            H = V.add(L).norm();
            A = pl.getConstant() + pl.getLinear() * len
                    + pl.getExponent() * len * len + Main.PRECISION;
        } else
            return new Vector();

        final float NdotL = Math.min(N.dot(L), 1f);
        final float NdotV = Math.min(N.dot(V), 1f);
        final float NdotH = Math.min(N.dot(H), 1f);
        final float VdotH = Math.min(V.dot(H), 1f);

        if (m instanceof PhongMaterial) {
            final PhongMaterial pm = (PhongMaterial) m;

            final float lambertian = Math.min(NdotL, 1f);
            float specular;
            if (pm.getShininess() > 0f)
                specular = (float) Math.pow(Math.max(NdotH, 0f),
                        pm.getShininess());
            else
                specular = 0f;

            final Vector C = l.getColor().mul(pm.getDiffuse()).mul(lambertian)
                    .mul(l.getIntensity())
                    .add(m.getSpecular().mul(specular).mul(l.getIntensity()));
            return C.div(A);
        } else {
            final CookTorranceMaterial ctm = (CookTorranceMaterial) m;

            if (NdotL < Main.PRECISION)
                return new Vector();

            final float R = ctm.getRoughness();
            final float F = ctm.getFresnel();
            final float K = ctm.getDensity();

            // Geometric
            final float geometric = Math.min(1f, Math.min(
                    2f * NdotH * NdotV / VdotH, 2f * NdotH * NdotL / VdotH));

            // Roughness
            final float roughness = (float) Math
                    .exp((NdotH * NdotH - 1f) / (R * R * NdotH * NdotH))
                    / ((float) Math.PI * R * R * (float) Math.pow(NdotH, 4));

            // Fresnel
            final float fresnel = F
                    + (1f - F) * (float) Math.pow(1f - VdotH, 5);

            // Specular
            final float specular = fresnel * geometric * roughness
                    / (NdotV * NdotL * (float) Math.PI); // Use precision?
            // final float specular = fresnel * geometric * roughness /
            // Math.max(NdotV * NdotL, Main.PRECISION);

            final Vector C = l.getColor().mul(NdotL).mul(l.getIntensity())
                    .mul(m.getDiffuse().mul(K)
                            .add(m.getSpecular().mul(specular * (1f - K))));
            return C.div(A);
        }
    }

    public float traceShadow(final Ray r, final Object thisobj,
            final float len) {
        Object obj = null;
        Intersection x = null;
        float dist = Float.MAX_VALUE;

        for (final Object o : scene.getObjs()) {
            if (o.equals(thisobj))
                continue;
            for (final Primitive p : o.getPrims()) {
                final Intersection xTmp = p.intersect(r);
                if (xTmp != null && xTmp.getDist() < dist
                        && xTmp.getDist() < len) {
                    obj = o;
                    x = xTmp;
                    dist = x.getDist();
                }
            }
        }

        if (x == null)
            return 1f;

        float weight = 1f;
        if (obj.getMat().getReflectivity() > 0f)
            weight -= obj.getMat().getReflectivity();
        if (obj.getMat().getRefractivity() > 0f)
            weight *= obj.getMat().getRefractivity();
        return weight;
    }
}
