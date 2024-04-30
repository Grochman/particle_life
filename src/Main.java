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
        private double[][] color_matrix;
        private final int window_width;
        private final int window_height;
        private final int color_count = 5;
        private final int population_count = 1000;

        Random rand = new Random();

        public World(int window_width, int window_height) {
            this.window_height = window_height;
            this.window_width = window_width;
            particles = new ArrayList<>();

            generateParticles("normal");

            generateColorMatrix("random");
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
                    if (Math.abs(interaction_vector_x) > window_width/2){
                        if(interaction_vector_x > 0){
                            interaction_vector_x -= window_width;
                        }
                        else{
                            interaction_vector_x += window_width;
                        }
                    }
                    double interaction_vector_y = neighbour.getY()-particle.getY();
                    if (Math.abs(interaction_vector_y) > window_height/2){
                        if(interaction_vector_y > 0){
                            interaction_vector_y -= window_height;
                        }
                        else{
                            interaction_vector_y += window_height;
                        }
                    }
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

        private void generateParticles(String style){

            for (int i = 0; i < population_count; i++) {
                int color = rand.nextInt(color_count);
                if(style == "normal"){
                    int x = rand.nextInt(window_width-20);
                    int y = rand.nextInt(window_height-20);
                    particles.add(new Particle(x+10, y+10, 3, color));
                }
                if(style == "big_bang"){
                    int x = rand.nextInt(window_width/10);
                    int y = rand.nextInt(window_height/10);
                    particles.add(new Particle(x+window_width/2-window_width/20, y+window_height/2-window_height/20, 3, color));
                }
            }
        }
        private void generateColorMatrix(String style){
            color_matrix = new double[color_count][color_count];
            for(int i = 0; i < color_count; i++){
                for(int j = 0; j < color_count; j++){
                    if(style=="clumps"){
                        if (i == j) {
                            color_matrix[i][j] = 1;
                        } else {
                            color_matrix[i][j] = -1;
                        }
                    }
                    if(style == "snake" || style == "infinite_snake") {
                        if (i == j) {
                            color_matrix[i][j] = 1;
                        } else if (j == i + 1) {
                            color_matrix[i][j] = 0.2;
                        } else {
                            color_matrix[i][j] = 0;
                        }
                    }
                    else if(style== "random"){
                        double plus = (Math.pow(-1, rand.nextInt(2)));
                        color_matrix[i][j] = rand.nextDouble()*plus;
                    }
                }
            }
            if (style == "infinite_snake"){
                color_matrix[color_count-1][0] = 0.2;
            }
        }
    }
}
