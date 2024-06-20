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
            // PreparedStatement stmt = dbConn.prepareStatement("SELECT name FROM monsters");
            // ResultSet results = stmt.executeQuery();
            // while (results.next()) {
            //     System.out.println(results.getString("name"));
            // }

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
            PreparedStatement stmt = dbConn.prepareStatement(query);

            // to access an array value e.g.: foreNames[4]

            for (int k = 0; k < 10000; k++) {
                stmt.setString(1, foreNames[rndm.nextInt(foreNames.length)] + " " + surNames[rndm.nextInt(surNames.length)]);
                //int attrs[] = new int[4];
                // int maxAttrIndex = -1; // the index (aka the place) of the highest attribute we've seen so far

                // the indices of the highest attributes we've seen so far
                ArrayList<Integer> maxAttrsIndices = new ArrayList<Integer>();
                int maxAttrValue = 0;  // the value of the highest attribute we've seen so far
                for (int i = 0; i < 4; i++) {
                    int roll1 = rndm.nextInt(5)+2;
                    int roll2 = rndm.nextInt(5)+2;
                    int roll3 = rndm.nextInt(5)+2;
                    int attrValue = roll1 + roll2 + roll3;
                    //attrs[i] = attrValue;
                    stmt.setInt(i+3, attrValue);

                    if (attrValue == maxAttrValue) {
                        //add i to maxAttrIndices
                        maxAttrValue = attrValue;
                        maxAttrsIndices.add(i);
                        //maxAttrIndex = i;
                    } else if (attrValue > maxAttrValue) {
                        //delete everything from maxAttrIndices
                        maxAttrValue = attrValue;
                        maxAttrsIndices.clear();
                        maxAttrsIndices.add(i);
                        //add i to maxAttrIndices
                    }
                }


                //stmt.setInt(3, attrs[0]);
                //stmt.setInt(4, attrs[1]);
                //stmt.setInt(5, attrs[2]);
                //stmt.setInt(6, attrs[3]);

                String charClass[] = new String[] {
                    "Warrior", "Thief", "Sage", "Shaman"
                };

                //randomly choose an item from maxAttrIndices to be the value of charClass[]
                

                if (maxAttrValue < 13) {
                    stmt.setString(2, "Normie");
                } else {
                    //stmt.setString(2, charClass[maxAttrIndex]);
                    stmt.setString(2, charClass[maxAttrsIndices.get(rndm.nextInt(maxAttrsIndices.size()))]);
                    //foreNames[rndm.nextInt(foreNames.length)]
                };
                stmt.executeUpdate();
                // stmt.setString(2, charClass[maxAttrIndex]);
                // stmt.executeUpdate();
            }

            dbConn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}