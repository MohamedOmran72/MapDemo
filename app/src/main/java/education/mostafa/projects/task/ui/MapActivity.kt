package education.mostafa.projects.task.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnFailureListener
import education.mostafa.projects.task.R
import education.mostafa.projects.task.helper.Constants
import education.mostafa.projects.task.helper.Constants.MY_PERMISSION_ACCESS_COARSE_LOCATION
import education.mostafa.projects.task.views.MapsView

class MapActivity : AppCompatActivity(), MapsView, View.OnClickListener {

    lateinit var mFusedLocationClient: FusedLocationProviderClient
    var mGoogleMap: GoogleMap? = null
    lateinit var locationRequest: LocationRequest
    lateinit var marker1: Marker
    lateinit var marker2: Marker
    lateinit var polyline: Polyline
    var polyLines = ArrayList<Polyline>()
    var OneAdded: Boolean = false
    var TwoAdded: Boolean = false
    var PolylineAdded: Boolean = false
    var doubleBackToExitPressedOnce: Boolean = false
    lateinit var latLngBoundsBuilder: LatLngBounds.Builder
    lateinit var latLngBounds : LatLngBounds
    val padding:Int = 10
    var clickMap: Int = 1
    lateinit var cu:CameraUpdate


    lateinit var map: MapView
    lateinit var go_txt: TextView
    lateinit var twoScreenTxt:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission()
        }
        initView()
        initClient()
        setUpMap(savedInstanceState)
        initMap()
        createLocationRequest()
        getLastLocation()
    }

    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) { //Can add more as per requirement
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                123
            )
        }
    }

    fun createLocationRequest() {
        locationRequest = LocationRequest.create()?.apply {
            interval = 1000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }!!
    }



    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                MY_PERMISSION_ACCESS_COARSE_LOCATION
            )
        } else {
            mFusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    val lat = location!!.latitude
                    val long = location.longitude
                    val currentUserLatLang = LatLng(lat, long)
                    mGoogleMap!!.clear()
                    mGoogleMap!!.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            currentUserLatLang,
                            15f
                        )
                    )
                }.addOnFailureListener(object : OnFailureListener {
                    override fun onFailure(p0: java.lang.Exception) {
                        Toast.makeText(this@MapActivity, p0.message, Toast.LENGTH_LONG).show()
                    }

                })

        }


    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == Constants.PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                getLastLocation()

            }
        }
    }

    override fun initView() {
        map = findViewById(R.id.map)
        go_txt = findViewById(R.id.go_txt)
        go_txt.setOnClickListener(this)
        twoScreenTxt = findViewById(R.id.twoScreenTxt)
        twoScreenTxt.setOnClickListener(this)
    }

    override fun initObjects() {

    }

    override fun initClient() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun setUpMap(bundle: Bundle?) {
        map.onCreate(bundle)
        map.onResume()
    }

    override fun initMap() {
        try {
            MapsInitializer.initialize(this.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        map.getMapAsync(OnMapReadyCallback { mMap ->
            mGoogleMap = mMap
            mGoogleMap!!.isBuildingsEnabled = true
            mGoogleMap!!.isTrafficEnabled = true
            mGoogleMap!!.uiSettings.isZoomGesturesEnabled = false
            mGoogleMap!!.uiSettings.isZoomControlsEnabled = false
            mGoogleMap!!.uiSettings.isRotateGesturesEnabled = false
            mGoogleMap!!.uiSettings.isMyLocationButtonEnabled = true
            mGoogleMap!!.uiSettings.isCompassEnabled = true
            mGoogleMap!!.uiSettings.isMapToolbarEnabled = false
            mGoogleMap!!.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
                override fun onMapClick(p0: LatLng?) {
                    if (clickMap == 1) {
                        if (p0 != null) {
                            val markerOptions = MarkerOptions().position(p0).title("marker1")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            marker1 = mGoogleMap!!.addMarker(markerOptions)
                            OneAdded = true
                        }
                        mGoogleMap!!.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                p0,
                                15f
                            )
                        )
                        clickMap = 2
                    } else if (clickMap == 2) {
                        if (p0 != null) {
                            val markerOptions = MarkerOptions().position(p0).title("marker2")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            marker2 = mGoogleMap!!.addMarker(markerOptions)
                            TwoAdded = true
                        }
                        mGoogleMap!!.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                p0,
                                15f
                            )
                        )
                        clickMap = 3
                    } else if (clickMap == 3) {
                        if (p0 != null) {
                            polyline.remove()
                            PolylineAdded = false
                            val markerOptions = MarkerOptions().position(p0)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            marker1.remove()
                            marker1 = mGoogleMap!!.addMarker(markerOptions)
                        }
                        mGoogleMap!!.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                p0,
                                15f
                            )
                        )
                    }
                    Toast.makeText(this@MapActivity, "latLong = $p0", Toast.LENGTH_SHORT).show()
                }

            })

        })
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            this.finishAffinity()
            return
        } else {
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this , getString(R.string.exitW) , Toast.LENGTH_SHORT).show()
            Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false } , 2000)
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.go_txt -> {
                if(OneAdded.equals(false) || TwoAdded.equals(false)){
                    Toast.makeText(this , R.string.markersWarn , Toast.LENGTH_SHORT).show()
                }else{
                    if(!PolylineAdded){
                        polyline = mGoogleMap!!.addPolyline(
                            PolylineOptions().add(marker1.position).add(marker2.position).color(R.color.colorRed)
                        )
                        latLngBoundsBuilder = LatLngBounds.builder()
                        latLngBoundsBuilder.include(marker1.position)
                        latLngBoundsBuilder.include(marker2.position)

                        latLngBounds = latLngBoundsBuilder.build()
                        cu = CameraUpdateFactory.newLatLngBounds(latLngBounds , padding)
                        mGoogleMap!!.animateCamera(cu)
                        PolylineAdded = true
                    }
                }
            }
            R.id.twoScreenTxt -> {
                startActivity(Intent(this , NetworkActivity::class.java))
            }
        }
    }


}
