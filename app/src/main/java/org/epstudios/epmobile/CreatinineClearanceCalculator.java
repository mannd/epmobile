package org.epstudios.epmobile;

import android.content.Intent;
import android.os.Bundle;

/**
 * Copyright (C) 2015 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p/>
 * Created by mannd on 3/12/15.
 * <p/>
 * This file is part of EP Mobile.
 * <p/>
 * EP Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * EP Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with EP Mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
public class CreatinineClearanceCalculator extends DrugCalculator {
    @Override
    protected int getDose(int crCl) {

        return CREATININE_CLEARANCE_ONLY;
    }

    @Override
    protected Boolean pediatricDosingOk() {
        return true;
    }

    @Override
    protected String defaultResultLabel() {
        ccTextView.setTextAppearance(this,
                android.R.style.TextAppearance_Medium);
        return getString(R.string.short_creatinine_clearance_label);
    }

    @Override
    protected String getDisclaimer() {
        return "";  // no disclaimer for CrCl
    }

    @Override
    protected void calculateDose() {
        super.calculateDose();
        Bundle bundle = new Bundle();
        bundle.putString("EXTRA_RESULT_STRING", getCreatinineClearanceReturnString());
        Intent mIntent = new Intent();
        mIntent.putExtras(bundle);
        setResult(RESULT_OK, mIntent);
    }

}
