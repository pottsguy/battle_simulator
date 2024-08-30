package dsm;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Campaign {

    //this is where the overworld hex map is defined
    public static HexMap makeWorldMap() {
        Hex overworldMap[] = new Hex[] {
            new Hex(2, 0, Terrain.Woods, null),
            new Hex(3, 0, Terrain.Woods, null),
            new Hex(4, 0, Terrain.Woods, null),
            new Hex(5, 0, Terrain.Mountains, null),
            new Hex(6, 0, Terrain.Mountains, null),
            new Hex(1, 1, Terrain.Woods, null),
            new Hex(2, 1, Terrain.Woods, "Cultist Lair"),
            new Hex(3, 1, Terrain.Woods, null),
            new Hex(4, 1, Terrain.Mountains, null),
            new Hex(5, 1, Terrain.Mountains, "Temple"),
            new Hex(6, 1, Terrain.Mountains, null),
            new Hex(0, 2, Terrain.Hills, null),
            new Hex(1, 2, Terrain.Woods, null),
            new Hex(2, 2, Terrain.Woods, null),
            new Hex(3, 2, Terrain.Woods, null),
            new Hex(4, 2, Terrain.Mountains, null),
            new Hex(5, 2, Terrain.Mountains, "Dragon Lair"),
            new Hex(6, 2, Terrain.Mountains, null),
            new Hex(-1, 3, Terrain.Hills, null),
            new Hex(0, 3, Terrain.Hills, null),
            new Hex(1, 3, Terrain.Hills, null),
            new Hex(2, 3, Terrain.Woods, null),
            new Hex(3, 3, Terrain.Swamp, "Adamantite Deposit"),
            new Hex(4, 3, Terrain.Mountains, null),
            new Hex(5, 3, Terrain.Mountains, null),
            new Hex(6, 3, Terrain.Woods, null),
            new Hex(-2, 4, Terrain.Hills, null),
            new Hex(-1, 4, Terrain.Hills, null),
            new Hex(0, 4, Terrain.Hills, null),
            new Hex(1, 4, Terrain.Hills, null),
            new Hex(2, 4, Terrain.Hills, null),
            new Hex(3, 4, Terrain.Swamp, null),
            new Hex(4, 4, Terrain.Woods, null),
            new Hex(5, 4, Terrain.Woods, "Troglodyte Lair"),
            new Hex(6, 4, Terrain.Woods, null),
            new Hex(2, 5, Terrain.Hills, null),
            new Hex(-1, 5, Terrain.Hills, null),
            new Hex(0, 5, Terrain.Hills, "Village"),
            new Hex(1, 5, Terrain.Hills, null),
            new Hex(2, 5, Terrain.Swamp, "Slith Lair"),
            new Hex(3, 5, Terrain.Swamp, null),
            new Hex(4, 5, Terrain.Woods, null),
            new Hex(5, 5, Terrain.Woods, null),
            new Hex(-2, 6, Terrain.Swamp, null),
            new Hex(-1, 6, Terrain.Swamp, null),
            new Hex(0, 6, Terrain.Swamp, null),
            new Hex(1, 6, Terrain.Swamp, null),
            new Hex(2, 6, Terrain.Swamp, null),
            new Hex(3, 6, Terrain.Woods, null),
            new Hex(4, 6, Terrain.Woods, null),
            new Hex(-2, 7, Terrain.Woods, null),
            new Hex(-1, 7, Terrain.Woods, "Bandit Camp"),
            new Hex(0, 7, Terrain.Swamp, null),
            new Hex(1, 7, Terrain.Swamp, null),
            new Hex(2, 7, Terrain.Woods, "Spider Lair"),
            new Hex(3, 7, Terrain.Woods, null),
            new Hex(-2, 8, Terrain.Woods, null),
            new Hex(-1, 8, Terrain.Woods, null),
            new Hex(0, 8, Terrain.Swamp, null),
            new Hex(1, 8, Terrain.Woods, null),
            new Hex(2, 8, Terrain.Woods, null),
        };
        HexMap overworld = new HexMap(overworldMap);
        return overworld;
    }

    Dice dice;
    Scanner scan;
    int day;
    WeatherGenerator weatherGen;
    Weather weather;
    TimeOfDay time;
    HexMap world;
    Coordinate partyCoordinate;
    ArrayList<Character> party;
    WeaponType weapons[];
    ArmourType armour[];

    Campaign(Dice dice, Scanner scan) {
        this.dice = dice;
        this.scan = scan;
        this.day = 1;
        this.weatherGen = new WeatherGenerator(dice);
        this.time = TimeOfDay.Morning;
        this.world = makeWorldMap();
        this.partyCoordinate = new Coordinate(0, 5);
        this.world.coordinate.add(partyCoordinate);
        this.party = new ArrayList<>();
        this.weapons = new WeaponType[] {
            new WeaponType(WeaponName.None, 2, 2, 2),
            new WeaponType(WeaponName.Dagger, 2, 3, 4),
            new WeaponType(WeaponName.Staff, 2, 4, 6),
            new WeaponType(WeaponName.Spear1h, 2, 4, 6),
            new WeaponType(WeaponName.Sword, 2, 4, 6),
            new WeaponType(WeaponName.Sling, 2, 4, 6),
            new WeaponType(WeaponName.Spear2h, 2, 5, 7),
            new WeaponType(WeaponName.Battleaxe, 2, 5, 7),
            new WeaponType(WeaponName.Bow, 2, 5, 7)
        };
        this.armour = new ArmourType[] {
            new ArmourType(ArmourName.Helmet, 2),
            new ArmourType(ArmourName.Breastplate, 2),
            new ArmourType(ArmourName.Pauldrons, 1),
            new ArmourType(ArmourName.Greaves, 1),
        };
    }

    void tellTerrain() {
        Hex currentHex = world.hexAt(partyCoordinate.east, partyCoordinate.southeast);
        System.out.print("You are standing ");
        if(currentHex.terrain == Terrain.Hills) {
            System.out.print("upon a grassy hillside");
        } else if(currentHex.terrain == Terrain.Mountains) {
            System.out.print("upon a rugged mountainside");
        } else if(currentHex.terrain == Terrain.Swamp) {
            System.out.print("within a foetid swamp");
        } else if(currentHex.terrain == Terrain.Woods) {
            System.out.print("within a dense woodland");
        };
        if(currentHex.landmark == null) {
            System.out.println(".");
        } else {
            System.out.println(" and there is a " + currentHex.landmark.toString() + " nearby.");
        };
    }

    //this is where the game asks the player which direction they want to travel when hex-crawling
    void hexTravel() {
        tellTerrain();
        MultipleChoiceQuestion directionQuestion = new MultipleChoiceQuestion();
        for(int i=0; i<Coordinate.directionNames.length; i++) {
            if(world.hexAtMaybe(partyCoordinate.plus(Coordinate.DIRECTIONS[i])) != null) {
                directionQuestion.addOption("Move " + Coordinate.directionNames[i] + " into " + world.hexAt(partyCoordinate.plus(Coordinate.DIRECTIONS[i])).terrain + ".");
            } else {
                directionQuestion.skipOption();
            }
        };
        int ss = directionQuestion.addOption("Stay still in " + world.hexAt(partyCoordinate).terrain.toString().toLowerCase() + ".");
        int directionChoice = directionQuestion.ask(scan);

        int navigationChances;
        if (directionChoice == ss) {
            tellTerrain();
            return;
        } else if(world.hexAt(partyCoordinate).terrain == Terrain.Hills) {
            navigationChances = 5;
        } else if(world.hexAt(partyCoordinate).terrain == Terrain.Mountains) {
            navigationChances = 4;
        } else if(world.hexAt(partyCoordinate).terrain == Terrain.Swamp) {
            navigationChances = 4;
        } else if(world.hexAt(partyCoordinate).terrain == Terrain.Woods) {
            navigationChances = 3;
        } else {
            throw new RuntimeException("terrain not supported");
        }

        int navigationRoll = dice.d6();
        int navigationFlavour = dice.d6();
        if(navigationRoll <= navigationChances) {
            partyCoordinate.move(Coordinate.DIRECTIONS[directionChoice]);
        } else {
            if(navigationFlavour==1 || navigationFlavour==2) {
                System.out.println("You got lost and veered in an unexpected direction.");
            } else if(navigationFlavour==3 || navigationFlavour==4) {
                System.out.println("You lose sight of the path you were on, have appear to have gotten a bit lost.");
            } else if(navigationFlavour==5 || navigationFlavour==6) {
                System.out.println("You have gotten lost and gone in a different direction.");
            }
            int lostDirection = dice.d6();
            int directionOffset;
            if (lostDirection > 3) {
                directionOffset = 1;
            } else {
                directionOffset = -1;
            }
            if (world.hexAtMaybe(partyCoordinate.plus(Coordinate.DIRECTIONS[(directionChoice + directionOffset + 6) % 6])) != null) {
                directionChoice = (directionChoice + directionOffset + 6) % 6;
            } else {
                directionChoice = (directionChoice - directionOffset + 6) % 6;
            }
            partyCoordinate.move(Coordinate.DIRECTIONS[directionChoice]);
        }
    }

    public void run() throws IOException {
        try {
            // FileReader extends Reader
            FileReader reader = new FileReader("saveFile.json");
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
            mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
            // T readValue(Reader src, Class<T> valueType) throws IOException, StreamReadException, DatabindException
            Character[] loadedCharacters = mapper.readValue(reader, Character[].class);
            for(int i=0; i<loadedCharacters.length; i++) {
                party.add(loadedCharacters[i]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("A new adventure awaits you ...");
        }

        while(true) {
            charGenProcedure();
            wildernessProcedure();
            //call town procedure
            //call dungeon exploration procedure
            //call encounter procedure
            //call combat procedure
        }
    }

    public void generateCharacter() {
        Character generated = new Character();
        System.out.println("---Generating a character.---");
        generated.strength = dice.d6() + dice.d6() + 3;
        System.out.println("Strength score: " + generated.strength);
        generated.dexterity = dice.d6() + dice.d6() + 3;
        System.out.println("Dexterity score: " + generated.dexterity);
        generated.intelligence = dice.d6() + dice.d6() + 3;
        System.out.println("Intelligence score: " + generated.intelligence);
        generated.charisma = dice.d6() + dice.d6() + 3;
        System.out.println("Charisma score: " + generated.charisma);

        if(generated.strength<12 && generated.dexterity<12 && generated.intelligence<12 && generated.charisma<12) {
            System.out.println("Your attributes are too low. Choose an attribute to boost to 12.");
            MultipleChoiceQuestion attributeQuestion = new MultipleChoiceQuestion();
            attributeQuestion.addOption("Strength");
            attributeQuestion.addOption("Dexterity");
            attributeQuestion.addOption("Intelligence");
            attributeQuestion.addOption("Charisma");
            int attributeChoice = attributeQuestion.ask(scan);
            if(attributeChoice == 0) {
                generated.strength = 12;
            } else if(attributeChoice == 1) {
                generated.dexterity = 12;
            } else if(attributeChoice == 2) {
                generated.intelligence = 12;
            } else if(attributeChoice == 3) {
                generated.charisma = 12;
            }
        }
        System.out.println("Now, choose a profession.");
        MultipleChoiceQuestion professionQuestion = new MultipleChoiceQuestion();
        if(generated.intelligence > 11) {
            professionQuestion.addOption(Profession.Sage.toString());
        } else {
            professionQuestion.skipOption();
        };
        if(generated.charisma > 11) {
            professionQuestion.addOption(Profession.Shaman.toString());
        } else {
            professionQuestion.skipOption();
        };
        if(generated.dexterity > 11) {
            professionQuestion.addOption(Profession.Thief.toString());
        } else {
            professionQuestion.skipOption();
        };
        if(generated.strength > 11) {
            professionQuestion.addOption(Profession.Warrior.toString());
        } else {
            professionQuestion.skipOption();
        };
        int professionChoice = professionQuestion.ask(scan);
        if(professionChoice == 0) {
            generated.profession = Profession.Sage;
        } else if(professionChoice == 1) {
            generated.profession = Profession.Shaman;
        } else if(professionChoice == 2) {
            generated.profession = Profession.Thief;
        } else if(professionChoice == 3) {
            generated.profession = Profession.Warrior;
        }

        if(generated.profession == Profession.Warrior) {
            generated.hitsMax = dice.d6()+4;
        } else if(generated.profession == Profession.Thief) {
            generated.hitsMax = dice.d6()+3;
        } else if(generated.profession == Profession.Sage) {
            generated.hitsMax = dice.d6()+2;
        } else if(generated.profession == Profession.Shaman) {
            generated.hitsMax = dice.d6()+3;
        }
        generated.hitsCurrent = generated.hitsMax;
        System.out.println("Your character has " + generated.hitsMax + " hits.");

        System.out.println("Choose a starting weapon.");
        MultipleChoiceQuestion startingWeaponQuestion = new MultipleChoiceQuestion();
        if(generated.profession == Profession.Warrior) {
            startingWeaponQuestion.addOption("Sword & shield.");
            startingWeaponQuestion.addOption("Battleaxe.");
            startingWeaponQuestion.addOption("Bow.");
        } else if(generated.profession == Profession.Thief) {
            startingWeaponQuestion.addOption("Sword & sling.");
        } else if(generated.profession == Profession.Sage) {
            startingWeaponQuestion.addOption("Staff.");
        } else if(generated.profession == Profession.Shaman) {
            startingWeaponQuestion.addOption("Sword & shield.");
            startingWeaponQuestion.addOption("Sling & shield.");
        }
        startingWeaponQuestion.ask(scan);
        
        System.out.println("What is this character's name?");
        //TODO: the following line is probably wrong?
        scan.next(); // consume the previous newline character
        generated.name = scan.nextLine();
        party.add(generated);
        //return generated;
    }

    private void charGenProcedure() {
        while(party.size()<3) {
            generateCharacter();
        }
        try {
            FileWriter writer = new FileWriter("saveFile.json");
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
            mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
            Character[] partyAsArray = party.toArray(new Character[party.size()]);
            mapper.writeValue(writer, partyAsArray);
            System.out.println("Saved game.");
        } catch (IOException e) {
            System.out.println("failed to save.");
            e.printStackTrace();
        }
    }

    private void randomEncounter() {
        int encounterRoll=dice.d6();
        int encounterChances=1;
        if(encounterRoll<=encounterChances) {
            if(time == TimeOfDay.Night) {
                System.out.println("Your rest is interrupted by the sound of something approaching.");
            } else if(weather == Weather.Rainy || weather == Weather.Stormy) {
                System.out.println("You notice something approaching through the lashing rain.");
            } else if(world.hexAt(partyCoordinate).terrain == Terrain.Mountains) {
                System.out.println("You hear pebbles tumbling and sense something approaching.");
            } else if(world.hexAt(partyCoordinate).terrain == Terrain.Woods) {
                System.out.println("The birds scatter suddenly, signalling the approach of something.");
            } else {
                System.out.println("You sense an unexpected presence nearby.");
            }
            ArrayList<Combatant> battlefield = new ArrayList<>();
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
            Battle.battleMain(dice, scan, battlefield);
        }
    }

    private void survival() {
        int forageChances;
        int fireChances;
        if (world.hexAt(partyCoordinate).terrain == Terrain.Hills && weather == Weather.Clear) {
            forageChances = 2;
            fireChances = 6;
        } else if (world.hexAt(partyCoordinate).terrain == Terrain.Hills && weather == Weather.Rainy) {
            forageChances = 1;
            fireChances = 3;
        } else if (world.hexAt(partyCoordinate).terrain == Terrain.Hills && weather == Weather.Stormy) {
            forageChances = 0;
            fireChances = 0;
        } else if (world.hexAt(partyCoordinate).terrain == Terrain.Mountains && weather == Weather.Clear) {
            forageChances = 1;
            fireChances = 5;
        } else if (world.hexAt(partyCoordinate).terrain == Terrain.Mountains && weather == Weather.Rainy) {
            forageChances = 0;
            fireChances = 2;
        } else if (world.hexAt(partyCoordinate).terrain == Terrain.Mountains && weather == Weather.Stormy) {
            forageChances = 0;
            fireChances = 0;
        } else if (world.hexAt(partyCoordinate).terrain == Terrain.Swamp && weather == Weather.Clear) {
            forageChances = 3;
            fireChances = 4;
        } else if (world.hexAt(partyCoordinate).terrain == Terrain.Swamp && weather == Weather.Rainy) {
            forageChances = 2;
            fireChances = 1;
        } else if (world.hexAt(partyCoordinate).terrain == Terrain.Swamp && weather == Weather.Stormy) {
            forageChances = 1;
            fireChances = 0;
        } else if (world.hexAt(partyCoordinate).terrain == Terrain.Woods && weather == Weather.Clear) {
            forageChances = 3;
            fireChances = 6;
        } else if (world.hexAt(partyCoordinate).terrain == Terrain.Woods && weather == Weather.Rainy) {
            forageChances = 4;
            fireChances = 4;
        } else if (world.hexAt(partyCoordinate).terrain == Terrain.Woods && weather == Weather.Stormy) {
            forageChances = 3;
            fireChances = 2;
        } else {
            throw new RuntimeException("weather/terrain combo not supported");
        }

        MultipleChoiceQuestion survivalQuestion = new MultipleChoiceQuestion();
        Boolean fireChosen = false;
        Boolean forageChosen = false;
        survivalQuestion.addOption("Light a fire.");
        survivalQuestion.addOption("Forage for food.");
        survivalQuestion.addOption("Forage for food and light a fire.");
        int survivalChoice = survivalQuestion.ask(scan);
        int fireRoll=dice.d6();
        int forageRoll=dice.d6();
        int fireFlavour=dice.d6();
        int forageFlavour=dice.d6();
        
        if(survivalChoice==0) {
            fireChosen = true;
            forageChosen = false;
        } else if(survivalChoice==1) {
            forageChosen = true;
            fireChosen = false;
        } else if(survivalChoice==2) {
            fireChosen = true;
            forageChosen = true;
        }

        if(forageChosen==true) {
            if(forageRoll<=forageChances) {
                if(world.hexAt(partyCoordinate).terrain==Terrain.Hills) {
                    if(forageFlavour==1 || forageFlavour==2) {
                        System.out.println("You find some wild herbs, it's not much, but it will do.");
                    } else if(forageFlavour==3 || forageFlavour==4) {
                        System.out.println("You pull some wild onions out of the ground, this should do nicely.");
                    } else if(forageFlavour==5 || forageFlavour==5) {
                        System.out.println("You catch a wild hornhare, quite the treat.");
                    };
                } else if(world.hexAt(partyCoordinate).terrain==Terrain.Mountains) {
                    if(forageFlavour==1 || forageFlavour==2) {
                        System.out.println("You find some surprisingly-filling lichen on a rock.");
                    } else if(forageFlavour==3 || forageFlavour==4) {
                        System.out.println("You find a bed of thick moss. It's not the tastiest, but it is nutritious.");
                    } else if(forageFlavour==5 || forageFlavour==5) {
                        System.out.println("You find a snake nest and take a few of the eggs.");
                    };
                } else if(world.hexAt(partyCoordinate).terrain==Terrain.Swamp) {
                    if(forageFlavour==1 || forageFlavour==2) {
                        System.out.println("You find some bitter-tasting but edible herbs growing beside the water.");
                    } else if(forageFlavour==3 || forageFlavour==4) {
                        System.out.println("You catch some small freshwater crabs. They nip at your fingers.");
                    } else if(forageFlavour==5 || forageFlavour==5) {
                        System.out.println("You pull a fat fish out of the water, this should fill you up.");
                    };
                } else if(world.hexAt(partyCoordinate).terrain==Terrain.Woods) {
                    if(forageFlavour==1 || forageFlavour==2) {
                        System.out.println("You find a patch of plump, brown mushrooms. They're probably safe to eat.");
                    } else if(forageFlavour==3 || forageFlavour==4) {
                        System.out.println("You find a bush teeming with rich, red berries.");
                    } else if(forageFlavour==5 || forageFlavour==5) {
                        System.out.println("You find a bird's nest in a tree and take a few eggs for yourself.");
                    };
                };
            } else {
                if(world.hexAt(partyCoordinate).terrain==Terrain.Hills) {
                    if(forageFlavour==1 || forageFlavour==2) {
                        System.out.println("You find nothing substantial among the tall grass.");
                    } else if(forageFlavour==3 || forageFlavour==4) {
                        System.out.println("You try to catch a hornhare, but it gets away.");
                    } else if(forageFlavour==1 || forageFlavour==2) {
                        System.out.println("You wander around the area until you are forced to give up the hunt.");
                    }
                } else if(world.hexAt(partyCoordinate).terrain==Terrain.Mountains) {
                    if(forageFlavour==1 || forageFlavour==2) {
                        System.out.println("This barren mountainside yields no sustenance.");
                    } else if(forageFlavour==3 || forageFlavour==4) {
                        System.out.println("You find only tough grass and lichen.");
                    } else if(forageFlavour==1 || forageFlavour==2) {
                        System.out.println("You only hear the cawing of crows that eye you hungrily.");
                    }
                } else if(world.hexAt(partyCoordinate).terrain==Terrain.Swamp) {
                    if(forageFlavour==1 || forageFlavour==2) {
                        System.out.println("You trudge about for a while, but return to camp empty-handed.");
                    } else if(forageFlavour==3 || forageFlavour==4) {
                        System.out.println("You see some fish in the water, but don't manage to catch any.");
                    } else if(forageFlavour==1 || forageFlavour==2) {
                        System.out.println("You grab a fat crawfish, but it nips you and you drop it.");
                    }
                } else if(world.hexAt(partyCoordinate).terrain==Terrain.Woods) {
                    if(forageFlavour==1 || forageFlavour==2) {
                        System.out.println("You try to catch a squirrel, but it evades you.");
                    } else if(forageFlavour==3 || forageFlavour==4) {
                        System.out.println("You find nothing but pine cones and rotten acorns.");
                    } else if(forageFlavour==1 || forageFlavour==2) {
                        System.out.println("You nibble a few plants, but they are all to bitter to eat.");
                    }
                }
                if(fireChosen==true) {
                    if(fireRoll<=fireChances) {
                        if(fireFlavour == 1 || fireFlavour == 2) {
                            System.out.println("You warm yourself by a crackling campfire.");
                        } else if(fireFlavour == 3 || fireFlavour == 4) {
                            System.out.println("The heat of the campfire warms you to your bones.");
                        } else if(fireFlavour == 5 || fireFlavour == 6) {
                            System.out.println("You manage to light a roaring fire.");
                        }
                    } else {
                        if(world.hexAt(partyCoordinate).terrain == Terrain.Mountains) {
                            System.out.println("You find no decent, dry wood to burn around here.");
                        } else if(world.hexAt(partyCoordinate).terrain == Terrain.Swamp) {
                            System.out.println("All the wood you find around here is damp and rotten to light.");
                        } else if(weather==Weather.Rainy || weather==Weather.Stormy) {
                            System.out.println("It's simply too damp to get a fire lit tonight.");
                        } else if(fireFlavour==1 || fireFlavour==2) {
                            System.out.println("You don't manage to get a fire lit.");
                        } else if(fireFlavour==3 || fireFlavour==4) {
                            System.out.println("Try as you might, your campfire just won't catch.");
                        } else if(fireFlavour==5 || fireFlavour==6) {
                            System.out.println("You fail to light a fire, you'll have to go without tonight.");
                        }
                    }
                }
            }
        }
    }

    private void tellDay() {
        System.out.print("---It is " + time.toString().toLowerCase() + " of day " + day);
        int weatherFlavour = dice.d6();
        if(weather == Weather.Clear) {
            if(weatherFlavour == 1 || weatherFlavour == 2) {
                System.out.println(", the sky is clear and blue.---");
            } else if (weatherFlavour == 3 || weatherFlavour == 4) {
                System.out.println(", the blue sky is populated with fluffly, white clouds.---");
            } else if (weatherFlavour == 5 || weatherFlavour == 6) {
                System.out.println(", a pleasant breeze is blowing.---");
            }
        } else if(weather == Weather.Rainy) {
            if(weatherFlavour == 1 || weatherFlavour == 2) {
                System.out.println(", the sky is grey and rain is pouring.---");
            } else if (weatherFlavour == 3 || weatherFlavour == 4) {
                System.out.println(", a harsh wind is blowing wind into your face.---");
            } else if (weatherFlavour == 5 || weatherFlavour == 6) {
                System.out.println(", the ground is wet and the rain is lashing down.---");
            }
        } else if(weather == Weather.Stormy) {
            if(weatherFlavour == 1 || weatherFlavour == 2) {
                System.out.println(", a storm is raging all around.---");
            } else if (weatherFlavour == 3 || weatherFlavour == 4) {
                System.out.println(", the wind howls and the sound of thunder fills the air.---");
            } else if (weatherFlavour == 5 || weatherFlavour == 6) {
                System.out.println(", the sky is dark with crackling stormclouds.---");
            }
        }
    }
    
    private void wildernessProcedure() {
        if(time == TimeOfDay.Morning) {
            weather = weatherGen.generate();
            tellDay();
            hexTravel();
            randomEncounter();
            time = TimeOfDay.Afternoon;
        } else if (time == TimeOfDay.Afternoon) {
            tellDay();
            hexTravel();
            randomEncounter();
            time = TimeOfDay.Night;
        } else if (time == TimeOfDay.Night) {
            tellDay();
            tellTerrain();
            System.out.println("It's time to set up camp for the night.");
            survival();
            randomEncounter();
            day ++;
            time = TimeOfDay.Morning;
        }
    }
}
