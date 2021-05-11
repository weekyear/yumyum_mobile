package com.omnyom.yumyum.helper

import android.content.Context
import com.danikula.videocache.HttpProxyCacheServer

class ProxyFactory {
    companion object {
        private lateinit var sharedProxy: HttpProxyCacheServer

        fun getProxy(context: Context) : HttpProxyCacheServer {
            return if (sharedProxy == null) newProxy(context) else sharedProxy
        }

        private fun newProxy(context: Context): HttpProxyCacheServer {
            return HttpProxyCacheServer.Builder(context)
                .maxCacheFilesCount(20)
                .build()
        }
    }
}