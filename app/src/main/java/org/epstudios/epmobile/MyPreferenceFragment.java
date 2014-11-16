package org.epstudios.epmobile;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

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
        qtcFormulaDefaultValue = activity.getString(R.string.qtc_formula_default_value);
        qtcFormulaKey = activity.getString(R.string.qtc_formula_key);
        qtcFormulaPref = findPreference(qtcFormulaKey);
        setPreferenceSummary(qtcFormulaPref, qtcFormulaKey, qtcFormulaDefaultValue, NULL);
        maximumQtcDefaultValue = activity.getString(R.string.maximum_qtc_default_value);
        maximumQtcKey = activity.getString(R.string.maximum_qtc_key);
        maximumQtcPref = findPreference(maximumQtcKey);
        // handle empty maximum Qtc
        String maximumQtc = getPreferenceScreen().getSharedPreferences().getString(maximumQtcKey,
                maximumQtcDefaultValue);
        if (maximumQtc.length() == 0) {
            maximumQtcPref.setSummary("No entry (440 msec will be used)");
        }
        else {
            maximumQtcPref.setSummary(maximumQtc + " " + msec);
        }
        intervalRateDefaultValue = activity.getString(R.string.interval_rate_default_value);
        intervalRateKey = activity.getString(R.string.interval_rate_key);
        intervalRatePref = findPreference(intervalRateKey);
        setPreferenceSummary(intervalRatePref, intervalRateKey, intervalRateDefaultValue, NULL);
        warfarinTabletDefaultValue = activity.getString(R.string.warfarin_tablet_default_value);
        warfarinTabletKey = activity.getString(R.string.warfarin_tablet_key);
        warfarinTabletPref = findPreference(warfarinTabletKey);
        setPreferenceSummary(warfarinTabletPref, warfarinTabletKey, warfarinTabletDefaultValue, "mg");
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
            // if they enter empty string, show default (and it will be default)
            if (maximumQtc.length() == 0) {
                maximumQtcPref.setSummary("No entry (440 msec will be used)");
            }
            else {
                maximumQtcPref.setSummary(sharedPreferences.getString(key,
                        maximumQtcDefaultValue) + " " + msec);
            }
        }
        if (key.equals(intervalRateKey)) {
            intervalRatePref.setSummary(sharedPreferences.getString(key, intervalRateDefaultValue));
        }
        if (key.equals(warfarinTabletKey)) {
            warfarinTabletPref.setSummary(sharedPreferences.getString(key, warfarinTabletDefaultValue)
                + " " + "mg");
        }
        // TODO more prefs
    }

    private void setPreferenceSummary(Preference pref, String key, String defaultValue, String units) {
        pref.setSummary(getPreferenceScreen().getSharedPreferences().getString(key, defaultValue)
         + (units.length() > 0 ? " " + units : ""));
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
