package ru.artempugachev.bmi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText mHeightCmEditText;
    private EditText mHeightFeetEditText;
    private EditText mHeightInchesEditText;
    private EditText mWeightEditText;
    private TextView mBmiTextView;

    private static final String TAG = MainActivity.class.getSimpleName();
    private  Toast CONVERSION_ERROR_TOAST;

    private boolean mIsMetric;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CONVERSION_ERROR_TOAST = Toast.makeText(MainActivity.this,
                R.string.conversion_error, Toast.LENGTH_SHORT);

        mIsMetric = getResources().getBoolean(R.bool.isMetricDefault);
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
     * Check if metric or imperial units
     * Read it from preferences
     * In prefs we store strings, so transform it to boolean, true if metric
     * */
    private boolean isMetric() {
        // todo 2 test
        boolean isMetric = getResources().getBoolean(R.bool.isMetricDefault);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String unitsStr = sharedPreferences.getString(getString(R.string.pref_units_key),
                getString(R.string.pref_units_value_metric));

        if (unitsStr.equals(getString(R.string.pref_units_value_metric))) {
            isMetric = true;
        } else {
            isMetric = false;
        }

        return isMetric;
    }

    // todo 1 onSharedPreferencesChangedListener

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

        mHeightCmEditText = (EditText) findViewById(R.id.etHeightMetric);
        mHeightFeetEditText = (EditText) findViewById(R.id.etHeightImperialFeet);
        mHeightInchesEditText = (EditText) findViewById(R.id.etHeightImperialInches);
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


        if (mIsMetric) {
            String cmStr = String.valueOf(mHeightCmEditText.getText());
            String kgStr = String.valueOf(mWeightEditText.getText());

            if (!(cmStr.isEmpty() || kgStr.isEmpty())) {
                try {
                    heightCm = Integer.parseInt(cmStr);
                    weightKg = Float.parseFloat(kgStr);
                } catch (Exception e) {
                    CONVERSION_ERROR_TOAST.show();
                    return null;
                }
            }
        } else {
            String feetStr = String.valueOf(mHeightFeetEditText.getText());
            String inchStr = String.valueOf(mHeightInchesEditText.getText());
            String lbStr = String.valueOf(mWeightEditText.getText());

            int feet;
            int inch;
            float lb;

            if (!(feetStr.isEmpty() || inchStr.isEmpty() || lbStr.isEmpty())) {

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
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (mIsMetric) {
            String sHeightCm = String.valueOf(mHeightCmEditText.getText());
            String sWeightKg = String.valueOf(mWeightEditText.getText());

            editor.putString(getString(R.string.pref_height_cm), sHeightCm);
            editor.putString(getString(R.string.pref_weight_kg), sWeightKg);

        } else {
            String sHeightFeet = String.valueOf(mHeightFeetEditText.getText());
            String sHeightInch = String.valueOf(mHeightInchesEditText.getText());
            String sWeightLb = String.valueOf(mWeightEditText.getText());

            editor.putString(getString(R.string.pref_height_feet), sHeightFeet);
            editor.putString(getString(R.string.pref_height_inch), sHeightInch);
            editor.putString(getString(R.string.pref_weight_lb), sWeightLb);
        }

        String sBmi = String.valueOf(mBmiTextView.getTag());
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
                mHeightCmEditText.setText(heightCm);
            }

            if (!weightKg.equals("")) {
                mWeightEditText.setText(weightKg);
            }

        } else {
            String heightFeet = sharedPreferences.getString(getString(R.string.pref_height_feet), "");
            String heightInch = sharedPreferences.getString(getString(R.string.pref_height_inch), "");
            String weightLb = sharedPreferences.getString(getString(R.string.pref_weight_lb), "");

            mHeightFeetEditText.setText(heightFeet);
            mHeightInchesEditText.setText(heightInch);
            mWeightEditText.setText(weightLb);
        }

        String bmi = sharedPreferences.getString(getString(R.string.pref_cur_bmi), "");
        if (!bmi.equals("")) {
            mBmiTextView.setText(getString(R.string.bmi, bmi));
        }
    }

    private void setListeners() {
        if (mHeightCmEditText != null) {
            mHeightCmEditText.addTextChangedListener(new HeightWeightTextWatcher());
        }

        if (mHeightFeetEditText != null) {
            mHeightFeetEditText.addTextChangedListener(new HeightWeightTextWatcher());
        }

        if (mHeightInchesEditText != null) {
            mHeightInchesEditText.addTextChangedListener(new HeightWeightTextWatcher());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_settings:
                Intent settingsActivityIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsActivityIntent);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
