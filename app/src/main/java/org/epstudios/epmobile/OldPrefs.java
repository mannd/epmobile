package org.epstudios.epmobile;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Copyright (C) 2013, 2014 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p/>
 * Created by mannd on 11/15/14.

 */

// used for preferences in pre-Android 11
@SuppressWarnings("deprecation")
public class OldPrefs extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
