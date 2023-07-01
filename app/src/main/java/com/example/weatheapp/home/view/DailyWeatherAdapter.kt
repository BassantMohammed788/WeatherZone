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
import com.example.weatheapp.R
import com.example.weatheapp.databinding.DailyRowBinding
import com.example.weatheapp.model.Daily
import com.example.weatheapp.utilities.Constants
import java.util.*

class DailyDiffUtil: DiffUtil.ItemCallback<Daily>() {

    override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem == newItem
    }
}


class DailyWeatherAdapter (private val mySharedPreferences: MySharedPreferences):ListAdapter<Daily, DailyWeatherAdapter.DailyViewHolder>(DailyDiffUtil()) {
    lateinit var context: Context
    lateinit var binding: DailyRowBinding

    inner class DailyViewHolder(val binding: DailyRowBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        context = parent.context
        binding = DailyRowBinding.inflate(inflater, parent, false)
        return DailyViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val lang = mySharedPreferences.getLanguagePreference()
        val unitpref = mySharedPreferences.getTempratureUnitPreference()
        val unit = getTemperatureUnit(context,unitpref.toString())

        if (position in 0..6) {

            val currentObject = getItem(position)
            val dayName = if (position == 1 && lang == "en" ) { "Tomorrow" }
            else if(position == 1 && lang == "ar"){ "غداً" }
            else { getDayNameFromTimeStamp("${mySharedPreferences.getLanguagePreference()}",currentObject.dt) }

            holder.binding.dailyDayNameTv.text = dayName
            holder.binding.dailyDegreeTv.text = "${currentObject.temp.max} / ${currentObject.temp.min} $unit"
            holder.binding.dailyDescriptionTv.text = currentObject.weather[0].description
            val drawableResId = getIconResource(currentObject.weather[0].icon,context)
            holder.binding.dailyImage.setImageResource(drawableResId)

            Log.i("lang", "onBindViewHolder: $unit ")

        }
        if(position == 7){ binding.dailyConstraintLayout.visibility = View.INVISIBLE }
    }


    }


fun getDayNameFromTimeStamp(language: String, timeStamp: Long): String {
    val locale = Locale(language)
    val calendar = Calendar.getInstance(locale)
    calendar.timeInMillis = timeStamp * 1000 // Convert seconds to milliseconds
    return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale)
}

public fun getIconResource(desc: String, context: Context): Int {
    return when (desc) {
        "01d" -> R.drawable.clear_sky
        "02d" -> R.drawable.few_clouds
        "03d" -> R.drawable.scattered_clouds
        "04d" -> R.drawable.broken_clouds
        "09d" -> R.drawable.shower_rain
        "10d" -> R.drawable.rain
        "11d" -> R.drawable.thunderstorm
        "13d" -> R.drawable.snow
        "50d" -> R.drawable.mist
        "01n" -> R.drawable.night_clear_sky
        "02n" -> R.drawable.few_clouds
        "03n" -> R.drawable.scattered_clouds
        "04n" -> R.drawable.broken_clouds
        "09n" -> R.drawable.shower_rain
        "10n" -> R.drawable.rain
        "11n" -> R.drawable.thunderstorm
        "13n" -> R.drawable.snow
        "50n" -> R.drawable.mist
        else -> 0
    }
}
fun getTemperatureUnit(context: Context, unitPref: String): String {
    return when (unitPref) {
        Constants.standard.toString() -> context.getString(R.string.KDeg)
        Constants.imperial.toString() -> context.getString(R.string.FDeg)
        else -> context.getString(R.string.CDeg)
    }
}

