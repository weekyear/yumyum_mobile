package com.omnyom.yumyum.ui.eureka

import android.app.Application
import android.util.Log.d
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.omnyom.yumyum.helper.KakaoMapUtils
import com.omnyom.yumyum.model.eureka.Chat
import com.omnyom.yumyum.ui.base.BaseViewModel


class EurekaViewModel(application: Application) : BaseViewModel(application) {
    val db = Firebase.firestore
    var positions : List<Double>? = null
    var geoHash : String? = null

    private val _matchingDocs = MutableLiveData<List<Chat>>().apply {
        value = arrayListOf()
    }
    val matchingDocs : LiveData<List<Chat>> = _matchingDocs


    fun getLocation() {
        positions = KakaoMapUtils.getMyPosition(getApplication())
    }

    fun getNowLocation(message: String, userId: Int) {
        getLocation()
        val lng = positions!![0]
        val lat = positions!![1]
        val geoLocation = GeoLocation(lat, lng)
        geoHash = GeoFireUtils.getGeoHashForLocation(geoLocation)
        val messageHashMap = HashMap<String, Any?>()
        messageHashMap["userId"] = userId
        messageHashMap["lat"] = lat
        messageHashMap["lng"] = lng
        messageHashMap["message"] = message
        messageHashMap["geohash"] = geoHash

        val locationRef: DocumentReference = db.collection("Chats").document("$userId")
        locationRef.set(messageHashMap).addOnCompleteListener {
            getGeoHashes()
        }
    }

    fun getGeoHashes() {
        val center = GeoLocation(positions!![1], positions!![0])
        val radiusInM = 5000.toDouble()

        val bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM)
        val tasks: MutableList<Task<QuerySnapshot>> = ArrayList()
        for (b in bounds) {
            val q: Query = db.collection("Chats")
                .orderBy("geohash")
                .startAt(b.startHash)
                .endAt(b.endHash)
            tasks.add(q.get())
        }

        Tasks.whenAllComplete(tasks)
            .addOnCompleteListener {
                val matchingDocs: MutableList<Chat> = ArrayList()
                for (task in tasks) {
                    val snap = task.result
                    for (doc in snap!!.documents) {
                        val lat = doc.getDouble("lat")!!
                        val lng = doc.getDouble("lng")!!
                        val docLocation = GeoLocation(lat, lng)
                        val distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center)
                        if (distanceInM <= radiusInM) {
                            val inner = doc.getData() as Map<String, Any>
                            val chat = Chat(inner["userId"].toString().toInt(), inner["geohash"] as String, inner["lat"] as Double, inner["lng"] as Double, inner["message"] as String)
                            matchingDocs.add(chat)
                        }
                    }
                }
                _matchingDocs.postValue(matchingDocs)
            }

    }

}
