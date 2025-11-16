package org.epstudios.epmobile.features.diagnosis.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import org.epstudios.epmobile.features.riskscores.ui.ErsScore
import org.epstudios.epmobile.LinkView
import org.epstudios.epmobile.LongQtList
import org.epstudios.epmobile.LvhList
import org.epstudios.epmobile.R
import org.epstudios.epmobile.ShortQt
import org.epstudios.epmobile.TamponadeScore
import org.epstudios.epmobile.VtList
import org.epstudios.epmobile.WctAlgorithmList
import org.epstudios.epmobile.WpwAlgorithmList
import org.epstudios.epmobile.core.ui.base.EpActivity
import org.epstudios.epmobile.features.riskscores.ui.Arvc
import org.epstudios.epmobile.features.riskscores.ui.ArvcOld

class DiagnosisList : EpActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selectionlist)
        setupInsets(R.id.selection_list_root_view)
        initToolbar()
        val adapter = ArrayAdapter.createFromResource(
            this, R.array.diagnosis_list,
            android.R.layout.simple_list_item_1
        )
        val lv = findViewById<ListView>(R.id.list)
        lv.setAdapter(adapter)

        lv.setTextFilterEnabled(true)


        lv.setOnItemClickListener(AdapterView.OnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            val selection = (view as TextView).getText().toString()
            if (selection
                == getString(R.string.wct_algorithm_list_title)
            ) wctAlgorithm()
            else if (selection == getString(R.string.short_qt_title)) shortQt()
            else if (selection
                == getString(R.string.wpw_algorithm_list_title)
            ) wpw()
            else if (selection
                == getString(R.string.lqt_syndrome_title)
            ) longQt()
            else if (selection == getString(R.string.lvh_list_title)) lvhList()
            else if (selection
                == getString(R.string.brugada_syndrome_title)
            ) brugadaList()
            else if (selection == getString(R.string.ers_title)) ersScore()
            else if (selection == getString(R.string.vt_list_title)) vtList()
            else if (selection
                == getString(R.string.atrial_tachycardia_localization_title)
            ) atrialTachLocalization()
            else if (selection == getString(R.string.rvh_title)) rvhCriteria()
            else if (selection
                == getString(R.string.arvc_2010_criteria_title)
            ) arvc2010()
            else if (selection
                == getString(R.string.arvc_old_criteria_title)
            ) arvcOld()
            else if (selection == getString(R.string.lbbb_title)) lbbbCriteria()
            else if (selection == getString(R.string.tamponade_title)) tamponade()
        })
    }

    protected fun vtList() {
        val i = Intent(this, VtList::class.java)
        startActivity(i)
    }

    private fun wctAlgorithm() {
        val i = Intent(this, WctAlgorithmList::class.java)
        startActivity(i)
    }

    private fun shortQt() {
        val i = Intent(this, ShortQt::class.java)
        startActivity(i)
    }

    private fun wpw() {
        val i = Intent(this, WpwAlgorithmList::class.java)
        startActivity(i)
    }

    private fun longQt() {
        val i = Intent(this, LongQtList::class.java)
        startActivity(i)
    }

    private fun lvhList() {
        val i = Intent(this, LvhList::class.java)
        startActivity(i)
    }

    private fun brugadaList() {
        val i = Intent(this, BrugadaList::class.java)
        startActivity(i)
    }

    private fun atrialTachLocalization() {
        val i = Intent(this, AtrialTachLocalization::class.java)
        startActivity(i)
    }

    private fun rvhCriteria() {
        val i = Intent(this, LinkView::class.java)
        i.putExtra("EXTRA_URL", "file:///android_asset/rvh.html")
        i.putExtra("EXTRA_TITLE", getString(R.string.rvh_title))
        startActivity(i)
    }

    private fun arvc2010() {
        val i = Intent(this, Arvc::class.java)
        startActivity(i)
    }

    private fun arvcOld() {
        val i = Intent(this, ArvcOld::class.java)
        startActivity(i)
    }

    private fun ersScore() {
        val i = Intent(this, ErsScore::class.java)
        startActivity(i)
    }

    private fun lbbbCriteria() {
        val i = Intent(this, LinkView::class.java)
        i.putExtra("EXTRA_URL", "file:///android_asset/lbbb.html")
        i.putExtra("EXTRA_TITLE", getString(R.string.lbbb_title))
        startActivity(i)
    }

    private fun tamponade() {
        val i = Intent(this, TamponadeScore::class.java)
        startActivity(i)
    }
}