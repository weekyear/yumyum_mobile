package com.omnyom.yumyum.ui.myinfo

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.gms.common.internal.service.Common
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.FeedListItemBinding
import com.omnyom.yumyum.databinding.FragmentMyFeedBinding
import com.omnyom.yumyum.model.feed.FeedData

class MyFeedFragment : Fragment() {
    private lateinit var myFeedviewModel: MyFeedViewModel
    val binding  by lazy { FragmentMyFeedBinding.inflate(layoutInflater) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myFeedviewModel = ViewModelProvider(this).get(MyFeedViewModel::class.java)

        myFeedviewModel.foodData.observe(viewLifecycleOwner, Observer {
            binding.rvMyFeed.adapter = FeedPagesAdapter(it)
        })

        val context = context
        binding.rvMyFeed.layoutManager = GridLayoutManager(context, 3)

        return binding.root
    }


    // 어탭터 형성
    class FeedPagesAdapter(foodList: List<FeedData>) : RecyclerView.Adapter<FeedPagesAdapter.Holder>() {
        var item = foodList

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : Holder {
            val innerBinding = FeedListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return Holder(innerBinding)
        }

        override fun getItemCount(): Int = item.size

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val radius = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.corner_radius)
            Glide.with(holder.itemView.context).load(item[position].thumbnailPath).transform(CenterCrop(), RoundedCorners(radius)).into(holder.thumbnail)
        }

        class Holder(private val innerBinding: FeedListItemBinding) : RecyclerView.ViewHolder(innerBinding.root) {
            val thumbnail = innerBinding.ivThumbnail
        }
    }

}