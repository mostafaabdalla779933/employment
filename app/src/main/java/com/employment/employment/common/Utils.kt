package com.employment.employment.common

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Matrix
import android.location.Location
import android.location.LocationManager
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.material.textfield.TextInputEditText
import java.util.regex.Pattern
import java.math.RoundingMode
import java.text.DecimalFormat
import android.location.Geocoder
import android.os.Looper
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.*


fun TextInputEditText.getString(): String {
    return this.text.toString().trim()
}


fun EditText.getString(): String {
    return this.text.toString().trim()
}


fun EditText.getInt(): Int = try {
    this.text.toString().toInt()
} catch (e: Exception) {
   0
}

fun String.getIntFromString(): Int = try {
    this.toInt()
} catch (e: Exception) {
    0
}

fun EditText.getDouble(): Double = try {
    this.text.toString().toDouble()
} catch (e: Exception) {
    0.0
}

fun String.getDouble(): Double = try {
    this.toDouble()
} catch (e: Exception) {
    0.0
}


fun TextInputEditText.isStringEmpty(): Boolean {
    return this.text.toString().trim().isEmpty()
}

fun EditText.isStringEmpty(): Boolean {
    return this.text.toString().trim().isEmpty()
}


fun isValidEmail(email: String): Boolean {
    return Pattern.compile(
        "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
    ).matcher(email).matches()
}





fun Uri.getPath(context: Context): String {
    var result: String? = null
    val proj = arrayOf(MediaStore.Images.Media.DATA)
    val cursor: Cursor = context.contentResolver.query(
        this, proj, null, null, null)!!
    if (cursor.moveToFirst()) {
        val column_index: Int = cursor.getColumnIndexOrThrow(proj[0])
        result = cursor.getString(column_index)
    }
    cursor.close()
    if (result == null) {
        result = "Not found"
    }
    return result
}


fun getRotated(path: String):Float{
    return when(ExifInterface(path).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)){
        ExifInterface.ORIENTATION_ROTATE_90 -> 90F
        ExifInterface.ORIENTATION_ROTATE_180 -> 180F
        ExifInterface.ORIENTATION_ROTATE_270 ->  270F
        ExifInterface.ORIENTATION_NORMAL-> 0F
        else ->0F
    }
}


fun ImageView.setImageFromUri(uri: Uri, context: Context){
    uri.getPath(context).let{ path ->
        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            .let { bitmap ->
                this.setImageBitmap(bitmap.rotateImage(getRotated(path)))
            }
    }
}


fun Bitmap.rotateImage(angle: Float): Bitmap? {
    Matrix().let { matrix ->
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            this, 0, 0, this.width,this.height,
            matrix, true
        )
    }
}

fun Double.roundOffDecimal(): Double {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.FLOOR
    return df.format(this).toDouble()
}

fun Context.showMessage(message:String){
    Toast.makeText(this,message, Toast.LENGTH_LONG).show()
}

fun getLocation(lat:Double, long:Double,context:Context,location:(String)->Unit){
    Geocoder(context, Locale("in"))
        .getAddress(lat,long) { address: android.location.Address? ->
            if (address != null) {
                location("${address.countryName?:""} ${address.adminArea}" )
            }else{
                context.showMessage("something went wrong")
            }
        }
}


fun Geocoder.getAddress(
    latitude: Double,
    longitude: Double,
    address: (android.location.Address?) -> Unit
) {
    try {
        address(getFromLocation(latitude, longitude, 1)?.firstOrNull())
    } catch(e: Exception) {
        address(null)
    }
}


fun Fragment.requestPermissionToLocation(code:Int) {

    ActivityCompat.requestPermissions(
        requireActivity(), arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ), code
    )
}


fun Fragment.checkPermissions(): Boolean {
    if (ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        return true
    }
    return false
}

fun Fragment.isLocationEnabled(): Boolean {
    val locationManager: LocationManager =
        requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
        LocationManager.NETWORK_PROVIDER
    )
}

@SuppressLint("MissingPermission")
fun Fragment.requestNewLocationData(getLocation: (Location) -> Unit) {  //get fresh location
    val locationRequest = LocationRequest()
    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    locationRequest.interval = 0
    locationRequest.fastestInterval = 0
    locationRequest.numUpdates = 1
    val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
    fusedLocationProviderClient.requestLocationUpdates(
        locationRequest,
        this.locationCallBack(getLocation),
        Looper.myLooper()!!
    )
}

fun Fragment.locationCallBack(getLocation: (Location) -> Unit): LocationCallback {
    val locationCallBack: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            val location = p0.lastLocation
            if (location != null) {
                getLocation(location)
            }
        }
    }
    return  locationCallBack
}

@SuppressLint("MissingPermission")
fun Fragment.getLastLocation(getLocation: (Location) -> Unit,enableLocation:()->Unit ={},requestPermission:()->Unit ={}) {

    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
    if (checkPermissions()) {
        if (isLocationEnabled()) {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener(requireActivity()) { location ->
                    if (location == null) {
                        requestNewLocationData(getLocation) //get fresh location
                    } else {
                        getLocation(location)
                    }
                }
        } else {
            enableLocation()
        }
    } else {
        requestPermission()
    }
}


fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}

fun getDistance( startLatitude:Double?,startLongitude:Double?, endLatitude:Double?,endLongitude:Double?) :Float{
    if(startLongitude == null || startLatitude == null || endLatitude  == null || endLongitude == null){
        return 0F
    }
    val startLocation = Location("a")
    startLocation.latitude = startLatitude
    startLocation.longitude = startLongitude
    val endLocation = Location("b")
    endLocation.latitude = endLatitude
    endLocation.longitude = endLongitude
    return startLocation.distanceTo(endLocation)
}



