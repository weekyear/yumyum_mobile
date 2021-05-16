package com.omnyom.yumyum.ui.home

import android.content.Intent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.net.toUri
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.bumptech.glide.Glide
import com.ms.square.android.expandabletextview.ExpandableTextView
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ListItemFoodBinding
import com.omnyom.yumyum.helper.RotateTransformation
import com.omnyom.yumyum.helper.changeLayersColor
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.ui.base.BaseBindingFragment
import com.omnyom.yumyum.ui.userfeed.UserFeedActivity

class FeedFragment(private var feed: FeedData, private val homeVM : HomeViewModel) : BaseBindingFragment<ListItemFoodBinding>(R.layout.list_item_food)  {
    companion object {
        lateinit var curFeed : FeedData
    }

    private lateinit var clkRotate : Animation

    private lateinit var expandable : TextView
    private lateinit var btnExpend : ImageButton
    private lateinit var foodVideo : VideoView
    private lateinit var foodName : TextView
    private lateinit var placeName : TextView
    private lateinit var detail : ExpandableTextView
    private lateinit var userName : TextView
    private lateinit var thumbUp : LottieAnimationView
    private lateinit var likeNum : TextView
    private lateinit var ivThumbnail : ImageView
    private lateinit var progressBar : ProgressBar
    private lateinit var btnEdit : ImageButton
    private lateinit var avStar : LottieAnimationView
    private var isLikeAnimating : Boolean = false

    override fun extraSetupBinding() { }

    override fun setup() {
        initViewByBinding()
        clkRotate = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_clockwise)
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
        likeNum = binding.tvLikeNum
        ivThumbnail = binding.ivThumbnail
        progressBar = binding.progressBar
        avStar = binding.avStar
    }

    override fun onResume() {
        initProgressBar()
        initAVStar()
        curFeed = feed

        super.onResume()
    }

    override fun onPause() {
        foodVideo.pause()
        ivThumbnail.visibility = View.VISIBLE
        super.onPause()
    }

    override fun setupViews() {
        initIsCompleted()
        initIVThumbnail()
        initExpandable()
        initTVPlace()
        initTVTitle()
        initTVContent()
        initTVUserName()
        initTVLikeNum()
        initLikeBtn()
        initVideoFood()
    }

    override fun onSubscribe() {
    }

    override fun release() { }

    private fun initProgressBar() {
        progressBar.visibility = View.VISIBLE
        progressBar.startAnimation(clkRotate)
    }

    private fun initAVStar() {
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
    }

    private fun initIsCompleted() {
        if (!feed.isCompleted) {
            thumbUp.visibility = View.GONE
            likeNum.visibility = View.GONE
            btnEdit.visibility = View.VISIBLE
        }
    }

    private fun initIVThumbnail() {
        Glide.with(requireActivity())
                .load(feed.thumbnailPath)
                .centerCrop()
                .transform(RotateTransformation(context, 90f))
                .into(ivThumbnail)
    }

    private fun initExpandable() {
        if (expandable.lineCount == 1) {
            btnExpend.visibility = View.GONE
        }
    }

    private fun initTVPlace() {
        if (feed.title == "") {
            foodName.text = "음식명를 입력해주세요"
        } else {
            foodName.text = feed.title
        }
    }

    private fun initTVTitle() {
        if (feed.place == null) {
            placeName.text = "장소를 추가해주세요"
        } else {
            placeName.text = feed.place.name + " | " + feed.place.address
        }
    }

    private fun initTVContent() {
        if (feed.content == "") {
            detail.text = "내용을 입력해주세요"
        } else {
            detail.text = feed.content
        }
    }

    private fun initTVUserName() {
        userName.text = "@" + feed.user.nickname
        userName.setOnClickListener{
            goUserFeed()
        }
    }

    private fun initTVLikeNum() {
        likeNum.text = feed.likeCount.toString()
    }

    private fun initLikeBtn() {
        if (feed.isLike) {
            thumbUp.progress = 0.5f
            thumbUp.changeLayersColor(R.color.colorPrimary)
        } else {
            thumbUp.progress = 0.0f
            thumbUp.changeLayersColor(R.color.white)
        }

        thumbUp.setOnClickListener {
            if (!isLikeAnimating) {
                isLikeAnimating = true
                if (feed.isLike) {
                    homeVM.unlikeFeed(feed.id.toLong())
                    likeNum.text = feed.likeCount.toString()
                    thumbUp.setMinAndMaxProgress(0.5f, 1.0f)
                    thumbUp.changeLayersColor(R.color.white)
                } else {
                    homeVM.likeFeed(feed.id.toLong())
                    likeNum.text = (feed.likeCount + 1).toString()
                    thumbUp.setMinAndMaxProgress(0.0f, 0.5f)
                    thumbUp.changeLayersColor(R.color.colorPrimary)
                }
                feed.isLike = !feed.isLike
                thumbUp.playAnimation()
                isLikeAnimating = false
            }
        }
    }

    private fun initThumbUpLikeState() {
        thumbUp.setMinAndMaxProgress(0.0f, 0.5f)
        thumbUp.changeLayersColor(R.color.colorPrimary)
    }

    private fun initThumbUpUnLikeState() {
        thumbUp.setMinAndMaxProgress(0.5f, 1.0f)
        thumbUp.changeLayersColor(R.color.white)
    }

    private fun initVideoFood() {
        foodVideo.setVideoURI(feed.videoPath.toUri())

        foodVideo.setOnPreparedListener { mp ->
            foodVideo.start()
            mp.setVolume(0f,0f)
            mp!!.isLooping = true;
            foodVideo.visibility = View.VISIBLE
            progressBar.clearAnimation()
            progressBar.visibility = View.GONE
            ivThumbnail.visibility = View.INVISIBLE
        }

        foodVideo.setOnCompletionListener {
            progressBar.clearAnimation()
            progressBar.visibility = View.GONE
            ivThumbnail.visibility = View.INVISIBLE
        }
    }

    private fun goUserFeed() {
        val intent = Intent(context, UserFeedActivity::class.java)
        val authorId = feed.user.id.toString()
        intent.putExtra("authorId", authorId)
        context?.startActivity(intent)
    }
}