/*  EP Mobile -- Mobile tools for electrophysiologists
    Copyright (C) 2012 EP Studios, Inc.
    www.epstudiossoftware.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
import org.epstudios.epmobile.core.data.Reference;
import org.epstudios.epmobile.core.ui.base.LocationAlgorithm;

public class OutflowVt extends LocationAlgorithm implements OnClickListener {
    private Button yesButton;
    private Button noButton;
    protected Button backButton;
    private Button morphologyButton;
    protected TextView stepTextView;

    private boolean isRvot = false;
    private boolean isLvot = false;
    private boolean isIndeterminate = false;
    private boolean isSupraValvular = false;
    private boolean isRvFreeWall = false;
    private boolean isAnterior = false;
    private boolean isCaudal = false;

    private final int lateTransitionStep = 1;
    private final int freeWallStep = 2;
    private final int anteriorLocationStep = 3;
    private final int caudalLocationStep = 4;
    private final int v3TransitionStep = 6;
    private final int indeterminateLocationStep = 7;
    private final int supraValvularStep = 9;

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
        }
    }

    private void getBackResult() {
        adjustStepsBackward();
        gotoStep();
    }

    private void getNoResult() {
        adjustStepsForward();
        switch (step) {
            case lateTransitionStep:
                step = v3TransitionStep;
                break;
            case freeWallStep:
                isRvFreeWall = false;
                step = anteriorLocationStep;
                break;
            case anteriorLocationStep:
                isAnterior = false;
                step = caudalLocationStep;
                break;
            case caudalLocationStep:
                isCaudal = false;
                showResult();
                break;
            case v3TransitionStep:
                isLvot = true;
                isRvot = false;
                isIndeterminate = false;
                step = supraValvularStep;
                break;
            case indeterminateLocationStep:
                isLvot = true;
                isRvot = false;
                isIndeterminate = true;
                step = supraValvularStep;
                break;

            case supraValvularStep:
                isSupraValvular = false;
                showResult();
        }
        gotoStep();
    }

    protected void getYesResult() {
        adjustStepsForward();
        switch (step) {
            case lateTransitionStep:
                isRvot = true;
                isIndeterminate = false;
                isLvot = false;
                step = freeWallStep;
                break;
            case freeWallStep:
                isRvFreeWall = true;
                step = anteriorLocationStep;
                break;
            case anteriorLocationStep:
                isAnterior = true;
                step = caudalLocationStep;
                break;
            case caudalLocationStep:
                isCaudal = true;
                showResult();
                break;
            case v3TransitionStep:
                step = indeterminateLocationStep;
                break;
            case indeterminateLocationStep:
                isRvot = true;
                isLvot = false;
                isIndeterminate = true;
                isRvFreeWall = false;
                step = anteriorLocationStep;
                break;
            case supraValvularStep:
                isSupraValvular = true;
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
                .setText(getString(R.string.outflow_vt_late_transition_step));
        backButton.setEnabled(false);
    }

    protected void gotoStep() {
        if (step != indeterminateLocationStep)
            resetButtons();
        switch (step) {
            case lateTransitionStep:
                step1();
                break;
            case freeWallStep:
                stepTextView.setText(getString(R.string.outflow_vt_free_wall_step));
                break;
            case anteriorLocationStep:
                stepTextView
                        .setText(getString(R.string.outflow_vt_anterior_location_step));
                break;
            case caudalLocationStep:
                stepTextView
                        .setText(getString(R.string.outflow_vt_caudal_location_step));
                break;
            case v3TransitionStep:
                stepTextView
                        .setText(getString(R.string.outflow_vt_v3_transition_step));
                break;
            case indeterminateLocationStep:
                stepTextView
                        .setText(getString(R.string.outflow_vt_indeterminate_location_step));
                yesButton.setText(getString(R.string.rv_label));
                noButton.setText(getString(R.string.lv_label));
                break;
            case supraValvularStep:
                stepTextView
                        .setText(getString(R.string.outflow_vt_supravalvular_step));
                break;
        }
        if (step != lateTransitionStep)
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
            message += "Note: Location (RV vs LV) is indeterminate. "
                    + "Results reflect one possible localization.\n";
        }
        if (isRvot) {
            message += getString(R.string.rvot_label);
            message += isRvFreeWall ? "\nFree wall" : "\nSeptal";
            message += isAnterior ? "\nAnterior" : "\nPosterior";
            message += isCaudal ? "\nCaudal (> 2 cm from pulmonic valve)"
                    : "\nCranial (< 2 cm from pulmonic valve)";
        } else if (isLvot) {
            message += getString(R.string.lvot_label);
            message += isSupraValvular ? getString(R.string.cusp_vt_label)
                    : getString(R.string.mitral_annular_vt_label);
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
            Reference[] references = new Reference[3];
            references[0] = new Reference(this, R.string.outflow_vt_reference_0,
                    R.string.outflow_vt_link_0);
            references[1] = new Reference(this, R.string.outflow_vt_reference_1,
                    R.string.outflow_vt_link_1);
            references[2] = new Reference(this, R.string.outflow_vt_reference_2,
                    R.string.outflow_vt_link_2);
            showReferenceAlertDialog(references);
        }

        @Override
        protected boolean hideInstructionsMenuItem() {
            return false;
        }

        @Override
        protected void showActivityInstructions() {
            showAlertDialog(R.string.outflow_tract_vt_title,
                    R.string.outflow_vt_instructions);
        }

}
