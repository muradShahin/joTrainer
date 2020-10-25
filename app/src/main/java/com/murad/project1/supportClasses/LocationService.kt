package com.murad.project1.supportClasses

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.model.LatLng
import com.murad.project1.activites.Index
import com.murad.project1.activites.ui.dashboard_teacher.DashboardFragment_teacher

class LocationService() :Service() {

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)

        val currentLocation = CurrentLocationNew.Builder(this)
                .setIntervalv2(5000)
                .setFastestInterval(2000) //.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setListener(object : CurrentLocationNew.EasyLocationCallback {
                    override fun onGoogleAPIClient(googleApiClient: GoogleApiClient?, message: String?) {
                    }

                    override fun onLocationUpdated(latitude: Double, longitude: Double) {
                        Log.i("EasyLocationProvider", "onLocationUpdated:: Latitude: $latitude Longitude: $longitude")


                    }

                    override fun onLocationUpdateRemoved() {

                    }

                }).build()


        val activity=Index()
        if(activity.lifecycle !=null)
          activity.lifecycle.addObserver(currentLocation)
        else
            Log.i("errorKf","error")

        this.stopSelf()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("testService","service started")
        /*
*/
        return START_STICKY
    }

    override fun onCreate() {
        Log.i("testService","service Created")

    }

    override fun onDestroy() {
        Log.i("testService","service Destroyed")

    }
}
