import bagel.*;
import bagel.util.Point;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Skeleton Code for SWEN20003 Project 1, Semester 2, 2023
 * Please enter your name below
 * @author Lixinqian YU
 */
public class ShadowDance extends AbstractGame  {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW DANCE";
    private final static double START_Y_NOTE = 100;
    private final static double STEP_SIZE = 2;
    private final Image BACKGROUND_IMAGE = new Image("res/background.png");
    private final Font FONT64 = new Font("res/FSO8BITR.TTF", 64);
    private final Font FONT24 = new Font("res/FSO8BITR.TTF", 24);
    private final Font FONT30 = new Font("res/FSO8BITR.TTF", 30);
    private final Font FONT40 = new Font("res/FSO8BITR.TTF", 40);
    private final String START_MESSAGE1 = "PRESS SPACE TO START";
    private final String START_MESSAGE2 = "USE ARROW KEYS TO PLAY";
    private final String SCORE_MESSAGE = "SCORE ";
    private final Lane[] FOUR_LANES = new Lane[4];
    private ArrayList<String> notes = new ArrayList<String>();
    private ArrayList<String> actions = new ArrayList<String>();
    private ArrayList<Double> noteTime = new ArrayList<Double>();
    private ArrayList<Point> coordinate = new ArrayList<Point>();
    private Image noteLeft = new Image("res/noteLeft.png");
    private static double frame = 0;
    private int textFrame = 0;
    private static double yyy=100;
    private static int LEFT;
    private static int RIGHT;
    private static int UP;
    private static int DOWN;
    private final static int GOOD = 5;
    private final static int PERFECT = 10;
    private final static int BAD = -1;
    private final static int MISS = -5;
    private int currentScore = 0;
    private int grade = 0;
    private ArrayList<Note> waitDrawn = new ArrayList<Note>();
    private ArrayList<Note> waitDeleted = new ArrayList<Note>();

    // judge the stage of the game, involving "START", "GAMING"
    private String flag = "START";

    public ShadowDance(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }

    /**
     * Method used to read file and create objects (you can change
     * this method as you wish).
     */
    private void readCSV() {
        try(BufferedReader br = new BufferedReader(new FileReader("res/level1.csv"))) {
            String line;
            int numOfLane = 0;

            while((line = br.readLine()) != null) {
                String[] item = line.split(",");
                String type = item[0];
                String typeOfLaneOrNote = item[1];
                double x = Double.parseDouble(item[2]);

                if (type.equals("Lane")) {
                    if  (typeOfLaneOrNote.equals("Left") || typeOfLaneOrNote.equals("Right") || typeOfLaneOrNote.equals("Down") || typeOfLaneOrNote.equals("Up")){
                        FOUR_LANES[numOfLane++] = new Lane("lane"+typeOfLaneOrNote, x);
                        switch (typeOfLaneOrNote) {
                            case "Left":
                                LEFT = (int)x;
                                break;
                            case "Right":
                                RIGHT = (int)x;
                                break;
                            case "Up":
                                UP = (int)x;
                                break;
                            case "Down":
                                DOWN = (int)x;
                                break;
                            default:
                                System.out.println("No x coordinate for Lane.");
                        }
                    } else {
                        System.out.println("Invalid direction for Lane!");
                    }
                } else if (type.equals("Left") || type.equals("Right") || type.equals("Down") || type.equals("Up")){                                                                                                             
                    notes.add(type);
                    actions.add(typeOfLaneOrNote);
                    noteTime.add(x);
                    switch (type) {
                        case "Left":
                            coordinate.add(new Point(LEFT, START_Y_NOTE));
                            break;
                        case "Right":
                            coordinate.add(new Point(RIGHT, START_Y_NOTE));
                            break;
                        case "Up":
                            coordinate.add(new Point(UP, START_Y_NOTE));
                            break;
                        case "Down":
                            coordinate.add(new Point(DOWN, START_Y_NOTE));
                            break;
                        default:
                            System.out.println("No x coordinate for note.");
                    }
                } else {
                    System.out.println("Invalid record in " + "res/level1.csv");
                }                                                                                                                                                                          

            }
        } catch (FileNotFoundException e) {
            System.err.println("File not exist : " + e.getMessage() + "\n");
        } catch (Exception e) {
            System.err.println("Error : " + e.getMessage() + "\n");
        }
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDance game = new ShadowDance();
        game.readCSV();
        game.run();
    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {
        String key;

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        if (input.wasPressed(Keys.SPACE)){
            flag = "GAMING";
        }

        if (this.flag.equals("GAMING")) {
            updateGaming(this.currentScore);

            // detect whether there is a note at current frame.
            if (noteTime.contains(frame)) {
                // if there is a frame, add it into `waitDrawn`.
                int index = noteTime.indexOf(frame);
                String type = actions.get(index);
                // if note is normal, use `Note` class.
                if (type.equals("Normal")) {
                    Note note = new Note(notes.get(index), coordinate.get(index), type);
                    waitDrawn.add(note);
                } else  {
                    Note note = new Note(notes.get(index), coordinate.get(index), type);
                    waitDrawn.add(note);
                }

            }

            // read each note in `waitDrawn`
            drawGaming();
            // score part
            key = detectPress(input);
            if (! key.equals("NO")) {
                this.grade = score(waitDrawn, key);
                this.currentScore += grade;
                textFrame =0;
            }
            if (grade != 0) {
                if (textFrame < 30) {
                    textScore(grade);
                    System.out.println(textFrame);
                    textFrame++;
                }
            }


            waitDeleted.forEach((e) -> {
                waitDrawn.remove(e);
            });

            //System.out.println("frame="+frame);
            frame++;
        } else {
            updateInitial();
        }
    }

    public void updateInitial() {
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        FONT64.drawString(GAME_TITLE,220,250);
        FONT24.drawString(START_MESSAGE1,220+100,250+190);
        FONT24.drawString(START_MESSAGE2,220+100,250+190+24);
    }

    public void updateGaming(int score) {
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        for(Lane i : FOUR_LANES) {
            i.draw();
        }
        FONT30.drawString(SCORE_MESSAGE+score, 35, 35);
    }

    public void drawGaming() {
        // read each note in `waitDrawn`
        waitDrawn.forEach((e) -> {
            // System.out.println(e);
            if (! e.draw(STEP_SIZE)) {
                waitDeleted.add(e);
                this.grade = MISS;
                textFrame = 0;
                this.currentScore += MISS;
                textScore(MISS);
            }
        });
    }

    public String detectPress(Input input) {
        String key = "NO";
        if (input.wasPressed(Keys.LEFT)) {
            //System.out.println("left!");
            key = "Left";
        } else if (input.wasPressed(Keys.RIGHT)) {
            //System.out.println("right!");
            key = "Right";
        } else if (input.wasPressed(Keys.DOWN)) {
            //System.out.println("down!");
            key = "Down";
        } else if (input.wasPressed(Keys.UP)) {
            //System.out.println("up!");
            key = "Up";
        } else {
            return key;
        }
        return key;
    }

    public int score(ArrayList<Note> waitDrawn, String key){
        ArrayList<Note> left = new ArrayList<Note>();
        ArrayList<Note> right = new ArrayList<Note>();
        ArrayList<Note> down = new ArrayList<Note>();
        ArrayList<Note> up = new ArrayList<Note>();

        // Point location = new Point();
        // double distance;
        int finalScore = 0;
        waitDrawn.forEach((e) -> {
            switch (e.getDirection()) {
                case "Left":
                    left.add(e);
                    break;
                case "Right":
                    right.add(e);
                    break;
                case "Down":
                    down.add(e);
                    break;
                case "Up":
                    up.add(e);
                    break;
                default:
                    break;
            }
        });

        if (key.equals("Left")) {
            if (left.size() > 0) {
                finalScore = checkScore(left, LEFT);
            }
        } else if (key.equals("Right")) {
            if (right.size() > 0) {
                finalScore = checkScore(right, RIGHT);
            }
        } else if (key.equals("Down")) {
            if (down.size() > 0) {
                finalScore = checkScore(down, DOWN);
            }
        } else if (key.equals("Up")) {
            if (up.size() > 0) {
                finalScore = checkScore(up, UP);
            }
        }
        return finalScore;
    }

    //helper function for scoring
    private int checkScore(ArrayList<Note> lane, int laneX) {
        Point location = new Point();
        double distance;
        int score = 0;

        location = lane.get(0).location();
        distance = location.distanceTo(new Point (laneX, 657));
        if (distance <= 15 ) {
            score = PERFECT;
            System.out.println("perfect");
        } else if (distance<=50) {
            score = GOOD;
            System.out.println("good");
        } else if (distance<=100) {
            score = BAD;
            System.out.println("bad");
        } else if (distance<=200) {
            score = MISS;
            System.out.println("miss");
        } else {
            System.out.println("200");
        }

        if (score != 0) {
            // when the score is calculated, don't draw the note
            waitDrawn.remove(lane.get(0));
        }
        return score;
    }

    // helper function for rendering score's text
    private void textScore(int grade) {
        String text;
        double widthFont;
        double x;
        double y;
        switch (grade) {
            case PERFECT:
                text = "PERFECT";
                break;
            case GOOD:
                text = "GOOD";
                break;
            case BAD:
                text = "BAD";
                break;
            case MISS:
            default:
                text = "MISS";
                break;
        }
        widthFont = FONT40.getWidth(text);
        x = Window.getWidth()/2.0 - widthFont/2.0;
        y = Window.getHeight()/2.0 + 40/2.0;
        FONT40.drawString(text, x, y);
    }
}
