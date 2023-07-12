package com.example.weatheapp.home.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.weatheapp.database.*
import com.example.weatheapp.models.*
import com.example.weatheapp.network.ApiState
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class HomeViewModelTest {

    val weather = listOf<Weather>(Weather(1, "", "", ""))
    val current = Current(1, 2, 3, 4.0, 5.0, 6.0, 7, 8.0, 9.0, 10, 0, 11.0, 12, weather)
    val minutelyWeather = listOf<MinutelyWeather>(MinutelyWeather(1, 2.0))
    val hourlyWeather = listOf<Hourly>(Hourly(1, 2.0, 3.0, weather))
    val dailyWeather =
        listOf<Daily>(Daily(1, Temperature(2.0, 3.0, 4.0, 5.0, 6.0, 7.0), 2.0, weather))
    val alerts = listOf<Alert>(Alert("", "", 1, 2, "", listOf("", "")))
    var remoteResponse = MyResponse(
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
    val homeWeather2 = MyResponseEntity(
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

    val alert1 = AlertWeatherEntity("b242", 123, 821, 122, 333, "alarm")
    val alert2 = AlertWeatherEntity("b243", 456, 124, 524, 333, "alarm")
    val alert3 = AlertWeatherEntity("b244", 784, 544, 222, 542, "alarm")
    val alert4 = AlertWeatherEntity("b245", 123, 542, 222, 333, "alarm")

    lateinit var remoteResponse2: MyResponse
    lateinit var localResponse: MyResponseEntity

    private val localList = mutableListOf<MyResponseEntity>(homeWeather)
    private val favList =
        mutableListOf<FavWeatherEntity>(favWeather1, favWeather2, favWeather3, favWeather4)
    private val alertList = mutableListOf<AlertWeatherEntity>(alert1, alert2, alert3, alert4)

    lateinit var fakeRepository: FakeRepository
    lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        fakeRepository = FakeRepository(remoteResponse, localList, alertList, favList)
        homeViewModel = HomeViewModel(fakeRepository)
    }


    @Test
    fun getWeatherOverNetwork_ReturnFlowableOfRemoteResponse() = runBlockingTest {
        homeViewModel.getWeatherOverNetwork(2.0,22.3,"","")
        homeViewModel.weather.test {
            this.awaitItem().apply {
                when (this) {
                    is ApiState.Success -> {
                        remoteResponse2 = this.weather
                    }
                    else -> {}
                }
                assertEquals(remoteResponse.current, remoteResponse2.current)
                assertEquals(remoteResponse, remoteResponse2)
            }
        }
    }

    @Test
    fun getWeatherFromRoom_ReturnFlowableOfMyResposeEntity() = runBlockingTest {
        homeViewModel.getWeatherFromRoom("home","1")
        homeViewModel.homeWeather.test {
            this.awaitItem().apply {
                when (this) {
                    is RoomState.Success -> {
                        localResponse = this.weather
                    }
                    else -> {}
                }
                assertEquals(homeWeather.current, localResponse.current)
                assertEquals(homeWeather, localResponse)
            }
        }
    }


    @Test
    fun insertWeatherIntoRoom() = runBlockingTest {

        homeViewModel.insertWeatherIntoRoom(homeWeather)
        homeViewModel.getWeatherFromRoom("home","1")
        homeViewModel.homeWeather.test {
            this.awaitItem().apply {
                when (this) {
                    is RoomState.Success -> {
                        localResponse = this.weather
                    }
                    else -> {}
                }
                assertEquals(localList.last().current, localResponse.current)
            }
        }

    }

}

