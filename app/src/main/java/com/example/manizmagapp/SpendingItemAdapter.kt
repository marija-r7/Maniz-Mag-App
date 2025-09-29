package com.example.manizmagapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SpendingItemAdapter(private val spendingItems: ArrayList<SpendingItem>):
    RecyclerView.Adapter<SpendingItemAdapter.SpendingItemHolder>() {

    class SpendingItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        val heading : TextView = view.findViewById(R.id.spending_item_heading)
        val amount : TextView = view.findViewById(R.id.spending_item_amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpendingItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.spending_fragment_rv_item, parent, false)
        return SpendingItemHolder(view)
    }

    override fun onBindViewHolder(holder: SpendingItemHolder, position: Int) {
        val spendingItem: SpendingItem = spendingItems[position]

        holder.heading.text = spendingItem.heading
        holder.amount.text = spendingItem.amount.toString()
    }

    override fun getItemCount(): Int {
        return spendingItems.size
    }
}