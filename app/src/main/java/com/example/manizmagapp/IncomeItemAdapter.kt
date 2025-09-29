package com.example.manizmagapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IncomeItemAdapter(private val incomeItems: ArrayList<IncomeItem>):
    RecyclerView.Adapter<IncomeItemAdapter.IncomeItemHolder>() {

    class IncomeItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        val heading : TextView = view.findViewById(R.id.spending_item_heading)
        val amount : TextView = view.findViewById(R.id.spending_item_amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.income_fragment_rv_item, parent, false)
        return IncomeItemHolder(view)
    }

    override fun onBindViewHolder(holder: IncomeItemHolder, position: Int) {
        val incomeItem: IncomeItem = incomeItems[position]

        holder.heading.text = incomeItem.source
        holder.amount.text = incomeItem.amount.toString()
    }

    override fun getItemCount(): Int {
        return incomeItems.size
    }
}