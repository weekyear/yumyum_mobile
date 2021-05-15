package com.omnyom.yumyum.helper.recycler

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log.d
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ListItemFoodBinding
import com.omnyom.yumyum.helper.PreferencesManager
import com.omnyom.yumyum.helper.PreferencesManager.Companion.userId
import com.omnyom.yumyum.helper.RetrofitManager
import com.omnyom.yumyum.model.feed.CreateFeedResponse
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.model.like.LikeRequest
import com.omnyom.yumyum.model.like.LikeResponse
import com.omnyom.yumyum.ui.base.BaseRecyclerAdapter
import com.omnyom.yumyum.ui.base.BaseViewHolder
import com.omnyom.yumyum.ui.feed.FeedCreateActivity
import com.omnyom.yumyum.ui.userfeed.UserFeedActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FlipFeedAdapter(val context: Context) : BaseRecyclerAdapter<FlipFeedAdapter.Holder, FeedData>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : Holder {
        val itemBinding = ListItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return Holder(itemBinding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // 좋아요!
        fun likeFeed() {
            var Call = RetrofitManager.retrofitService.feedLike(LikeRequest(items[position].id.toLong(), userId).get())
            Call.enqueue(object : Callback<LikeResponse> {
                override fun onResponse(call: Call<LikeResponse>, response: Response<LikeResponse>) {
                    if (response.isSuccessful) {
                    }
                }

                override fun onFailure(call: Call<LikeResponse>, t: Throwable) {
                    t
                }

            })
        }

        // 안좋아요!
        fun unlikeFeed() {
            var Call = RetrofitManager.retrofitService.cancelFeedLike(items[position].id.toLong(), userId)
            Call.enqueue(object : Callback<LikeResponse> {
                override fun onResponse(call: Call<LikeResponse>, response: Response<LikeResponse>) {
                    if (response.isSuccessful) {
                    }
                }
                override fun onFailure(call: Call<LikeResponse>, t: Throwable) {
                    t
                }

            })
        }

        // 유저 피드로 이동
        fun goUserFeed() {
            val intent = Intent(context, UserFeedActivity::class.java)
            val authorId = items[position].user.id.toString()
            intent.putExtra("authorId", authorId)
            context?.startActivity(intent)
        }

        // 피드 수정
        fun goEditFeed() {
            val intent = Intent(context, FeedCreateActivity::class.java)
            val nowFeedData : FeedData = items[position]
            intent.putExtra("feedData", nowFeedData)
            context?.startActivity(intent)
        }

        // 피드 삭제
        fun deleteFeed(id: Long) {
            RetrofitManager.retrofitService.deleteFeed(id).enqueue(object : Callback<CreateFeedResponse> {
                override fun onResponse(call: Call<CreateFeedResponse>, response: Response<CreateFeedResponse>) {
                    d("delete", "$response")
                }
                override fun onFailure(call: Call<CreateFeedResponse>, t: Throwable) {
                    t
                }
            })
        }

        // 삭제 경고
        fun deleteAlert() {
            val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.Theme_AppCompat_Light_Dialog))
            builder.setTitle("피드 삭제")
            builder.setMessage("정말 삭제하시겠습니까?")

            builder.setPositiveButton("삭제") { _, _ ->
                deleteFeed(items[position].id.toString().toLong())
            }
            builder.setNegativeButton("취소") { _, _ ->
            }
            builder.show()
        }


        // 긴 텍스트 열기
        if (holder.expendable.lineCount == 1) {
            holder.btnExpend.visibility = View.GONE
        }


        // 기본값 입력
        if (items[position].place == null) {
            holder.placeName.text = "장소를 추가해주세요"
        } else {
            holder.placeName.text = items[position].place!!.name + " | " + items[position].place!!.address
        }

//        holder.food.setVideoURI(ProxyFactory.feedVideoPathList[position])
        holder.food.setVideoURI(items[position].videoPath.toUri())
        if (items[position].title == "") {
            holder.foodName.text = "음식명를 입력해주세요"
        } else {
            holder.foodName.text = items[position].title
        }

        if (items[position].content == "") {
            holder.detail.text = "내용을 입력해주세요"
        } else {
            holder.detail.text = items[position].content
        }
        holder.userName.text = "@" + items[position].user.nickname
        holder.likeNum.text = items[position].likeCount.toString()

        // 다양한 버튼
        holder.userName.setOnClickListener{ goUserFeed() }
//        holder.btnEdit.setOnClickListener { goEditFeed() }
//        holder.btnDelete.setOnClickListener { deleteAlert() }

        // 좋아요 버튼 관련 모든 내용들
        holder.thumbUp.setMaxFrame(15)
        if (items[position].isLike) {
            holder.thumbUp.visibility = View.INVISIBLE
        } else {
            holder.thumbUp.visibility = View.VISIBLE
        }
        if (!items[position].isCompleted) {
            holder.thumbUp.visibility = View.GONE
            holder.likeNum.visibility = View.GONE
        }
        holder.thumbUp.setOnClickListener {
            likeFeed()
            d("nanta", "쪼아요")
            holder.likeNum.text = (items[position].likeCount + 1).toString()
            holder.thumbUp.playAnimation()
            Handler().postDelayed({
                holder.thumbUp.progress = 0.0f
                holder.thumbUp.visibility = View.INVISIBLE
            }, 800)
        }

        // 동영상 기본 설정
        holder.food.setOnPreparedListener { mp ->
            holder.food.start()
            mp.setVolume(0f,0f)
            mp!!.isLooping = true;
            holder.progressBar.visibility = View.GONE
        };



    }

    class Holder(private val innerBinding: ListItemFoodBinding) : BaseViewHolder(innerBinding.root) {
        val expendable = innerBinding.expandableText
        val btnExpend = innerBinding.expandCollapse
        val food = innerBinding.foodVideo
        val foodName = innerBinding.textName
        val placeName = innerBinding.textPlacename
        val detail = innerBinding.textDetail
        val userName = innerBinding.textUser
        val thumbUp = innerBinding.avThumbUp
        val likeNum = innerBinding.tvLikeNum
        val progressBar = innerBinding.progressBar
    }
}
