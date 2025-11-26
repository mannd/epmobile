package org.epstudios.epmobile.features.riskscores.ui

import org.epstudios.epmobile.features.riskscores.data.HcmAfModel.HcmAfRiskData
import org.epstudios.epmobile.features.riskscores.data.HcmAfValidationError

/**
Copyright (C) 2025 EP Studios, Inc.
www.epstudiossoftware.com

Created by mannd on 11/25/25.

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

public data class HcmAfUiState (
    public val score: Int? = null,
    public val riskData: HcmAfRiskData? = null,
    public val error: HcmAfValidationError? = null
)
