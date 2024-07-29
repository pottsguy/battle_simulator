package dsm;

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