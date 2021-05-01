package com.omnyom.yumyum.ui.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ActivityLocationListBinding
import com.omnyom.yumyum.databinding.PlaceListItemBinding
import com.omnyom.yumyum.model.maps.SearchPlaceResult
import com.omnyom.yumyum.ui.base.BaseBindingActivity

class LocationListActivity : BaseBindingActivity<ActivityLocationListBinding>(R.layout.activity_location_list) {
    private val locationListVM : LocationListViewModel by viewModels()

    private val documents : MutableList<SearchPlaceResult> by lazy {
        loadData()
    }

    override fun extraSetupBinding() {
    }

    override fun setup() {
    }

    override fun setupViews() {
        var adapter = LocationListAdapter(documents)
        binding.rvPlace.adapter = adapter
        binding.rvPlace.layoutManager = LinearLayoutManager(this)
    }

    override fun onSubscribe() {
    }

    override fun release() {
    }

    fun loadData(): MutableList<SearchPlaceResult> {
        val data: MutableList<SearchPlaceResult> = mutableListOf()
        val documentList = intent.getSerializableExtra("DocumentList") as List<SearchPlaceResult>
        for (docu in documentList) {
            val document = docu as SearchPlaceResult
            data.add(document!!)
        }
        return data
    }

    class LocationListAdapter(docuList: List<SearchPlaceResult>) : RecyclerView.Adapter<LocationListAdapter.Holder>() {
        var item = docuList

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : Holder {
            val innerBinding = PlaceListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            return Holder(innerBinding)
        }

        override fun getItemCount(): Int = item.size

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val document = item.get(position)
            holder.setDocument(document)

        }

        class Holder(private val innerBinding: PlaceListItemBinding) : RecyclerView.ViewHolder(innerBinding.root) {


            fun setDocument(document: SearchPlaceResult) {
//                innerBinding.tvName.text = document.place_name
//                innerBinding.tvAddress.text = document.address_name
//                innerBinding.tvDistance.text = document.distance
                innerBinding.root.setOnClickListener {
                    Toast.makeText(innerBinding.root.context, "${document.address_name}", Toast.LENGTH_SHORT).show()

                }
            }

        }
    }
}


