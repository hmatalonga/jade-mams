package jadeproject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by user on 16-01-2016.
 */
public class TimeSlot {
    private double preference;

    public TimeSlot() {
        this.setPreference(generatePreference(0.3));
    }

    private double generatePreference(double probability) {
        double val = roundP(Math.random());
        if (val < probability)
            return 0.0;
        return val;
    }

    public double getPreference() {
        return preference;
    }

    public void setPreference(double preference) {
        this.preference = preference;
    }

    private double roundP(double d) {
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.FLOOR);
        return Double.valueOf(df.format(d));
    }

    @Override
    public String toString() {
        return String.valueOf(preference);
    }
}
