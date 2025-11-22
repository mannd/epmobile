package org.epstudios.epmobile.core.ui.base

import android.os.Bundle
import android.view.View
import org.epstudios.epmobile.R

abstract class DiagnosticScore : EpActivity(), View.OnClickListener {
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView()
        setupInsets();
        initToolbar()

        val calculateButton = findViewById<View>(R.id.calculate_button)
        calculateButton.setOnClickListener(this)
        val clearButton = findViewById<View>(R.id.clear_button)
        clearButton.setOnClickListener(this)

        init()

        clearEntries()
    }

    override fun onClick(v: View) {
        val id = v.getId()
        if (id == R.id.calculate_button) {
            calculateResult()
        } else if (id == R.id.clear_button) {
            clearEntries()
        }
    }

    protected abstract fun calculateResult()

    protected abstract fun setContentView()

    protected abstract fun setupInsets()

    protected abstract fun init()

    protected abstract fun displayResult(message: String?, title: String?)

    protected abstract fun clearEntries()
}