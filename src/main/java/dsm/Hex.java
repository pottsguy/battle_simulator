package dsm;

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