package dsm;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Collections;

public class Battle {

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

    public static void battleMain(Dice dice, Scanner scan, ArrayList<Combatant> battlefield) {


        //this is the start of the combat cycle.
        boolean combatOngoing = true;
        int round = 1;
        boolean moraleChecked = false;
        while (combatOngoing) {
            System.out.println("--- Round " + round + ". ---");
            round++;

            System.out.println();

            //this counts the number of active combatants each round and asks if the player wants to fight or flee.
            CombatantCounts count = countCombatants(battlefield);
            System.out.println("There are currently " + count.alliesActive + " allies and " + count.enemiesActive + " enemies active.");

            int decision = 0;
            while (decision == 0) {
                System.out.println("Do you want to 1) keep fighting or 2) flee?");
                decision = scan.nextInt();
                if (decision == 1) {
                    System.out.println("You fight on.");
                } else if (decision == 2) {
                    System.out.println("The Ally is fleeing.");
                    combatOngoing = false;
                } else {
                    System.out.println("Error, try again.");
                    decision = 0;
                }
            }

            //this is where initiative is rolled (d6, 1-3=enemies, 4-6=allies).
            int initiativeRoll = dice.d6();
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
                    if (!candidate.incapacitated && candidate.team != attacker.team) {
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
                if (!attacker.incapacitated) {
                    int attackRoll = dice.d20() + battlefield.get(i).attack;
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
                if (dice.d6()<4) {
                    System.out.println("The Enemy is fleeing.");
                    combatOngoing = false;
                } else {
                    System.out.println("The Enemy is fighting on.");
                }
            }
        }
    }
}
