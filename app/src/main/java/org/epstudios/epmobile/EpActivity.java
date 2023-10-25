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
            MenuItem notesItem = menu.findItem(R.id.instructions);
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
        if (hideKeyMenuItem()) {
            MenuItem keyItem = menu.findItem(R.id.key);
            if (keyItem != null) {
                keyItem.setVisible(false);
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
        } else if (itemId == R.id.instructions) {
            if (!hideInstructionsMenuItem()) {
                showActivityInstructions();
            }
        } else if (itemId == R.id.reference) {
            if (!hideReferenceMenuItem()) {
                showActivityReference();
            }
        } else if (itemId == R.id.key) {
            if (!hideKeyMenuItem()) {
                showActivityKey();
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

    protected boolean hideKeyMenuItem() { return true; }

    // Override to inherited activities
    protected void showActivityInstructions() {
        System.out.print("showNotes should be overridden.");
    }

    protected void showActivityReference() {
        System.out.print("showReference should be overridden.");
    }

    protected void showActivityKey() {
        System.out.print("showKey should be overridden.");
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

    final protected void showReferenceAlertDialog(@StringRes int referenceId,
                                                  @StringRes int linkId) {
        Spanned html = convertReferenceToHtml(referenceId, linkId);
        if (html != null) {
            showAlertDialog(getString(R.string.reference_label), html);
        } else {
            showAlertDialog(getString(R.string.error_dialog_title),
                    getString(R.string.error_message));
        }
    }

    // TODO: References without links not showing up?
    final protected void showReferenceAlertDialog(Reference[] references) {
        Spanned html = convertReferencesToHtml(references);
        if (html != null) {
            showAlertDialog(getString(R.string.references_label), html);
        } else {
            showAlertDialog(getString(R.string.error_dialog_title),
                    getString(R.string.error_message));
        }
    }

    final protected void showKeyAlertDialog(@StringRes int keyId) {
        showAlertDialog(R.string.key_label, keyId);
    }

    public static String convertReferenceToHtmlString(@NonNull String reference,
                                                      String link) {
        String html = "";
        if (link != null) {
            html = "<p>" + reference +
                    "<br/><a href =\"" +
                    link + "\">Link to reference</a></p>";
        } else {
            html = "<p>" + reference + "<br/><i>No link available</i></p>";
        }
        return html;
    }

    public static String convertReferencesToHtmlString(Reference[] references) {
        String htmlString = "";
        for (Reference reference: references ) {
            // Only forbidden combo is reference == null.  Can have a null
            // link if the paper is old.
            if (reference.getText() != null) {
                htmlString += convertReferenceToHtmlString(reference.getText(),
                        reference.getLink());
            }
            else {
                return null;
            }
        }
        return htmlString;
    }

    public Spanned convertReferenceToHtml(@StringRes int referenceId,
                                          @StringRes int linkId) {
        String reference = getString(referenceId);
        String link = getString(linkId);
        // Only forbidden combo is reference == null.  Can have a null
        // link if the paper is old.
        if (reference != null) {
            String htmlString = convertReferenceToHtmlString(reference, link);
            return Html.fromHtml(htmlString);
        } else {
            return null;
        }
    }

    public String convertReferenceToText(@StringRes int referenceId,
                                         @StringRes int linkId) {
        String reference = getString(referenceId);
        String link = getString(linkId);
        if (reference != null) {
            String referencePlusLink = reference + " " + link;
            return referencePlusLink;
        }
        return null;
    }

    // Handle multiple references.
    public Spanned convertReferencesToHtml(Reference[] references) {
        String htmlString = convertReferencesToHtmlString(references);
        if (htmlString == null) {
            return null;
        } else {
            return Html.fromHtml(htmlString);
        }
    }
}
