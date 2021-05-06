package com.omnyom.yumyum.helper.recycler

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import com.omnyom.yumyum.databinding.FeedListItemBinding
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.ui.base.BaseRecyclerAdapter
import com.omnyom.yumyum.ui.base.BaseViewHolder
import com.omnyom.yumyum.ui.myinfo.URLtoBitmapTask
import com.omnyom.yumyum.ui.userfeed.UserFeedActivity
import java.net.URL

class AuthorFeedAdapter(private val activity: UserFeedActivity) : BaseRecyclerAdapter<AuthorFeedAdapter.AuthorFeedViewHolder, FeedData>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : AuthorFeedViewHolder {
        val itemBinding = FeedListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AuthorFeedViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: AuthorFeedViewHolder, position: Int) {
        holder.bind(items[position])

        var image_task : URLtoBitmapTask = URLtoBitmapTask().apply {
            url = URL(items[position].thumbnailPath)
        }
        var bitmap: Bitmap = image_task.execute().get()
        holder.thumbnail.setImageBitmap(bitmap)

    }

    override fun getItemCount(): Int = items.size

    inner class AuthorFeedViewHolder(private val itemBinding: FeedListItemBinding) : BaseViewHolder(itemBinding.root) {
        val thumbnail = itemBinding.ivThumbnail
    }


}