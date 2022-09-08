package org.epstudios.epmobile;

import android.app.AlertDialog;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import androidx.annotation.StringRes;

public abstract class LocationAlgorithm extends EpActivity {
    protected int step = 1;
    private int priorStep = 1;
    private int priorStep1 = 1;
    private int priorStep2 = 1;
    private int priorStep3 = 1;
    private int priorStep4 = 1;
    private int priorStep5 = 1;
    private int priorStep6 = 1;
    private int priorStep7 = 1;

    protected void adjustStepsForward() {
        priorStep7 = priorStep6;
        priorStep6 = priorStep5;
        priorStep5 = priorStep4;
        priorStep4 = priorStep3;
        priorStep3 = priorStep2;
        priorStep2 = priorStep1;
        priorStep1 = priorStep;
        priorStep = step;
    }

    protected void adjustStepsBackward() {
        step = priorStep;
        priorStep = priorStep1;
        priorStep1 = priorStep2;
        priorStep2 = priorStep3;
        priorStep3 = priorStep4;
        priorStep4 = priorStep5;
        priorStep5 = priorStep6;
        priorStep6 = priorStep7;
    }

    protected void resetSteps() {
        priorStep7 = priorStep6 = priorStep5 = priorStep4 = 1;
        priorStep3 = priorStep2 = priorStep1 = priorStep = step = 1;
    }

    protected void displayInstructionsWithLinks(@StringRes int titleId,
                                                @StringRes int messageId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titleId);
        final SpannableString message = new SpannableString(
                getString(messageId));
        Linkify.addLinks(message, Linkify.WEB_URLS);
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string.ok_button_label), null);
        AlertDialog alert = builder.create();
        alert.show();
        ((TextView)alert.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
    }
}
