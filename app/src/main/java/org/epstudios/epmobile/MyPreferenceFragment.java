package org.epstudios.epmobile;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import java.util.ArrayList;

/**
 * Copyright (C) 2013, 2014 EP Studios, Inc.
 * www.epstudiossoftware.com
 */

// TODO possible add same code to OldPrefs
public class MyPreferenceFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private String qtcFormulaDefaultValue;
    private String qtcFormulaKey;
    private Preference qtcFormulaPref;
    private String maximumQtcDefaultValue;
    private String maximumQtcKey;
    private Preference maximumQtcPref;
    private String intervalRateKey;
    private String intervalRateDefaultValue;
    private Preference intervalRatePref;
    private String warfarinTabletDefaultValue;
    private String warfarinTabletKey;
    private Preference warfarinTabletPref;
    private final String NULL = "";
    private final String msec = "msec";

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        Activity activity = getActivity();
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();

        qtcFormulaDefaultValue = activity.getString(R.string.qtc_formula_default_value);
        qtcFormulaKey = activity.getString(R.string.qtc_formula_key);
        qtcFormulaPref = findPreference(qtcFormulaKey);
        qtcFormulaPref.setSummary(sharedPreferences.getString(qtcFormulaKey, qtcFormulaDefaultValue));

        maximumQtcDefaultValue = activity.getString(R.string.maximum_qtc_default_value);
        maximumQtcKey = activity.getString(R.string.maximum_qtc_key);
        maximumQtcPref = findPreference(maximumQtcKey);
        maximumQtcPref.setSummary(getMaximumQtcSummary(
                sharedPreferences.getString(maximumQtcKey, maximumQtcDefaultValue)));

        intervalRateDefaultValue = activity.getString(R.string.interval_rate_default_value);
        intervalRateKey = activity.getString(R.string.interval_rate_key);
        intervalRatePref = findPreference(intervalRateKey);
        intervalRatePref.setSummary(sharedPreferences.getString(intervalRateKey, intervalRateDefaultValue));

        warfarinTabletDefaultValue = activity.getString(R.string.warfarin_tablet_default_value);
        warfarinTabletKey = activity.getString(R.string.warfarin_tablet_key);
        warfarinTabletPref = findPreference(warfarinTabletKey);
        warfarinTabletPref.setSummary(getWarfarinTablet(activity, sharedPreferences));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if (key.equals(qtcFormulaKey)) {
            qtcFormulaPref.setSummary(sharedPreferences.getString(key,
                    qtcFormulaDefaultValue));
        }
        if (key.equals(maximumQtcKey)) {
            String maximumQtc = sharedPreferences.getString(key, maximumQtcDefaultValue);
            maximumQtcPref.setSummary(getMaximumQtcSummary(maximumQtc));
        }
        if (key.equals(intervalRateKey)) {
            intervalRatePref.setSummary(sharedPreferences.getString(key, intervalRateDefaultValue));
        }
        if (key.equals(warfarinTabletKey)) {
            warfarinTabletPref.setSummary(getWarfarinTablet(getActivity(), sharedPreferences));
        }
        // TODO more prefs
    }

    private String getMaximumQtcSummary(String maximumQtc) {
        // if they enter empty string, show default (and it will be default)
        if (maximumQtc.length() == 0) {
            maximumQtc = "No entry (440 msec will be used)";
        }
        else {
            maximumQtc += " " + msec;
        }
        return maximumQtc;
    }

    private String getWarfarinTablet(Activity activity, SharedPreferences sharedPreferences) {
        String[] warfarinTabletArray = activity.getResources().getStringArray(R.array.warfarin_tablets);
        String warfarinTabletIndexString = sharedPreferences.getString(warfarinTabletKey, warfarinTabletDefaultValue);
        Integer warfarinTabletIndex = Integer.parseInt(warfarinTabletIndexString);
        return warfarinTabletArray[warfarinTabletIndex];
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}
