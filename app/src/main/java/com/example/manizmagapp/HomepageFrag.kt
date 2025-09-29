package com.example.manizmagapp

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.rgb
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomepageFrag : Fragment() {

    private lateinit var authorization: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var monthSpendingAmount: TextView
    private lateinit var monthIncomeAmount: TextView
    private lateinit var totalIncomeCountTV: TextView
    private lateinit var pieChart: PieChart
    private var totalSpending: Double = 0.0
    private var totalIncome : Double = 0.0
    private var totalIncomeCount: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.homepage_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        monthSpendingAmount = view.findViewById(R.id.Spending_Amount_Monthly)
        monthIncomeAmount = view.findViewById(R.id.Income_Amount_Monthly)
        totalIncomeCountTV = view.findViewById(R.id.CountIncomeText)
        pieChart=view.findViewById(R.id.pie_chart)

        GetSpendingDataFromDB()
        GetIncomeDataFromDB()
    }

    private fun GetSpendingDataFromDB() {
        authorization = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference.child("Spending")
            .child(authorization.currentUser!!.uid)

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.key?.let {
                        SpendingItem(
                            itemSnapshot.child("spendingItem/heading").value.toString(),
                            itemSnapshot.child("spendingItem/amount").value.toString()
                                .toDoubleOrNull()
                        )
                    }
                    if (item != null){
                        totalSpending += item.amount!!.toDouble()
                        monthSpendingAmount.setText("-"+totalSpending)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun GetIncomeDataFromDB() {
        authorization = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference.child("Income")
            .child(authorization.currentUser!!.uid)

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.key?.let {
                        IncomeItem(
                            itemSnapshot.child("incomeItem/source").value.toString(),
                            itemSnapshot.child("incomeItem/amount").value.toString()
                                .toDoubleOrNull()
                        )
                    }
                    if (item != null) {
                        totalIncomeCount += 1
                        totalIncome += item.amount!!.toDouble()
                        monthIncomeAmount.setText((totalIncome - totalSpending).toString())
                        totalIncomeCountTV.setText(totalIncomeCount.toString())
                    }
                }
                FillPieChart()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun FillPieChart(){
        val incomeF = totalIncome.toFloat()
        val spendF = totalSpending.toFloat()

        val charColors: IntArray = intArrayOf(rgb("#A3D8FF"), rgb("#FFB3A3"))

        val list:ArrayList<PieEntry> = ArrayList()

        list.add(PieEntry(incomeF,"Prihod"))
        list.add(PieEntry(spendF,"Potrosnja"))

        val pieDataSet= PieDataSet(list,"")

        pieDataSet.setColors(charColors,255)
        pieDataSet.valueTextColor= Color.BLACK
        pieDataSet.valueTextSize=20f

        val pieData= PieData(pieDataSet)

        pieChart.data= pieData

        pieChart.description.text = ""

        pieChart.animateY(2000)
    }
}