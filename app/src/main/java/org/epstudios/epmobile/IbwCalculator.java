package org.epstudios.epmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;

public class IbwCalculator extends EpActivity implements OnClickListener {
    private RadioGroup sexRadioGroup;
    private EditText weightEditText;
    private EditText heightEditText;
    private Spinner weightSpinner;
    private Spinner heightSpinner;
    private TextView ibwTextView;
    private TextView abwTextView;
    private TextView ibwResultTextView;
    private TextView abwResultTextView;
    private TextView messageTextView;

    private enum WeightUnit {
        KG, LB
    }

    private enum HeightUnit {
        CM, IN
    }

    private final static int KG_SELECTION = 0;
    private final static int LB_SELECTION = 1;
    private final static int CM_SELECTION = 0;
    private final static int IN_SELECTION = 1;

    private WeightUnit defaultWeightUnitSelection = WeightUnit.KG;
    private HeightUnit defaultHeightUnitSelection = HeightUnit.CM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ibw);
        initToolbar();

        View calculateButton = findViewById(R.id.calculate_button);
        calculateButton.setOnClickListener(this);
        View clearButton = findViewById(R.id.clear_button);
        clearButton.setOnClickListener(this);

        sexRadioGroup = findViewById(R.id.sexRadioGroup);
        weightEditText = findViewById(R.id.weightEditText);
        heightEditText = findViewById(R.id.heightEditText);
        weightSpinner = findViewById(R.id.weight_spinner);
        heightSpinner = findViewById(R.id.height_spinner);
        ibwTextView = findViewById(R.id.ibwTextView);
        abwTextView = findViewById(R.id.abwTextView);
        ibwResultTextView = findViewById(R.id.ibwResultTextView);
        abwResultTextView = findViewById(R.id.abwResultTextView);
        messageTextView = findViewById(R.id.messageTextView);

        getPrefs();
        setAdapters();
        clearEntries();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent parentActivityIntent = new Intent(this, CalculatorList.class);
            parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(parentActivityIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.calculate_button) {
            calculate();
        } else if (id == R.id.clear_button) {
            clearEntries();
        }
    }

    private void setAdapters() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.weight_unit_labels,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightSpinner.setAdapter(adapter);
        if (defaultWeightUnitSelection.equals(WeightUnit.KG))
            weightSpinner.setSelection(KG_SELECTION);
        else
            weightSpinner.setSelection(LB_SELECTION);
        // do nothing
        OnItemSelectedListener itemListener = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v,
                                       int position, long id) {
                updateWeightUnitSelection();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }

        };

        ArrayAdapter<CharSequence> heightAdapter = ArrayAdapter
                .createFromResource(this, R.array.height_unit_labels,
                        android.R.layout.simple_spinner_item);
        heightAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        heightSpinner.setAdapter(heightAdapter);
        if (defaultHeightUnitSelection.equals(HeightUnit.CM))
            heightSpinner.setSelection(CM_SELECTION);
        else
            heightSpinner.setSelection(IN_SELECTION);
        // do nothing
        OnItemSelectedListener heightItemListener = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v,
                                       int position, long id) {
                updateHeightUnitSelection();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }

        };

        weightSpinner.setOnItemSelectedListener(itemListener);
        heightSpinner.setOnItemSelectedListener(heightItemListener);
    }

    private void updateWeightUnitSelection() {
        WeightUnit weightUnitSelection = getWeightUnitSelection();
        if (weightUnitSelection.equals(WeightUnit.KG)) {
            weightEditText.setHint(getString(R.string.weight_hint));
            ibwTextView.setText(getString(R.string.ibw_label));
            ibwResultTextView.setHint(getString(R.string.ibw_hint));
            abwTextView.setText(getString(R.string.abw_label));
            abwResultTextView.setHint(getString(R.string.abw_hint));
        } else {
            weightEditText.setHint(getString(R.string.weight_lb_hint));
            ibwTextView.setText(getString(R.string.ibw_lb_label));
            ibwResultTextView.setHint(getString(R.string.ibw_lb_hint));
            abwTextView.setText(getString(R.string.abw_lb_label));
            abwResultTextView.setHint(getString(R.string.abw_lb_hint));
        }
    }

    private WeightUnit getWeightUnitSelection() {
        int result = weightSpinner.getSelectedItemPosition();
        if (result == KG_SELECTION)
            return WeightUnit.KG;
        else
            return WeightUnit.LB;
    }

    private void updateHeightUnitSelection() {
        HeightUnit heightUnitSelection = getHeightUnitSelection();
        if (heightUnitSelection.equals(HeightUnit.CM))
            heightEditText.setHint(getString(R.string.height_hint));
        else
            heightEditText.setHint(getString(R.string.height_inches_hint));
    }

    private HeightUnit getHeightUnitSelection() {
        int result = heightSpinner.getSelectedItemPosition();
        if (result == CM_SELECTION)
            return HeightUnit.CM;
        else
            return HeightUnit.IN;
    }

    private void calculate() {
        // clear any message
        messageTextView.setText(null);
        // make sure message white with 2 calculations in row, 1st invalid
        resetResultTextColor();
        boolean isMale = sexRadioGroup.getCheckedRadioButtonId() == R.id.male;
        CharSequence weightText = weightEditText.getText();
        CharSequence heightText = heightEditText.getText();
        try {
            boolean unitsInLbs = false;
            double weight = Double.parseDouble(weightText.toString());
            double originalWeight = weight;
            if (getWeightUnitSelection().equals(WeightUnit.LB)) {
                weight = UnitConverter.lbsToKgs(weight);
                unitsInLbs = true;
            }
            double height = Double.parseDouble(heightText.toString());
            if (getHeightUnitSelection().equals(HeightUnit.CM))
                height = UnitConverter.cmsToIns(height);
            double ibw = idealBodyWeight(height, isMale);
            double abw = adjustedBodyWeight(ibw, weight);
            boolean overweight = isOverweight(ibw, weight);
            boolean underheight = isUnderHeight(height);
            boolean underweight = isUnderWeight(weight, ibw);
            String weightUnitAbbreviation = getString(R.string.kg_abbreviation);
            if (unitsInLbs) {
                ibw = UnitConverter.kgsToLbs(ibw);
                abw = UnitConverter.kgsToLbs(abw);
                weightUnitAbbreviation = getString(R.string.pound_abbreviation);
            }
            String formattedIbw = new DecimalFormat("#.#").format(ibw);
            String formattedAbw = new DecimalFormat("#.#").format(abw);
            ibwResultTextView.setText(formattedIbw);
            abwResultTextView.setText(formattedAbw);
            if (underheight)
                messageTextView
                        .setText(getString(R.string.underheight_message));
            else if (overweight) {
                messageTextView.setText(getString(R.string.overweight_message,
                        formatWeight(formattedAbw, weightUnitAbbreviation)));
            } else if (underweight)
                messageTextView
                        .setText(getString(R.string.underweight_message,
                                formatWeight(new DecimalFormat("#.#")
                                                .format(originalWeight),
                                        weightUnitAbbreviation)));
            else
                // normal weight
                messageTextView
                        .setText(getString(R.string.normalweight_message,
                                formatWeight(formattedIbw,
                                        weightUnitAbbreviation)));

        } catch (NumberFormatException e) {
            ibwResultTextView.setText(getString(R.string.invalid_warning));
            ibwResultTextView.setTextColor(Color.RED);
            abwResultTextView.setText(getString(R.string.invalid_warning));
            abwResultTextView.setTextColor(Color.RED);
        }
    }

    private String formatWeight(String weight, String units) {
        return weight + " " + units + ").";
    }

    public static double idealBodyWeight(double height, boolean isMale) {
        double weight = height > 60.0 ? (height - 60.0) * 2.3 : 0.0;
        if (isMale)
            weight += 50.0;
        else
            weight += 45.5;
        return weight;
    }

    public static double adjustedBodyWeight(double ibw, double actualWeight) {
        // for now, literature seems to support 0.4 as best correction factor
        double abw = ibw + 0.4 * (actualWeight - ibw);
        abw = actualWeight > ibw ? abw : actualWeight;
        return abw;
    }

    public static boolean isOverweight(double ibw, double actualWeight) {
        return actualWeight > ibw + .3 * ibw;
    }

    public static boolean isUnderHeight(double height) {
        return height <= 60.0;
    }

    public static boolean isUnderWeight(double weight, double ibw) {
        return weight < ibw;
    }

    private void clearEntries() {
        weightEditText.setText(null);
        heightEditText.setText(null);
        ibwResultTextView.setText(null);
        abwResultTextView.setText(null);
        resetResultTextColor();
        messageTextView.setText(null);
        weightEditText.requestFocus();

    }

    private void resetResultTextColor() {
        ibwResultTextView.setTextAppearance(this,
                android.R.style.TextAppearance_Medium);
        abwResultTextView.setTextAppearance(this,
                android.R.style.TextAppearance_Medium);
    }

    private void getPrefs() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        String weightUnitPreference = prefs.getString("default_weight_unit",
                "KG");
        String heightUnitPreference = prefs.getString("default_height_unit",
                "CM");
        if (weightUnitPreference.equals("KG"))
            defaultWeightUnitSelection = WeightUnit.KG;
        else
            defaultWeightUnitSelection = WeightUnit.LB;
        if (heightUnitPreference.equals("CM"))
            defaultHeightUnitSelection = HeightUnit.CM;
        else
            defaultHeightUnitSelection = HeightUnit.IN;
    }

    @Override
    protected boolean hideInstructionsMenuItem() {
        return false;
    }

    @Override
    protected void showActivityInstructions() {
        showAlertDialog(R.string.ibw_calculator_title,
                R.string.ibw_calculator_instructions );
    }

    @Override
    protected boolean hideReferenceMenuItem() {
        return false;
    }

    @Override
    protected void showActivityReference() {
        showReferenceAlertDialog(R.string.ibw_calculator_reference,
                R.string.ibw_calculator_link);
    }

}
