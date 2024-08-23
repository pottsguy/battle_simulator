package dsm;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

// this prompts and reads a response from the player, needs a function to add options to multiple choice, outputs an int
class MultipleChoiceQuestion {
    private ArrayList<String> options;

    public MultipleChoiceQuestion() {
        this.options = new ArrayList<String>();
    }

    public int addOption(String description) {
        options.add(description);
        return options.size() - 1;
    }

    public void skipOption() {
        options.add(null);
    }

    //ask() automatically prints option indexes; ask the question and return the index of the option chosen
    public int ask(Scanner scan) {
        if (options.size()==0) {
            throw new RuntimeException("Question asked with no options!");
        }
        int decision = -1;
        while (decision == -1) {
            System.out.println("What do you do?");
            for (int i=0; i<options.size(); i++) {
                if(options.get(i) != null) {
                    System.out.println(i+1 + ") " + options.get(i));
                }
            }
            try {
                decision = scan.nextInt() - 1;
            } catch (InputMismatchException ex) {
                scan.next();
                decision= -1;
            }
            if (decision<0 || decision>=options.size() || options.get(decision) == null) {
                System.out.println("Not a valid option, try again.");
                decision=-1;
            }
        }
        System.out.println("You " + options.get(decision));
        return decision;
    }
}