package com.omnyom.yumyum.helper

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import com.kakao.sdk.link.KakaoLinkIntentClient
import com.kakao.sdk.link.LinkClient
import com.kakao.sdk.template.model.*
import com.omnyom.yumyum.R
import com.omnyom.yumyum.model.feed.FeedData

class KakaoLinkUtils {
    companion object {
        fun kakaoLink(activity: Activity, feedData: FeedData) {
            val templateId = activity.getString(R.string.kakao_link_template_id).toLong()

            val templateArgs = mapOf(
                "title" to feedData.title,
                "userName" to feedData.user.nickname,
                "imageUrl" to feedData.thumbnailPath,
                "profilePath" to feedData.user.profilePath,
                "content" to feedData.content,
                "likeNum" to feedData.likeCount.toString()
            )

            val serverCallbackArgs: MutableMap<String, String> = HashMap()
            serverCallbackArgs["user_id"] = "\${current_user_id}"
            serverCallbackArgs["product_id"] = "\${shared_product_id}"

            LinkClient.instance.customTemplate(activity, templateId, templateArgs) { linkResult, error ->
                if (error != null) {
                    Log.e(TAG, "카카오링크 보내기 실패", error)
                }
                else if (linkResult != null) {
                    Log.d(TAG, "카카오링크 보내기 성공 ${linkResult.intent}")

                    activity.startActivity(linkResult.intent)

                    // 카카오링크 보내기에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                    Log.w(TAG, "Warning Msg: ${linkResult.warningMsg}")
                    Log.w(TAG, "Argument Msg: ${linkResult.argumentMsg}")
                }
            }
        }
    }
}