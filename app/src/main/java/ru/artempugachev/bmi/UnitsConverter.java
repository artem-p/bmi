package ru.artempugachev.bmi;

/**
 * Convert height and weight units from metric to imperial and vice versa
 * 1 kg = 2.2046223302272 lb
 */

public class UnitsConverter {
    private static final float LBS_IN_KG = 2.2046223302272f;

    public float kgToLb(float kg) {
        return kg * LBS_IN_KG;
    }

    public float lbToKg(float lb) {
        return lb / LBS_IN_KG;
    }
}
