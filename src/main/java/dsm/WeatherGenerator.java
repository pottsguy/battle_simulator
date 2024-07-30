package dsm;

public class WeatherGenerator {

    Dice dice;

    WeatherGenerator(Dice dice) {
        this.dice = dice;
    }

    public Weather generate() {

        int firstRoll = dice.d6();
        int secondRoll = dice.d6();

        int lowerRoll = Math.min(firstRoll, secondRoll);

        if(lowerRoll < 4) {
            return Weather.Clear;
        } else if(lowerRoll < 6) {
            return Weather.Rainy;
        } else {
            return Weather.Stormy;
        }
    }
}
