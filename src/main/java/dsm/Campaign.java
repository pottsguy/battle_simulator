package dsm;

import java.util.ArrayList;
import java.util.Scanner;

public class Campaign {
    public static HexMap makeWorldMap() {
        Hex overworldMap[] = new Hex[] {
            new Hex(0, 2, 0, Terrain.Woods, "empty"),
            new Hex(1, 3, 0, Terrain.Woods, "empty"),
            new Hex(2, 4, 0, Terrain.Woods, "empty"),
            new Hex(3, 5, 0, Terrain.Mountains, "empty"),
            new Hex(4, 6, 0, Terrain.Mountains, "empty"),
            new Hex(5, 1, 1, Terrain.Woods, "empty"),
            new Hex(6, 2, 1, Terrain.Woods, "Cultist Lair"),
            new Hex(7, 3, 1, Terrain.Woods, "empty"),
            new Hex(8, 4, 1, Terrain.Mountains, "empty"),
            new Hex(9, 5, 1, Terrain.Mountains, "Temple"),
            new Hex(10, 6, 1, Terrain.Mountains, "empty"),
            new Hex(11, 0, 2, Terrain.Hills, "empty"),
            new Hex(12, 1, 2, Terrain.Woods, "empty"),
            new Hex(13, 2, 2, Terrain.Woods, "empty"),
            new Hex(14, 3, 2, Terrain.Woods, "empty"),
            new Hex(15, 4, 2, Terrain.Mountains, "empty"),
            new Hex(16, 5, 2, Terrain.Mountains, "Dragon Lair"),
            new Hex(17, 6, 2, Terrain.Mountains, "empty"),
            new Hex(18, -1, 3, Terrain.Hills, "empty"),
            new Hex(19, 0, 3, Terrain.Hills, "empty"),
            new Hex(20, 1, 3, Terrain.Hills, "empty"),
            new Hex(21, 2, 3, Terrain.Woods, "empty"),
            new Hex(22, 3, 3, Terrain.Swamp, "Adamantine Deposit"),
            new Hex(23, 4, 3, Terrain.Mountains, "empty"),
            new Hex(24, 5, 3, Terrain.Mountains, "empty"),
            new Hex(25, 6, 3, Terrain.Woods, "empty"),
            new Hex(26, -2, 4, Terrain.Hills, "empty"),
            new Hex(27, -1, 4, Terrain.Hills, "empty"),
            new Hex(28, 0, 4, Terrain.Hills, "empty"),
            new Hex(29, 1, 4, Terrain.Hills, "empty"),
            new Hex(30, 2, 4, Terrain.Hills, "empty"),
            new Hex(31, 3, 4, Terrain.Swamp, "empty"),
            new Hex(32, 4, 4, Terrain.Woods, "empty"),
            new Hex(33, 5, 4, Terrain.Woods, "Troglodyte Lair"),
            new Hex(34, 6, 4, Terrain.Woods, "empty"),
            new Hex(-35, 2, 5, Terrain.Hills, "empty"),
            new Hex(36, -1, 5, Terrain.Hills, "empty"),
            new Hex(37, 0, 5, Terrain.Hills, "Village"),
            new Hex(38, 1, 5, Terrain.Hills, "empty"),
            new Hex(39, 2, 5, Terrain.Swamp, "Slith Lair"),
            new Hex(40, 3, 5, Terrain.Swamp, "empty"),
            new Hex(41, 4, 5, Terrain.Woods, "empty"),
            new Hex(42, 5, 5, Terrain.Woods, "empty"),
            new Hex(43, -2, 6, Terrain.Swamp, "empty"),
            new Hex(44, -1, 6, Terrain.Swamp, "empty"),
            new Hex(45, 0, 6, Terrain.Swamp, "empty"),
            new Hex(46, 1, 6, Terrain.Swamp, "empty"),
            new Hex(47, 2, 6, Terrain.Swamp, "empty"),
            new Hex(48, 3, 6, Terrain.Woods, "empty"),
            new Hex(49, 4, 6, Terrain.Woods, "empty"),
            new Hex(50, -2, 7, Terrain.Woods, "empty"),
            new Hex(51, -1, 7, Terrain.Woods, "Bandit Camp"),
            new Hex(52, 0, 7, Terrain.Swamp, "empty"),
            new Hex(53, 1, 7, Terrain.Swamp, "empty"),
            new Hex(54, 2, 7, Terrain.Woods, "Spider Lair"),
            new Hex(55, 3, 7, Terrain.Woods, "empty"),
            new Hex(56, -2, 8, Terrain.Woods, "empty"),
            new Hex(57, -1, 8, Terrain.Woods, "empty"),
            new Hex(58, 0, 8, Terrain.Swamp, "empty"),
            new Hex(59, 1, 8, Terrain.Woods, "empty"),
            new Hex(60, 2, 8, Terrain.Woods, "empty"),
        };
        HexMap overworld = new HexMap(overworldMap);
        return overworld;
    }

    Dice dice;
    Scanner scan;
    int day;
    WeatherGenerator weatherGen;
    Weather weather;
    TimeOfDay time;
    HexMap world;
    Coordinate partyCoordinate;

    Campaign(Dice dice, Scanner scan) {
        this.dice = dice;
        this.scan = scan;
        this.day = 1;
        this.weatherGen = new WeatherGenerator(dice);
        this.time = TimeOfDay.Morning;
        this.world = makeWorldMap();
        this.partyCoordinate = new Coordinate(0, 5);
        this.world.coordinate.add(partyCoordinate);
    }

    public void run() {
        while(true) {
            wildernessProcedure();

            //call character generation procedure
        
            //call town procedure

            //call hex crawl procedure

            //call dungeon exploration procedure

            //call encounter procedure

            //call combat procedure
        }
    }

    void tellTerrain() {
        Hex currentHex = world.hexAt(partyCoordinate.east, partyCoordinate.southeast);
        System.out.println("This area consists of " + currentHex.terrain.toString().toLowerCase() + ".");
    }

    void hexTravel() {
        MultipleChoiceQuestion directionQuestion = new MultipleChoiceQuestion();
        for(int i=0; i<Coordinate.directionNames.length; i++) {
            directionQuestion.addOption("Move " + Coordinate.directionNames[i] + " into " + world.hexAt(partyCoordinate.plus(Coordinate.DIRECTIONS[i])).terrain + ".");
        };
        int ss = directionQuestion.addOption("Stay still in " + world.hexAt(partyCoordinate).terrain.toString().toLowerCase() + ".");
        int directionChoice = directionQuestion.ask(scan);

        int navigationChances;
        if (directionChoice == ss) {
            tellTerrain();
            return;
        } else if(world.hexAt(partyCoordinate).terrain == Terrain.Hills && weather == Weather.Clear) {
            navigationChances = 5;
        } else if(world.hexAt(partyCoordinate).terrain == Terrain.Hills && weather == Weather.Rainy) {
            navigationChances = 5;
        } else if(world.hexAt(partyCoordinate).terrain == Terrain.Hills && weather == Weather.Stormy) {
            navigationChances = 5;
        } else if(world.hexAt(partyCoordinate).terrain == Terrain.Mountains && weather == Weather.Clear) {
            navigationChances = 4;
        } else if(world.hexAt(partyCoordinate).terrain == Terrain.Mountains && weather == Weather.Rainy) {
            navigationChances = 4;
        } else if(world.hexAt(partyCoordinate).terrain == Terrain.Mountains && weather == Weather.Stormy) {
            navigationChances = 4;
        } else if(world.hexAt(partyCoordinate).terrain == Terrain.Swamp && weather == Weather.Clear) {
            navigationChances = 4;
        } else if(world.hexAt(partyCoordinate).terrain == Terrain.Swamp && weather == Weather.Rainy) {
            navigationChances = 4;
        } else if(world.hexAt(partyCoordinate).terrain == Terrain.Swamp && weather == Weather.Stormy) {
            navigationChances = 4;
        } else if(world.hexAt(partyCoordinate).terrain == Terrain.Woods && weather == Weather.Clear) {
            navigationChances = 3;
        } else if(world.hexAt(partyCoordinate).terrain == Terrain.Woods && weather == Weather.Rainy) {
            navigationChances = 3;
        } else if(world.hexAt(partyCoordinate).terrain == Terrain.Woods && weather == Weather.Stormy) {
            navigationChances = 3;
        } else {
            throw new RuntimeException("weather/terrain combo not supported");
        }

        int navigationRoll = dice.d6();
        if(navigationRoll <= navigationChances) {
            partyCoordinate.move(Coordinate.DIRECTIONS[directionChoice]);
        } else {
            System.out.println("You got lost.");
            partyCoordinate.move(Coordinate.DIRECTIONS[dice.d6()]);
        }
        tellTerrain();
    }

    private void randomEncounter() {
        int encounterRoll=dice.d6();
        int encounterChances=1;
        if(encounterRoll<=encounterChances) {
            System.out.println("A random encounter ensues.");
            ArrayList<Combatant> battlefield = new ArrayList<>();
            battlefield.add(new Combatant(Team.Ally, "Knight 1", Rank.Vanguard));
            battlefield.add(new Combatant(Team.Ally, "Knight 2", Rank.Vanguard));
            battlefield.add(new Combatant(Team.Ally, "Spearman 1", Rank.Rear));
            battlefield.add(new Combatant(Team.Ally, "Spearman 2", Rank.Rear));
            battlefield.add(new Combatant(Team.Ally, "Archer 1", Rank.Artillery));
            battlefield.add(new Combatant(Team.Ally, "Archer 2", Rank.Artillery));
            battlefield.add(new Combatant(Team.Enemy, "Trorc 1", Rank.Vanguard));
            battlefield.add(new Combatant(Team.Enemy, "Trorc 2", Rank.Vanguard));
            battlefield.add(new Combatant(Team.Enemy, "Trobgoblin 1", Rank.Rear));
            battlefield.add(new Combatant(Team.Enemy, "Trobgoblin 2", Rank.Rear));
            battlefield.add(new Combatant(Team.Enemy, "Trobold 1", Rank.Artillery));
            battlefield.add(new Combatant(Team.Enemy, "Trobold 2", Rank.Artillery));
            Battle.battleMain(dice, scan, battlefield);
        }
    }

    private void survival() {
        int forageChances;
        int fireChances;
        if (world.hexAt(partyCoordinate).terrain == Terrain.Hills && weather == Weather.Clear) {
            forageChances = 2;
            fireChances = 6;
        } else if (world.hexAt(partyCoordinate).terrain == Terrain.Hills && weather == Weather.Rainy) {
            forageChances = 1;
            fireChances = 3;
        } else if (world.hexAt(partyCoordinate).terrain == Terrain.Hills && weather == Weather.Stormy) {
            forageChances = 0;
            fireChances = 0;
        } else if (world.hexAt(partyCoordinate).terrain == Terrain.Mountains && weather == Weather.Clear) {
            forageChances = 1;
            fireChances = 5;
        } else if (world.hexAt(partyCoordinate).terrain == Terrain.Mountains && weather == Weather.Rainy) {
            forageChances = 0;
            fireChances = 2;
        } else if (world.hexAt(partyCoordinate).terrain == Terrain.Mountains && weather == Weather.Stormy) {
            forageChances = 0;
            fireChances = 0;
        } else if (world.hexAt(partyCoordinate).terrain == Terrain.Swamp && weather == Weather.Clear) {
            forageChances = 3;
            fireChances = 4;
        } else if (world.hexAt(partyCoordinate).terrain == Terrain.Swamp && weather == Weather.Rainy) {
            forageChances = 2;
            fireChances = 1;
        } else if (world.hexAt(partyCoordinate).terrain == Terrain.Swamp && weather == Weather.Stormy) {
            forageChances = 1;
            fireChances = 0;
        } else if (world.hexAt(partyCoordinate).terrain == Terrain.Woods && weather == Weather.Clear) {
            forageChances = 3;
            fireChances = 6;
        } else if (world.hexAt(partyCoordinate).terrain == Terrain.Woods && weather == Weather.Rainy) {
            forageChances = 4;
            fireChances = 4;
        } else if (world.hexAt(partyCoordinate).terrain == Terrain.Woods && weather == Weather.Stormy) {
            forageChances = 3;
            fireChances = 2;
        } else {
            throw new RuntimeException("weather/terrain combo not supported");
        }

        int forageRoll=dice.d6();
        if(forageRoll < forageChances) {
            System.out.println("You found a mushrooms.");
        }

        int fireRoll=dice.d6();
        if(fireRoll < fireChances) {
            System.out.println("You lit a fire.");
        } else {
            System.out.println("You didn't manage to light a fire.");
        }
    }

    private void tellDay() {
        System.out.println("It is " + time.toString().toLowerCase() + " of day " + day + ", the weather is " + weather.toString().toLowerCase() + ", and you are in the " + world.hexAt(partyCoordinate).terrain.toString().toLowerCase() + ".");
    }
    
    private void wildernessProcedure() {
        if(time == TimeOfDay.Morning) {
            weather = weatherGen.generate();
            tellDay();
            hexTravel();
            randomEncounter();
            time = TimeOfDay.Afternoon;
        } else if (time == TimeOfDay.Afternoon) {
            tellDay();
            hexTravel();
            randomEncounter();
            time = TimeOfDay.Night;
        } else if (time == TimeOfDay.Night) {
            tellDay();
            survival();
            randomEncounter();
            day ++;
            time = TimeOfDay.Morning;
        }
    }
}
