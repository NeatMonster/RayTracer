package fr.neatmonster.raytracer;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import fr.neatmonster.raytracer.util.Vector;

public class Display extends Canvas {
    private static final long serialVersionUID = 1L;

    private final BufferedImage image;
    private final int[]         pixels;

    private final JFrame         frame;
    private final BufferStrategy buffer;

    public Display() {
        image = new BufferedImage(Main.WIDTH, Main.HEIGHT,
                BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        clear();

        setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));
        frame = new JFrame();
        frame.setResizable(false);
        frame.setTitle("RayTracer");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);

        createBufferStrategy(3);
        buffer = getBufferStrategy();
    }

    public void clear() {
        Arrays.fill(pixels, 0);
    }

    public void drawPixel(final int x, final int y, final Vector c) {
        final int r = (int) (c.getX() * 255f);
        final int g = (int) (c.getY() * 255f);
        final int b = (int) (c.getZ() * 255f);
        pixels[x + y * Main.WIDTH] = r << 16 | g << 8 | b;
    }

    public void render() {
        try {
            ImageIO.write(image, "png", new File(".", "render.png"));
        } catch (final IOException e) {
            e.printStackTrace();
        }
        final Graphics g = buffer.getDrawGraphics();
        g.drawImage(image, 0, 0, Main.WIDTH, Main.HEIGHT, null);
        g.dispose();
        buffer.show();
    }
}
