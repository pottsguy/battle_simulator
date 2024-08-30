package dsm;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;

public class Main {

    public static void mainTest(String args[]) throws StreamWriteException, DatabindException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

        // File outputFile = new File("myTestOutput.json");
        // Character myValue = new Character("Alan Johnson", Profession.Thief);
        // myValue.dexterity = 300;
        // mapper.writeValue(outputFile, myValue);
        // System.out.println("Wrote to myTestOutput.json");
        FileReader reader = new FileReader("myTestOutput.json");
        Character aj = mapper.readValue(reader, Character.class);
        System.out.println("Loaded " + aj.name + " with charisma " + aj.charisma);
    }

    public static void main(String args[]) throws IOException {
        Dice dice = new Dice();
        Scanner scan = new Scanner(System.in);

        System.out.println("Welcome, daring mercenary, to the subterranean realm!");
        Campaign myCampaign = new Campaign(dice, scan);
        myCampaign.run();
    }
}
