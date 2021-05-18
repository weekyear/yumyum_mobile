package com.omnyom.yumyum.helper.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.omnyom.yumyum.databinding.ActivityFeedCreateBinding
import com.omnyom.yumyum.databinding.ListItemRecommendTitleBinding
import com.omnyom.yumyum.ui.base.BaseRecyclerAdapter
import com.omnyom.yumyum.ui.base.BaseViewHolder

class RecommendTitleAdapter(private val binding: ActivityFeedCreateBinding) : BaseRecyclerAdapter<RecommendTitleAdapter.RecommendTitleViewHolder, String>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendTitleViewHolder {
        val itemBinding = ListItemRecommendTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendTitleViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecommendTitleViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class RecommendTitleViewHolder(private val itemBinding: ListItemRecommendTitleBinding) : BaseViewHolder(itemBinding.root) {
        init {
            itemBinding.btnRecommend.setOnClickListener {
                binding.tvTitle.text = itemBinding.btnRecommend.text
            }
        }

        override fun bind(model: Any) {
            itemBinding.btnRecommend.text = model.toString()
        }
    }


}