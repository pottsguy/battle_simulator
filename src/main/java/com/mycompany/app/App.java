package com.mycompany.app;

// mvn assembly:assembly -DdescriptorId=jar-with-dependencies && java -cp ./target/db_test-1.0-SNAPSHOT-jar-with-dependencies.jar com.mycompany.app.App

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws ClassNotFoundException
    {
        Class.forName("org.mariadb.jdbc.Driver");
        String url = "jdbc:mariadb://localhost:3306/monsterManual";
        try {
            Connection dbConn = DriverManager.getConnection(url, "guy", "1234");

            String foreNames[] = new String[] {
                "Antonio", "Imelda", "Pasqualina", "Gennaro", "Arca", "Porta", "Lama", "Poe", "Clerici", "Dommario", "Krochi", "Christine", "Pugnala", "Giovanna", "Poppea", "Concetta", "Mortaccio", "Cavallo", "Ramba", "O'Sole", "Ambrojoe", "Gallo", "Divano", "Zi'Assunta", "Sigma", "Robbert", "She-Moon", "Santa",
                "Ariaric", "Aoric", "Athanaric", "Rothesteus", "Winguric", "Alavivus", "Fritigern", "Athaulf", "Sigeric", "Wallia", "Theodoric", "Thorismund", "Euric", "Gesalec", "Theudis", "Theudigisel", "Agila", "Athanagild", "Luiva", "Liuvigild", "Hermenegild", "Recarred", "Segga", "Argimund", "Witteric", "Gundemar",
                "Sisebut", "Swinthila", "Recimmer", "Sissenand", "Iudila", "Chintila", "Tulga", "Chindaswinth", "Recceswinth", "Froia", "Wamba", "Hilderic", "Paul", "Erwig", "Egica", "Suniefred", "Wittiza", "Roderic", "Oppas", "Ardo"
            }; 

            String surNames[] = new String[] {
                "Mcintyre", "Montoya", "Blankenship", "Nixon", "Briggs", "Lozano",
                "Strickland", "Reilly", "Good", "Coleman", "Ho", "Phelps",
                "Ferguson", "Tate", "Jackson", "Pearson", "Logan", "Macias",
                "Gates",
            };

            Random rndm = new Random();

            String query = "INSERT INTO pregens (name, class, strength, dexterity, intelligence, charisma) VALUES (?,?,?,?,?,?)";
            final int GEN_ATTR_START_IX = 3; // remember, SQL uses one-based indices (unlike java which is zero-based)
            final int GEN_ATTR_COUNT = 4;
            PreparedStatement stmt = dbConn.prepareStatement(query);

            for (int k = 0; k < 10000; k++) {
                stmt.setString(1, foreNames[rndm.nextInt(foreNames.length)] + " " + surNames[rndm.nextInt(surNames.length)]);

                //generates stats for str, dex, int & cha with 3d6 (re-roll 1's, giving a 2-6 range)
                ArrayList<Integer> maxAttrsIndices = new ArrayList<Integer>();
                int maxAttrValue = 0;
                for (int i = 0; i < GEN_ATTR_COUNT; i++) {
                    int roll1 = rndm.nextInt(5)+2;
                    int roll2 = rndm.nextInt(5)+2;
                    int roll3 = rndm.nextInt(5)+2;
                    int attrValue = roll1 + roll2 + roll3;
                    stmt.setInt(GEN_ATTR_START_IX + i, attrValue);

                    //keeps track of highest stat to determine class
                    if (attrValue == maxAttrValue) {
                        maxAttrValue = attrValue;
                        maxAttrsIndices.add(i);
                    } else if (attrValue > maxAttrValue) {
                        maxAttrValue = attrValue;
                        maxAttrsIndices.clear();
                        maxAttrsIndices.add(i);
                    }
                }

                // each of these base classes corresponds to an attribute (respective of position in the query)
                String charClass[] = new String[] {
                    "Warrior", "Thief", "Sage", "Shaman"
                };

                //determines class based on highest attribute
                if (maxAttrValue < 13) {
                    stmt.setString(2, "Normie");
                } else {
                    //randomises class if there are two equal maximum attributes
                    stmt.setString(2, charClass[maxAttrsIndices.get(rndm.nextInt(maxAttrsIndices.size()))]);
                };
                stmt.executeUpdate();
            }

            dbConn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}