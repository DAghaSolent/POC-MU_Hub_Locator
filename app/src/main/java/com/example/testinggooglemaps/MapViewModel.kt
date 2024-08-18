package com.example.testinggooglemaps

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.CameraPosition

data class UtilitaPOI(
    var lat: Double, var lon: Double, var description: String, var address: String,
    var city_town: String, var postcode: String, var phone_number: String,
    var emailAddress: String, var openingTimes: String, var distanceFromLocation : Double? = null
    )

class MapViewModel : ViewModel(){
    val userGPSLocationCameraPositionLiveData = MutableLiveData<CameraPosition>()
    val postcodeCameraPositionLiveData = MutableLiveData<CameraPosition>()

    val listUtilitaPOI = MutableLiveData<List<UtilitaPOI>>()

    init {
        listUtilitaPOI.value = listOf(

            UtilitaPOI(50.92093950723424, -1.4319340450913751, "Shirley Energy Hub"
            , "7A Shirley High St Shirley", "Southampton", "SO15 3LR",
                "+44 23 8077 3563", "southampton@utilita.co.uk",
                "Mon - Sat 9 am-5:30 pm\n Sun Closed", null),

            UtilitaPOI(50.795097495513346, -1.1190049720984165, "Gosport Energy Hub"
            , "67 High St", "Gosport", "PO12 1DR",
                "+44 23 9252 6215", "gosport@utilita.co.uk",
                "Mon - Sat 9:00 am-5:30 pm \n Sun Closed", null),

            UtilitaPOI(50.70010393206105, -1.294763730385918, "Isle of Wight Energy Hub"
            , "121 High St Newport", "Isle of Wight", "PO30 1TP",
                "+44 1983 219709", "iow@utilita.co.uk",
                "Mon - Sat 9:00 am-5:30 pm \n Sun Closed", null),

            UtilitaPOI(51.87931774400996, -0.41281400150775666, "Luton Energy Hub",
                "Unit 47 The Mall Luton", "Bedfordshire", "LU1 2TD",
                "01582283522", "luton@utilita.co.uk",
                "Mon - Sat 9:00 am-6:00 pm \n Sun Closed",null),

            UtilitaPOI(52.51994669999933, -1.996025999986881, "West Brom Energy Hub"
            , "3D Astle Park", "West Bromwich", "B70 8NS",
                "+44 121 803 6776", "westbrom@utilita.co.uk",
                "Mon - Sat 9:00 am-5:30 pm \n Sun Closed",null),

            UtilitaPOI(52.63582158611183, -1.1298075705522772, "Leicester Energy Hub"
            , "56 Charles St", "Leicester", "LE1 1FB",
                "+44 116 478 4903", "leicester@utilita.co.uk",
                "Mon - Sat 9:00 am-5:30 pm \n Sun Closed",null),

            UtilitaPOI(52.920091772168796, -1.4751015014704316, "Derby Energy Hub",
                "Unit 8 St Peters St Mall", "Derby", "DE1 2NR",
                "+44 1332 957954", "derby@utilita.co.uk",
                "Mon - Sat 9:00 am-5:30 pm \n Sun Closed",null),

            UtilitaPOI(53.382725134508654, -1.4699754513393264, "Sheffield Energy Hub"
            , "Unit 2 St James Row, Sheffield City Centre", "Sheffield", "S1 2EU"
            , "+44 114 551 9925", "sheffield@utilita.co.uk",
                "Mon - Sat 9:00 am-5:30 pm \n Sun Closed",null),

            UtilitaPOI(53.64607191512829, -1.7828796013130694, "Huddersfield Hub",
                "71 New St", "Huddersfield", "HD1 2BQ", "",
                "huddersfield@utilita.co.uk",
                "Mon - Sat 9:00 am-5:30 pm \n Sun Closed",null),

            UtilitaPOI(53.74908977240895, -2.483491901440301, "Blackburn Energy Hub"
            , "Unit 158 The Mall, 28 King William St", "Blackburn", "BB1 5AF"
            , "01254946460", "blackburn@utilita.co.uk",
                "Mon - Sat 8:30 am-5:00 pm \n Sun Closed",null),

            UtilitaPOI(54.68435937270752, -1.216988601405927, "Hartlepool Hub",
                "Unit 1 Jubilee House, York Rd", "Hartlepool", "TS26 9EN",
                "+44 1429 800717", "hartlepool@utilita.co.uk",
                "Mon - Sat 8:30 am-5:00 pm \n Sun Closed",null),

            UtilitaPOI(55.97103998567562, -3.171519370150762, "Leith Energy Hub",
                "41 Newkirkgate Leith", "Edinburgh", "EH6 6AA",
                "+44 131 378 2992", "edinburgh@utilita.co.uk",
                "Mon - Sat 8:30 am-5:00 pm \n Sun Closed",null)

        )
    }
}