package com.omnyom.yumyum.helper.recycler

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.omnyom.yumyum.databinding.ListItemSearchPlaceBinding
import com.omnyom.yumyum.model.search.SearchPlaceData
import com.omnyom.yumyum.ui.base.BaseRecyclerAdapter
import com.omnyom.yumyum.ui.base.BaseViewHolder
import com.omnyom.yumyum.ui.feed.MapActivity
import java.io.Serializable

class SearchPlaceAdapter(private val activity: Activity) : BaseRecyclerAdapter<SearchPlaceAdapter.SearchPlaceDataViewHolder, SearchPlaceData>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPlaceDataViewHolder {
        val itemBinding = ListItemSearchPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchPlaceDataViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: SearchPlaceDataViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class SearchPlaceDataViewHolder(private val itemBinding: ListItemSearchPlaceBinding) : BaseViewHolder(itemBinding.root) {
        init {
            itemBinding.root.setOnClickListener {
                Toast.makeText(itemBinding.root.context, "클릭된 아이템 = ${itemBinding.tvPlaceListItemName.text}", Toast.LENGTH_SHORT).show()
            }

            itemBinding.btnPlace.setOnClickListener {
                checkPlaceByMap()
            }
        }

        private fun checkPlaceByMap() {
            val placeIntent = Intent(activity, MapActivity::class.java).apply {
                putExtra("placeResult", itemBinding.data as Serializable)
            }
            activity.startActivity(placeIntent)
        }

        override fun bind(model: Any) {
            itemBinding.data = model as? SearchPlaceData
        }
    }
}