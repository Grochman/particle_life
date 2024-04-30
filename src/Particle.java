import java.awt.*;

class Particle {
    private double x;
    private double y;
    private final int radius;
    private double dx;
    private double dy;
    private final double t_half;
    private Color color;
    private final int color_id;
    private final int interaction_radius = 100;

    public Particle(int x, int y, int radius, int color_id) {
        this.t_half = 0.5;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.dx = 0;
        this.dy = 0;
        this.color_id = color_id;
        switch (color_id){
            case 0:
                color = new Color(255, 0, 0);
                break;
            case 1:
                color = new Color(0, 255, 102);
                break;
            case 2:
                color = new Color(0, 42, 255);
                break;
            case 3:
                color = new Color(255, 255, 0);
                break;
        }
    }

    public void draw(Graphics g) {
        g.setColor(color);
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

        if ((x < 0 && dx < 0) || (x > panelWidth && dx > 0)) {
            dx = -dx;
        }
        if ((y < 0 && dy < 0) || (y > panelHeight && dy > 0)) {
            dy = -dy;
        }
    }
    public double getX(){return x;}
    public double getY(){return y;}
    public int getColor_id(){return color_id;}
    public int getInteraction_radius(){return interaction_radius;}
}
