package ru.artempugachev.bmi;

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
        } else {
            // todo empty input
        }

        Person person = new Person(height, weight);

        String bmi = person.getBmi();

        mBmiTextView.setText(getString(R.string.bmi, bmi));
    }

    private void setListeners() {
        mHeightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mWeihtEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
