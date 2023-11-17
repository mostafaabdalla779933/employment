package com.employment.employment.common.firebase.data

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class UserModel(
    var email: String? = "",
    var password: String? = "",
    var name: String? = "",
    var firstName: String? = "",
    var lastName: String? = "",
    var userId: String? = "",
    var uri: Uri? = null,
    var profileUrl: String? = "",
    var mobile: String? = "",
    var userType: String? = "",
    var listOfBranches: MutableList<BranchModel> = mutableListOf(),
    var address: String? = "",
    var about: String? = "",
    var location: SelectedLocation? = null,
    var companyWebsite: String? = ""
) : Parcelable {

    fun getFullName() = "$firstName $lastName"
}


enum class UserType(val value: String) {
    User("User"), Company("Company")
}

val listOfQualifications = listOf(
    "Less than high school",
    "high school",
    "bachelor's",
    "master's",
    "doctorate"
)

val listOfJobs = listOf(
    "IT Programmer/Developer",
    "Network Administrator",
    "PC Support",
    "Database Administrator",
    "HR",
    "Data Entry",
    "Accountant",
    "Arabic/English Translator"
)

val listOfResidencyTypes = listOf(
    "Transferable",
    "Tourist Visa",
    "Non-Transferable"
)


val listOfAges = listOf(
    "20 - 25",
    "26 - 30",
    "31 - 40",
    "41 - 50",
    "More than 50"
)

val listOfWorkTime = listOf(
    "Full time",
    "Part time"
)

val listOfExperience = listOf(
    "Fresh",
    "1-3 years"
)


val listOfNationality = listOf(
    "Kuwait",
    "saudi arabia",
    "Egyptian",
    "India"
)


enum class UserState(val value: String) {
    Pending("Pending"), Accepted("Accepted"), Rejected("Rejected")
}

@Parcelize
data class JobModel(
    val hashed:String?="",
    var name: String? = "",
    var location: String? = "",
    var desc: String? = "",
    var conditions: String? = "",
    var requirement: String? = "",
    var salary: String? = "",
    var workingFrom: String? = "",
    var workingTo: String? = "",
    var vacations: String? = "",
    var nationality: String? = "",
    var age: String? = "",
    var experience: String? = "",
    var jobType: String? = "",
    var company: UserModel? = null
) : Parcelable

@Parcelize
data class BranchModel(
    var name: String? = "",
    var mobile: String? = ""
) : Parcelable


@Parcelize
data class NotificationModel(
    val message: String? = null,
    val hash: String? = null,
    val date: String? = null,
    val fromId: String? = null,
    var from: UserModel? = null,
    var store: UserModel? = null,
    val toUserId: String? = null,
    val type: String? = null,
) : Parcelable


@Parcelize
data class SelectedLocation(
    var lat: Double? = null,
    var long: Double? = null,
    var address: String? = null
) : Parcelable



