package com.example.weathertracker.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.example.weathertracker.BuildConfig.OPEN_WEATHER_ICON_4X
import com.example.weathertracker.BuildConfig.OPEN_WEATHER_ICON_URL
import com.example.weathertracker.R
import com.example.weathertracker.domain.models.CurrentWeather
import com.example.weathertracker.network.adapter.Error
import com.example.weathertracker.network.adapter.NetworkResult
import com.example.weathertracker.ui.MainViewModel
import com.example.weathertracker.util.toDate
import com.example.weathertracker.util.toTemperature
import java.util.Locale

@ExperimentalAnimationApi
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WeatherContent(
    viewModel: MainViewModel,
    city: String
) {
    Surface(color = MaterialTheme.colors.background) {
        var cityName by rememberSaveable {
            mutableStateOf(city)
        }
        // Boolean state to hold if request was successful
        var isLoaded by rememberSaveable { mutableStateOf(false) }

        // Retry callback
        val retry: () -> Unit = { viewModel.fetchWeatherCall(cityName) }

        LaunchedEffect(Unit) {
            // If request unsuccessful, call again
            if (isLoaded.not())
                viewModel.fetchWeatherCall(cityName)
        }

        // Update UI according to network result state
        when (val result = viewModel.weatherData.collectAsState().value) {
            is NetworkResult.Loading -> {
                LoadingScreen()
            }

            is NetworkResult.Success -> {
                isLoaded = true
                cityName=result.data.name.toString()
                WeatherScreen(
                    weatherData = result.data,
                    navigateToSearch = {
                        viewModel.fetchWeatherCall(it)
                    }
                )
            }

            is NetworkResult.Error -> {
                ErrorScreen(result.data!!, retry)
            }

            is NetworkResult.Failure -> {
                FailureScreen(result.message, retry)
            }

            is NetworkResult.Empty -> {
                LoadingScreen()
            }

            else -> {}
        }

    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalAnimationApi
@Composable
fun  WeatherScreen(
    weatherData: CurrentWeather,
    navigateToSearch: (city:String) -> Unit
) {
    Scaffold(
        topBar = {
            ExpandableSearchView(onSearchDisplayChanged = {}, onSearchDisplayClosed = { /*TODO*/ }, navigateToSearch = navigateToSearch)
        }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = weatherData.name!!,
                        style = TextStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp
                        )
                    )

                    Text(
                        text = weatherData.dt!!.toDate(),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 10.sp
                        )
                    )
                    AsyncImage(
                        model = "${OPEN_WEATHER_ICON_URL}${weatherData.weather?.get(0)?.icon}${OPEN_WEATHER_ICON_4X}",
                        contentDescription = null,
                        modifier = Modifier
                            .size(150.dp, 150.dp)
                    )
                    // Temperature text
                    Text(
                        text = weatherData.main?.temp!!.toTemperature(),
                        style = TextStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp
                        )
                    )
                    // Weather description text

                    Text(
                        text = weatherData.weather?.get(0)!!.description!!.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.getDefault()
                            ) else it.toString()
                        },
                        style = TextStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                    DetailWeatherGrid(weatherData)
            }
        }

    }
}

@ExperimentalAnimationApi
@Composable
fun LoadingScreen() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {

        val (centerLayout, textTitle) = createRefs()

        // Open weather image
        Image(
            painter = painterResource(R.drawable.weather),
            contentDescription = null,
            modifier = Modifier
                .constrainAs(centerLayout) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .size(200.dp, 200.dp)
                .padding(0.dp, 0.dp, 8.dp, 0.dp)
        )

        // Open weather text
        Text(
            text = "Fetching Weather...",
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .constrainAs(textTitle) {
                    bottom.linkTo(parent.bottom)
                }
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(0.dp, 0.dp, 0.dp, 10.dp)
        )

    }

}

@Composable
fun ErrorScreen(
    error: Error,
    retry: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, bottom = 50.dp, end = 20.dp)
    ) {

        val (titleLayout, descriptionLayout, retryButton) = createRefs()

        // Ooops text
        Text(
            text = "Ooops!",
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .constrainAs(titleLayout) {
                    bottom.linkTo(descriptionLayout.top)
                    start.linkTo(parent.start)
                }
                .height(IntrinsicSize.Min)
                .padding(0.dp, 0.dp, 0.dp, 10.dp)
        )

        // Error message text
        Text(
            text = error.message,
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
            ),
            modifier = Modifier
                .constrainAs(descriptionLayout) {
                    start.linkTo(parent.start)
                    bottom.linkTo(retryButton.top)
                }
                .width(300.dp)
                .height(IntrinsicSize.Max)
                .padding(bottom = 10.dp)
        )

        // Retry button
        Button(
            onClick = {
                retry.invoke()
            },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .constrainAs(retryButton) {
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }
                .padding(end = 5.dp),
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text(text = "Retry")
        }

    }
}

@Composable
fun FailureScreen(
    message: String,
    retry: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, bottom = 50.dp, end = 20.dp)
    ) {

        val (titleLayout, descriptionLayout, retryButton) = createRefs()

        // Ooops text
        Text(
            text = "Ooops!",
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .constrainAs(titleLayout) {
                    bottom.linkTo(descriptionLayout.top)
                    start.linkTo(parent.start)
                }
                .height(IntrinsicSize.Min)
                .padding(0.dp, 0.dp, 0.dp, 10.dp)
        )

        // Error message text
        Text(
            text = message,
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
            ),
            modifier = Modifier
                .constrainAs(descriptionLayout) {
                    start.linkTo(parent.start)
                    bottom.linkTo(retryButton.top)
                }
                .width(300.dp)
                .height(IntrinsicSize.Max)
                .padding(bottom = 10.dp)
        )

        // Retry button
        Button(
            onClick = {
                retry.invoke()
            },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .constrainAs(retryButton) {
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }
                .padding(end = 5.dp),
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text(text = "Retry")
        }

    }
}