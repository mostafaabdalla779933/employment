package com.employment.employment.common.firebase

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import com.employment.employment.common.firebase.data.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


object FirebaseHelp {

    val auth: FirebaseAuth = Firebase.auth
    val fireStore = Firebase.firestore

    var user: UserModel? =  null
    const val USERS = "all_users"

    const val PRODUCT_TAKEN = "PRODUCT_TAKEN"

    const val PRODUCTS  = "all_products"
    const val Categories  = "Categories"
    const val NOTIFICATION = "NOTIFICATION"

    const val ORDER = "ORDER"

    fun getUserID() = auth.currentUser?.uid ?: ""

    fun uploadImageToCloudStorage(context: Context, imageFileUri: Uri, imageType: String,onSuccess :(String)->Unit,onFailure :(Exception)-> Unit){

        val sRef : StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() + "." + getFileExtension(context, imageFileUri) )

        sRef.putFile(imageFileUri)
            .addOnSuccessListener { taskSnapshot ->
                Log.e("Firebase Image URL", taskSnapshot.metadata!!.reference!!.downloadUrl.toString())
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { url ->
                        Log.e("Downloadable Image URL", url.toString())
                        onSuccess(url.toString())
                    }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    private fun getFileExtension(context: Context, uri: Uri?) : String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(context.contentResolver.getType(uri!!))
    }


    fun getUser(onSuccess: (UserModel)->Unit, onFailure: (String) -> Unit){
        fireStore.collection(USERS).document(auth.currentUser?.uid?:"").get().
        addOnSuccessListener { document ->
            document.toObject(UserModel::class.java)?.let{
                onSuccess(it)
            } ?: kotlin.run {
                onFailure( "something wrong")
            }
        }.addOnFailureListener { e ->
            onFailure(e.localizedMessage ?: "something wrong")
        }
    }


//    fun addProduct(productModel: ProductModel, onSuccess: ()->Unit, onFailure: (String) -> Unit){
//        fireStore.collection(PRODUCTS)
//            .document().set(productModel, SetOptions.merge())
//            .addOnSuccessListener {
//                onSuccess()
//            }
//            .addOnFailureListener { e ->
//                onFailure(e.localizedMessage ?: "something wrong")
//            }
//    }
//
//    fun deleteProduct(productModel: ProductModel, onSuccess: ()->Unit, onFailure: (String) -> Unit) {
//        fireStore.collection(PRODUCTS).whereEqualTo("hashing", productModel.hashing).get()
//            .addOnSuccessListener { documents ->
//
//                val document =documents.firstOrNull()
//                deleteProductWithDocument(document?.id?: "",{
//                    onSuccess()
//                },{
//                    onFailure(it)
//                })
//
//            }.addOnFailureListener { e ->
//            onFailure(e.localizedMessage ?: "something wrong")
//        }
//    }
//
//    fun deleteProductWithDocument(id:String,onSuccess: ()->Unit,onFailure: (String) -> Unit){
//        fireStore.collection(PRODUCTS).document(id).delete()
//            .addOnSuccessListener {
//                onSuccess()
//            }.addOnFailureListener { e ->
//                onFailure(e.localizedMessage ?: "something wrong")
//            }
//    }
//
//    fun addCard(productModel: PaymentCard, onSuccess: ()->Unit, onFailure: (String) -> Unit){
//        fireStore.collection(CARDS)
//            .document().set(productModel, SetOptions.merge())
//            .addOnSuccessListener {
//                onSuccess()
//            }
//            .addOnFailureListener { e ->
//                onFailure(e.localizedMessage ?: "something wrong")
//            }
//    }
//
//    fun addOrder(orderModel: OrderModel, onSuccess: ()->Unit, onFailure: (String) -> Unit){
//        fireStore.collection(ORDERS)
//            .document().set(orderModel, SetOptions.merge())
//            .addOnSuccessListener {
//                onSuccess()
//            }
//            .addOnFailureListener { e ->
//                onFailure(e.localizedMessage ?: "something wrong")
//            }
//    }
//
//    fun getSpecificOrders(key:String, value:String, onSuccess: (MutableList<OrderModel>, String)->Unit, onFailure: (String) -> Unit){
//        fireStore.collection(ORDERS).whereEqualTo(key,value).get().
//        addOnSuccessListener { documents ->
//            val list = mutableListOf<OrderModel>()
//            documents.forEach { document->
//                list.add( document.toObject(OrderModel::class.java))
//            }
//            onSuccess(list, getUserID())
//        }.addOnFailureListener { e ->
//            onFailure(e.localizedMessage ?: "something wrong")
//        }
//    }
//
//
//    fun getAllOrders(onSuccess: (MutableList<OrderModel>, String)->Unit, onFailure: (String) -> Unit){
//        fireStore.collection(ORDERS).get().
//        addOnSuccessListener { documents ->
//            val list = mutableListOf<OrderModel>()
//            documents.forEach { document->
//                list.add( document.toObject(OrderModel::class.java))
//            }
//            onSuccess(list, getUserID())
//        }.addOnFailureListener { e ->
//            onFailure(e.localizedMessage ?: "something wrong")
//        }
//    }
//
//
//    fun getAllProducts(onSuccess: (MutableList<ProductModel>, String)->Unit, onFailure: (String) -> Unit){
//        fireStore.collection(PRODUCTS).get().
//        addOnSuccessListener { documents ->
//            val list = mutableListOf<ProductModel>()
//            documents.forEach { document->
//               list.add( document.toObject(ProductModel::class.java))
//            }
//            onSuccess(list, getUserID())
//        }.addOnFailureListener { e ->
//            onFailure(e.localizedMessage ?: "something wrong")
//        }
//    }
//
//    fun getSpecificProducts(key:String, value:Any, onSuccess: (MutableList<ProductModel>, String)->Unit, onFailure: (String) -> Unit){
//        fireStore.collection(PRODUCTS).whereEqualTo(key,value).get().
//        addOnSuccessListener { documents ->
//            val list = mutableListOf<ProductModel>()
//            documents.forEach { document->
//                list.add( document.toObject(ProductModel::class.java))
//            }
//            onSuccess(list, getUserID())
//        }.addOnFailureListener { e ->
//            onFailure(e.localizedMessage ?: "something wrong")
//        }
//    }
//
//
//    fun getAllCards(onSuccess: (MutableList<PaymentCard>, String)->Unit, onFailure: (String) -> Unit){
//        fireStore.collection(CARDS).get().
//        addOnSuccessListener { documents ->
//            val list = mutableListOf<PaymentCard>()
//            documents.forEach { document->
//                list.add( document.toObject(PaymentCard::class.java))
//            }
//            onSuccess(list, getUserID())
//        }.addOnFailureListener { e ->
//            onFailure(e.localizedMessage ?: "something wrong")
//        }
//    }
//
//    fun getAllUsers(onSuccess: (MutableList<UserModel>, String)->Unit, onFailure: (String) -> Unit){
//        fireStore.collection(USERS).get().
//        addOnSuccessListener { documents ->
//            val list = mutableListOf<UserModel>()
//            documents.forEach { document->
//                list.add( document.toObject(UserModel::class.java))
//            }
//            onSuccess(list, getUserID())
//        }.addOnFailureListener { e ->
//            onFailure(e.localizedMessage ?: "something wrong")
//        }
//    }
//
//
//    fun getSpecificUsers(key:String, value:String, onSuccess: (MutableList<UserModel>, String)->Unit, onFailure: (String) -> Unit){
//        fireStore.collection(USERS).whereEqualTo(key,value).get().
//        addOnSuccessListener { documents ->
//            val list = mutableListOf<UserModel>()
//            documents.forEach { document->
//                list.add( document.toObject(UserModel::class.java))
//            }
//            onSuccess(list, getUserID())
//        }.addOnFailureListener { e ->
//            onFailure(e.localizedMessage ?: "something wrong")
//        }
//    }
//
//
//    fun updateUser(userModel: UserModel, hashMap: HashMap<String,Any>, onSuccess: ()->Unit, onFailure: (String) -> Unit){
//        fireStore.collection(USERS).document(userModel.userId ?: "").update(hashMap)
//            .addOnSuccessListener {
//                onSuccess()
//        }.addOnFailureListener { e ->
//            onFailure(e.localizedMessage ?: "something wrong")
//        }
//    }
//
//
//
//
//    fun updateProduct(productModel: ProductModel, stock:String, onSuccess: ()->Unit, onFailure: (String) -> Unit) {
//
//        fireStore.collection(PRODUCTS).whereEqualTo("hashing", productModel.hashing).get()
//            .addOnSuccessListener { documents ->
//                val document =documents.firstOrNull()
//                updateProductDocument(document?.id ?:"",stock,{
//                    onSuccess()
//                },{
//                    onFailure(it)
//                })
//            }.addOnFailureListener { e ->
//                onFailure(e.localizedMessage ?: "something wrong")
//            }
//    }

    fun updateProductDocument(id:String,stock:String,onSuccess: ()->Unit,onFailure: (String) -> Unit) {
        val hashMap = hashMapOf<String,Any>("stock" to stock)
        fireStore.collection(PRODUCTS).document(id).update(hashMap)
            .addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener { e ->
                onFailure(e.localizedMessage ?: "something wrong")
            }
    }


    fun logout(){
        auth.signOut()
    }


    inline fun <reified T> addObject(
        objectToSet:Any,
        collection: String,
        document: String,
        crossinline onSuccess: () -> Unit,
        crossinline onFailure: (String) -> Unit
    ) {
        fireStore.collection(collection)
            .document(document).set(objectToSet, SetOptions.merge())
            .addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener { e ->
                onFailure(e.localizedMessage ?: "something wrong")
            }

    }


    inline fun <reified T> getObject(
        collection: String,
        document: String,
        crossinline onSuccess: (T) -> Unit,
        crossinline onFailure: (String) -> Unit
    ) {
        fireStore.collection(collection).document(document).get().addOnSuccessListener { document ->
            document.toObject(T::class.java)?.let {
                onSuccess(it)
            } ?: kotlin.run {
                onFailure("something wrong")
            }
        }.addOnFailureListener { e ->
            onFailure(e.localizedMessage ?: "something wrong")
        }
    }


    inline fun <reified T> getAllObjects(
        collection: String,
        crossinline onSuccess: (MutableList<T>) -> Unit,
        crossinline onFailure: (String) -> Unit
    ) {
        fireStore.collection(collection).get().addOnSuccessListener { documents ->
            val list = mutableListOf<T>()
            documents.forEach { document ->
                list.add(document.toObject(T::class.java))
            }
            onSuccess(list)
        }.addOnFailureListener { e ->
            onFailure(e.localizedMessage ?: "something wrong")
        }
    }

    inline fun <reified T> deleteObject(
        collection: String, document: String,
        crossinline onSuccess: () -> Unit,
        crossinline onFailure: (String) -> Unit
    ) {
        fireStore.collection(collection).document(document).delete()
            .addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener { e ->
                onFailure(e.localizedMessage ?: "something wrong")
            }
    }

    fun deleteImageFromCloudStorage(imageUrl: String,onSuccess :()->Unit, onFailure :(Exception)-> Unit){
        val photoRef: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)

        photoRef.delete().addOnSuccessListener { // File deleted successfully
            onSuccess()
        }.addOnFailureListener { // Uh-oh, an error occurred!
            onFailure(it)
        }
    }

}