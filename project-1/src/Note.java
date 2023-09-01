import bagel.*;
import bagel.util.Point;

public class Note {
    protected final Image note;
    private Point coordinate;
    private final String direction;
    protected double noteX;
    protected double noteY;
    private double height;

    public Note(String direction, Point coordinate, String type) {
        if (type.equals("Normal")) {
            this.note = new Image(String.format("res/note%s.png", direction));
        } else {
            this.note = new Image(String.format("res/holdNote%s.png", direction));
        }
        this.coordinate = coordinate;
        this.noteX = coordinate.x;
        this.noteY = coordinate.y;
        this.height = this.note.getHeight();
        this.direction = direction;
    }

    /**
     * return ture, when note can be successfully drawn. otherwise, return false.
     */
    public boolean draw(double speed) {
        // check whether `y` is out of window
        double top = noteY - (this.height/2);
        if (top < Window.getHeight()) {
            this.note.draw(noteX, noteY);
            // keep `x`, increment y
            noteY += speed;
            return true;
        } else {
            return false;
        }
    }

    public String getDirection() {
        return this.direction;
    }

    public Point location() {
        return new Point(noteX, noteY);
    }
}
