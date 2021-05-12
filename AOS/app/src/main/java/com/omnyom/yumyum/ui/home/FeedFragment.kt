package com.omnyom.yumyum.ui.home

import android.content.Intent
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.bumptech.glide.Glide
import com.ms.square.android.expandabletextview.ExpandableTextView
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ListItemFoodBinding
import com.omnyom.yumyum.helper.PreferencesManager
import com.omnyom.yumyum.helper.RetrofitManager
import com.omnyom.yumyum.helper.RotateTransformation
import com.omnyom.yumyum.helper.changeLayersColor
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.model.like.LikeRequest
import com.omnyom.yumyum.model.like.LikeResponse
import com.omnyom.yumyum.ui.base.BaseBindingFragment
import com.omnyom.yumyum.ui.userfeed.UserFeedActivity
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedFragment(private var feed: FeedData) : BaseBindingFragment<ListItemFoodBinding>(R.layout.list_item_food)  {
    private val mainVM: MainViewModel by viewModels({requireParentFragment()})
    private var userId: Long = 0

    private lateinit var clkRotate : Animation

    private lateinit var expandable : TextView
    private lateinit var btnExpend : ImageButton
    private lateinit var foodVideo : VideoView
    private lateinit var foodName : TextView
    private lateinit var placeName : TextView
    private lateinit var detail : ExpandableTextView
    private lateinit var userName : TextView
    private lateinit var thumbUp : LottieAnimationView
    private lateinit var thumbUp2 : LottieAnimationView
    private lateinit var likeNum : TextView
    private lateinit var ivThumbnail : ImageView
    private lateinit var progressBar : ProgressBar
    private lateinit var btnEdit : ImageButton
    private lateinit var avStar : LottieAnimationView

    override fun extraSetupBinding() { }

    override fun setup() {
        initViewByBinding()
        clkRotate = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_clockwise)
        userId = PreferencesManager.getLong(requireContext(), "userId")?: 0
    }

    private fun initViewByBinding() {
        expandable = binding.expandableText
        btnExpend = binding.expandCollapse
        foodVideo = binding.foodVideo
        foodName = binding.textName
        placeName = binding.textPlacename
        detail = binding.textDetail
        userName = binding.textUser
        thumbUp = binding.avThumbUp
        thumbUp2 = binding.avThumbUp2
        likeNum = binding.tvLikeNum
        ivThumbnail = binding.ivThumbnail
        progressBar = binding.progressBar
        btnEdit = binding.btnEditFeed
        avStar = binding.avStar
    }

    override fun onResume() {
        progressBar.visibility = View.VISIBLE
        progressBar.startAnimation(clkRotate)

        when(feed.score) {
            0 -> avStar.visibility = View.INVISIBLE
            1 -> avStar.setAnimation(R.raw.ic_vomited)
            2 -> avStar.setAnimation(R.raw.ic_confused)
            3 -> avStar.setAnimation(R.raw.ic_neutral)
            4 -> avStar.setAnimation(R.raw.ic_lol)
            5 -> avStar.setAnimation(R.raw.ic_inloveface)
        }
        avStar.repeatCount = LottieDrawable.INFINITE
        avStar.changeLayersColor(R.color.colorPrimary)
        avStar.playAnimation()
        super.onResume()
    }

    override fun onPause() {
        foodVideo.pause()
        ivThumbnail.visibility = View.VISIBLE
        super.onPause()
    }

    override fun setupViews() {

        Glide.with(requireActivity())
            .load(feed.thumbnailPath)
            .centerCrop()
            .transform(RotateTransformation(context, 90f))
            .into(ivThumbnail)

        if (expandable.lineCount == 1) {
            btnExpend.visibility = View.GONE
        }

        if (feed.place == null) {
            placeName.text = "장소를 추가해주세요"
        } else {
            placeName.text = feed.place.name + " | " + feed.place.address
        }

        foodVideo.setVideoURI(feed.videoPath.toUri())

        if (feed.title == "") {
            foodName.text = "음식명를 입력해주세요"
        } else {
            foodName.text = feed.title
        }

        if (feed.content == "") {
            detail.text = "내용을 입력해주세요"
        } else {
            detail.text = feed.content
        }
        userName.text = "@" + feed.user.nickname
        likeNum.text = feed.likeCount.toString()
        userName.setOnClickListener{
            goUserFeed()
        }

        thumbUp.setMaxFrame(15)
        thumbUp2.setMinFrame(15)

        // 버튼 구현
        if (feed.isLike) {
            thumbUp.visibility = View.INVISIBLE
            thumbUp2.visibility = View.VISIBLE
        } else {
            thumbUp2.visibility = View.INVISIBLE
            thumbUp.visibility = View.VISIBLE
        }

        if (!feed.isCompleted) {
            thumbUp2.visibility = View.GONE
            thumbUp.visibility = View.GONE
            likeNum.visibility = View.GONE
            btnEdit.visibility = View.VISIBLE
        }

        thumbUp.setOnClickListener {
            likeFeed()
            likeNum.text = (feed.likeCount + 1).toString()
            thumbUp.playAnimation()
            Handler().postDelayed({
                thumbUp.progress = 0.0f
                thumbUp.visibility = View.INVISIBLE
                thumbUp2.visibility = View.VISIBLE
            }, 800)
        }

        thumbUp2.setOnClickListener {
            unlikeFeed()
            likeNum.text = feed.likeCount.toString()
            thumbUp2.playAnimation()
            Handler().postDelayed({
                thumbUp2.progress = 0.5f
                thumbUp2.visibility = View.INVISIBLE
                thumbUp.visibility = View.VISIBLE
            }, 800)
        }

        foodVideo.setOnPreparedListener { mp ->
            foodVideo.start()
            mp.setVolume(0f,0f)
            mp!!.isLooping = true;
            foodVideo.visibility = View.VISIBLE
            progressBar.clearAnimation()
            progressBar.visibility = View.GONE
            ivThumbnail.visibility = View.INVISIBLE
        }
    }

    override fun onSubscribe() {
    }

    override fun release() { }

    fun likeFeed() {
        var Call = RetrofitManager.retrofitService.feedLike(LikeRequest(feed.id, userId.toInt()).get())
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
        var Call = RetrofitManager.retrofitService.cancelFeedLike(feed.id.toLong(), userId)
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
        val authorId = feed.user.id.toString()
        intent.putExtra("authorId", authorId)
        context?.startActivity(intent)
    }
}