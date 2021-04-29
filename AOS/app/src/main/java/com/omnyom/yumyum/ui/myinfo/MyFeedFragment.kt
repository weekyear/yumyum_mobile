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
import com.google.android.gms.common.internal.service.Common
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
            Log.d("HomFrag", "${it}")
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
            Log.d("HomFrag", "${item}")

            return Holder(innerBinding)
        }

        override fun getItemCount(): Int = item.size

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.foodName.text = item[position].title
            holder.thumbnail.setImageURI(item[position].thumbnailPath.toUri())

        }

        class Holder(private val innerBinding: FeedListItemBinding) : RecyclerView.ViewHolder(innerBinding.root) {
            init {
                innerBinding.root.setOnClickListener {
                    Toast.makeText(innerBinding.root.context, "클릭된 아이템 = ${innerBinding.tvFoodName.text}", Toast.LENGTH_SHORT).show()
                }
            }

            val thumbnail = innerBinding.ivThumbnail
            val foodName = innerBinding.tvFoodName


        }
    }

}