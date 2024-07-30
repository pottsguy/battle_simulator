package dsm;

import java.util.Random;

class Movable {
    private String name;
    int east;
    int southeast;

    Movable(String name, int east, int southeast) {
        this.name = name;
        this.east = east;
        this.southeast = southeast;
    }

    public String getName() {
        return name;
    }
    public void moveNW() {
        this.southeast --;
    }
    public void moveNE() {
        this.southeast --;
        this.east ++;
    }
    public void moveE() {
        this.east ++;
    }
    public void moveSE() {
        this.southeast ++;
    }
    public void moveSW() {
        this.southeast ++;
        this.east --;
    }
    public void moveW() {
        this.east --;
    }
    public void moveRandom(Dice dice) {
        int randomDirection[][] = {
            {1, -1}, //northeast
            {1,  0}, //east
            {0,  1}, //southeast
            {-1, 1}, //southwest
            {-1, 0}, //west
            {0, -1}, //northwest
        };
        int directionRoll = dice.d6();
        int row[] = randomDirection[directionRoll-1];
        east = east+row[0];
        southeast = southeast+row[1];
    }
}