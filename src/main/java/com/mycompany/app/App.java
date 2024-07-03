package com.mycompany.app;

// mvn assembly:assembly -DdescriptorId=jar-with-dependencies && java -cp ./target/db_test-1.0-SNAPSHOT-jar-with-dependencies.jar com.mycompany.app.App

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Collections;
import java.util.Comparator;

enum Team {
    Ally, Enemy
}
enum Rank {
    Vanguard, Rear, Artillery
}

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

public class App {
    public static void main(String[] args) {
        Random rndm = new Random();

        //this is where the combatants are added to their teams.
        Combatant battlefield[] = new Combatant [12];
        battlefield[0] = new Combatant(Team.Ally, "Knight 1", Rank.Vanguard);
        battlefield[1] = new Combatant(Team.Ally, "Knight 2", Rank.Vanguard);
        battlefield[2] = new Combatant(Team.Ally, "Spearman 1", Rank.Rear);
        battlefield[3] = new Combatant(Team.Ally, "Spearman 2", Rank.Rear);
        battlefield[4] = new Combatant(Team.Ally, "Archer 1", Rank.Artillery);
        battlefield[5] = new Combatant(Team.Ally, "Archer 2", Rank.Artillery);
        battlefield[6] = new Combatant(Team.Enemy, "Trorc 1", Rank.Vanguard);
        battlefield[7] = new Combatant(Team.Enemy, "Trorc 2", Rank.Vanguard);
        battlefield[8] = new Combatant(Team.Enemy, "Trobgoblin 1", Rank.Rear);
        battlefield[9] = new Combatant(Team.Enemy, "Trobgoblin 2", Rank.Rear);
        battlefield[10] = new Combatant(Team.Enemy, "Trobold 1", Rank.Artillery);
        battlefield[11] = new Combatant(Team.Enemy, "Trobold 2", Rank.Artillery);
        /*Combatant allies[] = new Combatant[6];
        allies[0] = new Combatant(Team.Ally, "Knight 1", Rank.Vanguard);
        allies[1] = new Combatant(Team.Ally, "Knight 2", Rank.Vanguard);
        allies[2] = new Combatant(Team.Ally, "Spearman 1", Rank.Rear);
        allies[3] = new Combatant(Team.Ally, "Spearman 2", Rank.Rear);
        allies[4] = new Combatant(Team.Ally, "Archer 1", Rank.Artillery);
        allies[5] = new Combatant(Team.Ally, "Archer 2", Rank.Artillery);
        Combatant enemies[] = new Combatant[6];
        enemies[0] = new Combatant(Team.Enemy, "Trorc 1", Rank.Vanguard);
        enemies[1] = new Combatant(Team.Enemy, "Trorc 2", Rank.Vanguard);
        enemies[2] = new Combatant(Team.Enemy, "Trobgoblin 1", Rank.Rear);
        enemies[3] = new Combatant(Team.Enemy, "Trobgoblin 2", Rank.Rear);
        enemies[4] = new Combatant(Team.Enemy, "Trobold 1", Rank.Artillery);
        enemies[5] = new Combatant(Team.Enemy, "Trobold 2", Rank.Artillery);*/

        //this is the start of the combat cycle, the number of rounds is currently static.
        boolean combatOngoing = true;
        int round = 1;
        boolean moraleChecked = false;
        while (combatOngoing) {
            System.out.println("--- Round " + round + ". ---");
            round++;

            //this is where initiative is rolled (d6, 1-3=enemies, 4-6=allies).
            int initiativeRoll = rndm.nextInt(6)+1;
            Team first;
            if (initiativeRoll<4) {
                first = Team.Enemy;
            } else {
                first = Team.Ally;
            }
            System.out.println("Initiative roll: " + initiativeRoll + ", the " + first.toString() + " have the initiative.");

            //this sorts the combatants according to team, rank and initiative.
            Arrays.sort (battlefield, (Combatant a, Combatant b) -> {
                if (a.rank == Rank.Artillery) {
                    if (b.rank == Rank.Artillery && b.team == first) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else if (a.rank == Rank.Vanguard) {
                    if (b.rank == Rank.Artillery || b.rank == Rank.Vanguard && b.team == first) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else if (a.rank == Rank.Rear) {
                    if (b.rank == Rank.Artillery || b.rank == Rank.Vanguard || b.rank == Rank.Rear && b.team == first) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else {
                    return 0;
                }
            });

            //this counts the number of active combatants each round.
            int alliesCount = 0;
            int enemiesTotal = 0;
            int enemiesCount = 0;
            for (int i=0; i<battlefield.length; i++) {
                if (battlefield[i].team == Team.Ally && !battlefield[i].incapacitated) {
                    alliesCount++;
                } else if (battlefield[i].team == Team.Enemy) {
                    enemiesTotal++;
                    if (!battlefield[i].incapacitated) {
                        enemiesCount++;
                    }
                }
            }

            System.out.println("There are currently " + alliesCount + " allies and " + enemiesCount + " enemies active.");

            //all potential targets are added to a targets list.
            for (int i=0; combatOngoing && i < battlefield.length; i++) {
                Combatant attacker = battlefield[i];
                ArrayList<Combatant> targets = new ArrayList<Combatant>();
                for (int n=0; n<battlefield.length; n++) {
                    Combatant candidate = battlefield[n];
                    if (candidate.incapacitated == false && candidate.team != attacker.team) {
                        targets.add(candidate);
                    }
                }

                //the potential targets are sorted by priority.
                Collections.shuffle(targets);
                Collections.sort (targets, (Combatant a, Combatant b) -> {
                    if (attacker.rank == Rank.Vanguard || attacker.rank == Rank.Rear) {
                        if (a.rank == Rank.Vanguard && b.rank != Rank.Vanguard) {
                            return -1;
                        } else if (b.rank == Rank.Vanguard && a.rank != Rank.Vanguard) {
                            return 1;
                        } else if (a.rank == Rank.Rear && b.rank == Rank.Artillery) {
                            return -1;
                        } else if (b.rank == Rank.Rear && a.rank == Rank.Artillery) {
                            return -1;
                        } else {
                            return 0;
                        }
                    } else if (attacker.rank == Rank.Artillery) {
                        if (a.rank == Rank.Vanguard && b.rank != Rank.Vanguard) {
                            return 1;
                        } else if (b.rank == Rank.Vanguard && a.rank != Rank.Vanguard) {
                            return -1;
                        } else {
                            return 0;
                        }
                    } else {
                        return 0;
                    }
                });

                //the top priority target is chosen as the target.
                Combatant target = targets.get(0);
                assert !target.incapacitated;
                assert target.team != attacker.team;

                //this is where the attack roll occurs.
                if (attacker.incapacitated == false) {
                    int attackRoll = rndm.nextInt(20) + battlefield[i].attack;
                    if (attackRoll < 10) {
                        System.out.println(attacker.name + " misses " + target.name + ", leaving them with " + target.hitsCurrent + " hits.");
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

                    //this marks if the target has been killed
                    if (target.hitsCurrent < 1) {
                        target.incapacitated = true;
                        if (target.team == Team.Ally) {
                            alliesCount--;
                        } else if (target.team == Team.Enemy) {
                            enemiesCount--;
                        }
                        if (alliesCount > 0 && enemiesCount > 0) {
                            System.out.println("x " + target.name + " has been incapacitated, there are now " + alliesCount + " allies and " + enemiesCount + " enemies active.");
                        } else {
                            System.out.println("The combat is over, the " + target.team + " has been wiped out.");
                            combatOngoing = false;
                        }
                    }
                }
            }

            //checks morale if the enemy is reduced to half numbers.
            if (!moraleChecked && combatOngoing && enemiesCount<enemiesTotal/2) {
                moraleChecked = true;
                if (rndm.nextInt(6)<4) {
                    System.out.println("The Enemy is fleeing.");
                    combatOngoing = false;
                } else {
                    System.out.println("The Enemy is fighting on.");
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