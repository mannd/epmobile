package org.epstudios.epmobile;

/**
 * Copyright (C) 2024 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 5/24/24.
 * <p>
 * This file is part of epmobile.
 * <p>
 * epmobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * epmobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with epmobile.  If not, see <http://www.gnu.org/licenses/>.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class V2TransitionRatioVt extends LocationAlgorithm implements OnClickListener {
    private Button yesButton;
    private Button noButton;
    protected Button backButton;
    private Button morphologyButton;
    protected TextView stepTextView;

    private boolean isRvot = false;
    private boolean isCertainlyRvot = false;
    private boolean isLvot = false;
    private boolean isIndeterminate = false;

    private final int V3TransitionStep = 1;
    private final int lateTransitionStep = 2;
    private final int manualMeasureStep = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simplealgorithm);
        setupInsets(R.id.simplealgorithm_root_view);
        initToolbar();

        yesButton = findViewById(R.id.yes_button);
        yesButton.setOnClickListener(this);
        noButton = findViewById(R.id.no_button);
        noButton.setOnClickListener(this);
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(this);
        // Morphology button no longer used for instructions
        // since v2.30.0.
        morphologyButton = findViewById(R.id.morphology_button);
        morphologyButton.setVisibility(View.GONE);
        morphologyButton.setOnClickListener(this);
        morphologyButton.setText(R.string.v2_calculator_title);
        stepTextView = findViewById(R.id.stepTextView);
        step1();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent parentActivityIntent = new Intent(this, VtList.class);
            parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(parentActivityIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.yes_button) {
            getYesResult();
        } else if (id == R.id.no_button) {
            getNoResult();
        } else if (id == R.id.back_button) {
            getBackResult();
        } else if (id == R.id.morphology_button) {
            displayV2Calculator();
        }
    }

    private void displayV2Calculator() {
        Intent i = new Intent(this, V2Calculator.class);
        startActivity(i);
    }

    private void getBackResult() {
        adjustStepsBackward();
        gotoStep();
    }

    private void getNoResult() {
        adjustStepsForward();
        switch (step) {
            case V3TransitionStep:
                isIndeterminate = true;
                showResult();
                break;
            case lateTransitionStep:
                step = manualMeasureStep;
                break;
            case manualMeasureStep:
                isLvot = true;
                showResult();
                break;
        }
        gotoStep();
    }

    protected void getYesResult() {
        adjustStepsForward();
        switch (step) {
            case V3TransitionStep:
                isRvot = false;
                isCertainlyRvot = false;
                isIndeterminate = false;
                isLvot = false;
                step = lateTransitionStep;
                break;
            case lateTransitionStep:
                isRvot = true;
                isCertainlyRvot = true;
                showResult();
                break;
            case manualMeasureStep:
                isRvot = true;
                showResult();
                break;
        }
        gotoStep();
    }

    private void resetButtons() {
        yesButton.setText(getString(R.string.yes));
        noButton.setText(getString(R.string.no));
    }

    protected void step1() {
        stepTextView
                .setText(getString(R.string.v2_transition_ratio_v3_transition_step));
        backButton.setEnabled(false);
    }

    protected void gotoStep() {
        if (step == manualMeasureStep) {
            morphologyButton.setVisibility(View.VISIBLE);
            yesButton.setText(R.string.less_than_06);
            noButton.setText(R.string.more_than_06);
        }
        else {
            morphologyButton.setVisibility(View.GONE);
            resetButtons();
        }
        switch (step) {
            case V3TransitionStep:
                step1();
                break;
            case lateTransitionStep:
                stepTextView.setText(getString(R.string.v2_transition_ratio_vt_late_transition_step));
                break;
            case manualMeasureStep:
                stepTextView
                        .setText(getString(R.string.v2_transition_ratio_vt_manual_measure_step));
                break;
        }
        if (step != V3TransitionStep)
            backButton.setEnabled(true);
    }

    protected void showResult() {
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);
        String message = getMessage();
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setTitle(getString(R.string.outflow_vt_location_label));
        dialog.setPositiveButton(
                getString(R.string.done_label),
                (dialog12, which) -> finish());
        dialog.setNegativeButton(
                getString(R.string.reset_label),
                (dialog1, which) -> {
                    resetSteps();
                    gotoStep();
                });

        dialog.show();
    }

    protected String getMessage() {
        String message = "";
        if (isIndeterminate) {
            message += getString(R.string.v2_transtion_ratio_vt_indeterminate_location);
        }
        else if (isCertainlyRvot) {
            message += getString(R.string.v2_transition_ratio_vt_is_certainly_rvot);
        }
        else if (isRvot) {
            message += getString(R.string.v2_transition_ratio_vt_is_rvot);
        } else if (isLvot) {
            message += getString(R.string.v2_transition_ratio_vt_is_lvot);
        } else {
            message = getString(R.string.indeterminate_location);
        }
        return message;
    }


    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {
        showReferenceAlertDialog(R.string.v2_transition_ratio_vt_reference_0,
                R.string.v2_transition_ratio_vt_link_0);
    }


    @Override
    protected boolean hideInstructionsMenuItem() {
        return false;
    }

    @Override
    protected void showActivityInstructions() {
        showAlertDialog(R.string.v2_transition_ratio_vt_title,
                R.string.v2_transition_ratio_vt_instructions);
    }

}
