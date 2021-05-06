package com.omnyom.yumyum.ui.userfeed

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ActivityUserFeedBinding
import com.omnyom.yumyum.databinding.FeedListItemBinding
import com.omnyom.yumyum.helper.recycler.AuthorFeedAdapter
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.ui.base.BaseBindingActivity
import com.omnyom.yumyum.ui.myinfo.MyFeedFragment
import com.omnyom.yumyum.ui.myinfo.URLtoBitmapTask
import java.net.URL

class UserFeedActivity : BaseBindingActivity<ActivityUserFeedBinding>(R.layout.activity_user_feed) {
    private val userFeedVM: UserFeedViewModel by viewModels()

    override fun extraSetupBinding() {
        binding.apply {
            vm = userFeedVM
            lifecycleOwner = this@UserFeedActivity
        }
    }

    override fun setup() {
        val authorId = intent.getStringExtra("authorId")!!.toLong()
        userFeedVM.getAuthorFeed(authorId)
        userFeedVM.getAuthorData(authorId)
    }

    override fun setupViews() {
        supportActionBar?.hide()
        binding.rvAuthorFeed.apply {
            adapter = AuthorFeedAdapter(this@UserFeedActivity)
            layoutManager = GridLayoutManager(this@UserFeedActivity, 3)
        }
    }

    override fun onSubscribe() {
        userFeedVM.foodData.observe(this) {
            val adapter = binding.rvAuthorFeed.adapter as AuthorFeedAdapter
            adapter.run {
                setItems(it)
                notifyDataSetChanged()
            }
        }
        userFeedVM.authorData.observe(this) {
            binding.tvUsername.text = it.nickname
            binding.tvIntroduction.text = it.introduction
            Glide.with(this).load(it.profilePath).into(binding.ivAvatar)
        }
    }

    override fun release() {
    }
}