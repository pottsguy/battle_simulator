package dsm;

//this defines the stats that a combatant has.
class Combatant {
    Team team;
    String name;
    Rank rank;
    int hitsMax;
    int hitsCurrent;
    int attack;
    int minDamage;
    int midDamage;
    int maxDamage;
    boolean incapacitated;

    // these stats are currently the same for all members of the "combatant" class.
    Combatant(Team team, String name, Rank rank) {
        this.team = team;
        this.name = name;
        this.rank = rank;
        this.hitsMax = 6;
        this.hitsCurrent = this.hitsMax;
        this.attack = 1;
        this.minDamage = 2;
        this.midDamage = 4;
        this.maxDamage = 6;
        this.incapacitated = false;
    }
}