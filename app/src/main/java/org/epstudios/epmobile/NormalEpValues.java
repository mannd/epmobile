package org.epstudios.epmobile;

import android.os.Bundle;

public class NormalEpValues extends EpActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normalepvalues);
        initToolbar();
    }

    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {
        showReferenceAlertDialog(R.string.normal_ep_values_reference,
                R.string.normal_ep_values_link);
    }
}
