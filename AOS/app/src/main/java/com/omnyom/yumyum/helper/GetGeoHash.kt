package com.omnyom.yumyum.helper

import android.content.Context
import android.util.Log.d
import android.widget.Toast

class GetGeoHash(context: Context?) {
    val context = context
    val base32 : String = "0123456789bcdefghjkmnpqrstuvwxyz"
    val way = mutableListOf("n", "s", "e", "w")

    fun adjacent(geohash : String, direction : String) {
        var geohash = geohash.toLowerCase()
        var direction = direction.toLowerCase()

        val neighbor = mutableMapOf<String, Array<String>>()
        neighbor.put("n", arrayOf("p0r21436x8zb9dcf5h7kjnmqesgutwvy", "bc01fg45238967deuvhjyznpkmstqrwx"))
        neighbor.put("s", arrayOf("14365h7k9dcfesgujnmqp0r2twvyx8zb", "238967debc01fg45kmstqrwxuvhjyznp"))
        neighbor.put("e", arrayOf("bc01fg45238967deuvhjyznpkmstqrwx", "p0r21436x8zb9dcf5h7kjnmqesgutwvy"))
        neighbor.put("w", arrayOf("238967debc01fg45kmstqrwxuvhjyznp", "14365h7k9dcfesgujnmqp0r2twvyx8zb"))


        val border = mutableMapOf<String, Array<String>>()
        border.put("n", arrayOf("prxz", "bcfguvyz"))
        border.put("s", arrayOf("028b", "0145hjnp"))
        border.put("e", arrayOf("bcfguvyz", "prxz"))
        border.put("w", arrayOf("0145hjnp", "028b"))

        if (geohash.length == 0) {
            Toast.makeText(context, "Invalid geohash", Toast.LENGTH_SHORT).show()
        }
        if (way.indexOf(direction) == -1) {
            Toast.makeText(context, "Invalid direction", Toast.LENGTH_SHORT).show()
        }

        val lastCh = geohash[geohash.lastIndex]
        val parent = geohash.substring(0, geohash.length-1)
        val type = geohash.length % 2
        val border_direction = border[direction]!!

        d("charcheck", "${border_direction[type]}")


    }
}

