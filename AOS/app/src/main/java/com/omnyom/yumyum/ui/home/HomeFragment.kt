package com.omnyom.yumyum.ui.home

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.omnyom.yumyum.databinding.FoodListItemBinding
import com.omnyom.yumyum.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    data class MyVideo(var no: Int, var photo: Uri, var detail: String)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root = binding.root
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        homeViewModel.foodData.observe(viewLifecycleOwner, Observer {
            binding.viewPagerHome.adapter = FeedPagesAdapter(it)
        })

        binding.viewPagerHome.orientation = ViewPager2.ORIENTATION_VERTICAL

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    // 어탭터 형성
    class FeedPagesAdapter(foodList: List<MyVideo>) : RecyclerView.Adapter<FeedPagesAdapter.Holder>() {
        var item = foodList

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : Holder {
            val innerBinding = FoodListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            return Holder(innerBinding)
        }

        override fun getItemCount(): Int = item.size

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.food.setVideoURI(item[position].photo)
            holder.foodName.text = item[position].detail

            // 루프 설정!
            holder.food.setOnPreparedListener { mp -> //Start Playback
                holder.food.start()
                //Loop Video
                mp!!.isLooping = true;
            };

        }

        class Holder(private val innerBinding: FoodListItemBinding) : RecyclerView.ViewHolder(innerBinding.root) {
            val food = innerBinding.foodVideo
            val foodName = innerBinding.textName

        }
    }
}