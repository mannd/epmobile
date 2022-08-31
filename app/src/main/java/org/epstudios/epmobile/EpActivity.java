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
        if (hideInstructionsMenuItem()) {
            MenuItem notesItem = menu.findItem(R.id.notes);
            if (notesItem != null) {
                notesItem.setVisible(false);
            }
        }
        if (hideReferenceMenuItem()) {
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
            if (!hideInstructionsMenuItem()) {
                showActivityInstructions();
            }
        } else if (itemId == R.id.reference) {
            if (!hideReferenceMenuItem()) {
                showActivityReference();
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

    // Override in inherited activities to show these menu items.
    // Default is to hide these menu items.
    protected boolean hideInstructionsMenuItem() {
        return true;
    }

    protected boolean hideReferenceMenuItem() { return true; }

    // Override to inherited activities
    protected void showActivityInstructions() {
        System.out.print("showNotes should be overridden.");
    }

    protected void showActivityReference() {
        System.out.print("showReference should be overridden.");
    }

    final protected void showAlertDialog(@StringRes int titleId, @StringRes int messageId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titleId);
        builder.setMessage(messageId);
        builder.setPositiveButton(getString(R.string.ok_button_label), null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    final protected void showAlertDialog(String title, Spanned message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string.ok_button_label), null);
        AlertDialog alert = builder.create();
        alert.show();
        ((TextView)alert.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
    }

    final protected void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string.ok_button_label), null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    // TODO: handle multiple references...
    // Need array of [reference, link] tuples
    final protected void showReferenceAlertDialog(@StringRes int reference,
                                                  @StringRes int link) {
        Spanned html = convertReferenceToHtml(R.string.cms_icd_references,
                R.string.cms_icd_link);
        if (html != null) {
            showAlertDialog(getString(R.string.reference_label), html);
        } else {
            showAlertDialog(getString(R.string.error_dialog_title),
                    getString(R.string.error_message));
        }
    }

    public static String convertReferenceToHtmlString(@NonNull String reference, @NonNull String link) {
        String html = "<p>" + reference + "<br/><a href =\"" + link + "\">Link to reference</a></p>" ;
        return html;
    }

    public Spanned convertReferenceToHtml(@StringRes int referenceId,
                                                 @StringRes int linkId) {
        String reference = getString(referenceId);
        String link = getString(linkId);
        if (reference == null || link == null) {
            return null;
        }
        String htmlString = convertReferenceToHtmlString(reference, link);
        return Html.fromHtml(htmlString);
    }
}
