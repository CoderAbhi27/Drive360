package com.drive.drive360.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.drive.drive360.DataClass.bookRideData
import com.drive.drive360.R

class RidesAdapter(private val dataList: ArrayList<bookRideData>, val context: Context) :
    RecyclerView.Adapter<RidesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rides, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.name.text = "Name: ${data.name}"
        holder.phone.text = "Phone no: ${data.phone}"
        holder.pickupAddress.text = "Pickup Address: ${data.pickupAddress}"
        holder.destinationAddress.text = "Destination Address: ${data.destinationAddress}"
        holder.fromDate.text = "From: ${data.fromDate}"
        holder.toDate.text = "To: ${data.toDate}"
        holder.paymentMethod.text = "Payment Method: ${data.paymentMethod}"
        holder.amountDue.text = "Amount Due:  ₹${data.amountDue.toString()}"
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val name : TextView = view.findViewById(R.id.textName)
        val phone : TextView = view.findViewById(R.id.textPhone)
        val pickupAddress : TextView = view.findViewById(R.id.textPickupAddress)
        val destinationAddress : TextView = view.findViewById(R.id.textDestinationAddress)
        val fromDate : TextView = view.findViewById(R.id.textFromDate)
        val toDate : TextView = view.findViewById(R.id.textToDate)
        val paymentMethod : TextView = view.findViewById(R.id.textPaymentMethod)
        val amountDue : TextView = view.findViewById(R.id.textAmountDue)

    }


}