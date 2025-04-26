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
package org.epstudios.epmobile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

//adds option menu functions
abstract class EpActivity : BasicEpActivity() {
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        val inflater = getMenuInflater()
        inflater.inflate(R.menu.menu, menu)

        if (hideInstructionsMenuItem()) {
            val notesItem = menu.findItem(R.id.instructions)
            if (notesItem != null) {
                notesItem.setVisible(false)
            }
        }
        if (hideReferenceMenuItem()) {
            val referenceItem = menu.findItem(R.id.reference)
            if (referenceItem != null) {
                referenceItem.setVisible(false)
            }
        }
        if (hideKeyMenuItem()) {
            val keyItem = menu.findItem(R.id.key)
            if (keyItem != null) {
                keyItem.setVisible(false)
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.getItemId()
        if (itemId == R.id.settings) {
            startActivity(Intent(this, Prefs::class.java))
            return true
        } else if (itemId == R.id.about) {
            startActivity(Intent(this, About::class.java))
            return true
        } else if (itemId == android.R.id.home) {
            finish()
            return true
        } else if (itemId == R.id.instructions) {
            if (!hideInstructionsMenuItem()) {
                showActivityInstructions()
            }
        } else if (itemId == R.id.reference) {
            if (!hideReferenceMenuItem()) {
                showActivityReference()
            }
        } else if (itemId == R.id.key) {
            if (!hideKeyMenuItem()) {
                showActivityKey()
            }
        }
        return false
    }

    // Override in inherited activities to show these menu items.
    // Default is to hide these menu items.
    protected open fun hideInstructionsMenuItem(): Boolean {
        return true
    }

    protected open fun hideReferenceMenuItem(): Boolean {
        return true
    }

    protected open fun hideKeyMenuItem(): Boolean {
        return true
    }

    // Override to inherited activities
    protected open fun showActivityInstructions() {
        print("showNotes should be overridden.")
    }

    protected open fun showActivityReference() {
        print("showReference should be overridden.")
    }

    protected open fun showActivityKey() {
        print("showKey should be overridden.")
    }

    protected fun showAlertDialog(@StringRes titleId: Int, @StringRes messageId: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(titleId)
        builder.setMessage(messageId)
        builder.setPositiveButton(getString(R.string.ok_button_label), null)
        val alert = builder.create()
        alert.show()
    }

    protected fun showAlertDialog(title: String?, message: Spanned?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(getString(R.string.ok_button_label), null)
        val alert = builder.create()
        alert.show()
        (alert.findViewById<View?>(android.R.id.message) as TextView).setMovementMethod(
            LinkMovementMethod.getInstance()
        )
    }

    protected fun showAlertDialog(title: String?, message: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(getString(R.string.ok_button_label), null)
        val alert = builder.create()
        alert.show()
    }

    protected fun showReferenceAlertDialog(
        @StringRes referenceId: Int,
        @StringRes linkId: Int
    ) {
        val html = convertReferenceToHtml(referenceId, linkId)
        if (html != null) {
            showAlertDialog(getString(R.string.reference_label), html)
        } else {
            showAlertDialog(
                getString(R.string.error_dialog_title),
                getString(R.string.error_message)
            )
        }
    }

    protected fun showReferenceAlertDialog(references: Array<Reference>) {
        val html = convertReferencesToHtml(references)
        if (html != null) {
            showAlertDialog(getString(R.string.references_label), html)
        } else {
            showAlertDialog(
                getString(R.string.error_dialog_title),
                getString(R.string.error_message)
            )
        }
    }

    protected fun showKeyAlertDialog(@StringRes keyId: Int) {
        showAlertDialog(R.string.key_label, keyId)
    }

    fun convertReferenceToHtml(
        @StringRes referenceId: Int,
        @StringRes linkId: Int
    ): Spanned? {
        val reference = getString(referenceId)
        val link = getString(linkId)
        // Only forbidden combo is reference == null.  Can have a null
        // link if the paper is old.
        if (reference != null) {
            val htmlString: String = convertReferenceToHtmlString(reference, link)
            return Html.fromHtml(htmlString)
        } else {
            return null
        }
    }

    fun convertReferenceToText(
        @StringRes referenceId: Int,
        @StringRes linkId: Int
    ): String? {
        val reference = getString(referenceId)
        val link = getString(linkId)
        if (reference != null) {
            val referencePlusLink = reference + " " + link
            return referencePlusLink
        }
        return null
    }

    // Handle multiple references.
    fun convertReferencesToHtml(references: Array<Reference>): Spanned? {
        val htmlString: String? = convertReferencesToHtmlString(references)
        if (htmlString == null) {
            return null
        } else {
            return Html.fromHtml(htmlString)
        }
    }

    companion object {
        @JvmStatic
        fun convertReferenceToHtmlString(
            reference: String,
            link: String?
        ): String {
            var html = ""
            if (link != null) {
                html = "<p>" + reference +
                        "<br/><a href =\"" +
                        link + "\">Link to reference</a></p>"
            } else {
                html = "<p>" + reference + "<br/><i>No link available</i></p>"
            }
            return html
        }

        fun convertReferencesToHtmlString(references: Array<Reference>): String? {
            var htmlString = ""
            for (reference in references) {
                // Only forbidden combo is reference == null.  Can have a null
                // link if the paper is old.
                if (reference.getText() != null) {
                    htmlString += convertReferenceToHtmlString(
                        reference.getText(),
                        reference.getLink()
                    )
                } else {
                    return null
                }
            }
            return htmlString
        }
    }
}
