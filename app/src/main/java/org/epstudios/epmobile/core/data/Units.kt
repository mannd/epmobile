package org.epstudios.epmobile.core.data

/**
Copyright (C) 2025 EP Studios, Inc.
www.epstudiossoftware.com

Created by mannd on 12/12/25.

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
/**
 * A centralized collection of measurement unit enums used across the app.
 */

enum class Sex(val arrayIndex: Int, val displayName: String) {
    MALE(0, "M"),
    FEMALE(1, "F")
}

enum class WeightUnit(val arrayIndex: Int, val displayName: String) {
    KG(0, "kg"),
    LB(1, "lb")
}

enum class HeightUnit(val arrayIndex: Int, val displayName: String) {
    CM(0, "cm"),
    IN(1, "in")
}

enum class CreatinineUnit(val arrayIndex: Int, val displayName: String) {
    MG(0, "mg/dL"),
    MMOL(1, "µmol/L")
}