package dsm;

import java.util.Random;

public class Dice {
    private Random rndm;

    public Dice() {
        rndm = new Random();
    }

    public int d6() {
        int roll = rndm.nextInt(6)+1;
        //System.out.println("You rolled a " + roll + ".");
        return roll;
    }

    public int d20() {
        int roll = rndm.nextInt(20)+1;
        //System.out.println("You rolled a " + roll + ".");
        return roll;
    }
}
