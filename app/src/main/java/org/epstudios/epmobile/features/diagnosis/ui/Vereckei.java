package org.epstudios.epmobile.features.diagnosis.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.epstudios.epmobile.R;
import org.epstudios.epmobile.core.ui.base.EpActivity;

public class Vereckei extends EpActivity implements OnClickListener {
    private Button backButton;
    private TextView stepTextView;
    private static int step = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simplealgorithm);
        setupInsets(R.id.simplealgorithm_root_view);
        initToolbar();

        Button yesButton = findViewById(R.id.yes_button);
        yesButton.setOnClickListener(this);
        Button noButton = findViewById(R.id.no_button);
        noButton.setOnClickListener(this);
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(this);
        Button morphologyButton = findViewById(R.id.morphology_button);
        morphologyButton.setOnClickListener(this);
        morphologyButton.setVisibility(View.GONE);
        stepTextView = findViewById(R.id.stepTextView);

        step = 1; // needed to reset this when activity starts
        step1();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent parentActivityIntent = new Intent(this,
                    WctAlgorithmList.class);
            parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(parentActivityIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void step1() {
        stepTextView.setText(getString(R.string.vereckei_step1_label));
        backButton.setEnabled(false);
    }

    protected void step2() {
        stepTextView.setText(getString(R.string.vereckei_step2_label));
        backButton.setEnabled(true);
    }

    protected void step3() {
        stepTextView.setText(getString(R.string.vereckei_step3_label));
        backButton.setEnabled(true);
    }

    protected void step4() {
        stepTextView.setText(getString(R.string.vereckei_step4_label));
        backButton.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.yes_button) {
            displayVtResult();
        } else if (id == R.id.no_button) {
            getNoResult();
        } else if (id == R.id.back_button) {
            step--;
            gotoStep();
        }
    }

    private void getNoResult() {
        switch (step) {
            case 1:
            case 2:
            case 3:
                step++;
                gotoStep();
                break;
            case 4:
                displaySvtResult();
                break;
        }

    }

    private void gotoStep() {
        switch (step) {
            case 1:
                step1();
                break;
            case 2:
                step2();
                break;
            case 3:
                step3();
                break;
            case 4:
                step4();
                break;
        }
    }

    private void displayVtResult() {
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);
        String message;
        message = getString(R.string.vt_result);
        message = message + "\n" + getString(R.string.vereckei_reference);
        dialog.setMessage(message);
        dialog.setTitle(getString(R.string.wct_result_label));
        dialog.setCancelable(false);
        dialog.setPositiveButton("Done",
                (dialog12, which) -> finish());
        dialog.setNegativeButton("Back",
                (dialog1, which) -> {
                });
        dialog.show();
    }

    private void displaySvtResult() {
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);
        String message;
        message = getString(R.string.svt_result);
        dialog.setMessage(message);
        dialog.setTitle(getString(R.string.wct_result_label));
        dialog.setPositiveButton("Done",
                (dialog12, which) -> finish());
        dialog.setNegativeButton("Back",
                (dialog1, which) -> {
                });
        dialog.show();
    }


        @Override
        protected boolean hideReferenceMenuItem() {
            return false;
        }

        @Override
        protected void showActivityReference() {
            showReferenceAlertDialog(R.string.vereckei_reference,
                    R.string.vereckei_link);
        }
}
