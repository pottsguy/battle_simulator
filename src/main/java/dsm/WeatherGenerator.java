package dsm;

import java.util.Random;

public class WeatherGenerator {

    Random rndm;

    WeatherGenerator(Random rndm) {
        this.rndm = rndm;
    }

    public Weather generate() {

        int firstRoll = rndm.nextInt(6);
        int secondRoll = rndm.nextInt(6);

        int lowerRoll = Math.min(firstRoll, secondRoll);

        if(lowerRoll < 3) {
            return Weather.Pleasant;
        } else if(lowerRoll < 5) {
            return Weather.Rain;
        } else {
            return Weather.Storm;
        }
    }
}
