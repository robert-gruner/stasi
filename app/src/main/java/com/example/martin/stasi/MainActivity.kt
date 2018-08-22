package com.example.martin.stasi

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    var adapter: Adapter = Adapter(this@MainActivity)
    lateinit var pusher: Pusher
    val MY_PERMISSIONS_REQUEST_LOCATION = 100
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setSupportActionBar(toolbar)

        setupPusher()
        fab.setOnClickListener { view ->
            if (checkLocationPermission())
                sendLocation()
        }
        with(recyclerView){
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location!=null){
                        Log.e("TAG","location is not null")
                        val jsonObject = JSONObject()
                        jsonObject.put("latitude",location.latitude)
                        jsonObject.put("longitude",location.longitude)
                        jsonObject.put("username",intent.extras.getString("username"))

                        val body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
                        Log.e("TAG",jsonObject.toString())
                        LocationClient().getClient().sendLocation(body).enqueue(object: Callback<String> {
                            override fun onResponse(call: Call<String>, response: Response<String>) {}

                            override fun onFailure(call: Call<String>?, t: Throwable) {
                                Log.e("TAG",t.message)
                            }

                        })

                    } else {
                        Log.e("TAG","location is null")
                    }
                }

    }

    private fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            askForPermission()
            return false
        } else {
            return true
        }
    }
    private fun askForPermission():Unit {
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

            AlertDialog.Builder(this)
                    .setTitle("Location permission")
                    .setMessage("You need the location permission for some things to work")
                    .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->

                        ActivityCompat.requestPermissions(this@MainActivity,
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                MY_PERMISSIONS_REQUEST_LOCATION)
                    })
                    .create()
                    .show()

        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        sendLocation()
                    }
                } else {
                    // permission denied!
                }
                return
            }
        }
    }
    private fun setupPusher() {
        val options = PusherOptions()
        options.setCluster("eu")
        pusher = Pusher("602f4fbd93a329552c58", options)
        val channel = pusher.subscribe("feed")
        channel.bind("location") { _, _, data ->
            val jsonObject = JSONObject(data)
            Log.d("TAG",jsonObject.toString())
            val lat:Double = jsonObject.getString("latitude").toDouble()
            val lon:Double = jsonObject.getString("longitude").toDouble()
            val name:String = jsonObject.getString("username").toString()
            runOnUiThread {
                val model = UserPosition(lat,lon,name)
                adapter.addItem(model)
            }
        }
    }
    override fun onStart() {
        super.onStart()
        pusher.connect()
    }

    override fun onStop() {
        super.onStop()
        pusher.disconnect()
    }
}