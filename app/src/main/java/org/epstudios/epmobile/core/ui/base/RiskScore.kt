package org.epstudios.epmobile.core.ui.base

import android.R
import android.content.ClipData
import android.content.ClipboardManager
import android.content.DialogInterface
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder

abstract class RiskScore : DiagnosticScore() {
    @JvmField
    protected var resultMessage: String? = null
    private val selectedRisks: MutableList<String?> = ArrayList<String?>()
    private var displayRisks = true // skip displaying list of risks if false

    @JvmField
    protected var checkBoxes: Array<CheckBox>? = null
    fun setDisplayRisks(displayRisks: Boolean) {
        this.displayRisks = displayRisks
    }

    public override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun clearEntries() {
        if (checkBoxes != null) {
            for (aCheckBox in checkBoxes) aCheckBox.setChecked(false)
        }
    }

    override fun displayResult(message: String?, title: String?) {
        // NB: This ensures that the clipboard gets the result message,
        // however, the callers are already doing this individually, which
        // is unnecessary.  But setting the resultMessage twice does no harm
        // and it is not worth rewriting this in every module.
        this.resultMessage = message
        val dialog = MaterialAlertDialogBuilder(this).create()
        dialog.setMessage(message)
        dialog.setButton(
            DialogInterface.BUTTON_POSITIVE,
            getString(org.epstudios.epmobile.R.string.reset_label),
            DialogInterface.OnClickListener { dialog13: DialogInterface?, which: Int -> clearEntries() })
        dialog.setButton(
            DialogInterface.BUTTON_NEUTRAL,
            getString(org.epstudios.epmobile.R.string.dont_reset_label),
            DialogInterface.OnClickListener { dialog12: DialogInterface?, which: Int -> })
        dialog.setButton(
            DialogInterface.BUTTON_NEGATIVE,
            getString(org.epstudios.epmobile.R.string.copy_report_label),
            DialogInterface.OnClickListener { dialog1: DialogInterface?, which: Int ->
                val textToCopy = this.fullRiskReport
                showToast()
                val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied Text", textToCopy)
                clipboard.setPrimaryClip(clip)
            })
        dialog.setTitle(title)
        dialog.show()
    }

    private val fullRiskReport: String
        get() {
            var report = "Risk score:\n" + this.riskLabel
            if (displayRisks) {
                report += "\nRisks:\n" + getSelectedRisks()
            }
            report += ("\nResult:\n" + this.resultMessage + "\n"
                    + getString(org.epstudios.epmobile.R.string.reference_label) + ":\n")
            report += this.fullReference + "\n"
            return report
        }

    private fun showToast() {
        Toast.makeText(this, "Result copied to clipboard", Toast.LENGTH_SHORT)
            .show()
    }

    // NB: No more short references,
    // This just returns the result message, eventually should refactor this method away.
    protected fun resultWithShortReference(): String? {
        return this.resultMessage
    }

    protected fun clearSelectedRisks() {
        selectedRisks.clear()
    }

    protected fun addSelectedRisk(risk: String?) {
        selectedRisks.add(risk)
    }

    protected fun addSelectedRisks(checkBoxes: Array<CheckBox>? = this.checkBoxes) {
        if (checkBoxes != null) {
            for (i in checkBoxes.indices) {
                if (checkBoxes.get(i).isChecked()) {
                    addSelectedRisk(checkBoxes.get(i).getText().toString())
                }
            }
        }
    }

    protected fun getSelectedRisks(): String {
        if (selectedRisks.isEmpty()) {
            return getString(org.epstudios.epmobile.R.string.none_label)
        } else {
            return selectedRisks.toString()
        }
    }

    // each risk score needs these
    protected abstract val fullReference: String?

    // this is the R.string.risk_label, not risk_title. No "score" attached
    protected abstract val riskLabel: String?
}