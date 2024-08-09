package com.example.testinggooglemaps

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.CameraPosition

class MapViewModel : ViewModel(){
    val cameraPositionLiveData = MutableLiveData<CameraPosition>()
}