package com.omnyom.yumyum.helper.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.view.marginBottom
import androidx.core.view.marginRight
import com.omnyom.yumyum.databinding.ListItemEurekaBinding
import com.omnyom.yumyum.helper.recycler.EurekaAdapter.*
import com.omnyom.yumyum.model.eureka.Chat
import com.omnyom.yumyum.ui.base.BaseRecyclerAdapter
import com.omnyom.yumyum.ui.base.BaseViewHolder
import java.util.Random

class EurekaAdapter(val context: Context) : BaseRecyclerAdapter<EurekaViewHolder, Chat>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EurekaViewHolder {
        val itemBinding = ListItemEurekaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EurekaViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: EurekaViewHolder, position: Int) {
        holder.bind(items[position])
        holder.message.text = items[position].message
    }

    class EurekaViewHolder(private val itemBinding: ListItemEurekaBinding) : BaseViewHolder(itemBinding.root) {
        val message = itemBinding.tvMessage
        val profile = itemBinding.ivCircleProfile
        val username = itemBinding.tvUsernameEureka
    }

}
