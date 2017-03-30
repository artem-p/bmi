package ru.artempugachev.bmi;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText mHeightEditText;
    private EditText mWeihtEditText;
    private TextView mBmiTextView;

    private static final String TAG = MainActivity.class.getSimpleName();
    private  Toast CONVERSION_ERROR_TOAST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CONVERSION_ERROR_TOAST = Toast.makeText(MainActivity.this,
                R.string.conversion_error, Toast.LENGTH_SHORT);

        mHeightEditText = (EditText) findViewById(R.id.etHeight);
        mWeihtEditText = (EditText) findViewById(R.id.etWeight);
        mBmiTextView = (TextView) findViewById(R.id.tvBmi);

        setListeners();
        updateBmi();

        loadCurrentValsFromPreferences(getPreferences(Context.MODE_PRIVATE));
    }

    /**
     * Get height and weight values from inputs.
     * Compute Bmi and display it
     * */
    private void updateBmi() {
        int height = 0;
        float weight = 0;

        String sHeight = String.valueOf(mHeightEditText.getText());
        String sWeight = String.valueOf(mWeihtEditText.getText());

        if (!(sHeight.isEmpty() || sWeight.isEmpty())) {
            try {
                height = Integer.parseInt(sHeight);
                weight = Float.parseFloat(sWeight);
            } catch (Exception e) {
                CONVERSION_ERROR_TOAST.show();
                return;
            }
        }

        Person person = new Person(height, weight);

        String bmi = person.getBmi();

        mBmiTextView.setText(getString(R.string.bmi, bmi));

        // save plain bmi value as tag to use it when saving vals to prefs
        mBmiTextView.setTag(bmi);
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
        String sHeight = String.valueOf(mHeightEditText.getText());
        String sWeight = String.valueOf(mWeihtEditText.getText());
        String sBmi = String.valueOf(mBmiTextView.getTag());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.pref_cur_height), sHeight);
        editor.putString(getString(R.string.pref_cur_weight), sWeight);
        editor.putString(getString(R.string.pref_cur_bmi), sBmi);
        editor.apply();
    }


    /**
     * get current vals from preferences and display them
     * */
    private void loadCurrentValsFromPreferences(SharedPreferences sharedPreferences) {
        String height = sharedPreferences.getString(getString(R.string.pref_cur_height), "");
        String weight = sharedPreferences.getString(getString(R.string.pref_cur_weight), "");
        String bmi = sharedPreferences.getString(getString(R.string.pref_cur_bmi), "");

        if (!height.equals("")) {
            mHeightEditText.setText(height);
        }

        if (!weight.equals("")) {
            mWeihtEditText.setText(weight);
        }

        if (!weight.equals("")) {
            mBmiTextView.setText(getString(R.string.bmi, bmi));
        }
    }

    private void setListeners() {
        mHeightEditText.addTextChangedListener(new HeightWeightTextWatcher());
        mWeihtEditText.addTextChangedListener(new HeightWeightTextWatcher());
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
