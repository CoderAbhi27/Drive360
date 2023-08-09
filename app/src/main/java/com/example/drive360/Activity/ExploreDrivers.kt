package com.example.drive360.Activity

//import com.example.sagarmiles.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationRequest
import android.os.Bundle
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.model.Marker
import com.example.drive360.Adapters.DriverAdapter
import com.example.drive360.DataClass.Driver
import com.example.sagarmiles.R
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.database.*

class ExploreDrivers : AppCompatActivity() , ItemClickListener/*, OnMapReadyCallback,
    LocationListener, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener*/ {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DriverAdapter
    private var dataList : ArrayList<Driver> = ArrayList<Driver>()
    private lateinit var dbref : DatabaseReference

  /*  private lateinit var mMap: GoogleMap
    private lateinit var mLastLocation: Location
    private lateinit var mCurrLocationMarker: Marker
    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var mLocationRequest: LocationRequest*/

  /*  private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: Location? = null*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore_drivers)

        val driverType = intent.getStringExtra("DriverType")

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.setHasFixedSize(true)

//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        val filterLocationSwitch: Switch = findViewById(R.id.filterSwitch)

        filterLocationSwitch.setOnCheckedChangeListener { p0, p1 ->
            if (p1) {
                // The toggle is enabled
            } else {
                // The toggle is disabled
            }
        }

        dbref = FirebaseDatabase.getInstance().getReference("drivers").child(driverType!!)
        dbref.addValueEventListener( object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                if(snapshot.exists()){
                    for(dataSnap in snapshot.children){
                        if(dataSnap.key=="lastCode") continue
                        val data = dataSnap.getValue(Driver::class.java)
                        dataList.add(data!!)
                    }

                    adapter = DriverAdapter(dataList, this@ExploreDrivers)
                    recyclerView.adapter = adapter

                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ExploreDrivers, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })


    }

    override fun onItemClick(position: Int) {
        // Handle item click event here
        val intent = Intent(this, DriverProfileActivity::class.java)
        val driver = dataList[position]

        intent.putExtra("code",driver.code)
        intent.putExtra("name",driver.name)
        intent.putExtra("image",driver.image)
        intent.putExtra("phoneNo",driver.phoneNo)
        intent.putExtra("age",driver.age)
        intent.putExtra("address", driver.address)
        intent.putExtra("experience",driver.experience)
        intent.putExtra("salaryDemanded",driver.salaryDemanded)
        intent.putExtra("hiringFee",driver.hiringFee)
        intent.putExtra("certificate",driver.certificate)
        startActivity(intent)
    }

 /*   private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                requestcode
            )
            false
        } else {
            true
        }
    }*/


/*    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION)
                == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient()
                mMap.isMyLocationEnabled = true
            }
        } else {
            buildGoogleApiClient()
            mMap.isMyLocationEnabled = true
        }
    }

    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient.connect()
    }

    override fun onLocationChanged(p0: Location) {
        mLastLocation = p0
        if (::mCurrLocationMarker.isInitialized) {
            mCurrLocationMarker.remove()
        }
        val latLng = LatLng(p0.latitude, p0.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("Current Position")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        mCurrLocationMarker = mMap.addMarker(markerOptions)

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11))

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
        }
    }

    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 1000
        mLocationRequest.fastestInterval = 1000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this
            )
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("Not yet implemented")
    }*/

/*    // This is a placeholder function to simulate fetching data from a database
    private fun getDriverData(drivertype: String){

        dbref = FirebaseDatabase.getInstance().getReference(drivertype)
        dbref.addValueEventListener( object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                if(snapshot.exists()){
                    for(dataSnap in snapshot.children){
                        val data = dataSnap.getValue(Driver::class.java)
                        dataList.add(data!!)
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ExploreDrivers, error.toString(), Toast.LENGTH_LONG).show()
            }
        })
    }*/
}

interface ItemClickListener {
    fun onItemClick(position: Int)
}