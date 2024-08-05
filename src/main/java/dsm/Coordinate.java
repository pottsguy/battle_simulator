package dsm;

class Coordinate {
    int east;
    int southeast;

    Coordinate(int east, int southeast) {
        this.east = east;
        this.southeast = southeast;
    }

    public static final Coordinate NORTHEAST = new Coordinate(1, -1);
    public static final Coordinate EAST = new Coordinate(1, 0);
    public static final Coordinate SOUTHEAST = new Coordinate(0, 1);
    public static final Coordinate SOUTHWEST = new Coordinate(-1, 1);
    public static final Coordinate WEST = new Coordinate(-1, 0);
    public static final Coordinate NORTHWEST = new Coordinate(0, -1);
    public static final Coordinate DIRECTIONS[] = new Coordinate[] {
            NORTHWEST,
            NORTHEAST,
            EAST,
            SOUTHEAST,
            SOUTHWEST,
            WEST
    };

    public static String directionNames[] = {
            "northwest",
            "northeast",
            "east",
            "southeast",
            "southwest",
            "west"
    };

    public void move(Coordinate coordinate) {
        this.east += coordinate.east;
        this.southeast += coordinate.southeast;
    }

    public Coordinate plus(Coordinate coordinate) {
        Coordinate sum = new Coordinate(coordinate.east+this.east, coordinate.southeast+this.southeast);
        return sum;
    };
}