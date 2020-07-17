package com.meeting.tegal.ui.maps

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Display
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.lifecycle.Observer
import com.example.meeting.utilities.Constants
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager
import com.meeting.tegal.Partner
import com.meeting.tegal.R
import com.meeting.tegal.models.Order
import com.meeting.tegal.utilities.toast
import kotlinx.android.synthetic.main.activity_maps.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val mapsViewModel : MapsViewModel by viewModel()
    private lateinit var mapboxMap: MapboxMap
    private val tegal: LatLng = LatLng(-6.879704, 109.125595)
    private lateinit var markerManager : MarkerViewManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this@MapsActivity, getString(R.string.map_box_access_token))
        setContentView(R.layout.activity_maps)
        mapsViewModel.listenToState().observer(this, Observer { handleUiState(it) })
        mapView.getMapAsync(this)
    }

    private fun handleUiState(it: MapsState) {
        when(it){
            is MapsState.Loading -> {
                if (it.state){

                }else{

                }
            }
            is MapsState.ShowToast -> this@MapsActivity.toast(it.message)
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tegal, 8.5))
        mapboxMap.setStyle(Style.MAPBOX_STREETS){
            markerManager = MarkerViewManager(mapView, mapboxMap)
            mapsViewModel.listenPartners().observe(this, Observer { handlePartners(it) })
        }
    }

    private fun handlePartners(it: List<Partner>) {
        it.forEach {
            addMarker(LatLng(it.lat!!.toDouble(), it.lng!!.toDouble()), it, it.alamat!!)
        }
    }

    private fun getScreenInfo() : android.graphics.Point{
        val display: Display = this@MapsActivity.windowManager.defaultDisplay
        val size = android.graphics.Point()
        try {
            display.getRealSize(size)
        } catch (err: NoSuchMethodError) {
            display.getSize(size)
        }
        val width: Int = size.x
        val height: Int = size.y
        return size
    }

    private fun addMarker(latlng: LatLng, partner: Partner, addressName : String){
        val parent = LinearLayout(this@MapsActivity)
        parent.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        parent.orientation = LinearLayout.VERTICAL

        val size = getScreenInfo()
        val imageView = ImageView(this@MapsActivity).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT)
            setImageBitmap(BitmapFactory.decodeResource(this@MapsActivity.resources, R.drawable.mapbox_marker_icon_default))
        }

        val rel = LinearLayout(this@MapsActivity)
        rel.orientation = LinearLayout.VERTICAL
        rel.layoutParams = ViewGroup.LayoutParams(size.x/2, ViewGroup.LayoutParams.WRAP_CONTENT)
        rel.setPadding(16)
        rel.setBackgroundColor(ContextCompat.getColor(this@MapsActivity, R.color.white))

        val user = TextView(this@MapsActivity).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            maxLines = 1
            text = partner.nama_mitra
            setTextColor(ContextCompat.getColor(this@MapsActivity ,R.color.colorPrimary))
        }
        val orderId = TextView(this@MapsActivity).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            maxLines = 1
            text = addressName
        }
        rel.addView(orderId)
        rel.addView(user)
        parent.addView(rel)
        parent.addView(imageView)

        val marker = MarkerView(latlng, parent)
        markerManager.addMarker(marker)
    }


    // Add the mapView lifecycle to the activity's lifecycle methods
    override fun onResume() {
        super.onResume()
        mapsViewModel.fetchLatLngPartners(Constants.getToken(this@MapsActivity))
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
