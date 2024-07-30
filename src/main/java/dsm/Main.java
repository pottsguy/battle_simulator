package dsm;

import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String args[]) {

        Dice dice = new Dice();
        Scanner scan = new Scanner(System.in);

        System.out.println("Welcome, daring mercenary, to the subterranean realm!");
        Campaign myCampaign = new Campaign(dice, scan);
        myCampaign.run();
    }
}
