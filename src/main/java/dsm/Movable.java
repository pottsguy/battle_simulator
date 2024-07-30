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
    public void moveRandom(Random rndm) {
        this.east = east + rndm.nextInt(2)-1;
        this.southeast = southeast + rndm.nextInt(2)-1;
    }
}