package dsm;

import java.util.ArrayList;
import java.util.Scanner;

public class Campaign {

    //this is where the overworld hex map is defined
    public static HexMap makeWorldMap() {
        Hex overworldMap[] = new Hex[] {
            new Hex(2, 0, Terrain.Woods, null),
            new Hex(3, 0, Terrain.Woods, null),
            new Hex(4, 0, Terrain.Woods, null),
            new Hex(5, 0, Terrain.Mountains, null),
            new Hex(6, 0, Terrain.Mountains, null),
            new Hex(1, 1, Terrain.Woods, null),
            new Hex(2, 1, Terrain.Woods, "Cultist Lair"),
            new Hex(3, 1, Terrain.Woods, null),
            new Hex(4, 1, Terrain.Mountains, null),
            new Hex(5, 1, Terrain.Mountains, "Temple"),
            new Hex(6, 1, Terrain.Mountains, null),
            new Hex(0, 2, Terrain.Hills, null),
            new Hex(1, 2, Terrain.Woods, null),
            new Hex(2, 2, Terrain.Woods, null),
            new Hex(3, 2, Terrain.Woods, null),
            new Hex(4, 2, Terrain.Mountains, null),
            new Hex(5, 2, Terrain.Mountains, "Dragon Lair"),
            new Hex(6, 2, Terrain.Mountains, null),
            new Hex(-1, 3, Terrain.Hills, null),
            new Hex(0, 3, Terrain.Hills, null),
            new Hex(1, 3, Terrain.Hills, null),
            new Hex(2, 3, Terrain.Woods, null),
            new Hex(3, 3, Terrain.Swamp, "Adamantite Deposit"),
            new Hex(4, 3, Terrain.Mountains, null),
            new Hex(5, 3, Terrain.Mountains, null),
            new Hex(6, 3, Terrain.Woods, null),
            new Hex(-2, 4, Terrain.Hills, null),
            new Hex(-1, 4, Terrain.Hills, null),
            new Hex(0, 4, Terrain.Hills, null),
            new Hex(1, 4, Terrain.Hills, null),
            new Hex(2, 4, Terrain.Hills, null),
            new Hex(3, 4, Terrain.Swamp, null),
            new Hex(4, 4, Terrain.Woods, null),
            new Hex(5, 4, Terrain.Woods, "Troglodyte Lair"),
            new Hex(6, 4, Terrain.Woods, null),
            new Hex(2, 5, Terrain.Hills, null),
            new Hex(-1, 5, Terrain.Hills, null),
            new Hex(0, 5, Terrain.Hills, "Village"),
            new Hex(1, 5, Terrain.Hills, null),
            new Hex(2, 5, Terrain.Swamp, "Slith Lair"),
            new Hex(3, 5, Terrain.Swamp, null),
            new Hex(4, 5, Terrain.Woods, null),
            new Hex(5, 5, Terrain.Woods, null),
            new Hex(-2, 6, Terrain.Swamp, null),
            new Hex(-1, 6, Terrain.Swamp, null),
            new Hex(0, 6, Terrain.Swamp, null),
            new Hex(1, 6, Terrain.Swamp, null),
            new Hex(2, 6, Terrain.Swamp, null),
            new Hex(3, 6, Terrain.Woods, null),
            new Hex(4, 6, Terrain.Woods, null),
            new Hex(-2, 7, Terrain.Woods, null),
            new Hex(-1, 7, Terrain.Woods, "Bandit Camp"),
            new Hex(0, 7, Terrain.Swamp, null),
            new Hex(1, 7, Terrain.Swamp, null),
            new Hex(2, 7, Terrain.Woods, "Spider Lair"),
            new Hex(3, 7, Terrain.Woods, null),
            new Hex(-2, 8, Terrain.Woods, null),
            new Hex(-1, 8, Terrain.Woods, null),
            new Hex(0, 8, Terrain.Swamp, null),
            new Hex(1, 8, Terrain.Woods, null),
            new Hex(2, 8, Terrain.Woods, null),
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

    //this is a list of the weapons and armours available in the campaign
    WeaponType[] weapons = new WeaponType[] {
        new WeaponType(WeaponName.None, 2, 2, 2),
        new WeaponType(WeaponName.Dagger, 2, 3, 4),
        new WeaponType(WeaponName.Spear1h, 2, 4, 6),
        new WeaponType(WeaponName.Sword, 2, 4, 6),
        new WeaponType(WeaponName.Sling, 2, 4, 6),
        new WeaponType(WeaponName.Spear2h, 2, 5, 7),
        new WeaponType(WeaponName.Battleaxe, 2, 5, 7),
        new WeaponType(WeaponName.Bow, 2, 5, 7)
    };
    ArmourType[] armour = new ArmourType[] {
        new ArmourType(ArmourName.Helmet, 2),
        new ArmourType(ArmourName.Breastplate, 2),
        new ArmourType(ArmourName.Pauldrons, 1),
        new ArmourType(ArmourName.Greaves, 1),
    };

    //this is where the party is defined
    Character[] party = new Character[] {
        new Character("Filinus", Profession.Sage),
        new Character("Mysto", Profession.Shaman),
        new Character("Stoop", Profession.Thief),
        new Character("Brutus", Profession.Warrior)
    };

    void tellTerrain() {
        Hex currentHex = world.hexAt(partyCoordinate.east, partyCoordinate.southeast);
        System.out.print("This area consists of " + currentHex.terrain.toString().toLowerCase());
        if(currentHex.landmark != null) {
            System.out.print(", there is a " + currentHex.landmark + " in this area.");
        } else {
            System.out.print(".");
        };
        System.out.print("\n");
    }

    //this is where the game asks the player which direction they want to travel when hex-crawling

    //if the player chooses to stay still, then stay still
    //otherwise, roll to see if the party gets lost
    //if they don't get lost, respect their decision
    //if they get lost, override their decision with another non stay still option at random

    void hexTravel() {
        tellTerrain();
        MultipleChoiceQuestion directionQuestion = new MultipleChoiceQuestion();
        for(int i=0; i<Coordinate.directionNames.length; i++) {
            if(world.hexAtMaybe(partyCoordinate.plus(Coordinate.DIRECTIONS[i])) != null) {
                directionQuestion.addOption("Move " + Coordinate.directionNames[i] + " into " + world.hexAt(partyCoordinate.plus(Coordinate.DIRECTIONS[i])).terrain + ".");
            } else {
                directionQuestion.skipOption();
            }
        };
        int ss = directionQuestion.addOption("Stay still in " + world.hexAt(partyCoordinate).terrain.toString().toLowerCase() + ".");
        int directionChoice = directionQuestion.ask(scan);

        int navigationChances;
        if (directionChoice == ss) {
            tellTerrain();
            return;
        } else if(world.hexAt(partyCoordinate).terrain == Terrain.Hills) {
            navigationChances = 5;
        } else if(world.hexAt(partyCoordinate).terrain == Terrain.Mountains) {
            navigationChances = 4;
        } else if(world.hexAt(partyCoordinate).terrain == Terrain.Swamp) {
            navigationChances = 4;
        } else if(world.hexAt(partyCoordinate).terrain == Terrain.Woods) {
            navigationChances = 3;
        } else {
            throw new RuntimeException("terrain not supported");
        }

        int navigationRoll = dice.d6();
        if(navigationRoll <= navigationChances) {
            partyCoordinate.move(Coordinate.DIRECTIONS[directionChoice]);
        } else {
            System.out.println("You got lost.");
            if(dice.d6()<4) {
                directionChoice = directionChoice +1;
                partyCoordinate.move(Coordinate.DIRECTIONS[directionChoice]);
            } else {
                directionChoice = directionChoice -1;
                partyCoordinate.move(Coordinate.DIRECTIONS[directionChoice]);
            }
        }
    }

    public void run() {
        while(true) {
            //call character generation procedure

            //call town procedure

            wildernessProcedure();

            //call dungeon exploration procedure

            //call encounter procedure

            //call combat procedure
        }
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
        System.out.println("It is " + time.toString().toLowerCase() + " of day " + day + ", the weather is " + weather.toString().toLowerCase() + ".");
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
