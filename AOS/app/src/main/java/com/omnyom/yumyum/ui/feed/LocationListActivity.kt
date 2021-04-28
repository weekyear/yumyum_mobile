package com.omnyom.yumyum.ui.feed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.omnyom.yumyum.databinding.ActivityLocationListBinding
import com.omnyom.yumyum.databinding.PlaceListItemBinding
import com.omnyom.yumyum.model.maps.Document

class LocationListActivity : AppCompatActivity() {


    val binding by lazy { ActivityLocationListBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val data : MutableList<Document> = loadData()
        var adapter = LocationListAdapter(data)
        binding.rvPlace.adapter = adapter
        binding.rvPlace.layoutManager = LinearLayoutManager(this)


    }

    fun loadData(): MutableList<Document> {
        val data: MutableList<Document> = mutableListOf()
        val documentList = intent.getSerializableExtra("DocumentList") as List<Document>
        for (docu in documentList) {
            val document = docu as Document
            data.add(document!!)
        }
        return data
    }

    class LocationListAdapter(docuList: List<Document>) : RecyclerView.Adapter<LocationListAdapter.Holder>() {
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


            fun setDocument(document: Document) {
                innerBinding.tvName.text = document.place_name
                innerBinding.tvAddress.text = document.address_name
                innerBinding.tvDistance.text = document.distance
                innerBinding.root.setOnClickListener {
                    Toast.makeText(innerBinding.root.context, "${document.address_name}", Toast.LENGTH_SHORT).show()

                }
            }

        }
    }


}


