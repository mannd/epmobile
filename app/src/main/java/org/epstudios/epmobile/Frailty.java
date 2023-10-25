package org.epstudios.epmobile;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import kotlin.Function;

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
    interface FrailtyRule {
        int operation(int a);
    }

    private final FrailtyRule independentDependentRule = (a) -> a == 0 ? 0 : 1;
    private final FrailtyRule noYesRule = (a) -> a == 0 ? 0 : 1;
    private final FrailtyRule fitnessRule = (a) -> a < 7 ? 1 : 0;
    private final FrailtyRule sometimesNoYesRule = (a) -> a == 2 ? 1 : 0;
    private final FrailtyRule sometimesYesNoRule = (a) -> a == 0 ? 0 : 1;

    private class FrailtyRisk {
        FrailtyRule rule;
        Spinner spinner;

        FrailtyRisk(FrailtyRule rule, Spinner spinner) {
            this.rule = rule;
            this.spinner = spinner;
        }

        int result() {
            int value = spinner.getSelectedItemPosition();
            return rule.operation(value);
        }
    }

    private List<FrailtyRisk> risks = new ArrayList<>();

    private void initSpinners() {
        ArrayAdapter<CharSequence> independentAdapter = ArrayAdapter
                .createFromResource(this, R.array.independent_dependent_labels,
                        android.R.layout.simple_spinner_item);
        independentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> noYesAdapter = ArrayAdapter
                .createFromResource(this, R.array.no_yes_labels,
                        android.R.layout.simple_spinner_item);
        noYesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> fitnessAdapter = ArrayAdapter
                .createFromResource(this, R.array.fitness_labels,
                        android.R.layout.simple_spinner_item);
        fitnessAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> noSometimesYesAdapter = ArrayAdapter
                .createFromResource(this, R.array.no_sometimes_yes_labels,
                        android.R.layout.simple_spinner_item);
        noSometimesYesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (FrailtyRisk risk: risks) {
            if (risk.rule == independentDependentRule) {
                risk.spinner.setAdapter(independentAdapter);
            }
            else if (risk.rule == noYesRule) {
                risk.spinner.setAdapter(noYesAdapter);
            }
            else if (risk.rule == fitnessRule) {
                risk.spinner.setAdapter(fitnessAdapter);
            }
            else {
                risk.spinner.setAdapter(noSometimesYesAdapter);
            }
        }
    }

    @Override
    protected void calculateResult() {
        Log.d("EPS", "Risk = " + calculateRisk());
    }

    public int calculateRisk() {
        int riskScore = 0;
        for (FrailtyRisk risk: risks) {
            riskScore += risk.result();
        }
        return riskScore;
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.frailty);
    }

    @Override
    protected void init() {
        FrailtyRisk shoppingRisk = new FrailtyRisk(independentDependentRule, findViewById(R.id.shopping_spinner));
        FrailtyRisk walkingRisk = new FrailtyRisk(independentDependentRule, findViewById(R.id.walking_spinner));
        FrailtyRisk dressingRisk = new FrailtyRisk(independentDependentRule, findViewById(R.id.dressing_spinner));
        FrailtyRisk toiletRisk = new FrailtyRisk(independentDependentRule, findViewById(R.id.toilet_spinner));
        FrailtyRisk fitnessRisk = new FrailtyRisk(fitnessRule, findViewById(R.id.fitness_spinner));
        FrailtyRisk visionRisk = new FrailtyRisk(noYesRule, findViewById(R.id.vision_spinner));
        FrailtyRisk hearingRisk = new FrailtyRisk(noYesRule, findViewById(R.id.hearing_spinner));
        FrailtyRisk nourishmentRisk = new FrailtyRisk(noYesRule, findViewById(R.id.nourishment_spinner));
        FrailtyRisk medicationRisk = new FrailtyRisk(noYesRule, findViewById(R.id.medication_spinner));
        FrailtyRisk cognitionRisk = new FrailtyRisk(sometimesNoYesRule, findViewById(R.id.cognition_spinner));
        FrailtyRisk emptinessRisk = new FrailtyRisk(sometimesYesNoRule, findViewById(R.id.emptiness_spinner));
        FrailtyRisk missPeopleRisk = new FrailtyRisk(sometimesYesNoRule, findViewById(R.id.miss_people_spinner));
        FrailtyRisk abandonedRisk = new FrailtyRisk(sometimesYesNoRule, findViewById(R.id.abandoned_spinner));
        FrailtyRisk sadRisk = new FrailtyRisk(sometimesYesNoRule, findViewById(R.id.sad_spinner));
        FrailtyRisk nervousRisk = new FrailtyRisk(sometimesYesNoRule, findViewById(R.id.nervous_spinner));

        risks.add(shoppingRisk);
        risks.add(walkingRisk);
        risks.add(dressingRisk);
        risks.add(toiletRisk);
        risks.add(fitnessRisk);
        risks.add(visionRisk);
        risks.add(hearingRisk);
        risks.add(nourishmentRisk);
        risks.add(medicationRisk);
        risks.add(cognitionRisk);
        risks.add(emptinessRisk);
        risks.add(missPeopleRisk);
        risks.add(abandonedRisk);
        risks.add(sadRisk);
        risks.add(nervousRisk);

        initSpinners();
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
