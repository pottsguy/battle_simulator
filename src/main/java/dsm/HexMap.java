package dsm;

import java.util.ArrayList;

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