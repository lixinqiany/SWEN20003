import bagel.Image;
import bagel.util.Point;
public class Lane {
    private static final double Y = 384;
    private final String direction;
    private final Image LaneDirection;
    private final Point LOCATION;

    public Lane(String direction, double x){
        this.direction = direction;
        this.LaneDirection = new Image(String.format("res/%s.png", direction));
        this.LOCATION = new Point(x, Y);
    }

    public void draw(){
        this.LaneDirection.draw(LOCATION.x, LOCATION.y);
    }

}
