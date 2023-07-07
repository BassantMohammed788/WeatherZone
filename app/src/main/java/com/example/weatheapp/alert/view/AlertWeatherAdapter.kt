package com.example.weatheapp.alert.view

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatheapp.MySharedPreferences
import com.example.weatheapp.R
import com.example.weatheapp.database.AlertWeatherEntity
import com.example.weatheapp.databinding.AlertRowBinding


class AlertDiffUtil : DiffUtil.ItemCallback<AlertWeatherEntity>() {

    override fun areItemsTheSame(oldItem: AlertWeatherEntity, newItem: AlertWeatherEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AlertWeatherEntity, newItem: AlertWeatherEntity): Boolean {
        return oldItem == newItem
    }
}

class AlertWeatherAdapter(private var mySharedPreferences: MySharedPreferences, private var myListener: (AlertWeatherEntity) -> Unit, private var deleteListener: (AlertWeatherEntity) -> Unit) :
    ListAdapter<AlertWeatherEntity, AlertWeatherAdapter.AlertViewHolder>(AlertDiffUtil()) {
    lateinit var context: Context
    lateinit var binding: AlertRowBinding

    inner class AlertViewHolder(val binding: AlertRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        context = parent.context
        binding = AlertRowBinding.inflate(inflater, parent, false)
        return AlertViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val currentObject = getItem(position)
        holder.binding.alertRowFromDateTv.text = currentObject.alertStartDate

        holder.binding.alertRowToDateTv.text = currentObject.alertEndDate

        holder.binding.alertRowFromTimeTv.text = currentObject.alertStartTime

        holder.binding.alertRowFromTimeTv.text = currentObject.alertEndTime
        holder.binding.alertRowDeleteBtn.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.titleDeleteAlarmAlert)
            builder.setMessage(R.string.messageDeleteAlarmAlert)
            builder.setPositiveButton(R.string.yesMapAlert) { dialog, which ->
                deleteListener(currentObject)
            }
            builder.setNegativeButton(R.string.CancelMapAlert){ dialog, which ->
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }
        holder.binding.alertConstraintLayout.setOnClickListener {
            myListener(currentObject)
        }

    }
    override fun getItemCount(): Int {
        val count = super.getItemCount()
        return count
    }


}