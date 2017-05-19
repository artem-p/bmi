package ru.artempugachev.bmi;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import ru.artempugachev.bmi.R;


public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.prefs);
    }
}
