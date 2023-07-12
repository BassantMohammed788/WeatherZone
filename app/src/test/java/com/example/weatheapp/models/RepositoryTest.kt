package com.example.weatheapp.models

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatheapp.database.AlertWeatherEntity
import com.example.weatheapp.database.FakeConcreteLocalSource
import com.example.weatheapp.database.FavWeatherEntity
import com.example.weatheapp.database.MyResponseEntity
import com.example.weatheapp.network.FakeWeatherClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsEqual
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepositoryTest {
    @get: Rule()
    val rule = InstantTaskExecutorRule()
    val weather = listOf<Weather>(Weather(1, "", "", ""))
    val current = Current(1, 2, 3, 4.0, 5.0, 6.0, 7, 8.0, 9.0, 10, 0, 11.0, 12, weather)
    val minutelyWeather = listOf<MinutelyWeather>(MinutelyWeather(1, 2.0))
    val hourlyWeather = listOf<Hourly>(Hourly(1, 2.0, 3.0, weather))
    val dailyWeather = listOf<Daily>(Daily(1, Temperature(2.0, 3.0, 4.0, 5.0, 6.0, 7.0), 2.0, weather))
    val alerts = listOf<Alert>(Alert("","",1,2,"", listOf("","")))
    val remoteResponse = MyResponse(31.268104553222656, 30.019702911376953, "Africa/Cairo", 0, current, minutelyWeather, hourlyWeather, dailyWeather, alerts)
    val homeWeather=MyResponseEntity("alexandria",31.268104553222656,30.019702911376953,current, hourlyWeather,dailyWeather,alerts,"home","1")

    val favWeather1 = FavWeatherEntity(1.02,2.02,"cairo","1.022.02cairo")
    val favWeather2 = FavWeatherEntity(1.03,2.03,"cairo","1.032.03cairo")
    val favWeather3 = FavWeatherEntity(1.04,2.04,"cairo","1.042.04cairo")
    val favWeather4 = FavWeatherEntity(1.05,2.05,"cairo","1.052.05cairo")

    val favWeather5 = FavWeatherEntity(1.05,2.05,"cairo","1.052.05cairo")

    val alert1 = AlertWeatherEntity("b242",123,821,122,333,"alarm")
    val alert2 = AlertWeatherEntity("b243",456,124,524,333,"alarm")
    val alert3 = AlertWeatherEntity("b244",784,544,222,542,"alarm")
    val alert4 = AlertWeatherEntity("b245",123,542,222,333,"alarm")

    val alert5 = AlertWeatherEntity("b245",123,542,222,333,"alarm")


    private val localList = mutableListOf<MyResponseEntity>(homeWeather)
    private val favList = mutableListOf<FavWeatherEntity>(favWeather1,favWeather2,favWeather3,favWeather4)
    private val alerList = mutableListOf<AlertWeatherEntity>(alert1,alert2,alert3,alert4)

    private lateinit var remoteDataSource: FakeWeatherClient
    private lateinit var localDataSource: FakeConcreteLocalSource
    private lateinit var repository: Repository

    @Before
    fun setUp() {
        localDataSource = FakeConcreteLocalSource(localList,alerList,favList)
        remoteDataSource = FakeWeatherClient(remoteResponse)
        repository = Repository.getInstance(remoteDataSource,localDataSource)
    }
    @Test
fun getWeatherOverNetwork_returnRemoteApiResponse() = runBlockingTest {
        repository.getWeatherOverNetwork(2.0,2.3,"","").collect{
          weather->
          var myResponse = weather
            assertThat(myResponse, IsEqual(remoteResponse))
      }

    }

    @Test
    fun getSavedWeather_returnFlowableHomeResponse() = runBlockingTest {
        repository.getSavedWeather("home","1").collect{ weather->
            var myResponseEntity = weather
            assertThat(myResponseEntity, IsEqual(homeWeather))
        }
    }
    @Test
    fun insertHomeWeather() = runBlockingTest {
        repository.insertHomeWeather(homeWeather)
        repository.getSavedWeather("home","1").collect{
                weather->
            var myResponseEntity = weather
            assertThat(myResponseEntity, IsEqual(homeWeather))
        }
    }
    @Test
    fun getFavWeather_returnFlowableFavEntityList()= runBlockingTest {
        repository.getFavWeather().collect{
            fav->
            var favourite = fav.first()
            assertThat(favourite, IsEqual(favWeather1))
        }

    }

    @Test
    fun insertFavWeather()= runBlockingTest {
        repository.insertFavWeather(favWeather5)
        repository.getFavWeather().collect{
                fav->
            var favourite = fav.last()
            assertThat(favourite, IsEqual(favWeather5))
        }
    }
    @Test
    fun deleteFavWeather()= runBlockingTest {
        repository.deleteFavWeather(favWeather4)
        repository.getFavWeather().collect{
                fav->
            var favourite = fav.size
            assertThat(favourite, IsEqual(3))
        }

    }

    @Test
    fun getAlertWeather_returnFlowableAlertEntityList()= runBlockingTest {
        repository.getAlertWeather().collect{
                alert->
            var alert = alert.first()
            assertThat(alert, IsEqual(alert1))
        }
    }
    @Test
    fun insertAlertWeather()= runBlockingTest {
        repository.insertAlertWeather(alert5)
        repository.getAlertWeather().collect{
                fav->
            var favourite = fav.last()
            assertThat(favourite, IsEqual(alert5))
        }

    }
    @Test
    fun deleteAlertWeather()= runBlockingTest {
        repository.deleteAlertWeather(alert2)
        repository.getFavWeather().collect{
                alerts->
            var alert = alerts.size
            assertThat(alert, IsEqual(4))
        }
    }






}