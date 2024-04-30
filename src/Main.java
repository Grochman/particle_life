import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Main extends JFrame {
    int WINDOW_WIDTH = 800;
    int WINDOW_HEIGHT = 800;

    private final CustomPanel customPanel;

    public Main() {
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        customPanel = new CustomPanel();
        add(customPanel);

        setVisible(true);

        customPanel.animate();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    static class CustomPanel extends JPanel {

        private final ArrayList<Particle> particles;
        private final int INTERACTION_RADIUS = 100;
        private final int POPULATION_COUNT = 1500;
        private int frameCount = 0;

        public CustomPanel() {
            particles = new ArrayList<>();
            Random rand = new Random();
            for (int i = 0; i < POPULATION_COUNT; i++) {
                int x = rand.nextInt(750);
                int y = rand.nextInt(750);
                particles.add(new Particle(x+10, y+10, 5));
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (Particle circle : particles) {
                circle.draw(g);
            }
        }

        private void animate() {
            long lastTime = System.currentTimeMillis();
            long timefps = System.currentTimeMillis();
            while (true) {
                long currentTime = System.currentTimeMillis();
                double dt = (currentTime - lastTime) / 1000.0;
                lastTime = currentTime;

                updatePositions(dt);
                paintImmediately(0, 0, getWidth(), getHeight());
                //repaint();
                frameCount++;
                if (currentTime - timefps >= 1000) {
                    float fps = (float) (frameCount * 1000) / (currentTime - timefps);
                    System.out.println("FPS: " + fps);
                    frameCount = 0;
                    timefps = currentTime;
                }
            }
        }

        private void updatePositions(double dt) {
            for (Particle particle : particles) {
                if (particle.getY() != particle.getY()){
                    throw (new RuntimeException("Nan"));
                }
                double total_force_x = 0;
                double total_force_y = 0;
                for (Particle neighbour : particles){
                    if (neighbour == particle) {
                        continue;
                    }

                    double interaction_vector_x = neighbour.getX()-particle.getX();
                    double interaction_vector_y = neighbour.getY()-particle.getY();

                    double distance = Math.sqrt(Math.pow(interaction_vector_x,2)+Math.pow(interaction_vector_y,2));

                    interaction_vector_x /= distance;
                    interaction_vector_y /= distance;
                    distance /= INTERACTION_RADIUS;

                    if (distance <= 0){continue;}

                    double force = calculateForce(distance);
                    total_force_x += force * interaction_vector_x;
                    total_force_y += force * interaction_vector_y;
                }
                total_force_x *= INTERACTION_RADIUS;
                total_force_y *= INTERACTION_RADIUS;
                particle.updateVelocity(total_force_x,total_force_y,dt);
            }

            for (Particle particle : particles) {
                particle.move(getWidth(), getHeight(), dt);
            }
        }

        private double calculateForce(double distance){
            double beta = 0.3;
            double force = 0;
            if(distance < beta){
                force = distance/beta -1;
            }
            else if(distance < 0.5+beta/2){
                force = 1-Math.abs(2*distance-1-beta)/(1-beta);
            }
            return force;
        }
    }
}
