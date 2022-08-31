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

public class BrugadaEcg extends EpActivity implements OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brugadaecg);
        initToolbar();
        Button textButton = findViewById(R.id.text_button);
        textButton.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.text_button) {
            displayHelp();
        }
    }

    private void displayHelp() {
        final SpannableString message = new SpannableString(
                getString(R.string.brugada_ecg_description) + "\n\n"
                        + getString(R.string.brugada_ecg_reference));
        Linkify.addLinks(message, Linkify.WEB_URLS);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(message)
                .setTitle(getString(R.string.brugada_ecg_description_title))
                .create();
        dialog.show();
        ((TextView) dialog.findViewById(android.R.id.message))
                .setMovementMethod(LinkMovementMethod.getInstance());
    }
}
