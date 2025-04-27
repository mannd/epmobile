package org.epstudios.epmobile;

import android.os.Bundle;

public class NormalEpValues extends EpActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normalepvalues);
        setupInsets(R.id.normal_ep_values_root_view);
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
