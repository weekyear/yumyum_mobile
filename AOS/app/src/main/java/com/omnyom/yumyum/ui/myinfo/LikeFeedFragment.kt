package com.omnyom.yumyum.ui.myinfo

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.omnyom.yumyum.databinding.FragmentLikeFeedBinding
import com.omnyom.yumyum.databinding.ListItemFeedBinding
import com.omnyom.yumyum.model.feed.FeedData
import java.net.URL

class LikeFeedFragment : Fragment() {
    private lateinit var myLikeviewModel: LikeFeedViewModel
    val binding by lazy { FragmentLikeFeedBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myLikeviewModel = ViewModelProvider(this).get(LikeFeedViewModel::class.java)

        myLikeviewModel.likeFeedData.observe(viewLifecycleOwner, Observer {
            binding.rvLikeFeed.adapter = LikeFeedPagesAdapter(it)
        })

        val context = context
        binding.rvLikeFeed.layoutManager = GridLayoutManager(context, 3)

        return binding.root
    }

    // 어탭터 형성
    class LikeFeedPagesAdapter(foodList: List<FeedData>) : RecyclerView.Adapter<LikeFeedPagesAdapter.Holder>() {
        var item = foodList

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : Holder {
            val innerBinding = ListItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return Holder(innerBinding)
        }

        override fun getItemCount(): Int = item.size
        override fun onBindViewHolder(holder: Holder, position: Int) {
            var image_task : URLtoBitmapTask = URLtoBitmapTask().apply {
                url = URL(item[position].thumbnailPath)
            }
            var bitmap: Bitmap = image_task.execute().get()
            holder.thumbnail.setImageBitmap(bitmap)
        }


        class Holder(private val innerBinding: ListItemFeedBinding) : RecyclerView.ViewHolder(innerBinding.root) {
            val thumbnail = innerBinding.ivThumbnail
        }
    }

}