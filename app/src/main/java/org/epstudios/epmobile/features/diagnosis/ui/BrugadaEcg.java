package org.epstudios.epmobile.features.diagnosis.ui;

import android.os.Bundle;

import org.epstudios.epmobile.R;
import org.epstudios.epmobile.core.ui.base.EpActivity;

final public class BrugadaEcg extends EpActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brugadaecg);
        setupInsets(R.id.brugadaecg_root_view);
        initToolbar();
    }

    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {
        showReferenceAlertDialog(R.string.brugada_ecg_reference,
                R.string.brugada_ecg_link);
    }

    @Override
    protected boolean hideInstructionsMenuItem() {
        return false;
    }

    @Override
    protected void showActivityInstructions() {
        showAlertDialog(R.string.brugada_ecg_description_title,
                R.string.brugada_ecg_description);
    }

}
