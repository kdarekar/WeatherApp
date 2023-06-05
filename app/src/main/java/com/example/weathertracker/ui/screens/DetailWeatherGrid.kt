package com.example.weathertracker.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.weathertracker.R
import com.example.weathertracker.domain.models.CurrentWeather
import com.example.weathertracker.util.toDirection
import com.example.weathertracker.util.toHumidity
import com.example.weathertracker.util.toPressure
import com.example.weathertracker.util.toSpeed
import com.example.weathertracker.util.toTemperature
import com.example.weathertracker.util.toVisibility

class DetailWeatherGrid {
}

@Composable
fun DetailWeatherGrid(
    weather: CurrentWeather
) {
    Column(
        modifier = Modifier
            .padding(0.dp, 15.dp, 0.dp, 0.dp)
            .width(IntrinsicSize.Max)
    ) {
        Row {
            DetailItem(painterResource(R.drawable.ic_feels), "Feels like", weather.main?.feels_like!!.toTemperature())
            VerticalDivider()
            DetailItem(painterResource(R.drawable.ic_wind), "Wind Speed", weather.wind?.speed!!.toSpeed())
            VerticalDivider()
            DetailItem(painterResource(R.drawable.ic_direction), "Wind direction", weather.wind?.deg!!.toDirection())
        }
        HorizontalDivider()
        Row {
            DetailItem(painterResource(R.drawable.ic_humidity), "Humidity", weather.main?.humidity!!.toHumidity())
            VerticalDivider()
            DetailItem(painterResource(R.drawable.ic_pressure), "Pressure", weather.main?.pressure!!.toPressure())
            VerticalDivider()
            DetailItem(painterResource(R.drawable.ic_visibility), "Visibility", weather.visibility!!.toVisibility())
        }
    }
}

@Composable
fun DetailItem(
    painter: Painter,
    title: String,
    value: String
) {
    ConstraintLayout(
        modifier = Modifier
            .background(color = Color.White)
            .padding(5.dp, 10.dp, 5.dp, 0.dp)
    ) {
        val (detailIcon, detailTitle, detailValue) = createRefs()
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .constrainAs(detailIcon) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
                .size(20.dp, 20.dp)
                .padding(end = 4.dp)
        )
        Text(
            text = title,
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            ),
            modifier = Modifier
                .constrainAs(detailTitle) {
                    start.linkTo(detailIcon.end)
                    top.linkTo(parent.top)
                }

                .padding(top =3.dp)
        )
        Text(
            text = value,
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            ),
            modifier = Modifier
                .constrainAs(detailValue) {
                    start.linkTo(detailIcon.end)
                    top.linkTo(detailTitle.bottom)
                }
        )
    }
}

@Composable
fun VerticalDivider() {
    Spacer(modifier = Modifier
        .width(1.dp)
        .height(57.dp)
        .padding(2.dp)
        .background(color = Color.Gray))
}

@Composable
fun HorizontalDivider() {
    Spacer(modifier = Modifier
        .height(1.dp)
        .fillMaxWidth())
}