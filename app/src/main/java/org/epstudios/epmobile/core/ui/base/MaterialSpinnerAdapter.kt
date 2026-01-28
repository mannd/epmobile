package org.epstudios.epmobile.core.ui.base

/**
Copyright (C) 2026 EP Studios, Inc.
www.epstudiossoftware.com

Created by mannd on 1/28/26.

This file is part of epmobile.

epmobile is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

epmobile is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with epmobile.  If not, see <http://www.gnu.org/licenses/>.
 */
import android.content.Context

import android.widget.ArrayAdapter
import android.widget.Filter

/**
 * From: https://rmirabelle.medium.com/there-is-no-material-design-spinner-for-android-3261b7c77da8
 *
 * Almost unbelievably, if we want to create a Material Spinner,
 * we're forced to subclass ArrayAdapter.  That statement alone
 * is maddening.  The issue is that there's no such thing as a Material
 * Spinner. Instead, there's an Exposed Dropdown Menu, which is really
 * an AutoCompleteTextView wrapped in a TextInputLayout, which replaces a
 * Spinner. The reason we have to subclass ArrayAdapter is because we need
 * the AutoCompleteTextView to act like a proper Spinner.  Thus we have to
 * override the AutoCompleteTextView's Filter so that it NEVER performs
 * filtering of the dropdown menu items.
 */
public class MaterialSpinnerAdapter<String>(context: Context, layout: Int, var values: Array<String>) :
    ArrayAdapter<String>(context, layout, values) {
    private val filter_that_does_nothing = object: Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            results.values = values
            results.count = values.size
            return results
        }
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            notifyDataSetChanged()
        }
    }

    override fun getFilter(): Filter {
        return filter_that_does_nothing
    }
}