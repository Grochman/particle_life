import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Main extends JFrame {
    int WINDOW_WIDTH = 700;
    int WINDOW_HEIGHT = 700;

    public Main() {
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        World customPanel = new World(WINDOW_WIDTH, WINDOW_HEIGHT);
        add(customPanel);

        setVisible(true);

        customPanel.animate();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    static class World extends JPanel {
        private final ArrayList<Particle> particles;
        private int frameCount = 0;
        private long time_fps;
        private final double[][] color_matrix;

        public World(int width, int height) {
            particles = new ArrayList<>();
            Random rand = new Random();

            int color_count = 3;
            int population_count = 150;

            for (int i = 0; i < population_count; i++) {
                int x = rand.nextInt(width-20);
                int y = rand.nextInt(height-20);
                int color = rand.nextInt(color_count);
                particles.add(new Particle(x+10, y+10, 5, color));
            }

            color_matrix = new double[color_count][color_count];
            for(int i = 0; i < color_count; i++){
                for(int j = 0; j < color_count; j++){
                    if(i==j){
                        color_matrix[i][j] = 1;
                    }
                    else if(j == i+1){
                        color_matrix[i][j] = 0.5;
                    }
                    else{
                        color_matrix[i][j] = 0;
                    }
                }
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
            time_fps = System.currentTimeMillis();
            while (true) {
                long currentTime = System.currentTimeMillis();
                double dt = (currentTime - lastTime) / 1000.0;
                lastTime = currentTime;

                updatePositions(dt);
                paintImmediately(0, 0, getWidth(), getHeight());

                fps(currentTime);
            }
        }

        private void updatePositions(double dt) {
            for (Particle particle : particles) {
                if (particle.getY() != particle.getY()){
                    throw (new RuntimeException("Nan"));
                }
                double total_force_x = 0;
                double total_force_y = 0;
                int interaction_radius = particle.getInteraction_radius();
                for (Particle neighbour : particles){
                    if (neighbour == particle) {
                        continue;
                    }

                    double interaction_vector_x = neighbour.getX()-particle.getX();
                    double interaction_vector_y = neighbour.getY()-particle.getY();

                    double distance = Math.sqrt(Math.pow(interaction_vector_x,2)+Math.pow(interaction_vector_y,2));

                    interaction_vector_x /= distance;
                    interaction_vector_y /= distance;
                    distance /= interaction_radius;

                    if (distance <= 0){continue;}

                    double force = calculateForce(particle, neighbour, distance);
                    total_force_x += force * interaction_vector_x;
                    total_force_y += force * interaction_vector_y;
                }
                total_force_x *= interaction_radius;
                total_force_y *= interaction_radius;
                particle.updateVelocity(total_force_x,total_force_y,dt);
            }

            for (Particle particle : particles) {
                particle.move(getWidth(), getHeight(), dt);
            }
        }

        private double calculateForce(Particle particle, Particle neighbour, double distance){
            double beta = 0.3;
            double force = 0;
            double alfa = color_matrix[particle.getColor_id()][neighbour.getColor_id()];
            if(distance < beta){
                force = distance/beta -1;
            }
            else if(distance < 0.5+beta/2){
                force = alfa*(1-Math.abs(2*distance-1-beta))/(1-beta);
            }
            return force;
        }

        private void fps(long currentTime){
            frameCount++;
            if (currentTime - time_fps >= 1000) {
                float fps = (float) (frameCount * 1000) / (currentTime - time_fps);
                System.out.println(fps);
                frameCount = 0;
                time_fps = currentTime;
            }
        }
    }
}
