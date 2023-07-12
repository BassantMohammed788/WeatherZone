package com.example.weatheapp.alert.view

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatheapp.MySharedPreferences
import com.example.weatheapp.R
import com.example.weatheapp.database.AlertWeatherEntity
import com.example.weatheapp.databinding.AlertRowBinding
import com.example.weatheapp.utilities.getDateStringFromMillis
import com.example.weatheapp.utilities.getTimeStringFromMillis
import java.text.SimpleDateFormat
import java.util.*


class AlertDiffUtil : DiffUtil.ItemCallback<AlertWeatherEntity>() {

    override fun areItemsTheSame(oldItem: AlertWeatherEntity, newItem: AlertWeatherEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AlertWeatherEntity, newItem: AlertWeatherEntity): Boolean {
        return oldItem == newItem
    }
}

class AlertWeatherAdapter(private var mySharedPreferences: MySharedPreferences, private var deleteListener: (AlertWeatherEntity) -> Unit) :
    ListAdapter<AlertWeatherEntity, AlertWeatherAdapter.AlertViewHolder>(AlertDiffUtil()) {
    lateinit var context: Context

    inner class AlertViewHolder(val binding: AlertRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        context = parent.context
        val binding = AlertRowBinding.inflate(inflater, parent, false)
        return AlertViewHolder(binding)
    }


    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val currentObject = getItem(position)

        val language=mySharedPreferences.getLanguagePreference()
        holder.binding.alertRowFromDateTv.text = getDateStringFromMillis(currentObject.startDate,language!!)

        holder.binding.alertRowToDateTv.text = getDateStringFromMillis(currentObject.endDate,language!!)

        holder.binding.alertRowFromTimeTv.text = getTimeStringFromMillis(currentObject.startTime,language!!)

        holder.binding.alertRowToTimeTv.text = getTimeStringFromMillis(currentObject.endTime,language!!)
        holder.binding.alertRowDeleteBtn.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.titleDeleteAlarmAlert)
            builder.setMessage(R.string.messageDeleteAlarmAlert)
            builder.setPositiveButton(R.string.yesMapAlert) { _, _ ->
                deleteListener(currentObject)
            }
            builder.setNegativeButton(R.string.CancelMapAlert){ _, _ ->
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }

    }
    override fun getItemCount(): Int {
        val count = super.getItemCount()
        return count
    }


}