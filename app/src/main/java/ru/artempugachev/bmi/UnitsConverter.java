package ru.artempugachev.bmi;

/**
 * Convert height and weight units from metric to imperial and vice versa
 * 1 kg = 2.2046223302272 lb
 * 1 ft = 30.48 cm
 * 1 ft = 12 in
 */

public class UnitsConverter {
    private static final float LBS_IN_KG = 2.2046223302272f;
    private static final float CM_IN_FT = 30.48f;

    public float kgToLb(float kg) {
        return kg * LBS_IN_KG;
    }

    public float lbToKg(float lb) {
        return lb / LBS_IN_KG;
    }

    /**
     * Convert centimeters to integer feet value
     * */
    public int cmToIntFt(int cm) {
        float ft = cm / CM_IN_FT;
        int ift = (int) ft;
        return ift;
    }

    /**
     * Convert centimeters to remaining inches from feet
     * */
    public int cmToRemainInches(int cm) {
        int ft = cmToIntFt(cm);
        int inches = Math.round (cm / (CM_IN_FT / 12));
        int remainInches = inches - ft * 12;
        return remainInches;
    }
}
