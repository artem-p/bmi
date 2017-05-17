package ru.artempugachev.bmi;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText mHeightMetricEditText;
    private EditText mHeightImperialFeetEditText;
    private EditText mHeightImperialInchesEditText;
    private EditText mWeightEditText;
    private TextView mBmiTextView;

    private static final String TAG = MainActivity.class.getSimpleName();
    private  Toast CONVERSION_ERROR_TOAST;

    private final boolean mIsMetric = false; // todo from preferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CONVERSION_ERROR_TOAST = Toast.makeText(MainActivity.this,
                R.string.conversion_error, Toast.LENGTH_SHORT);


        setUpViews();
        setListeners();
        loadCurrentValsFromPreferences(getPreferences(Context.MODE_PRIVATE), mIsMetric);
        updateBmi();
    }


    private void setUpViews() {
        createHeightWeightInputs(mIsMetric);
        mBmiTextView = (TextView) findViewById(R.id.tvBmi);
    }


    /**
     * Create views for height and weight inputs
     * We use different views for metric and imperial input
     * */
    private void createHeightWeightInputs(boolean isMetric) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View heightInputMetric = inflater.inflate(R.layout.height_input_metric, null);
        View heightInputImperial = inflater.inflate(R.layout.height_input_imperial, null);
        View weightInputMetric = inflater.inflate(R.layout.weight_input_metric, null);
        View weightInputImperial = inflater.inflate(R.layout.weight_input_imperial, null);

        FrameLayout heightInputContainer = (FrameLayout) findViewById(R.id.heightInputContainer);
        FrameLayout weightInputContainer = (FrameLayout) findViewById(R.id.weightInputContainer);

        if (isMetric) {
            heightInputContainer.addView(heightInputMetric);
            weightInputContainer.addView(weightInputMetric);
        } else {
            heightInputContainer.addView(heightInputImperial);
            weightInputContainer.addView(weightInputImperial);
        }

        mHeightMetricEditText = (EditText) findViewById(R.id.etHeightMetric);
        mHeightImperialFeetEditText = (EditText) findViewById(R.id.etHeightImperialFeet);
        mHeightImperialInchesEditText = (EditText) findViewById(R.id.etHeightImperialInches);
        mWeightEditText = (EditText) findViewById(R.id.etWeight);
    }


    /**
     * Get height and weight values from inputs.
     * Compute Bmi and display it
     * */
    private void updateBmi() {
        Person person = buildPersonFromUi();
        if (person != null) {
            String bmi = person.getBmi();
            mBmiTextView.setText(getString(R.string.bmi, bmi));
            mBmiTextView.setTag(bmi);   // save plain bmi value as tag to use it when saving vals to prefs
        }
    }


    /**
     * Get values from input fields and create Person object
     * */
    private Person buildPersonFromUi() {
        int heightCm = 0;
        float weightKg = 0;
        String heightStr = "";
        String weightStr = "";

        if (mIsMetric) {
            heightStr = String.valueOf(mHeightMetricEditText.getText());
            weightStr = String.valueOf(mWeightEditText.getText());

            if (!(heightStr.isEmpty() || weightStr.isEmpty())) {
                try {
                    heightCm = Integer.parseInt(heightStr);
                    weightKg = Float.parseFloat(weightStr);
                } catch (Exception e) {
                    CONVERSION_ERROR_TOAST.show();
                    return null;
                }
            }
        } else {
            String feetStr = String.valueOf(mHeightImperialFeetEditText.getText());
            String inchStr = String.valueOf(mHeightImperialInchesEditText.getText());
            String lbStr = String.valueOf(mWeightEditText.getText());

            int feet;
            int inch;
            float lb;

            try {
                feet = Integer.parseInt(feetStr);
                inch = Integer.parseInt(inchStr);
                lb = Float.parseFloat(lbStr);

                UnitsConverter unitsConverter = new UnitsConverter();
                heightCm = unitsConverter.feetToCm(feet) + unitsConverter.inchToCm(inch);
                weightKg = unitsConverter.lbToKg(lb);
            } catch (Exception e) {
                CONVERSION_ERROR_TOAST.show();
                return null;
            }
        }

        return new Person(heightCm, weightKg);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveCurrentValsToPreferences(getPreferences(Context.MODE_PRIVATE));
    }


    /**
     * Save current values to preferences
     * */
    public void saveCurrentValsToPreferences(SharedPreferences sharedPreferences) {
        String sHeight = String.valueOf(mHeightMetricEditText.getText());
        String sWeight = String.valueOf(mWeightEditText.getText());
        String sBmi = String.valueOf(mBmiTextView.getTag());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.pref_height_cm), sHeight);
        editor.putString(getString(R.string.pref_weight_kg), sWeight);
        editor.putString(getString(R.string.pref_cur_bmi), sBmi);
        editor.apply();
    }


    /**
     * get current vals from preferences and display them
     * */
    private void loadCurrentValsFromPreferences(SharedPreferences sharedPreferences, boolean isMetric) {
        if (isMetric) {
            String heightCm = sharedPreferences.getString(getString(R.string.pref_height_cm), "");
            String weightKg = sharedPreferences.getString(getString(R.string.pref_weight_kg), "");

            if (!heightCm.equals("")) {
                mHeightMetricEditText.setText(heightCm);
            }

            if (!weightKg.equals("")) {
                mWeightEditText.setText(weightKg);
            }

        } else {
            String heightFeet = sharedPreferences.getString(getString(R.string.pref_height_feet), "");
            String heightInch = sharedPreferences.getString(getString(R.string.pref_height_inch), "");
            String weightLb = sharedPreferences.getString(getString(R.string.pref_weight_lb), "");

            mHeightImperialFeetEditText.setText(heightFeet);
            mHeightImperialInchesEditText.setText(heightInch);
            mWeightEditText.setText(weightLb);
        }

        String bmi = sharedPreferences.getString(getString(R.string.pref_cur_bmi), "");
        if (!bmi.equals("")) {
            mBmiTextView.setText(getString(R.string.bmi, bmi));
        }
    }

    private void setListeners() {
        if (mHeightMetricEditText != null) {
            mHeightMetricEditText.addTextChangedListener(new HeightWeightTextWatcher());
        }

        mWeightEditText.addTextChangedListener(new HeightWeightTextWatcher());
    }


    private class HeightWeightTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            updateBmi();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
