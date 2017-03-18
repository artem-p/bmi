package ru.artempugachev.bmi;

/**
 * A person with height and weight.
 * Can compute Bmi
 */

public class Person {
    private int height;
    private long weight;

    public Person(int height, long weight) {
        this.height = height;
        this.weight = weight;
    }

    /**
     * Getting Bmi based on height and weight
     * https://en.wikipedia.org/wiki/Body_mass_index
     *
    * */
    public String getBmi() {
        return "";
    }
}
