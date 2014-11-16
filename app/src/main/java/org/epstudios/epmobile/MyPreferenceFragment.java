package org.epstudios.epmobile;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 Copyright (C) 2013, 2014 EP Studios, Inc.
www.epstudiossoftware.com
 */

public class MyPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
