/*  EP Mobile -- Mobile tools for electrophysiologists
Copyright (C) 2011 EP Studios, Inc.
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
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

//adds option menu functions
public abstract class EpActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        if (hideNotes()) {
            MenuItem notesItem = menu.findItem(R.id.notes);
            if (notesItem != null) {
                notesItem.setVisible(false);
            }
        }
        if (hideReference()) {
            MenuItem referenceItem = menu.findItem(R.id.reference);
            if (referenceItem != null) {
                referenceItem.setVisible(false);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        if (itemId == R.id.settings) {
            startActivity(new Intent(this, Prefs.class));
            return true;
        } else if (itemId == R.id.about) {
            startActivity(new Intent(this, About.class));
            return true;
        } else if (itemId == android.R.id.home) {
            finish();
            return true;
        } else if (itemId == R.id.notes) {
            if (!hideNotes()) {
                showNotes();
            }
        } else if (itemId == R.id.reference) {
            if (!hideReference()) {
                showReference();
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
    }

    protected void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected boolean hideNotes() {
        return true;
    }

    protected void showNotes() {

        System.out.print("showNotes should be overridden.");
    }

    protected boolean hideReference() { return true; }

    protected void showReference() {
        System.out.print("showReference should be overridden.");
    }

    protected void showAlertDialogWithLink(@StringRes int titleId, @StringRes int messageId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titleId);
        builder.setMessage(messageId);
        builder.setPositiveButton(getString(R.string.ok_button_label), null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected void showAlertDialogWithLink(@StringRes int titleId, Spanned message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titleId);
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string.ok_button_label), null);
        AlertDialog alert = builder.create();
        alert.show();
        ((TextView)alert.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
    }

    protected void showReferenceAlertDialog(@StringRes int titleId, String reference, Spanned link) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.reference_label);
        builder.setTitle(titleId);
        Spanned message = link;
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string.ok_button_label), null);
        AlertDialog alert = builder.create();
        alert.show();
        ((TextView)alert.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
    }

    protected Spanned convertReferenceToHtml(@StringRes int referenceId, @StringRes int linkId) {
        String reference = getString(referenceId);
        String link = getString(linkId);
        if (reference == null || link == null) {
            return null;
        }
        String html = convertReferenceToHtmlString(reference, link);
        return Html.fromHtml(html);
    }

    protected void showAlertDialog(String title, Spanned message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string.ok_button_label), null);
        AlertDialog alert = builder.create();
        alert.show();
        ((TextView)alert.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
    }


    protected void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string.ok_button_label), null);
        AlertDialog alert = builder.create();
        alert.show();
    }


    public static String convertReferenceToHtmlString(String reference, String link) {
        String html = "<p>" + reference + "<br/><a href =\"" + link + "\">Link to reference</a></p>" ;
        return html;
    }
}
