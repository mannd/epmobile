package org.epstudios.epmobile.features.riskscores.ui;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.epstudios.epmobile.R;
import org.epstudios.epmobile.Reference;
import org.epstudios.epmobile.RiskScore;

import java.util.ArrayList;
import java.util.List;

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

    private final static FrailtyRule independentDependentRule = (a) -> a == 0 ? 0 : 1;
    private final static FrailtyRule noYesRule = (a) -> a == 0 ? 0 : 1;
    private final static FrailtyRule fitnessRule = (a) -> a < 7 ? 1 : 0;
    private final static FrailtyRule sometimesNoYesRule = (a) -> a == 2 ? 1 : 0;
    private final static FrailtyRule sometimesYesNoRule = (a) -> a == 0 ? 0 : 1;

    // Below just for testing
    public static FrailtyRule getFitnessRule() {
        return fitnessRule;
    }

    public static FrailtyRule getSometimesNoYesRule() {
        return sometimesNoYesRule;
    }

    public static FrailtyRule getSometimesYesNoRule() {
        return sometimesYesNoRule;
    }

    public static FrailtyRule getIndependentDependentRule() {
        return independentDependentRule;
    }


    public static FrailtyRule getNoYesRule() {
        return noYesRule;
    }

    private final class FrailtyRisk {
        FrailtyRule rule;
        Spinner spinner;
        String abnormalRiskDescription;

        FrailtyRisk(FrailtyRule rule, Spinner spinner, String abnormalRiskDescription) {
            this.rule = rule;
            this.spinner = spinner;
            this.abnormalRiskDescription = abnormalRiskDescription;
        }

        FrailtyRisk(FrailtyRule rule, Spinner spinner) {
            this(rule, spinner, "");
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
                risk.spinner.setSelection(10);
            }
            else {
                risk.spinner.setAdapter(noSometimesYesAdapter);
            }
        }
        // init fitness to 10, which gives a result of 0, like all the other default values.
    }

    @Override
    protected void calculateResult() {
        clearSelectedRisks();
        displayResult(getResultMessage(calculateRisk()),
                getString(R.string.frailty_title));
    }

    private String getResultMessage(int riskScore) {
        String message = "Score = " + riskScore + ".\n";
        if (riskScore >= 4) {
            message += "Score indicates frailty.";
        } else {
            message += "Score doesn't indicate frailty.";
        }
        resultMessage = message;
        return message;
    }

    public int calculateRisk() {
        int riskScore = 0;
        for (FrailtyRisk risk: risks) {
            if (risk.result() > 0) {
                addSelectedRisk(risk.abnormalRiskDescription);
            }
            riskScore += risk.result();
        }
        return riskScore;
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.frailty);
    }

    @Override
    protected void setupInsets() {
        setupInsets(R.id.frailty_root_view);
    }


    @Override
    protected void init() {
        FrailtyRisk shoppingRisk = new FrailtyRisk(independentDependentRule,
                findViewById(R.id.shopping_spinner), getMobilityRiskDescription("shop"));
        FrailtyRisk walkingRisk = new FrailtyRisk(independentDependentRule,
                findViewById(R.id.walking_spinner), getMobilityRiskDescription("walk"));
        FrailtyRisk dressingRisk = new FrailtyRisk(independentDependentRule,
                findViewById(R.id.dressing_spinner), getMobilityRiskDescription("dress"));
        FrailtyRisk toiletRisk = new FrailtyRisk(independentDependentRule,
                findViewById(R.id.toilet_spinner), getMobilityRiskDescription("go to the toilet"));
        FrailtyRisk fitnessRisk = new FrailtyRisk(fitnessRule,
                findViewById(R.id.fitness_spinner), "poor fitness");
        FrailtyRisk visionRisk = new FrailtyRisk(noYesRule,
                findViewById(R.id.vision_spinner), "poor vision");
        FrailtyRisk hearingRisk = new FrailtyRisk(noYesRule,
                findViewById(R.id.hearing_spinner), "poor hearing");
        FrailtyRisk nourishmentRisk = new FrailtyRisk(noYesRule,
                findViewById(R.id.nourishment_spinner), "poor nutrition");
        FrailtyRisk medicationRisk = new FrailtyRisk(noYesRule,
                findViewById(R.id.medication_spinner), "on multiple medications");
        FrailtyRisk cognitionRisk = new FrailtyRisk(sometimesNoYesRule,
                findViewById(R.id.cognition_spinner), "poor memory or dementia");
        FrailtyRisk emptinessRisk = new FrailtyRisk(sometimesYesNoRule,
                findViewById(R.id.emptiness_spinner), "feels empty");
        FrailtyRisk missPeopleRisk = new FrailtyRisk(sometimesYesNoRule,
                findViewById(R.id.miss_people_spinner), "Is lonely");
        FrailtyRisk abandonedRisk = new FrailtyRisk(sometimesYesNoRule,
                findViewById(R.id.abandoned_spinner), "feels abandoned");
        FrailtyRisk sadRisk = new FrailtyRisk(sometimesYesNoRule,
                findViewById(R.id.sad_spinner), "feels sad");
        FrailtyRisk nervousRisk = new FrailtyRisk(sometimesYesNoRule,
                findViewById(R.id.nervous_spinner), "feels nervous");

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

    private String getMobilityRiskDescription(String s) {
        return "not able to " + s + " independently";
    }

    @Override
    protected void clearEntries() {
        for (FrailtyRisk risk: risks) {
            if (risk.rule == fitnessRule) {
                risk.spinner.setSelection(10);
            } else {
                risk.spinner.setSelection(0);
            }
        }
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
        return getString(R.string.frailty_title);
    }

    @Override
    protected boolean hideInstructionsMenuItem() {
        return false;
    }

    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected boolean hideKeyMenuItem() {
        return true;
    }
}
