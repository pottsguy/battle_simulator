package dsm;

import java.util.Scanner;

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
    WeaponType weaponCombo[];
    ArmourType armourCombo[];

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
        this.weaponCombo = new WeaponType[2];
        this.armourCombo = new ArmourType[4];
    }

    public Character(Dice dice, Scanner scan) {
        System.out.println("Generating a character.");
        this.strength = dice.d6() + dice.d6() + dice.d6();
        System.out.println("Strength score: " + this.strength);
        this.dexterity = dice.d6() + dice.d6() + dice.d6();
        System.out.println("Dexterity score: " + this.dexterity);
        this.intelligence = dice.d6() + dice.d6() + dice.d6();
        System.out.println("Intelligence score: " + this.intelligence);
        this.charisma = dice.d6() + dice.d6() + dice.d6();
        System.out.println("Charisma score: " + this.charisma);

        if(this.strength<13 && this.dexterity<13 && this.intelligence<13 && this.charisma<13) {
            System.out.println("Your attributes are too low. Choose an attribute to boost to 13.");
            MultipleChoiceQuestion attributeQuestion = new MultipleChoiceQuestion();
            attributeQuestion.addOption("Strength");
            attributeQuestion.addOption("Dexterity");
            attributeQuestion.addOption("Intelligence");
            attributeQuestion.addOption("Charisma");
            int attributeChoice = attributeQuestion.ask(scan);
            if(attributeChoice == 0) {
                this.strength = 13;
            } else if(attributeChoice == 1) {
                this.dexterity = 13;
            } else if(attributeChoice == 2) {
                this.intelligence = 13;
            } else if(attributeChoice == 3) {
                this.charisma = 13;
            }
        }
        System.out.println("Now, choose a profession.");
        MultipleChoiceQuestion professionQuestion = new MultipleChoiceQuestion();
        if(this.strength > 12) {
            professionQuestion.addOption(Profession.Warrior.toString());
        } else {
            professionQuestion.skipOption();
        };
        if(this.dexterity > 12) {
            professionQuestion.addOption(Profession.Thief.toString());
        } else {
            professionQuestion.skipOption();
        };
        if(this.intelligence > 12) {
            professionQuestion.addOption(Profession.Sage.toString());
        } else {
            professionQuestion.skipOption();
        };
        if(this.charisma > 12) {
            professionQuestion.addOption(Profession.Shaman.toString());
        } else {
            professionQuestion.skipOption();
        };
        int professionChoice = professionQuestion.ask(scan);
        if(professionChoice == 0) {
            this.profession = Profession.Warrior;
        } else if(professionChoice == 1) {
            this.profession = Profession.Thief;
        } else if(professionChoice == 2) {
            this.profession = Profession.Sage;
        } else if(professionChoice == 3) {
            this.profession = Profession.Shaman;
        }

        System.out.println("What is this character's name?");
        scan.next(); // consume the previous newline character
        this.name = scan.nextLine();
    }
}