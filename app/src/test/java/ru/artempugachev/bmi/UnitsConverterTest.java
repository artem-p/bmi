package ru.artempugachev.bmi;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class UnitsConverterTest {
    private UnitsConverter mUnitsConverter;
    private final static double EPSILON = 0.001d;

    @Before
    public void setUp() {
        mUnitsConverter = new UnitsConverter();
    }

    @Test
    public void kg_to_lb() {
        float kg = 1f;
        float lb = mUnitsConverter.kgToLb(kg);
        assertEquals(2.2046223302272f, lb, EPSILON);

        kg = 73f;
        lb = mUnitsConverter.kgToLb(kg);
        assertEquals(160.93743010658f, lb, EPSILON);
    }

    @Test
    public void lb_to_kg() {
        float lb = 2.2046223302272f;
        float kg = mUnitsConverter.lbToKg(lb);
        assertEquals(1f, kg, EPSILON);

        lb = 160.93743010658f;
        kg = mUnitsConverter.lbToKg(lb);
        assertEquals(73f, kg, EPSILON);

        lb = 0;
        kg = mUnitsConverter.lbToKg(lb);
        assertEquals(0f, kg, EPSILON);
    }

    @Test
    public void cm_to_int_ft() {
        int cm = 178;
        int ft = mUnitsConverter.cmToIntFt(cm);

        assertEquals(5, ft);

        cm = 185;
        ft = mUnitsConverter.cmToIntFt(cm);
        assertEquals(6, ft);
    }
}
