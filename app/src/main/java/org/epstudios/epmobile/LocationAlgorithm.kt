package org.epstudios.epmobile

import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.epstudios.epmobile.core.ui.base.EpActivity

abstract class LocationAlgorithm : EpActivity() {
    @JvmField
    protected var step: Int = 1
    private var priorStep = 1
    private var priorStep1 = 1
    private var priorStep2 = 1
    private var priorStep3 = 1
    private var priorStep4 = 1
    private var priorStep5 = 1
    private var priorStep6 = 1
    private var priorStep7 = 1

    protected fun adjustStepsForward() {
        priorStep7 = priorStep6
        priorStep6 = priorStep5
        priorStep5 = priorStep4
        priorStep4 = priorStep3
        priorStep3 = priorStep2
        priorStep2 = priorStep1
        priorStep1 = priorStep
        priorStep = step
    }

    protected open fun adjustStepsBackward() {
        step = priorStep
        priorStep = priorStep1
        priorStep1 = priorStep2
        priorStep2 = priorStep3
        priorStep3 = priorStep4
        priorStep4 = priorStep5
        priorStep5 = priorStep6
        priorStep6 = priorStep7
    }

    protected fun resetSteps() {
        priorStep4 = 1
        priorStep5 = priorStep4
        priorStep6 = priorStep5
        priorStep7 = priorStep6
        step = 1
        priorStep = step
        priorStep1 = priorStep
        priorStep2 = priorStep1
        priorStep3 = priorStep2
    }

    protected fun displayInstructionsWithLinks(
        @StringRes titleId: Int,
        @StringRes messageId: Int
    ) {
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle(titleId)
        val message = SpannableString(
            getString(messageId)
        )
        Linkify.addLinks(message, Linkify.WEB_URLS)
        builder.setMessage(message)
        builder.setPositiveButton(getString(R.string.ok_button_label), null)
        val alert = builder.create()
        alert.show()
        (alert.findViewById<View?>(android.R.id.message) as TextView).setMovementMethod(
            LinkMovementMethod.getInstance()
        )
    }
}
