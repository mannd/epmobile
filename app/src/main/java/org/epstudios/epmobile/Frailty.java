package org.epstudios.epmobile;

/**
 * Copyright (C) 2023 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 10/21/23.
 * <p>
 * This file is part of epmobile.
 * <p>
 * epmobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * epmobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with epmobile.  If not, see <http://www.gnu.org/licenses/>.
 */
public class Frailty extends RiskScore {

    @Override
    protected void calculateResult() {

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.frailty);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void clearEntries() {

    }

    protected void showActivityInstructions() {
        showAlertDialog(R.string.frailty_title, R.string.frailty_instructions);
    }

    @Override
    protected void showActivityReference() {
        Reference[] references = new Reference[3];
        references[0] = new Reference( getString(R.string.frailty_reference_1), null);
        references[1] = new Reference(this, R.string.frailty_reference_2, R.string.frailty_link_2);
        references[2] = new Reference(this, R.string.frailty_reference_3, R.string.frailty_link_3);
        showReferenceAlertDialog(references);
    }

    @Override
    protected String getFullReference() {
        String reference1 = convertReferenceToText(R.string.frailty_reference_1,
                R.string.no_link_available);
        String reference2 = convertReferenceToText(R.string.frailty_reference_2,
                R.string.frailty_link_2);
        String reference3 = convertReferenceToText(R.string.frailty_reference_3,
                R.string.frailty_link_3);
        String fullReference = reference1 + "\n" + reference2 + "\n" + reference3;
        return fullReference;
    }

    @Override
    protected String getRiskLabel() {
        return null;
    }
}
