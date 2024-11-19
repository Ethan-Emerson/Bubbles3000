
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Random;

public class ShootingStar {
    double x = 0;
    double y = 0;
    Random rand = new Random();
    double velocityX = rand.nextDouble();
    double velocityY = rand.nextDouble();
    Image starImage; // The image for the shooting star
    double alpha;
    List<Particle> particles = new ArrayList<>();

    public ShootingStar(Image starImage) {
        this.starImage = starImage; // Load the image for the shooting star
        resetStarPosition();
    }

    public void drawShootingStar(GraphicsContext gc) {

        // Adjust the image position so it aligns with the front of the trail
        double imageWidth = starImage.getWidth();
        double imageHeight = starImage.getHeight();

        // Align the image with the front of the shooting star (center it)
        double drawX = x + (World.x * -1) - imageWidth / 2;
        double drawY = y + (World.y * -1) - imageHeight / 2;
        drawX = drawX + .5;
        drawY = drawY + .5;

        // Draw the image at the adjusted position
        gc.drawImage(starImage, drawX, drawY);

        // Update the position of the shooting star
        x = x + velocityX;
        y = y + velocityY;

        // Create and update particles
        particles.add(new Particle(x, y, 1.5, alpha));

        Iterator<Particle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            Particle particle = iterator.next();
            particle.update();
            particle.render(gc);

            if (particle.alpha <= 0) {
                iterator.remove();
            }
        }

        // If the shooting star moves out of bounds, reset its position
        if (x - World.x < -50 || x - World.x > Main.getWidth() + 50 ||
                y - World.y < -50 || y - World.y > Main.getHeight() + 50) {
            resetStarPosition();
        }
    }

    public void resetStarPosition() {
        double canvasWidth = Main.getWidth();
        double canvasHeight = Main.getHeight();

        int randomEdge = rand.nextInt(4);

        switch (randomEdge) {
            case 0:
                x = rand.nextDouble() * canvasWidth;
                y = -50;
                velocityX = rand.nextDouble() * 0.5;
                velocityY = rand.nextDouble() * 1.5;
                break;
            case 1:
                x = canvasWidth + 50;
                y = rand.nextDouble() * canvasHeight;
                velocityX = rand.nextDouble() * -0.5;
                velocityY = rand.nextDouble() * 1.5;
                break;
            case 2:
                x = rand.nextDouble() * canvasWidth;
                y = canvasHeight + 50;
                velocityX = rand.nextDouble() * -0.5;
                velocityY = rand.nextDouble() * 1.5;
                break;
            case 3:
                x = -50;
                y = rand.nextDouble() * canvasHeight;
                velocityX = rand.nextDouble() * 0.5;
                velocityY = rand.nextDouble() * 1.5;
                break;
        }

        // Adjust for world position
        x = x + World.x;
        y = y + World.y;

        // Set the star's transparency randomly
        double min = 0.8;
        double max = 1;
        alpha = min + (max - min) * rand.nextDouble();

        particles.clear();
    }

    static class Particle {
        double x, y, size, alpha;

        public Particle(double x, double y, double size, double alpha) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.alpha = alpha;
        }

        public void update() {
            alpha -= 0.018;
            if (alpha < 0) alpha = 0;
        }

        public void render(GraphicsContext gc) {
            gc.setFill(Color.rgb(255, 255, 255, alpha));
            gc.fillOval(x + (World.x * -1), y + (World.y * -1), size, size);
        }
    }
}
