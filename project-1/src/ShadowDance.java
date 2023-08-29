import bagel.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Skeleton Code for SWEN20003 Project 1, Semester 2, 2023
 * Please enter your name below
 * @author Lixinqian YU
 */
public class ShadowDance extends AbstractGame  {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW DANCE";
    private final Image BACKGROUND_IMAGE = new Image("res/background.png");
    private final Font FONT64 = new Font("res/FSO8BITR.TTF", 64);
    private final Font FONT24 = new Font("res/FSO8BITR.TTF", 24);
    private final String START_MESSAGE1 = "PRESS SPACE TO START";
    private final String START_MESSAGE2 = "USE ARROW KEYS TO PLAY";
    private final Lane[] FOUR_LANES = new Lane[4];


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
                switch (type) {
                    case "Lane":
                        switch (typeOfLaneOrNote) {
                            case "Left":
                                FOUR_LANES[numOfLane] = new Lane("lane"+typeOfLaneOrNote, x);
                                break;
                            case "Right":
                                FOUR_LANES[numOfLane] = new Lane("lane"+typeOfLaneOrNote, x);
                                break;
                            case "Up":
                                FOUR_LANES[numOfLane] = new Lane("lane"+typeOfLaneOrNote, x);
                                break;
                            case "Down":
                                FOUR_LANES[numOfLane] = new Lane("lane"+typeOfLaneOrNote, x);
                                break;
                        }
                        break;
                    case "Left":
                        break;
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
            BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
            for(Lane i : FOUR_LANES) {
                i.draw();
            }

        } else {
            BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
            FONT64.drawString(GAME_TITLE,220,250);
            FONT24.drawString(START_MESSAGE1,220+100,250+190);
            FONT24.drawString(START_MESSAGE2,220+100,250+190+24);


        }

        //BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

    }
}
