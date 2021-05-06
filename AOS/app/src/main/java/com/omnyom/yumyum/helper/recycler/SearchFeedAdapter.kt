package com.omnyom.yumyum.helper.recycler

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.omnyom.yumyum.databinding.ListItemSearchFeedBinding
import com.omnyom.yumyum.databinding.ListItemSearchPlaceBinding
import com.omnyom.yumyum.model.search.SearchFeedData
import com.omnyom.yumyum.model.search.SearchPlaceData
import com.omnyom.yumyum.ui.base.BaseRecyclerAdapter
import com.omnyom.yumyum.ui.base.BaseViewHolder
import com.omnyom.yumyum.ui.feed.MapActivity
import java.io.Serializable

class SearchFeedAdapter(private val activity: Activity) : BaseRecyclerAdapter<SearchFeedAdapter.SearchFeedDataViewHolder, SearchFeedData>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchFeedDataViewHolder {
        val itemBinding = ListItemSearchFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchFeedDataViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: SearchFeedDataViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class SearchFeedDataViewHolder(private val itemBinding: ListItemSearchFeedBinding) : BaseViewHolder(itemBinding.root) {
        init {
            itemBinding.root.setOnClickListener {
                Toast.makeText(itemBinding.root.context, "클릭된 아이템 = ${itemBinding.tvFeedTitle.text}", Toast.LENGTH_SHORT).show()
            }
        }

        private fun checkPlaceByMap() {
            val feedIntent = Intent(activity, MapActivity::class.java).apply {
                putExtra("feedResult", itemBinding.data as Serializable)
            }
            activity.startActivity(feedIntent)
        }

        override fun bind(model: Any) {
            itemBinding.data = model as? SearchFeedData
        }
    }
}