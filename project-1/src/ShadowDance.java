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
    private final String START_MESSAGE1 = "PRESS SPACE TO START";
    private final String START_MESSAGE2 = "USE ARROW KEYS TO PLAY";
    private final Lane[] FOUR_LANES = new Lane[4];
    private ArrayList<String> notes = new ArrayList<String>();
    private ArrayList<String> actions = new ArrayList<String>();
    private ArrayList<Double> noteX = new ArrayList<Double>();
    private ArrayList<Point> coordinate = new ArrayList<Point>();
    private Image noteLeft = new Image("res/noteLeft.png");
    private static double frame = 0;
    private static double yyy=100;
    private static int LEFT;
    private static int RIGHT;
    private static int UP;
    private static int DOWN;
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
                    noteX.add(x);
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

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        if (input.wasPressed(Keys.SPACE)){
            flag = "GAMING";
        }

        if (this.flag.equals("GAMING")) {
            updateGaming();

            // detect whether there is a note at current frame.
            if (noteX.contains(frame)) {
                // if there is a frame, add it into `waitDrawn`.
                int index = noteX.indexOf(frame);
                Note note = new Note(notes.get(index), coordinate.get(index));
                waitDrawn.add(note);
                System.out.println(1);
            }

            // read each note in `waitDrawn`
            waitDrawn.forEach((e) -> {
                // System.out.println(e);
               if (! e.draw(STEP_SIZE)) {
                   waitDeleted.add(e);
                   System.out.println(1);
               }
            });

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

    public void updateGaming() {
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        for(Lane i : FOUR_LANES) {
            i.draw();
        }
    }
}
