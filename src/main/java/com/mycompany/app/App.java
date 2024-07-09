package com.mycompany.app;

// mvn assembly:assembly -DdescriptorId=jar-with-dependencies && java -cp ./target/db_test-1.0-SNAPSHOT-jar-with-dependencies.jar com.mycompany.app.App

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
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

class CombatantCounts {
    int enemiesActive;
    int enemiesTotal;
    int alliesActive;
}

public class App {

    // reads the battlefield and returns the number of enemies (total and current) and allies (current)
    public static CombatantCounts countCombatants(ArrayList<Combatant> bfld) {
        CombatantCounts myCC;
        myCC = new CombatantCounts();
        for (int i=0; i<bfld.size(); i++) {
            if (bfld.get(i).team == Team.Ally && !bfld.get(i).incapacitated) {
                myCC.alliesActive++;
            } else if (bfld.get(i).team == Team.Enemy) {
                myCC.enemiesTotal++;
                if (!bfld.get(i).incapacitated) {
                    myCC.enemiesActive++;
                }
            }
        }
        return myCC;
    }

    public static void main(String[] args) {
        Random rndm = new Random();
        Scanner scan = new Scanner(System.in);

        //this is where the combatants are added to their teams.
        ArrayList<Combatant> battlefield = new ArrayList<Combatant>();
        battlefield.add(new Combatant(Team.Ally, "Knight 1", Rank.Vanguard));
        battlefield.add(new Combatant(Team.Ally, "Knight 2", Rank.Vanguard));
        battlefield.add(new Combatant(Team.Ally, "Spearman 1", Rank.Rear));
        battlefield.add(new Combatant(Team.Ally, "Spearman 2", Rank.Rear));
        battlefield.add(new Combatant(Team.Ally, "Archer 1", Rank.Artillery));
        battlefield.add(new Combatant(Team.Ally, "Archer 2", Rank.Artillery));
        battlefield.add(new Combatant(Team.Enemy, "Trorc 1", Rank.Vanguard));
        battlefield.add(new Combatant(Team.Enemy, "Trorc 2", Rank.Vanguard));
        battlefield.add(new Combatant(Team.Enemy, "Trobgoblin 1", Rank.Rear));
        battlefield.add(new Combatant(Team.Enemy, "Trobgoblin 2", Rank.Rear));
        battlefield.add(new Combatant(Team.Enemy, "Trobold 1", Rank.Artillery));
        battlefield.add(new Combatant(Team.Enemy, "Trobold 2", Rank.Artillery));

        //this is the start of the combat cycle.
        boolean combatOngoing = true;
        int round = 1;
        boolean moraleChecked = false;
        while (combatOngoing) {
            System.out.println("--- Round " + round + ". ---");
            round++;

            //this counts the number of active combatants each round and asks if the player wants to fight or flee.
            CombatantCounts count = countCombatants(battlefield);
            System.out.println("There are currently " + count.alliesActive + " allies and " + count.enemiesActive + " enemies active.");
            System.out.println("Do you want to 1) keep fighting or 2) flee?");
            if (scan.nextInt() == 1) {
                System.out.println("You fight on.");
            } else {
                System.out.println("The Ally is fleeing.");
                combatOngoing = false;
            }

            //this is where initiative is rolled (d6, 1-3=enemies, 4-6=allies).
            int initiativeRoll = rndm.nextInt(6)+1;
            Team first;
            if (initiativeRoll<4) {
                first = Team.Enemy;
            } else {
                first = Team.Ally;
            }
            if (combatOngoing) {
                System.out.println("Initiative roll: " + initiativeRoll + ", the " + first.toString() + " have the initiative.");
            }

            //this sorts the combatants according to team, rank and initiative.
            Collections.sort (battlefield, (Combatant a, Combatant b) -> {
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

            //all potential targets are added to a targets list.
            for (int i=0; combatOngoing && i < battlefield.size(); i++) {
                Combatant attacker = battlefield.get(i);
                ArrayList<Combatant> targets = new ArrayList<Combatant>();
                for (int n=0; n<battlefield.size(); n++) {
                    Combatant candidate = battlefield.get(n);
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
                    int attackRoll = rndm.nextInt(20) + battlefield.get(i).attack;
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
                            count.alliesActive--;
                        } else if (target.team == Team.Enemy) {
                            count.enemiesActive--;
                        }
                        if (count.alliesActive > 0 && count.enemiesActive > 0) {
                            System.out.println("x " + target.name + " has been incapacitated, there are now " + count.alliesActive + " allies and " + count.enemiesActive + " enemies active.");
                        } else {
                            System.out.println("The combat is over, the " + target.team + " has been wiped out.");
                            combatOngoing = false;
                        }
                    }
                }
            }

            //checks morale if the enemy is reduced to half numbers.
            if (!moraleChecked && combatOngoing && count.enemiesActive<count.enemiesTotal/2) {
                moraleChecked = true;
                if (rndm.nextInt(6)<4) {
                    System.out.println("The Enemy is fleeing.");
                    combatOngoing = false;
                } else {
                    System.out.println("The Enemy is fighting on.");
                }
            }
        }
        scan.close();
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
                    "Witteric", "Gundemar", "Sisebut", "Swinthila", "Recimmer", "Sissenand", "Iudila", "Chintila", 
                    "Tulga", "Chindaswinth", "Recceswinth", "Froia", "Wamba", "Hilderic", "Paul", "Erwig", "Egica", 
                    "Suniefred", "Wittiza", "Roderic", "Oppas", "Ardo"
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
