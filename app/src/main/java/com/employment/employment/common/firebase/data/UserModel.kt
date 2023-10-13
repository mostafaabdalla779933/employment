package com.employment.employment.common.firebase.data

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class UserModel(
    var email:String? = "",
    var password:String? ="",
    var name:String? ="",
    var userId :String? ="",
    var uri: Uri?=null,
    var profileUrl:String? ="",
    var mobile: String? = "",
    var userType:String?="",
    var specialization:String?="",
    var birthdate:String?="",
    var isMale:Boolean?= true
) : Parcelable


enum class UserType(val value:String){
    User("User"),Company("Company")
}

enum class UserState(val value:String){
    Pending("Pending"),Accepted("Accepted"),Rejected("Rejected")
}


@Parcelize
data class ProductModel(
    var name:String?=null,
    var imageUrl:String?=null,
    var code:String?=null,
    var desc:String?=null,
    var amount:Int?=null,
    var uri:Uri?=null,
    var hash:String?=null,
    var percentage:Double?=null,
    var listOfCategories:MutableList<CategoryModel> = mutableListOf()
) : Parcelable{
    fun getPriceKWD():String= "$percentage%"
}

@Parcelize
data class CategoryModel(
    var name:String?=null,
    var url:String?=null,
    var hash:Long?=null,
    var uri:Uri?=null,
) : Parcelable{

    var selected = false
}


@Parcelize
data class BranchModel(
    var name:String?=null,
    var district:String?="",
    var plot:String?="",
    var street:String?="",
    var building:String?="",
    var floor :String?="",
    var lat:Double?=null,
    var long:Double?=null,
    var hash:String?=null
) : Parcelable{
    fun getAddress(): String {
        return if (floor.isNullOrEmpty()) {
            "City:$district, Block:$plot, St:$street, Building:$building"
        } else {
            "City:$district, Block:$plot, St:$street, Building:$building, Floor:$floor"
        }
    }
}

@Parcelize
data class ProductTakenModel(
    var product: ProductModel?=null,
    var store: UserModel?=null,
    var amount: Int?=null,
    var price:Double?=null,
    var branch: BranchModel?=null,
    var hash: String?=null
) : Parcelable{
    fun getPriceKWD():String= "$price KWD"

}


@Parcelize
data class NotificationModel(
    val message :String?=null,
    val hash:String?=null,
    val date:String?=null,
    val fromId:String?=null,
    var from: UserModel?=null,
    var store: UserModel? = null,
    var product: ProductTakenModel? = null,
    val toUserId:String?=null,
    val type:String?=null,
    val order: OrderModel? = null
) : Parcelable

@Parcelize
data class ProductsToStores(
    var store: UserModel?=null,
    var listOfProducts:MutableList<ProductTakenModel>?= mutableListOf()
) : Parcelable

@Parcelize
data class OrderModel(
    val productTakenModel: ProductTakenModel?= null,
    val client: UserModel?=null,
    val hash:String?=null,
    val date:String?=null,
    val long:Long?=null,
    val amount: Int?=null,
    var state:Boolean?=false,
    val totalAmount:Double?= null
) : Parcelable{
    var isShow:Boolean = false
}


@Parcelize
data class SelectedLocation(
    var lat:Double?=null,
    var long:Double?=null,
    var address:String?=null
) : Parcelable



