package com.omnyom.yumyum.helper.recycler

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.omnyom.yumyum.databinding.ListItemFeedBinding
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.ui.base.BaseRecyclerAdapter
import com.omnyom.yumyum.ui.base.BaseViewHolder
import com.omnyom.yumyum.ui.myinfo.URLtoBitmapTask
import com.omnyom.yumyum.ui.selectedfeed.SelectedAllActivity
import com.omnyom.yumyum.ui.userfeed.UserFeedActivity
import java.net.URL
import kotlin.reflect.typeOf

class AuthorFeedAdapter(val context: Context) : BaseRecyclerAdapter<AuthorFeedAdapter.AuthorFeedViewHolder, FeedData>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : AuthorFeedViewHolder {
        val itemBinding = ListItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AuthorFeedViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: AuthorFeedViewHolder, position: Int) {
        holder.bind(items[position])

        var image_task : URLtoBitmapTask = URLtoBitmapTask().apply {
            url = URL(items[position].thumbnailPath)
        }
        var bitmap: Bitmap = image_task.execute().get()
        holder.thumbnail.setImageBitmap(bitmap)

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
