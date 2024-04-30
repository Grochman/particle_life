import java.awt.*;

class Particle {
    private double x;
    private double y;
    private final int radius;
    private double dx;
    private double dy;
    private final double t_half = 0.1;
    private Color color;
    private final int color_id;
    private final int interaction_radius = 100;
    private final int force_scale = 1;

    public Particle(int x, int y, int radius, int color_id) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.dx = 0;
        this.dy = 0;
        this.color_id = color_id;

        int r = (color_id)*17+100 % 256;
        int g = (color_id+30)*59 % 256;
        int b = (color_id+60)*29 % 256;
        color =  new Color(r, g, b);
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval((int) (x - radius), (int) (y - radius), 2 * radius, 2 * radius);
    }

    public void updateVelocity(double force_x, double force_y, double delta){
        dx *= Math.pow(0.5, delta/t_half);
        dx += force_x * delta *force_scale;
        dy *= Math.pow(0.5, delta/t_half);
        dy += force_y * delta * force_scale;
    }
    public void move(int panelWidth, int panelHeight, double delta) {
        x += dx*delta;
        y += dy*delta;

        //respectBounds(panelWidth,panelHeight);
        teleportBounds(panelWidth,panelHeight);
    }
    private void respectBounds(int panelWidth, int panelHeight){
        if ((x < 0 && dx < 0) || (x > panelWidth && dx > 0)) {
            dx = -dx;
            if(x<0){
                x = 0;
            }
            else{
                x = panelWidth;
            }
        }
        if ((y < 0 && dy < 0) || (y > panelHeight && dy > 0)) {
            dy = -dy;
            if(y<0){
                y = 0;
            }
            else{
                y = panelHeight;
            }
        }
    }
    private void teleportBounds(int panelWidth,int panelHeight){
        if(x+ radius<0){
            x = panelWidth;
        }
        else if(x - radius > panelWidth){
            x = 0;
        }
        if(y + radius < 0){
            y = panelHeight;
        }
        else if(y - radius > panelHeight){
            y = 0;
        }
    }

    public double getX(){return x;}
    public double getY(){return y;}
    public int getColor_id(){return color_id;}
    public int getInteraction_radius(){return interaction_radius;}
}
