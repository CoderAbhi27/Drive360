package com.drive.drive360.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.drive.drive360.R
import com.drive.drive360.Activity.ItemClickListener
import com.drive.drive360.DataClass.Driver
import com.squareup.picasso.Picasso

class DriverAdapter(private val drivers: List<Driver>, private val listener: ItemClickListener) :
    RecyclerView.Adapter<DriverAdapter.DriverViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_driver, parent, false)
        return DriverViewHolder(view)
    }

    override fun onBindViewHolder(holder: DriverViewHolder, position: Int) {
        val driver = drivers[position]
        if(driver.image!=null){
            Picasso.get().load(driver.image).into(holder.DriverImg)
        }

        holder.DriverName.text = driver.name
    }

    override fun getItemCount(): Int {
        return drivers.size
    }

    inner class DriverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val DriverImg: ImageView = itemView.findViewById(R.id.DriverImg)
        val DriverName: TextView = itemView.findViewById(R.id.DriverName)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }
}