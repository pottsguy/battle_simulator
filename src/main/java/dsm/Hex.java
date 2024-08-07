package dsm;

class Hex {
    int east;
    int southEast;
    Terrain terrain;
    String landmark;

    Hex(int east, int southeast, Terrain terrain, String landmark) {
        this.east = east;
        this.southEast = southeast;
        this.terrain = terrain;
        this.landmark = landmark;
    }

    public void printDescription() {
        System.out.println("This hex's terrain is " + this.terrain + ".");
        System.out.println("This hex's landmark is " + this.landmark + ".");
    }
}