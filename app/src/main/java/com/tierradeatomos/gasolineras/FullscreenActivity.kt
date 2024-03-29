package com.tierradeatomos.gasolineras

import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform


class FullscreenActivity : AppCompatActivity() {
    private lateinit var consentInformation: ConsentInformation
    private var mywebview: WebView? = null
    lateinit var mAdView : AdView

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.hide()

        consentInformation();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //MobileAds.initialize(this, getString(R.string.banner_app_id))
        MobileAds.initialize(
            this
        ) { }


        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mywebview = findViewById<WebView>(R.id.webview)
        mywebview?.addJavascriptInterface(this.JavaScriptInterface(), "Android")
        mywebview?.webViewClient = MyWebViewClient(this)
        val webSettings = mywebview!!.settings
        webSettings.javaScriptEnabled = true

        this.checkPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION)

        fusedLocationClient?.lastLocation?.
            addOnSuccessListener(this,
                {location : Location? ->
                    // Got last known location. In some rare
                    // situations this can be null.
                    if(location == null) {
                        mywebview!!.loadUrl("http://gasolineras.tierradeatomos.com/map")
                    } else location.apply {
                        mywebview!!.loadUrl("http://gasolineras.tierradeatomos.com/map?lat=" + location.latitude + "&lng=" + location.longitude)
                    }
                })

        this.hideSystemUI()
    }

    private fun hideSystemUI() {
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
    }

    private inner class JavaScriptInterface {
        @JavascriptInterface
        fun getLatLng() : String {
            var latlng : String = "";
            fusedLocationClient?.lastLocation?.
                addOnSuccessListener(parent,
                    {location : Location? ->
                        if(location == null) {
                        } else location.apply {
                            mywebview!!.evaluateJavascript("setLocation("+location.latitude+","+location.longitude+")",
                                ValueCallback<String> { s ->
                                })
                            latlng = "" + location.latitude + "," + location.longitude;
                        }
                    })
            return latlng;
        }

        @JavascriptInterface
        fun showToast(toast: String) {
            Toast.makeText(this@FullscreenActivity, toast, Toast.LENGTH_SHORT).show()
        }
    }

    private val PERMISSION_ID = 42
    private fun checkPermission(vararg perm:String) : Boolean {
        val havePermissions = perm.toList().all {
            ContextCompat.checkSelfPermission(this,it) ==
                    PackageManager.PERMISSION_GRANTED
        }
        if (!havePermissions) {
            if(perm.toList().any {
                    ActivityCompat.
                        shouldShowRequestPermissionRationale(this, it)}
            ) {
                val dialog = AlertDialog.Builder(this)
                    .setTitle("Permission")
                    .setMessage("Permission needed!")
                    .setPositiveButton("OK", {id, v ->
                        ActivityCompat.requestPermissions(
                            this, perm, PERMISSION_ID)
                    })
                    .setNegativeButton("No", {id, v -> })
                    .create()
                dialog.show()
            } else {
                ActivityCompat.requestPermissions(this, perm, PERMISSION_ID)
            }
            return false
        }
        return true
    }

    private fun consentInformation()
    {
        // Set tag for under age of consent. false means users are not under age
        // of consent.
        val params = ConsentRequestParameters
            .Builder()
            .setTagForUnderAgeOfConsent(false)
            .build()

        consentInformation = UserMessagingPlatform.getConsentInformation(this)
        consentInformation.requestConsentInfoUpdate(
            this,
            params,
            ConsentInformation.OnConsentInfoUpdateSuccessListener {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                    this@FullscreenActivity,
                    ConsentForm.OnConsentFormDismissedListener {
                            loadAndShowError ->
                        // Consent gathering failed.
                        Log.w(
                            "FullscreenActivity",
                            loadAndShowError.toString()
                        )

                        // Consent has been gathered.
                    }
                )
            },
            {
            })

    }
}
