package com.example.weatheapp.favourite.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatheapp.MySharedPreferences
import com.example.weatheapp.database.FavWeatherPojo
import com.example.weatheapp.databinding.FavouriteRowBinding

class FavouriteDiffUtil : DiffUtil.ItemCallback<FavWeatherPojo>() {

    override fun areItemsTheSame(oldItem: FavWeatherPojo, newItem: FavWeatherPojo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FavWeatherPojo, newItem: FavWeatherPojo): Boolean {
        return oldItem == newItem
    }
}

class FavouriteWeatherAdapter(private var mySharedPreferences: MySharedPreferences,private var myListener: (FavWeatherPojo) -> Unit,private var deleteListener: (FavWeatherPojo) -> Unit) :
    ListAdapter<FavWeatherPojo, FavouriteWeatherAdapter.FavouriteViewHolder>(FavouriteDiffUtil()) {
    lateinit var context: Context
    lateinit var binding: FavouriteRowBinding

    inner class FavouriteViewHolder(val binding: FavouriteRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        context = parent.context
        binding = FavouriteRowBinding.inflate(inflater, parent, false)
        return FavouriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val currentObject = getItem(position)
        holder.binding.favouriteRowCityTv.text = currentObject.city
        holder.binding.favRowDeleteBtn.setOnClickListener {
            deleteListener(currentObject)
        }
        holder.binding.favRowConstrainLayout.setOnClickListener {
            myListener(currentObject)
            mySharedPreferences.saveFavLat(currentObject.latt.toString())
            mySharedPreferences.saveFavLng(currentObject.lng.toString())
            Log.i("TAG", "onBindViewHolderFav: $currentObject")
        }

    }
    override fun getItemCount(): Int {
        val count = super.getItemCount()
        Log.d("FavouriteWeatherAdapter", "Number of items: $count")
        return count
    }


}