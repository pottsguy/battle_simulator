package dsm;

import java.util.ArrayList;

class HexMap {
    Hex hex[];
    ArrayList<Coordinate> coordinate;

    HexMap(Hex hex[]){
        this.hex = hex;
        this.coordinate = new ArrayList<>();
    }

    // Get the hex at east, southeast (or null)
    public Hex hexAtMaybe(int east, int southEast) {
        for (int i=0; i<this.hex.length; i++) {
            if (this.hex[i].east == east && this.hex[i].southEast == southEast) {
                return hex[i];
            }
        }
        return null;
    }

    public Hex hexAtMaybe(Coordinate m) {
        return hexAtMaybe(m.east, m.southeast);
    }

    // Get the hex at east, southeast or throw a runtime error
    public Hex hexAt(int east, int southEast) {
        Hex hex = this.hexAtMaybe(east, southEast);
        if(hex!=null) {
            return hex;
        };
        throw new RuntimeException("There is no hex at " + east + ", " + southEast);
    }

    public Hex hexAt(Coordinate m) {
        return hexAt(m.east, m.southeast);
    }

    public Hex[] neighbours(int east, int southeast) {
        Hex[] hexes = new Hex[6];
        hexes[0] = this.hexAtMaybe(east, southeast-1);
        hexes[1] = this.hexAtMaybe(east+1, southeast-1);
        hexes[2] = this.hexAtMaybe(east+1, southeast);
        hexes[3] = this.hexAtMaybe(east, southeast+1);
        hexes[4] = this.hexAtMaybe(east-1, southeast+1);
        hexes[5] = this.hexAtMaybe(east-1, southeast+1);
        return hexes;
    }

    public Hex[] neighbours(Coordinate m) {
        return neighbours(m.east, m.southeast);
    }
}