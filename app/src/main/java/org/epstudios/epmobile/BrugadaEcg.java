package org.epstudios.epmobile;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

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
