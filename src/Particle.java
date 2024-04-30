import java.awt.*;

class Particle {
    private double x;
    private double y;
    private final int radius;
    private double dx;
    private double dy;
    private final double t_half;

    public Particle(int x, int y, int radius) {
        this.t_half = 0.5;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.dx = 0;
        this.dy = 0;
    }

    public void draw(Graphics g) {
        g.setColor(new Color(255, 0, 41));
        g.fillOval((int) (x - radius), (int) (y - radius), 2 * radius, 2 * radius);
    }

    public void updateVelocity(double force_x, double force_y, double delta){
        dx *= Math.pow(0.5, delta/t_half);
        dx += force_x * delta;
        dy *= Math.pow(0.5, delta/t_half);
        dy += force_y * delta;
    }
    public void move(int panelWidth, int panelHeight, double delta) {
        x += dx*delta;
        y += dy*delta;

        if (x - radius < 0 || x + radius > panelWidth) {
            dx = -dx;

        }
        if (y - radius < 0 || y + radius > panelHeight) {
            dy = -dy;

        }
    }
    public double getX(){return x;}
    public double getY(){return y;}
}