package com.example.weatheapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarItemView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    lateinit var pagerMain: ViewPager2
    var fragmentArrayList: ArrayList<Fragment> = ArrayList()
    lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pagerMain = findViewById(R.id.pagerMain)

        bottomNavigationView = findViewById(R.id.bottomNavBar)
        fragmentArrayList.add(HomeFragment())
        fragmentArrayList.add(FavouriteFragment())
        fragmentArrayList.add(AlertFragment())
        fragmentArrayList.add(SettingFragment())

        var adapterViewPager: AdapterViewPager = AdapterViewPager(this, fragmentArrayList)
        pagerMain.adapter = adapterViewPager
        pagerMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottomNavigationView.selectedItemId
                when (position) {
                    0 -> bottomNavigationView.selectedItemId = R.id.menuNavHomeID
                    1 -> bottomNavigationView.selectedItemId = R.id.menuNavFavID
                    2 -> bottomNavigationView.selectedItemId = R.id.menuNavAlertID
                    3 -> bottomNavigationView.selectedItemId = R.id.menuNavSettingID
                }
            }
        })
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menuNavHomeID-> pagerMain.currentItem=0
                R.id.menuNavFavID-> pagerMain.currentItem=1
                R.id.menuNavAlertID-> pagerMain.currentItem=2
                R.id.menuNavSettingID-> pagerMain.currentItem=3

            }
            true
        }

    }
}
