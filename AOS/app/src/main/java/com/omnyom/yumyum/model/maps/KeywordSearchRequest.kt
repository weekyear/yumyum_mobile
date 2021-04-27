package com.omnyom.yumyum.model.maps


// 일단은 안쓰는 중
class KeywordSearchRequest (private val query: String, private val x : Double, private val y: Double, private val page: Int, private val size : Int) {
    fun get() : HashMap<String, Any> {
        var keywordSearchInfo = HashMap<String, Any>()
        keywordSearchInfo.put("query", query)
        keywordSearchInfo.put("x", x)
        keywordSearchInfo.put("y", y)
        keywordSearchInfo.put("page", page)
        keywordSearchInfo.put("size", size)
        return keywordSearchInfo
    }
}
