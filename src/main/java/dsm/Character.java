package dsm;

class Character {
    String name;
    Profession profession;
    int experience;
    int level;
    int attack;
    int hitsMax;
    int hitsCurrent;
    int strength;
    int dexterity;
    int intelligence;
    int charisma;
    Equipment equipment[];

    Character(String name, Profession profession) {
        this.name = name;
        this.profession = profession;
        this.experience = 0;
        this.level = 1;
        this.attack = 1;
        this.hitsMax = 1;
        this.hitsCurrent = 1;
        this.strength = 11;
        this.dexterity = 11;
        this.intelligence = 11;
        this.charisma = 11;
        this.equipment = new Equipment[EquipmentSlot.values().length];
    }

    public Character() {
    }
}