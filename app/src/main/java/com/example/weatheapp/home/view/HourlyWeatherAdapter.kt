package com.example.weatheapp.home.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatheapp.MySharedPreferences
import com.example.weatheapp.databinding.DailyRowBinding
import com.example.weatheapp.databinding.HourlyRowBinding
import com.example.weatheapp.model.Daily
import com.example.weatheapp.model.Hourly
import java.text.SimpleDateFormat
import java.util.*


class HourlyDiffUtil: DiffUtil.ItemCallback<Hourly>() {

    override fun areItemsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
        return oldItem == newItem
    }
}


class HourlyWeatherAdapter (private val mySharedPreferences: MySharedPreferences):
    ListAdapter<Hourly, HourlyWeatherAdapter.HourlyViewHolder>(HourlyDiffUtil()) {
    lateinit var context: Context
    lateinit var binding: HourlyRowBinding

    inner class HourlyViewHolder(val binding: HourlyRowBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        context = parent.context
        binding = HourlyRowBinding.inflate(inflater, parent, false)
        return HourlyViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val lang = mySharedPreferences.getLanguagePreference()
        val unitpref = mySharedPreferences.getTempratureUnitPreference()
        val unit = getTemperatureUnit(context,unitpref.toString())

            val currentObject = getItem(position)

            holder.binding.hourlyHourTv.text = getHourWithAmPm(currentObject.dt,lang!!)
            holder.binding.hourlyDegreeTv.text = "${currentObject.temp} $unit"

            val drawableResId = getIconResource(currentObject.weather[0].icon,context)
            holder.binding.hourlyImage.setImageResource(drawableResId)

            Log.i("lang", "onBindViewHolder: $unit ")

    }


}
private fun getHourWithAmPm(timestamp: Long,lang:String): String {
    val timeD = Date(timestamp * 1000)
    val sdf = SimpleDateFormat("h:mm aaa",Locale(lang))
    return sdf.format(timeD)
}
