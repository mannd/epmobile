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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

abstract public class DiagnosticScore extends EpActivity implements
        OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        initToolbar();

        View calculateButton = findViewById(R.id.calculate_button);
        calculateButton.setOnClickListener(this);
        View clearButton = findViewById(R.id.clear_button);
        clearButton.setOnClickListener(this);

        init();

        clearEntries();
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.calculate_button) {
            calculateResult();
        } else if (id == R.id.clear_button) {
            clearEntries();
        }
    }

    abstract protected void calculateResult();

    abstract protected void setContentView();

    abstract protected void init();

    protected void displayResult(String message, String title) {
        // put message in class field so inner class can use
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setMessage(message);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE,
                getString(R.string.reset_label),
                (dialog12, which) -> clearEntries());
        dialog.setButton(DialogInterface.BUTTON_NEUTRAL,
                getString(R.string.dont_reset_label),
                (dialog1, which) -> {
                });
        dialog.setTitle(title);
        dialog.show();
    }

    abstract protected void clearEntries();
}
