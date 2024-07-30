package dsm;

import java.util.ArrayList;

class HexMap {
    Hex hex[];
    ArrayList<Movable> movable;

    HexMap(Hex hex[]){
        this.hex = hex;
        this.movable = new ArrayList<>();
    }

    public Hex hexAt(int east, int southEast) {
        for (int i=0; i<this.hex.length; i++) {
            if (this.hex[i].east == east && this.hex[i].southEast == southEast) {
                return hex[i];
            }
        }
        throw new RuntimeException("There is no hex at " + east + ", " + southEast);
    }

    public Hex hexAt(Movable m) {
        return hexAt(m.east, m.southeast);
    }
}