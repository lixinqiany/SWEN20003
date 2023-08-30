import bagel.*;
import bagel.util.Point;

public class Note {
    private final Image note;
    private Point coordinate;
    double noteX;
    double noteY;

    public Note(String direction, Point coordinate) {
        this.note = new Image(String.format("res/note%s.png", direction));
        this.coordinate = coordinate;
        this.noteX = coordinate.x;
        this.noteY = coordinate.y;
    }

    /**
     * return ture, when note can be successfully drawn. otherwise, return false.
     */
    public boolean draw(double speed) {

        // check whether `y` is out of window
        if (noteY < Window.getHeight()) {
            this.note.draw(noteX, noteY);
            // keep `x`, increment y
            noteY += 2;
            return true;
        } else {
            return false;
        }
    }
}
