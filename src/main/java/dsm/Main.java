package dsm;

import java.util.Scanner;

public class Main {

    public static void mainTest(String args[]) {
        Scanner scan = new Scanner(System.in);
        Dice dice = new Dice();
        Character testChar = new Character(dice, scan);
    }

    public static void main(String args[]) {

        int remainder;
        for(int i=-10; i<11; i++) {
            remainder = i % 6;
            if(remainder < 0) {
                remainder = remainder+6;
            }
            System.out.println(i + "%" + 6 + "=" + remainder);
        };


        Dice dice = new Dice();
        Scanner scan = new Scanner(System.in);

        System.out.println("Welcome, daring mercenary, to the subterranean realm!");
        Campaign myCampaign = new Campaign(dice, scan);
        myCampaign.run();
    }
}
