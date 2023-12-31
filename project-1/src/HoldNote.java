import bagel.Window;
import bagel.util.Point;

/**
 * inherit from `Note` for process actions relevant to `Holding`
 * @author Lixinqian YU
 */
public class HoldNote extends Note {
    private final static String type = "Hold";

    public HoldNote(String direction, Point coordinate, String type) {
        super(direction, coordinate, type);
    }


}
