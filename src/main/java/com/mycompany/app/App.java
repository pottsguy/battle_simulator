package com.mycompany.app;

// mvn assembly:assembly -DdescriptorId=jar-with-dependencies && java -cp ./target/db_test-1.0-SNAPSHOT-jar-with-dependencies.jar com.mycompany.app.App

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

//this defines the stats that a combatant has.
class Combatant {
    String team;
    String name;
    int hitsMax;
    int hitsCurrent;
    int attack;
    int minDamage;
    int midDamage;
    int maxDamage;
    boolean incapacitated;

    // these stats are currently the same for all members of the "combatant" class.
    Combatant(String team, String name) {
        this.team = team;
        this.name = name;
        this.hitsMax = 6;
        this.hitsCurrent = this.hitsMax;
        this.attack = 1;
        this.minDamage = 2;
        this.midDamage = 4;
        this.maxDamage = 6;
        this.incapacitated = false;
    }
}

public class App {
    public static void main(String[] args) {
        Random rndm = new Random();

        //this is where the combatants are added to their individual ranks: [0][]=vanguard, [1][]=rear, [2][]=artillery
        Combatant allies[][] = new Combatant[3][2];
        allies[0][0] = new Combatant("Ally", "Knight 1");
        allies[0][1] = new Combatant("Ally", "Knight 2");
        allies[1][0] = new Combatant("Ally", "Spearman 1");
        allies[1][1] = new Combatant("Ally", "Spearman 2");
        allies[2][0] = new Combatant("Ally", "Archer 1");
        allies[2][1] = new Combatant("Ally", "Archer 2");
        Combatant enemies[][] = new Combatant[3][2];
        enemies[0][0] = new Combatant("Enemy", "Trorc 1");
        enemies[0][1] = new Combatant("Enemy", "Trorc 2");
        enemies[1][0] = new Combatant("Enemy", "Trobgoblin 1");
        enemies[1][1] = new Combatant("Enemy", "Trobgoblin 2");
        enemies[2][0] = new Combatant("Enemy", "Trobold 1");
        enemies[2][1] = new Combatant("Enemy", "Trobold 2");

        //this is the start of the combat cycle, the number of rounds is currently static
        Combatant combatOrder[] = new Combatant[allies[0].length + allies[1].length + allies[2].length + enemies[0].length + enemies[1].length + enemies[2].length];        
        boolean combatOngoing = true;
        int round = 1;
        while (combatOngoing) {
            System.out.println("--- Round " + round + ". ---");
            round++;

            //this is where initiative is rolled (d6, 1-3=enemies, 4-6=allies) and combatants are arranged in order
            int initiativeRoll = rndm.nextInt(6)+1;
            int initiativeIndex = 0;
            if (initiativeRoll<4) {
                System.out.println("Initiative roll: " + initiativeRoll + ", enemies have the initiative.");
                for (int i=0; i<enemies[2].length; i++) {
                    combatOrder[initiativeIndex] = enemies[2][i];
                    initiativeIndex++;
                }
                for (int i=0; i<allies[2].length; i++) {
                    combatOrder[initiativeIndex] = allies[2][i];
                    initiativeIndex++;
                }
                for (int i=0; i<enemies[0].length; i++) {
                    combatOrder[initiativeIndex] = enemies[0][i];
                    initiativeIndex++;
                }
                for (int i=0; i<allies[0].length; i++) {
                    combatOrder[initiativeIndex] = allies[0][i];
                    initiativeIndex++;
                }
                for (int i=0; i<enemies[1].length; i++) {
                    combatOrder[initiativeIndex] = enemies[1][i];
                    initiativeIndex++;
                }
                for (int i=0; i<allies[1].length; i++) {
                    combatOrder[initiativeIndex] = allies[1][i];
                    initiativeIndex++;
                }
            } else {
                System.out.println("Initiative roll: " + initiativeRoll + ", allies have the initiative.");
                for (int i=0; i<allies[2].length; i++) {
                    combatOrder[initiativeIndex] = allies[2][i];
                    initiativeIndex++;
                }
                for (int i=0; i<enemies[2].length; i++) {
                    combatOrder[initiativeIndex] = enemies[2][i];
                    initiativeIndex++;
                }
                for (int i=0; i<allies[0].length; i++) {
                    combatOrder[initiativeIndex] = allies[0][i];
                    initiativeIndex++;
                }
                for (int i=0; i<enemies[0].length; i++) {
                    combatOrder[initiativeIndex] = enemies[0][i];
                    initiativeIndex++;
                }
                for (int i=0; i<allies[1].length; i++) {
                    combatOrder[initiativeIndex] = allies[1][i];
                    initiativeIndex++;
                }
                for (int i=0; i<enemies[1].length; i++) {
                    combatOrder[initiativeIndex] = enemies[1][i];
                    initiativeIndex++;
                }
            }

            //this counts the number of active combatants each round
            int alliesCount = 0;
            int enemiesCount = 0;
            for (int i=0; i<combatOrder.length; i++) {
                if (combatOrder[i].team == "Ally" && combatOrder[i].incapacitated == false) {
                    alliesCount++;
                } else if (combatOrder[i].team == "Enemy" && combatOrder[i].incapacitated == false) {
                    enemiesCount++;
                }
            }
            System.out.println("There are currently " + alliesCount + " allies and " + enemiesCount + " enemies active.");

            //this randomly selects an opposing, non-incapacitated target for each combatant in order.
            for (int i=0; i<combatOrder.length; i++) {
                Combatant attacker = combatOrder[i];
                Combatant target = combatOrder[rndm.nextInt(combatOrder.length)];
                while (target.incapacitated || attacker.team == target.team) {
                    target = combatOrder[rndm.nextInt(combatOrder.length)];
                }

                //this is where the attack roll occurs.
                if (attacker.incapacitated == false) {
                    int attackRoll = rndm.nextInt(20) + combatOrder[i].attack;
                    if (attackRoll < 10) {
                        System.out.println(attacker.name + " misses " + target.name + ", leaving " + target.name + " with " + target.hitsCurrent + " hits.");
                    } else if (attackRoll < 15) {
                        target.hitsCurrent -= attacker.minDamage;
                        System.out.println(attacker.name + " deals a glancing blow against " + target.name + ", dealing " + attacker.minDamage + " damage and leaving them with " + target.hitsCurrent + " hits.");
                    } else if (attackRoll < 20) {
                        target.hitsCurrent -= attacker.midDamage;
                        System.out.println(attacker.name + " deals a solid blow against " + target.name + ", dealing " + attacker.midDamage + " damage and leaving them with " + target.hitsCurrent + " hits.");
                    } else if (attackRoll >=20) {
                        target.hitsCurrent -= attacker.maxDamage;
                        System.out.println(attacker.name + " deals a critical hit against " + target.name + ", dealing " + attacker.maxDamage + " damage and leaving them with " + target.hitsCurrent + " hits.");
                    }
                    if (target.hitsCurrent < 1) {
                        target.incapacitated = true;
                        if (target.team == "Ally") {
                            alliesCount--;
                        } else if (target.team == "Enemy") {
                            enemiesCount--;
                        }
                        if (alliesCount > 0 && enemiesCount > 0) {
                            System.out.println(target.name + " has been incapacitated, there are now " + alliesCount + " allies and " + enemiesCount + " enemies active.");
                        } else {
                            System.out.println("The combat is over, the " + target.team + " has been wiped out.");
                            combatOngoing = false;
                        }
                    }
                }
            }
        }
    }

    public static void pregenerate(String[] args) throws ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        String url = "jdbc:mariadb://localhost:3306/monsterManual";
        try {
            Connection dbConn = DriverManager.getConnection(url, "guy", "1234");

            String foreNames[] = new String[] {
                    "Antonio", "Imelda", "Pasqualina", "Gennaro", "Arca", "Porta", "Lama", "Poe", "Clerici", "Dommario",
                    "Krochi", "Christine", "Pugnala", "Giovanna", "Poppea", "Concetta", "Mortaccio", "Cavallo", "Ramba",
                    "O'Sole", "Ambrojoe", "Gallo", "Divano", "Zi'Assunta", "Sigma", "Robbert", "She-Moon", "Santa",
                    "Ariaric", "Aoric", "Athanaric", "Rothesteus", "Winguric", "Alavivus", "Fritigern", "Athaulf",
                    "Sigeric", "Wallia", "Theodoric", "Thorismund", "Euric", "Gesalec", "Theudis", "Theudigisel",
                    "Agila", "Athanagild", "Luiva", "Liuvigild", "Hermenegild", "Recarred", "Segga", "Argimund",
                    "Witteric", "Gundemar",
                    "Sisebut", "Swinthila", "Recimmer", "Sissenand", "Iudila", "Chintila", "Tulga", "Chindaswinth",
                    "Recceswinth", "Froia", "Wamba", "Hilderic", "Paul", "Erwig", "Egica", "Suniefred", "Wittiza",
                    "Roderic", "Oppas", "Ardo"
            };

            String surNames[] = new String[] {
                    "Mcintyre", "Montoya", "Blankenship", "Nixon", "Briggs", "Lozano", "Strickland", "Reilly", "Good", "Coleman",
                    "Ho", "Phelps", "Ferguson", "Tate", "Jackson", "Pearson", "Logan", "Macias", "Gates",
            };

            Random rndm = new Random();

            String query = "INSERT INTO pregens (name, class, strength, dexterity, intelligence, charisma) VALUES (?,?,?,?,?,?)";
            final int GEN_ATTR_START_IX = 3; // remember, SQL uses one-based indices (unlike java which is zero-based)
            final int GEN_ATTR_COUNT = 4;
            PreparedStatement stmt = dbConn.prepareStatement(query);

            for (int k = 0; k < 10000; k++) {
                stmt.setString(1,
                        foreNames[rndm.nextInt(foreNames.length)] + " " + surNames[rndm.nextInt(surNames.length)]);

                // generates stats for str, dex, int & cha with 3d6 (re-roll 1's, giving a 2-6
                // range)
                ArrayList<Integer> maxAttrsIndices = new ArrayList<Integer>();
                int maxAttrValue = 0;
                for (int i = 0; i < GEN_ATTR_COUNT; i++) {
                    int roll1 = rndm.nextInt(5) + 2;
                    int roll2 = rndm.nextInt(5) + 2;
                    int roll3 = rndm.nextInt(5) + 2;
                    int attrValue = roll1 + roll2 + roll3;
                    stmt.setInt(GEN_ATTR_START_IX + i, attrValue);

                    // keeps track of highest stat to determine class
                    if (attrValue == maxAttrValue) {
                        maxAttrValue = attrValue;
                        maxAttrsIndices.add(i);
                    } else if (attrValue > maxAttrValue) {
                        maxAttrValue = attrValue;
                        maxAttrsIndices.clear();
                        maxAttrsIndices.add(i);
                    }
                }

                // each of these base classes corresponds to an attribute (respective of
                // position in the query)
                String charClass[] = new String[] {
                        "Warrior", "Thief", "Sage", "Shaman"
                };

                // determines class based on highest attribute
                if (maxAttrValue < 13) {
                    stmt.setString(2, "Normie");
                } else {
                    // randomises class if there are two equal maximum attributes
                    stmt.setString(2, charClass[maxAttrsIndices.get(rndm.nextInt(maxAttrsIndices.size()))]);
                }
                ;
                stmt.executeUpdate();
            }

            dbConn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

/*
 * abstract class ClassDice {
 * abstract int rollHit();
 * abstract int rollAttack();
 * }
 * 
 * class WarriorDice extends ClassDice {
 * 
 * @Override
 * int rollHit() {
 * // TODO Auto-generated method stub
 * throw new UnsupportedOperationException("Unimplemented method 'rollHit'");
 * }
 * 
 * @Override
 * int rollAttack() {
 * // TODO Auto-generated method stub
 * throw new UnsupportedOperationException("Unimplemented method 'rollAttack'");
 * }
 * }
 * 
 * class ShamanTheifDice extends ClassDice {
 * 
 * @Override
 * int rollHit() {
 * // TODO Auto-generated method stub
 * throw new UnsupportedOperationException("Unimplemented method 'rollHit'");
 * }
 * 
 * @Override
 * int rollAttack() {
 * // TODO Auto-generated method stub
 * throw new UnsupportedOperationException("Unimplemented method 'rollAttack'");
 * }
 * }
 * 
 * class SageDice extends ClassDice {
 * 
 * @Override
 * int rollHit() {
 * // TODO Auto-generated method stub
 * throw new UnsupportedOperationException("Unimplemented method 'rollHit'");
 * }
 * 
 * @Override
 * int rollAttack() {
 * // TODO Auto-generated method stub
 * throw new UnsupportedOperationException("Unimplemented method 'rollAttack'");
 * }
 * }
 */