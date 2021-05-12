package com.omnyom.yumyum.helper

import android.content.Context
import com.airbnb.lottie.utils.Utils
import com.danikula.videocache.HttpProxyCacheServer

class ProxyFactory {
    companion object {
        private var sharedProxy: HttpProxyCacheServer? = null

        fun getProxy(context: Context) : HttpProxyCacheServer? {
            return if (sharedProxy == null) newProxy(context) else sharedProxy
        }

        private fun newProxy(context: Context): HttpProxyCacheServer? {
            sharedProxy = HttpProxyCacheServer.Builder(context)
                .cacheDirectory(getVideoCacheDir(context))
                .maxCacheFilesCount(40)
                .build()
            return sharedProxy
        }

        var feedVideoPathList : MutableList<String> = arrayListOf()
    }
}