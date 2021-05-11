package com.omnyom.yumyum.helper.recycler

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.omnyom.yumyum.databinding.ListItemFoodBinding
import com.omnyom.yumyum.helper.PreferencesManager
import com.omnyom.yumyum.helper.RetrofitManager
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.model.like.LikeRequest
import com.omnyom.yumyum.model.like.LikeResponse
import com.omnyom.yumyum.ui.base.BaseRecyclerAdapter
import com.omnyom.yumyum.ui.base.BaseViewHolder
import com.omnyom.yumyum.ui.userfeed.UserFeedActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FlipFeedAdapter(val context: Context) : BaseRecyclerAdapter<FlipFeedAdapter.Holder, FeedData>() {
    val userId = PreferencesManager.getLong(context, "userId").toString().toInt()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : Holder {
        val itemBinding = ListItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(itemBinding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // 좋아요!
        fun likeFeed() {
            var Call = RetrofitManager.retrofitService.feedLike(LikeRequest(items[position].id, userId).get())
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
            var Call = RetrofitManager.retrofitService.cancelFeedLike(items[position].id.toLong(), userId.toLong())
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

        fun goUserFeed() {
            val intent = Intent(context, UserFeedActivity::class.java)
            val authorId = items[position].user.id.toString()
            intent.putExtra("authorId", authorId)
            context?.startActivity(intent)
        }


        if (holder.expendable.lineCount == 1) {
            holder.btnExpend.visibility = View.GONE
        }

        if (items[position].place == null) {
            holder.placeName.text = "장소를 추가해주세요"
        } else {
            holder.placeName.text = items[position].place.name + " | " + items[position].place.address
        }
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
        holder.userName.setOnClickListener{
            goUserFeed()
        }

        holder.thumbUp.setMaxFrame(15)
        holder.thumbUp2.setMinFrame(15)

        // 버튼 구현
        if (items[position].isLike) {
            holder.thumbUp.visibility = View.INVISIBLE
            holder.thumbUp2.visibility = View.VISIBLE
        } else {
            holder.thumbUp2.visibility = View.INVISIBLE
            holder.thumbUp.visibility = View.VISIBLE
        }

        if (!items[position].isCompleted) {
            holder.thumbUp2.visibility = View.GONE
            holder.thumbUp.visibility = View.GONE
            holder.likeNum.visibility = View.GONE
            holder.btnEdit.visibility = View.VISIBLE
        }

        holder.thumbUp.setOnClickListener {
            likeFeed()
            Log.d("nanta", "쪼아요")
            holder.likeNum.text = (items[position].likeCount + 1).toString()
            holder.thumbUp.playAnimation()
            Handler().postDelayed({
                holder.thumbUp.progress = 0.0f
                holder.thumbUp.visibility = View.INVISIBLE
                holder.thumbUp2.visibility = View.VISIBLE
            }, 800)
        }

        holder.thumbUp2.setOnClickListener {
            unlikeFeed()
            Log.d("nanta", "씨러요")
            holder.likeNum.text = items[position].likeCount.toString()
            holder.thumbUp2.playAnimation()
            Handler().postDelayed({
                holder.thumbUp2.progress = 0.5f
                holder.thumbUp2.visibility = View.INVISIBLE
                holder.thumbUp.visibility = View.VISIBLE
            }, 800)
        }

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
        val thumbUp2 = innerBinding.avThumbUp2
        val likeNum = innerBinding.tvLikeNum
        val progressBar = innerBinding.progressBar
        val btnEdit = innerBinding.btnEditFeed
    }
}
