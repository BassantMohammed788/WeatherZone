package com.example.weatheapp.mainactivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class AdapterViewPager : FragmentStateAdapter  {
    constructor(fragmentActivity: FragmentActivity,arr:ArrayList<Fragment>) : super(fragmentActivity){
        this.arr=arr
    }


    lateinit var arr :ArrayList<Fragment>

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun createFragment(position: Int): Fragment {
        return arr[position]
    }
}