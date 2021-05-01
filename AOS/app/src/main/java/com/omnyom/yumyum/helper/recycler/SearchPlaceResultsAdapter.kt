package com.omnyom.yumyum.helper.recycler

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.omnyom.yumyum.databinding.PlaceListItemBinding
import com.omnyom.yumyum.model.maps.SearchPlaceResult
import com.omnyom.yumyum.ui.base.BaseRecyclerAdapter
import com.omnyom.yumyum.ui.base.BaseViewHolder
import com.omnyom.yumyum.ui.feed.FeedCreateActivity
import com.omnyom.yumyum.ui.feed.SearchPlaceActivity
import java.io.Serializable

class SearchPlaceResultsAdapter(private val activity: SearchPlaceActivity) : BaseRecyclerAdapter<SearchPlaceResultsAdapter.SearchPlaceResultViewHolder, SearchPlaceResult>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPlaceResultViewHolder {
        val itemBinding = PlaceListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchPlaceResultViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: SearchPlaceResultViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class SearchPlaceResultViewHolder(private val itemBinding: PlaceListItemBinding) : BaseViewHolder(itemBinding.root) {
        init {
            itemBinding.root.setOnClickListener {
                setPlaceResult()
                Toast.makeText(itemBinding.root.context, "클릭된 아이템 = ${itemBinding.tvPlaceListItemName.text}", Toast.LENGTH_SHORT).show()
            }
        }

        private fun setPlaceResult() {
            val intent = Intent().apply{
                putExtra("placeResult", itemBinding.data as Serializable)
            }
            activity.setResult(Activity.RESULT_OK, intent)
            activity.finish()
        }

        override fun bind(model: Any) {
            itemBinding.data = model as? SearchPlaceResult
        }
    }
}

