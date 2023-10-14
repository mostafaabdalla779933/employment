package com.employment.employment.feature.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.common.firebase.data.SelectedLocation
import com.employment.employment.databinding.FragmentSelectLocationBinding
import com.google.android.gms.location.*
import com.employment.employment.R
import com.employment.employment.common.getAddress
import com.employment.employment.common.getLastLocation
import com.employment.employment.common.isLocationEnabled
import com.employment.employment.common.showMessage
import com.google.android.datatransport.BuildConfig.APPLICATION_ID
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import java.util.*



class SelectLocationFragment : BaseFragment<FragmentSelectLocationBinding>(),
    OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var selectedMarker: Marker
    private lateinit var selectedPointOfInterest: PointOfInterest
    private lateinit var selectedLocation: SelectedLocation

    override fun initBinding() = FragmentSelectLocationBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.apply {
            btnSave.setOnClickListener {
                onLocationSelected()
            }
        }
    }

    private fun onLocationSelected() {
        if (this::selectedLocation.isInitialized) {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                "location",
                selectedLocation
            )
            findNavController().popBackStack()
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        setOnPoiClick(map)
        setOnMapClick(map)

        getLastLocation(getLocation = { location ->
            getAddress(
                lat = location.latitude ,
                long = location.longitude) { address ->
                binding.tvLocationSelected.text = address
                selectedLocation = SelectedLocation(lat = location.latitude, long = location.longitude, address = address)
                setUserLocationOnMap(location)
                //map.isMyLocationEnabled = true
            }
        }, enableLocation = {
            startForLocation.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }, requestPermission = {
            startPermission()
        })
    }

    private fun getAddress(lat:Double, long:Double,location:(String)->Unit){
        Geocoder(requireContext(), Locale("in"))
            .getAddress(lat,long) { address: android.location.Address? ->
                if (address != null) {
                    location(address.getAddressLine(0))
                }else{
                    hideLoading()
                    showErrorMsg("no internet")
                }
            }
    }

    private fun setOnMapClick(map: GoogleMap){
        map.setOnMapClickListener { latLng ->
            map.clear()

            val snippet = String.format(
                Locale.getDefault(),
                getString(R.string.lat_long_snippet),
                latLng.latitude,
                latLng.longitude
            )

            showLoading()
            getAddress(
                lat = latLng.latitude ,
                long = latLng.longitude) { address ->
                hideLoading()
                binding.tvLocationSelected.text = address
                selectedLocation = SelectedLocation(lat = latLng.latitude,long = latLng.longitude, address = address)
                selectedPointOfInterest = PointOfInterest(latLng, snippet, snippet)

                map.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .snippet(snippet)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                )?.let {
                    selectedMarker = it
                }
                val zoom = 15f
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
                selectedMarker.showInfoWindow()
            }
        }
    }

    private fun setOnMapLongClick(map: GoogleMap){
        map.setOnMapLongClickListener { latLng ->
            map.clear()

            val snippet = String.format(
                Locale.getDefault(),
                getString(R.string.lat_long_snippet),
                latLng.latitude,
                latLng.longitude
            )

            selectedPointOfInterest = PointOfInterest(latLng, snippet, snippet)

            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            )?.let {
                selectedMarker = it
            }
            selectedMarker.showInfoWindow()
        }
    }

    private fun setOnPoiClick(map: GoogleMap){
        map.setOnPoiClickListener { poi ->
            map.clear()

            map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
            )?.let {
                selectedMarker = it
            }

            selectedPointOfInterest = poi
            selectedMarker.showInfoWindow()
        }
    }



    private val startForLocation =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (isLocationEnabled()) {
                getLastLocation(getLocation = { location ->
                    getAddress(
                        lat = location.latitude ,
                        long = location.longitude ) { address ->
                        binding.tvLocationSelected.text = address
                        selectedLocation = SelectedLocation(lat = location.latitude, long = location.longitude, address = address)
                        setUserLocationOnMap(location)
                    }
                },requestPermission = {
                    startPermission()
                })
            }else{
                requireContext().showMessage("didn't enable location")
                findNavController().popBackStack()
            }
        }


    @SuppressLint("MissingPermission")
    private fun setUserLocationOnMap(location: Location) {
        val snippet = activity?.let { it1 ->
            String.format(
                Locale.getDefault(),
                it1.getString(R.string.lat_long_snippet),
                location.latitude,
                location.longitude
            )
        }
        val latLng = LatLng(location.latitude, location.longitude)
        snippet?.let {
            selectedPointOfInterest = PointOfInterest(latLng, it, "Current Location")
        }

        map.addMarker(
            MarkerOptions()
                .position(latLng)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        )?.let {
            selectedMarker = it
        }
        val zoom = 15f
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
        selectedMarker.showInfoWindow()

    }



    private fun showLocationPermissionSnackBar(view: View){
        Snackbar.make(
            view,
            R.string.permission_denied_explanation, Snackbar.LENGTH_INDEFINITE
        )
            .setAction(R.string.settings) {
                startActivity(Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", APPLICATION_ID, null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                })
            }.show()
    }


    private fun startPermission(){
        requestMultiplePermissions.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var accept = false
            permissions.entries.forEach {
                accept = it.value
            }
            if (accept) {
                getLastLocation(getLocation = { location ->
                    getAddress(
                        lat = location.latitude ,
                        long = location.longitude ) { address ->
                        binding.tvLocationSelected.text = address
                        selectedLocation = SelectedLocation(lat = location.latitude, long = location.longitude, address = address)
                        setUserLocationOnMap(location)
                    }
                }, enableLocation = {
                    startForLocation.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                })
            } else {
                requireContext().showMessage("permission denied")
                findNavController().popBackStack()
            }
        }


}