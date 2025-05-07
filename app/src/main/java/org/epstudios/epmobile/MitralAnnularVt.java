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

package org.epstudios.epmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

@SuppressWarnings("SpellCheckingInspection")
public class MitralAnnularVt extends LocationAlgorithm implements
        OnClickListener {
    protected Button backButton;
    private Button morphologyButton;
    protected TextView stepTextView;

    private boolean isNotMitralAnnular = false;
    private boolean isAnteroLateral = false;
    private boolean isAnteroMedial = false;
    private boolean isPosterior = false;
    private boolean isPosteroSeptal = false;

    private final int initialStep = 1;
    private final int positiveQrsInferiorLeadsStep = 2;
    private final int notchingRInferiorLeadsStep = 3;
    private final int notchingQInferiorLeadsStep = 4;

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
        stepTextView = findViewById(R.id.stepTextView);
        // Morphology button not used in this activity.
        morphologyButton = findViewById(R.id.morphology_button);
        morphologyButton.setVisibility(View.GONE);
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
            case initialStep:
                isNotMitralAnnular = true;
                showResult();
                break;
            case positiveQrsInferiorLeadsStep:
                step = notchingQInferiorLeadsStep;
                break;
            case notchingRInferiorLeadsStep:
                isAnteroMedial = true;
                showResult();
                break;
            case notchingQInferiorLeadsStep:
                isPosteroSeptal = true;
                showResult();
                break;
        }
        gotoStep();
    }

    protected void getYesResult() {
        adjustStepsForward();
        switch (step) {
            case initialStep:
                step = positiveQrsInferiorLeadsStep;
                break;
            case positiveQrsInferiorLeadsStep:
                step = notchingRInferiorLeadsStep;
                break;
            case notchingRInferiorLeadsStep:
                isAnteroLateral = true;
                showResult();
                break;
            case notchingQInferiorLeadsStep:
                isPosterior = true;
                showResult();
                break;
        }
        gotoStep();
    }

    private void resetResult() {
        isAnteroLateral = isAnteroMedial = false;
        isNotMitralAnnular = isPosterior = isPosteroSeptal = false;
    }

    protected void step1() {
        stepTextView.setText(getString(R.string.mavt_initial_step));
        backButton.setEnabled(false);
    }

    protected void gotoStep() {
        switch (step) {
            case initialStep:
                step1();
                break;
            case positiveQrsInferiorLeadsStep:
                stepTextView
                        .setText(getString(R.string.mavt_positive_qrs_inferior_leads_step));
                break;
            case notchingRInferiorLeadsStep:
                stepTextView
                        .setText(getString(R.string.mavt_notching_r_inferior_leads_step));
                break;
            case notchingQInferiorLeadsStep:
                stepTextView
                        .setText(getString(R.string.mavt_notching_q_inferior_leads_step));
                break;
        }
        if (step != initialStep)
            backButton.setEnabled(true);
    }

    protected void showResult() {
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);
        String message = getMessage();
        dialog.setMessage(message);
        //dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setTitle(getString(R.string.outflow_vt_location_label));
        dialog.setPositiveButton(
                getString(R.string.done_label),
                (dialog12, which) -> finish());
        dialog.setNegativeButton(
                getString(R.string.reset_label),
                (dialog1, which) -> {
                    resetSteps();
                    resetResult();
                    gotoStep();
                });
        dialog.show();
    }

    protected String getMessage() {
        String message;
        if (isNotMitralAnnular)
            message = getString(R.string.mavt_not_mitral_annular_label);
        else if (isAnteroLateral)
            message = getString(R.string.mavt_anterolateral_label);
        else if (isAnteroMedial)
            message = getString(R.string.mavt_anteromedial_label);
        else if (isPosterior)
            message = getString(R.string.mavt_posterior_label);
        else if (isPosteroSeptal)
            message = getString(R.string.mavt_posteroseptal_label);
        else
            message = getString(R.string.indeterminate_location);
        return message;
    }


    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {
        Reference[] references = new Reference[2];
        references[0] = new Reference(this, R.string.mitral_annular_vt_reference_0,
                R.string.mitral_annular_vt_link_0);
        references[1] = new Reference(this, R.string.mitral_annular_vt_reference_1,
                R.string.mitral_annular_vt_link_1);
        showReferenceAlertDialog(references);
    }

    @Override
    protected boolean hideInstructionsMenuItem() {
        return false;
    }

    @Override
    protected void showActivityInstructions() {
        // We use displayInstructions here to allow the link in the
        // instructions to display as a link, which showAlertDialog
        // does not do.
        displayInstructionsWithLinks(R.string.mitral_annular_vt_title,
                R.string.mitral_annular_vt_instructions);
    }

}
