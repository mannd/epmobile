package org.epstudios.epmobile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateCalculator extends EpActivity implements OnClickListener {
    private DatePicker indexDatePicker;
    private RadioGroup dayRadioGroup;
    private EditText numberOfDaysEditText;
    private TextView calculatedDateTextView;
    private CheckBox reverseTimeCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daycalculator);
        setupInsets(R.id.my_root_view);
        initToolbar();

        View calculateDateButton = findViewById(R.id.calculate_button);
        calculateDateButton.setOnClickListener(this);
        View clearButton = findViewById(R.id.clear_button);
        clearButton.setOnClickListener(this);

        indexDatePicker = findViewById(R.id.indexDatePicker);
        dayRadioGroup = findViewById(R.id.dayRadioGroup);
        numberOfDaysEditText = findViewById(R.id.numberOfDaysEditText);
        reverseTimeCheckBox = findViewById(R.id.reverseTimeCheckBox);
        calculatedDateTextView = findViewById(R.id.calculated_date);
        numberOfDaysEditText.setText(R.string.dc_default_number_of_days);

        dayRadioGroup
                .setOnCheckedChangeListener((group, checkedId) -> {
                    RadioButton checkedRadioButton = group
                            .findViewById(checkedId);
                    int index = group.indexOfChild(checkedRadioButton);
                    int number = 0;
                    switch (index) {
                        case 0:
                            number = 90;
                            break;
                        case 1:
                            number = 40;
                            break;
                        case 2:
                            number = 30;
                            break;
                        // else still = 0;
                    }
                    if (number != 0)
                        numberOfDaysEditText.setText(String.valueOf(number));
                });

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
            calculateDays();
        } else if (id == R.id.clear_button) {
            clearEntries();
        }
    }

    private void calculateDays() {
        CharSequence numberOfDays = numberOfDaysEditText.getText();
        try {
            int number = Integer.parseInt(numberOfDays.toString());
            if (reverseTimeCheckBox.isChecked())
                number = -number;
            Calendar cal = new GregorianCalendar(indexDatePicker.getYear(),
                    indexDatePicker.getMonth(), indexDatePicker.getDayOfMonth());
            cal.add(Calendar.DATE, number);
            // DateFormat =
            // SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String message = DateFormat.getDateInstance(DateFormat.MEDIUM).format(cal.getTime());
            calculatedDateTextView.setText(message);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        } catch (NumberFormatException e) {
            calculatedDateTextView.setText(getString(R.string.invalid_warning));
            calculatedDateTextView.setTextColor(Color.RED);
        }

    }

    private void clearEntries() {
        numberOfDaysEditText.setText(null);
        calculatedDateTextView.setText(getString(R.string.date_result_label));
        dayRadioGroup.check(R.id.ninetyRadio);
        numberOfDaysEditText.setText(getString(R.string.dc_default_number_of_days));
        calculatedDateTextView.setTextAppearance(this,
                android.R.style.TextAppearance_Large);

    }

    @Override
    protected boolean hideInstructionsMenuItem() {
        return false;
    }

    @Override
    protected void showActivityInstructions() {
        showAlertDialog(R.string.date_calculator_title,
                R.string.date_calculator_instructions);
    }
}
