package com.example.weatheapp.favourite.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.weatheapp.MainDispatcherRule
import com.example.weatheapp.database.AlertWeatherEntity
import com.example.weatheapp.database.FavWeatherEntity
import com.example.weatheapp.database.LocalFavState
import com.example.weatheapp.database.MyResponseEntity
import com.example.weatheapp.models.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class FavouriteViewModelTest {

    val mainDispatcherRule = MainDispatcherRule()

    val weather = listOf<Weather>(Weather(1, "", "", ""))
    val current = Current(1, 2, 3, 4.0, 5.0, 6.0, 7, 8.0, 9.0, 10, 0, 11.0, 12, weather)
    val minutelyWeather = listOf<MinutelyWeather>(MinutelyWeather(1, 2.0))
    val hourlyWeather = listOf<Hourly>(Hourly(1, 2.0, 3.0, weather))
    val dailyWeather =
        listOf<Daily>(Daily(1, Temperature(2.0, 3.0, 4.0, 5.0, 6.0, 7.0), 2.0, weather))
    val alerts = listOf<Alert>(Alert("", "", 1, 2, "", listOf("", "")))
    val remoteResponse = MyResponse(
        31.268104553222656,
        30.019702911376953,
        "Africa/Cairo",
        0,
        current,
        minutelyWeather,
        hourlyWeather,
        dailyWeather,
        alerts
    )
    val homeWeather = MyResponseEntity(
        "alexandria",
        31.268104553222656,
        30.019702911376953,
        current,
        hourlyWeather,
        dailyWeather,
        alerts,
        "home",
        "1"
    )

    val favWeather1 = FavWeatherEntity(1.02, 2.02, "cairo", "1.022.02cairo")
    val favWeather2 = FavWeatherEntity(1.03, 2.03, "cairo", "1.032.03cairo")
    val favWeather3 = FavWeatherEntity(1.04, 2.04, "cairo", "1.042.04cairo")
    val favWeather4 = FavWeatherEntity(1.05, 2.05, "cairo", "1.052.05cairo")

    val favWeather5 = FavWeatherEntity(1.05, 2.05, "cairo", "1.052.05cairo")

    val alert1 = AlertWeatherEntity("b242", 123, 821, 122, 333, "alarm")
    val alert2 = AlertWeatherEntity("b243", 456, 124, 524, 333, "alarm")
    val alert3 = AlertWeatherEntity("b244", 784, 544, 222, 542, "alarm")
    val alert4 = AlertWeatherEntity("b245", 123, 542, 222, 333, "alarm")

    val alert5 = AlertWeatherEntity("b245", 123, 542, 222, 333, "alarm")
    lateinit var favEntity: FavWeatherEntity

    private val localList = mutableListOf<MyResponseEntity>(homeWeather)
    private val favList =
        mutableListOf<FavWeatherEntity>(favWeather1, favWeather2, favWeather3, favWeather4)
    private val alertList = mutableListOf<AlertWeatherEntity>(alert1, alert2, alert3, alert4)

    lateinit var fakeRepository: FakeRepository
    lateinit var favouriteViewModel: FavouriteViewModel

    @Before
    fun setUp() {
        fakeRepository = FakeRepository(remoteResponse, localList, alertList, favList)
        favouriteViewModel = FavouriteViewModel(fakeRepository)
    }


    @Test
    fun getFavWeatherFromRoom_ReturnFlowableFavEntityList() = runBlockingTest {

        var list = listOf<FavWeatherEntity>()
        favouriteViewModel.getFavWeatherFromRoom()
        favouriteViewModel.weather.test {
            this.awaitItem().apply {
                when (this) {
                    is LocalFavState.Success -> {
                        list = this.favWeather
                    }
                    else -> {}
                }
                assertEquals(favList.size, list.size)
                assertEquals(favList.toList().first(), list[0])
                assertEquals(list[0].city, favWeather1.city)
            }

        }
    }

    @Test
    fun deleteFavWeatherFromRoom() = runBlockingTest {
        var list = listOf<FavWeatherEntity>()
        favouriteViewModel.deleteFavWeatherFromRoom(favWeather4)

        favouriteViewModel.getFavWeatherFromRoom()
        favouriteViewModel.weather.test {
            this.awaitItem().apply {
                when (this) {
                    is LocalFavState.Success -> {
                        list = this.favWeather
                    }
                    else -> {}
                }
            }
            assertEquals(3, list.size)

        }
    }

    @Test
    fun insertFavWeatherIntoRoom() = runBlockingTest {
        var list = listOf<FavWeatherEntity>()
        favouriteViewModel.insertFavWeatherIntoRoom(favWeather5)

        favouriteViewModel.getFavWeatherFromRoom()
        favouriteViewModel.weather.test {
            this.awaitItem().apply {
                when (this) {
                    is LocalFavState.Success -> {
                        list = this.favWeather
                    }
                    else -> {}
                }
            }
            assertEquals(5, list.size)

        }
    }

}

