package dsm;

import java.util.Random;
import java.util.Scanner;

public class Campaign {
    Random rndm;
    Scanner scan;
    int day;
    WeatherGenerator weatherGen;
    Weather weather;
    TimeOfDay time;
    HexMap world;
    Movable partyMovable;

    Campaign(Random rndm, Scanner scan) {
        this.rndm = rndm;
        this.scan = scan;
        this.day = 1;
        this.weatherGen = new WeatherGenerator(rndm);
        this.weather = weatherGen.generate();
        this.time = TimeOfDay.Morning;
        this.world = makeWorldMap();
        this.partyMovable = new Movable("Party", 0, 5);
        this.world.movable.add(partyMovable);
    }

    public void run() {
        wildernessProcedure();
        //call character generation procedure
        
        //call town procedure

        //call hex crawl procedure

        //call dungeon exploration procedure

        //call encounter procedure

        //call combat procedure
    }

    void narrateCurrentHex() {
        Hex currentHex = world.hexAt(partyMovable.east, partyMovable.southeast);
        System.out.println("This area consists of " + currentHex.terrain + ".");
    }

    void wildernessProcedure() {
        if(time == TimeOfDay.Morning) {
            weather = weatherGen.generate();
            System.out.println("It is morning, the weather is " + weather.toString().toLowerCase());
            MultipleChoiceQuestion decision = new MultipleChoiceQuestion();
            int nw = decision.addOption("move northwest.");
            int ne = decision.addOption("move northeast.");
            int e = decision.addOption("move east.");
            int se = decision.addOption("move southeast.");
            int sw = decision.addOption("move southwest.");
            int w = decision.addOption("move west.");
            int ss = decision.addOption("stay still.");
            int choice = decision.ask(scan);
            if(choice == nw) {
                partyMovable.moveNW();
            } else if(choice == ne) {
                partyMovable.moveNE();
            } else if(choice == e) {
                partyMovable.moveE();
            } else if (choice == se) {
                partyMovable.moveSE();
            } else if (choice == sw) {
                partyMovable.moveSW();
            } else if (choice == w) {
                partyMovable.moveW();
            } else if (choice == ss) {
            }
            if(choice != ss) {
                narrateCurrentHex();
            }
        } else if (time == TimeOfDay.Morning) {
            //run afternoon procedure
        } else if (time == TimeOfDay.Morning) {
            //run night procedure
        }
    }

    public static HexMap makeWorldMap() {
        Hex overworldMap[] = new Hex[] {
            new Hex(0, 2, 0, Terrain.woods, "empty"),
            new Hex(1, 3, 0, Terrain.woods, "empty"),
            new Hex(2, 4, 0, Terrain.woods, "empty"),
            new Hex(3, 5, 0, Terrain.mountains, "empty"),
            new Hex(4, 6, 0, Terrain.mountains, "empty"),
            new Hex(5, 1, 1, Terrain.woods, "empty"),
            new Hex(6, 2, 1, Terrain.woods, "Cultist Lair"),
            new Hex(7, 3, 1, Terrain.woods, "empty"),
            new Hex(8, 4, 1, Terrain.mountains, "empty"),
            new Hex(9, 5, 1, Terrain.mountains, "Temple"),
            new Hex(10, 6, 1, Terrain.mountains, "empty"),
            new Hex(11, 0, 2, Terrain.hills, "empty"),
            new Hex(12, 1, 2, Terrain.woods, "empty"),
            new Hex(13, 2, 2, Terrain.woods, "empty"),
            new Hex(14, 3, 2, Terrain.woods, "empty"),
            new Hex(15, 4, 2, Terrain.mountains, "empty"),
            new Hex(16, 5, 2, Terrain.mountains, "Dragon Lair"),
            new Hex(17, 6, 2, Terrain.mountains, "empty"),
            new Hex(18, -1, 3, Terrain.hills, "empty"),
            new Hex(19, 0, 3, Terrain.hills, "empty"),
            new Hex(20, 1, 3, Terrain.hills, "empty"),
            new Hex(21, 2, 3, Terrain.woods, "empty"),
            new Hex(22, 3, 3, Terrain.swamp, "Adamantine Deposit"),
            new Hex(23, 4, 3, Terrain.mountains, "empty"),
            new Hex(24, 5, 3, Terrain.mountains, "empty"),
            new Hex(25, 6, 3, Terrain.woods, "empty"),
            new Hex(26, -2, 4, Terrain.hills, "empty"),
            new Hex(27, -1, 4, Terrain.hills, "empty"),
            new Hex(28, 0, 4, Terrain.hills, "empty"),
            new Hex(29, 1, 4, Terrain.hills, "empty"),
            new Hex(30, 2, 4, Terrain.hills, "empty"),
            new Hex(31, 3, 4, Terrain.swamp, "empty"),
            new Hex(32, 4, 4, Terrain.woods, "empty"),
            new Hex(33, 5, 4, Terrain.woods, "Troglodyte Lair"),
            new Hex(34, 6, 4, Terrain.woods, "empty"),
            new Hex(-35, 2, 5, Terrain.hills, "empty"),
            new Hex(36, -1, 5, Terrain.hills, "empty"),
            new Hex(37, 0, 5, Terrain.hills, "Village"),
            new Hex(38, 1, 5, Terrain.hills, "empty"),
            new Hex(39, 2, 5, Terrain.swamp, "Slith Lair"),
            new Hex(40, 3, 5, Terrain.swamp, "empty"),
            new Hex(41, 4, 5, Terrain.woods, "empty"),
            new Hex(42, 5, 5, Terrain.woods, "empty"),
            new Hex(43, -2, 6, Terrain.swamp, "empty"),
            new Hex(44, -1, 6, Terrain.swamp, "empty"),
            new Hex(45, 0, 6, Terrain.swamp, "empty"),
            new Hex(46, 1, 6, Terrain.swamp, "empty"),
            new Hex(47, 2, 6, Terrain.swamp, "empty"),
            new Hex(48, 3, 6, Terrain.woods, "empty"),
            new Hex(49, 4, 6, Terrain.woods, "empty"),
            new Hex(50, -2, 7, Terrain.woods, "empty"),
            new Hex(51, -1, 7, Terrain.woods, "Bandit Camp"),
            new Hex(52, 0, 7, Terrain.swamp, "empty"),
            new Hex(53, 1, 7, Terrain.swamp, "empty"),
            new Hex(54, 2, 7, Terrain.woods, "Spider Lair"),
            new Hex(55, 3, 7, Terrain.woods, "empty"),
            new Hex(56, -2, 8, Terrain.woods, "empty"),
            new Hex(57, -1, 8, Terrain.woods, "empty"),
            new Hex(58, 0, 8, Terrain.swamp, "empty"),
            new Hex(59, 1, 8, Terrain.woods, "empty"),
            new Hex(60, 2, 8, Terrain.woods, "empty"),
        };

        HexMap overworld = new HexMap(overworldMap);
        return overworld;
    }
}
