package com.drive.drive360.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.drive.drive360.DataClass.OrderData
import com.drive.drive360.R

class OrdersAdapter(private val dataList: ArrayList<OrderData>, val context: Context) :
    RecyclerView.Adapter<OrdersAdapter.ViewHolder>(){

    private val PER_ORDER_COST = 1000

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_orders, parent, false)
        return OrdersAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.name.text = "Name: ${data.name}"
        holder.phone.text = "Phone no: ${data.phone}"
        holder.company.text = "Company: ${data.company}"
        holder.deliveryAddress.text = "Delivery Address: ${data.deliveryAddress}"
        holder.noOfDevices.text = "No of devices: ${data.noOfDevices}"
        holder.amount.text = "Total Cost:  ₹${data.noOfDevices*PER_ORDER_COST}"
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name : TextView = view.findViewById(R.id.tvName)
        val phone : TextView = view.findViewById(R.id.tvPhone)
        val company : TextView = view.findViewById(R.id.tvCompany)
        val deliveryAddress : TextView = view.findViewById(R.id.tvDeliveryAddress)
        val noOfDevices : TextView = view.findViewById(R.id.tvNoOfDevices)
        val amount : TextView = view.findViewById(R.id.tvAmount)
    }


}