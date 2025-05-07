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
package org.epstudios.epmobile

import android.os.Bundle
import android.view.View

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
