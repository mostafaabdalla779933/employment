package com.employment.employment.common.firebase.data

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class UserModel(
    var email:String? = "",
    var password:String? ="",
    var name:String? ="",
    var firstName:String?="",
    var lastName:String?="",
    var userId :String? ="",
    var uri: Uri?=null,
    var profileUrl:String? ="",
    var mobile: String? = "",
    var userType:String?="",
    var listOfBranches:MutableList<BranchModel> = mutableListOf(),
    var address: String?="",
    var about:String?="",
    var location:SelectedLocation?= null,
    var companyWebsite:String?=""
) : Parcelable


enum class UserType(val value:String){
    User("User"),Company("Company")
}

enum class UserState(val value:String){
    Pending("Pending"),Accepted("Accepted"),Rejected("Rejected")
}


@Parcelize
data class BranchModel(
    var name:String?="",
    var mobile:String?=""
) : Parcelable


@Parcelize
data class NotificationModel(
    val message :String?=null,
    val hash:String?=null,
    val date:String?=null,
    val fromId:String?=null,
    var from: UserModel?=null,
    var store: UserModel? = null,
    val toUserId:String?=null,
    val type:String?=null,
) : Parcelable






@Parcelize
data class SelectedLocation(
    var lat:Double?=null,
    var long:Double?=null,
    var address:String?=null
) : Parcelable



