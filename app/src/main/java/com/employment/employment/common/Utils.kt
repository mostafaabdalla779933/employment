package com.employment.employment.common

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Matrix
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.media.ExifInterface
import android.net.Uri
import android.os.Looper
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.employment.employment.common.firebase.data.listOfAges
import com.employment.employment.common.firebase.data.listOfExperience
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.textfield.TextInputEditText
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


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


fun calculateDateDifferenceToLong(date1: Date?, date2: Date?): Long {
    val calendar1 = Calendar.getInstance()
    val calendar2 = Calendar.getInstance()
    calendar1.time = date1
    calendar2.time = date2

    return calendar2.timeInMillis - calendar1.timeInMillis
}


fun Long.toYearsAndMonths():String{
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    val mYear: Int = calendar.get(Calendar.YEAR) - 1970
    val mMonth: Int = calendar.get(Calendar.MONTH)

    return when{
        mYear == 0 && mMonth == 0 -> ""
        mYear == 0 -> "$mMonth Month"
        mMonth == 0 -> "$mYear Years"
        else -> "$mYear Years $mMonth Month"
    }
}

fun calculateDateDifference(date1: Date?, date2: Date?): String {
    val calendar1 = Calendar.getInstance()
    val calendar2 = Calendar.getInstance()
    val calendar3 = Calendar.getInstance()

    calendar1.time = date1
    calendar2.time = date2

    val diff = calendar2.timeInMillis - calendar1.timeInMillis

    calendar3.setTimeInMillis(diff)
    val mYear: Int = calendar3.get(Calendar.YEAR) - 1970
    val mMonth: Int = calendar3.get(Calendar.MONTH)

    return when{
        mYear == 0 && mMonth == 0 -> ""
        mYear == 0 -> "$mMonth Month"
        mMonth == 0 -> "$mYear Years"
        else -> "$mYear Years $mMonth Month"
    }
}


fun String.getMonth(): String {
    val inputFormat = SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.US)
    val outputFormat = SimpleDateFormat("MMM", Locale.US)
    return outputFormat.format(inputFormat.parse(this))
}


fun String.getMonthAndYear(): String {
    val inputFormat = SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.US)
    val outputFormat = SimpleDateFormat("MMM yyyy", Locale.US)
    return outputFormat.format(inputFormat.parse(this))
}

fun String.getDayMonthAndYear(): String {
    return try {
        val inputFormat = SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.US)
        val outputFormat = SimpleDateFormat("d MMM yyyy", Locale.US)
        outputFormat.format(inputFormat.parse(this))
    }catch (e:Exception){
        ""
    }

}

fun String.calculateAge(): Int {
    val currentDate = Calendar.getInstance().time
    val birthDate = SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.US).parse(this)

    val currentCalendar = Calendar.getInstance()
    currentCalendar.time = currentDate

    val birthCalendar = Calendar.getInstance()
    birthCalendar.time = birthDate

    var age = currentCalendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)

    // Check if the birthday has occurred this year
    if (currentCalendar.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
        age--
    }

    return age
}


fun getAgeRange(age:Int):String{
    return when{
        age <= 25 -> listOfAges[0]
        age in 26..30 -> listOfAges[1]
        age in 31..40 -> listOfAges[2]
        age in 41..50 -> listOfAges[3]
        else -> listOfAges[4]
    }
}

fun Long.getExperienceRange(): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    val mYear: Int = calendar.get(Calendar.YEAR) - 197

    return when {
        mYear < 1-> listOfExperience[0]
        mYear in 1..3 -> listOfExperience[1]
        mYear in 4..5 -> listOfExperience[2]
        else -> listOfExperience[3]
    }
}


