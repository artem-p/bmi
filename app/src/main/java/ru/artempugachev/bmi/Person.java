package ru.artempugachev.bmi;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * A person with height and weight.
 * Can compute Bmi
 *
 */

public class Person {
    private int height;
    private float weight;

    /**
     * @param height height in cm
     * @param weight weight in kg
     */

    public Person(int height, float weight) {
        this.height = height;
        this.weight = weight;
    }

    /**
     * Getting Bmi based on height and weight
     * https://en.wikipedia.org/wiki/Body_mass_index
     */
    public String getBmi() {

        if (this.height == 0 || this.weight == 0) return "0";

        float heightInMeters = height / 100F;

        float bmi = weight / (heightInMeters * heightInMeters);

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat) nf;

        df.setMaximumFractionDigits(1);
        df.setMinimumFractionDigits(1);

        return df.format(bmi);
    }
}
