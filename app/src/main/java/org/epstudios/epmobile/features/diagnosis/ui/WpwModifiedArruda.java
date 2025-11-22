package org.epstudios.epmobile.features.diagnosis.ui;

import org.epstudios.epmobile.R;

public class WpwModifiedArruda extends WpwArruda {
    public WpwModifiedArruda() {
        modifiedArruda = true;
    }


        @Override
        protected boolean hideReferenceMenuItem() {
            return false;
        }

        @Override
        protected void showActivityReference() {
            showReferenceAlertDialog(R.string.modified_arruda_reference,
                    R.string.modified_arruda_link);
        }

        @Override
        protected boolean hideInstructionsMenuItem() {
            return false;
        }

        // Modified Arruda uses same instructions as Arruda
        @Override
        protected void showActivityInstructions() {
            showAlertDialog(R.string.modified_arruda_title,
                    R.string.arruda_instructions);
        }
}
