package com.example.drive360.Fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.drive360.Activity.FuelTrackingOrderActivity
import com.example.sagarmiles.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate

class FuelTrackingFragment : androidx.fragment.app.Fragment(R.layout.fragment_fuel_tracking) {
    private lateinit var pieChart: PieChart


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fuel_tracking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orderNowButton : Button = view.findViewById(R.id.orderNowButton)
        pieChart=view.findViewById(R.id.piechart)

        populatePieChart(pieChart)

        orderNowButton.setOnClickListener{
            val intent = Intent(activity, FuelTrackingOrderActivity::class.java)
            startActivity(intent)
        }
    }

    private fun populatePieChart(pieChart: PieChart) {
        pieChart.isDrawHoleEnabled=true
        pieChart.setHoleColor(Color.TRANSPARENT)
        //pieChart.setUsePercentValues(true)
        //pieChart.setEntryLabelColor(Color.BLACK)
        //pieChart.setEntryLabelTextSize(12f)
        pieChart.setDrawEntryLabels(false)
        pieChart.centerText="MEMORY ANALYSIS"
        pieChart.setCenterTextSize(16f)
        pieChart.setCenterTextColor(Color.WHITE)

        pieChart.description.isEnabled=false
        pieChart.setExtraOffsets(5f,5f,20f,5f)

        val l: Legend =pieChart.legend
        l.verticalAlignment= Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment= Legend.LegendHorizontalAlignment.RIGHT
        l.orientation= Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.textColor= Color.WHITE
        l.isEnabled=true
        l.textSize=12f
        l.xOffset=0f
        l.yOffset=0f
        val values:ArrayList<PieEntry> = ArrayList()
        values.add(PieEntry(0.42f,"FUEL COSTS"))
        values.add(PieEntry(0.11f,"INSURANCE"))
        values.add(PieEntry(0.05f,"TRAVEL EXPENSES"))
        values.add(PieEntry(0.09f,"REPAIR OF SPARE PARTS"))
        values.add(PieEntry(0.1f,"DEPRECIATION"))
        values.add(PieEntry(0.11f,"SALARIES OF DRIVERS"))
        values.add(PieEntry(0.12f,"TRAVEL, TERMINALS, PARKING, COMMUNICATIONS"))
        val colors:ArrayList<Int> = ArrayList()

        for (color:Int in ColorTemplate.MATERIAL_COLORS)
            colors.add(color)
        val dataSet: PieDataSet = PieDataSet(values,"FUEL COSTS TRACKING")
        dataSet.colors=colors
        val data: PieData = PieData(dataSet)
        data.setDrawValues(false)
        data.setValueFormatter(PercentFormatter(pieChart))
        data.setValueTextSize(12f)
        data.setValueTextColor(Color.BLACK)
        pieChart.data=data
        pieChart.invalidate()
        pieChart.animateY(1400, Easing.EaseInOutQuad)

    }
}