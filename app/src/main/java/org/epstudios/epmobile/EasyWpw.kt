package org.epstudios.epmobile

/**
Copyright (C) 2025 EP Studios, Inc.
www.epstudiossoftware.com

Created by mannd on 11/8/25.

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
class EasyWpw: Algorithm {
    override val name: String
        get() = "EASY-WPW"
    override val rootNodeResId: Int
        get() = R.raw.easy_wpw
    override val resultTitle: String
        get() = "Accessory Pathway Location"
    override val hasMap: Boolean
        get() = false

    override fun getReferences(): List<Reference> {
        val references = ArrayList<Reference>()
        references.add(Reference("Easy Wpw", "https://www.epstudiossoftware.com"))
        return references
    }

    override fun getInstructions(): String? {
        return "Easy Wpw instructions placeholder"
    }

    override fun getKey(): String? {
        return "Easy Wpw key placeholder"
    }
}
