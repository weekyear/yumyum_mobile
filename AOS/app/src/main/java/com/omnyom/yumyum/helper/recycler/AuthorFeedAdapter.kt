package com.omnyom.yumyum.helper.recycler

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.omnyom.yumyum.databinding.ListItemFeedBinding
import com.omnyom.yumyum.helper.RotateTransformation
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.ui.base.BaseRecyclerAdapter
import com.omnyom.yumyum.ui.base.BaseViewHolder
import com.omnyom.yumyum.ui.selectedfeed.SelectedAllActivity

class AuthorFeedAdapter(val context: Context) : BaseRecyclerAdapter<AuthorFeedAdapter.AuthorFeedViewHolder, FeedData>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : AuthorFeedViewHolder {
        val itemBinding = ListItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AuthorFeedViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: AuthorFeedViewHolder, position: Int) {
        holder.bind(items[position])

        Glide.with(context)
            .load(items[position].thumbnailPath)
            .transform(RotateTransformation(context, 90f))
            .thumbnail(0.1f)
            .into(holder.thumbnail)

        holder.thumbnail.setOnClickListener {
            val sendData : ArrayList<FeedData> = ArrayList(items.map { item -> item })
            val intent = Intent(context, SelectedAllActivity::class.java)
            intent.putExtra("FeedData", sendData)
            intent.putExtra("position", position)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class AuthorFeedViewHolder(private val itemBinding: ListItemFeedBinding) : BaseViewHolder(itemBinding.root) {
        val thumbnail = itemBinding.ivThumbnail
    }

}