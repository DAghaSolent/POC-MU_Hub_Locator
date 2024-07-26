package com.example.testinggooglemaps

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testinggooglemaps.ui.theme.TestingGoogleMapsTheme
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestingGoogleMapsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MapComposable()
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun MapComposable(){
    val defaultCameraPosition = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(LatLng(50.9161, -1.3649), 12f)
    }

    val mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.TERRAIN))
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = defaultCameraPosition,
        properties = mapProperties
    ){
        MarkerComposable(
            state = MarkerState(position = LatLng(50.9161, -1.3649)),
        ){
            Image(
                painterResource(id = R.drawable.utilita),
                contentDescription = null,
                Modifier.size(36.dp)
            )
        }

        MarkerComposable(
            state = MarkerState(position = LatLng(50.92139183397814, -1.4320641306790338)),
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ){

                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .background(Color.Red, shape = RoundedCornerShape(4.dp))
                ){
                    Text(
                        text = "Utilita Energy Hub Shirley",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(2.dp)
                    )
                }

                Image(
                    painterResource(id = R.drawable.utilita),
                    contentDescription = null,
                    Modifier.size(36.dp)
                )
            }
        }

    }
}
