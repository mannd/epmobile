package org.epstudios.epmobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

class CalculatorList : EpActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selectionlist)
        super.setupInsets(R.id.my_root_view)
        initToolbar()
        val adapter = ArrayAdapter.createFromResource(
            this, R.array.calculator_list, android.R.layout.simple_list_item_1
        )
        val lv = findViewById<ListView>(R.id.list)
        lv.setAdapter(adapter)

        lv.setTextFilterEnabled(true)
        lv.setOnItemClickListener(OnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            val selection = (view as TextView).getText().toString()
            if (selection
                == getString(R.string.cycle_length_calculator_title)
            ) intervalRateCalculator()
            else if (selection
                == getString(R.string.qtc_calculator_title)
            ) qtcCalculator()
            else if (selection
                == getString(R.string.drug_dose_calculator_list_title)
            ) drugDoseCalculators()
            else if (selection
                == getString(R.string.date_calculator_title)
            ) icdDayCalculator()
            else if (selection
                == getString(R.string.ibw_calculator_title)
            ) ibwCalculator()
            else if (selection == getString(R.string.creatinine_clearance_calculator_title)) creatinineClearanceCalculator()
            else if (selection == getString(R.string.qtc_ivcd_calculator_title)) qtcIvcdCalculator()
            else if (selection == getString(R.string.gfr_calculator_title)) gfrCalculator()
        })
    }

    private fun qtcCalculator() {
        val i = Intent(this, Qtc::class.java)
        startActivity(i)
    }

    private fun intervalRateCalculator() {
        val i = Intent(this, CycleLength::class.java)
        startActivity(i)
    }

    private fun drugDoseCalculators() {
        val i = Intent(this, DrugDoseCalculatorList::class.java)
        startActivity(i)
    }

    private fun icdDayCalculator() {
        val i = Intent(this, DateCalculator::class.java)
        startActivity(i)
    }

    private fun ibwCalculator() {
        val i = Intent(this, IbwCalculator::class.java)
        startActivity(i)
    }

    private fun creatinineClearanceCalculator() {
        val i = Intent(this, CreatinineClearanceCalculator::class.java)
        startActivity(i)
    }

    private fun qtcIvcdCalculator() {
        startActivity(Intent(this, QtcIvcd::class.java))
    }

    private fun gfrCalculator() {
        startActivity(Intent(this, GfrCalculator::class.java))
    }
}
