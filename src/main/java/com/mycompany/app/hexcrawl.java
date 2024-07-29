package com.mycompany.app;

import java.util.ArrayList;

enum Terrain {
    hills, woods, mountains, swamp
}

class Hex {
    int id;
    int east;
    int southeast;
    Terrain terrain;
    String landmark;

    Hex(int id, int east, int southeast, Terrain terrain, String landmark) {
        this.id = id;
        this.east = east;
        this.southeast = southeast;
        this.terrain = terrain;
        this.landmark = landmark;
    }

    public void printDescription() {
        System.out.println("This hex's terrain is " + this.terrain + ".");
        System.out.println("This hex's landmark is " + this.landmark + ".");
    }
}

class Movable {
    String name;
    int east;
    int southeast;

    Movable(String name, int east, int southeast) {
        this.name = name;
        this.east = east;
        this.southeast = southeast;
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
}

class HexMap {
    Hex hex[];
    ArrayList<Movable> movable;

    HexMap(Hex hex[]){
        this.hex = hex;
        this.movable = new ArrayList<>();
    }

    public Hex hexQuery(int queryE, int querySE) {
        for (int i=0; i<this.hex.length; i++) {
            if (this.hex[i].east == queryE && this.hex[i].southeast == querySE) {
                return hex[i];
            }
        }
        throw new RuntimeException("There is no hex at " + queryE + ", " + querySE);
    }
}

public class hexcrawl {
    
    public static void main(String[] args) {

        //here we create our Hexmaps
        Hex overworldMap[] = new Hex[] {
            new Hex(0, 2, 0, Terrain.woods, "empty"),
            new Hex(1, 3, 0,  Terrain.woods, "empty"),
            new Hex(2, 4, 0,  Terrain.woods, "empty"),
            new Hex(3, 5, 0,  Terrain.mountains, "empty"),
            new Hex(4, 6, 0,  Terrain.mountains, "empty"),
            new Hex(5, 1, 1,  Terrain.woods, "empty"),
            new Hex(6, 2, 1, Terrain.woods, "Cultist Lair"),
            new Hex(7, 3, 1,  Terrain.woods, "empty"),
            new Hex(8, 4, 1,  Terrain.mountains, "empty"),
            new Hex(9, 5, 1,  Terrain.mountains, "Temple"),
            new Hex(10, 6, 1,  Terrain.mountains, "empty"),
            new Hex(11, 0, 2,  Terrain.hills, "empty"),
            new Hex(12, 1, 2,  Terrain.woods, "empty"),
            new Hex(13, 2, 2,  Terrain.woods, "empty"),
            new Hex(14, 3, 2,  Terrain.woods, "empty"),
            new Hex(15, 4, 2,  Terrain.mountains, "empty"),
            new Hex(16, 5, 2, Terrain.mountains, "Dragon Lair"),
            new Hex(17, 6, 2,  Terrain.mountains, "empty"),
            new Hex(18, -1, 3,  Terrain.hills, "empty"),
            new Hex(19, 0, 3,  Terrain.hills, "empty"),
            new Hex(20, 1, 3,  Terrain.hills, "empty"),
            new Hex(21, 2, 3,  Terrain.woods, "empty"),
            new Hex(22, 3, 3, Terrain.swamp, "Adamantine Deposit"),
            new Hex(23, 4, 3,  Terrain.mountains, "empty"),
            new Hex(24, 5, 3,  Terrain.mountains, "empty"),
            new Hex(25, 6, 3,  Terrain.woods, "empty"),
            new Hex(26, -2, 4,  Terrain.hills, "empty"),
            new Hex(27, -1, 4,  Terrain.hills, "empty"),
            new Hex(28, 0, 4,  Terrain.hills, "empty"),
            new Hex(29, 1, 4,  Terrain.hills, "empty"),
            new Hex(30, 2, 4,  Terrain.hills, "empty"),
            new Hex(31, 3, 4,  Terrain.swamp, "empty"),
            new Hex(32, 4, 4,  Terrain.woods, "empty"),
            new Hex(33, 5, 4, Terrain.woods, "Troglodyte Lair"),
            new Hex(34, 6, 4,  Terrain.woods, "empty"),
            new Hex(-35, 2, 5,  Terrain.hills, "empty"),
            new Hex(36, -1, 5,  Terrain.hills, "empty"),
            new Hex(37, 0, 5,  Terrain.hills, "Village"),
            new Hex(38, 1, 5,  Terrain.hills, "empty"),
            new Hex(39, 2, 5, Terrain.swamp, "Slith Lair"),
            new Hex(40, 3, 5,  Terrain.swamp, "empty"),
            new Hex(41, 4, 5,  Terrain.woods, "empty"),
            new Hex(42, 5, 5,  Terrain.woods, "empty"),
            new Hex(43, -2, 6,  Terrain.swamp, "empty"),
            new Hex(44, -1, 6,  Terrain.swamp, "empty"),
            new Hex(45, 0, 6,  Terrain.swamp, "empty"),
            new Hex(46, 1, 6,  Terrain.swamp, "empty"),
            new Hex(47, 2, 6,  Terrain.swamp, "empty"),
            new Hex(48, 3, 6,  Terrain.woods, "empty"),
            new Hex(49, 4, 6,  Terrain.woods, "empty"),
            new Hex(50, -2, 7,  Terrain.woods, "empty"),
            new Hex(51, -1, 7, Terrain.woods, "Bandit Camp"),
            new Hex(52, 0, 7,  Terrain.swamp, "empty"),
            new Hex(53, 1, 7,  Terrain.swamp, "empty"),
            new Hex(54, 2, 7, Terrain.woods, "Spider Lair"),
            new Hex(55, 3, 7,  Terrain.woods, "empty"),
            new Hex(56, -2, 8,  Terrain.woods, "empty"),
            new Hex(57, -1, 8,  Terrain.woods, "empty"),
            new Hex(58, 0, 8,  Terrain.swamp, "empty"),
            new Hex(59, 1, 8,  Terrain.woods, "empty"),
            new Hex(60, 2, 8,  Terrain.woods, "empty"),
        };

        HexMap overworld = new HexMap(overworldMap);

        Movable partyMovable = new Movable("Party", 0, 5);
        overworld.movable.add(partyMovable);
        overworld.movable.add(new Movable("Dragon", 5, 2));

        for (int i=0; i<10; i++) {
            overworld.hexQuery(partyMovable.east, partyMovable.southeast).printDescription();
            partyMovable.moveE();
        }
    }
}