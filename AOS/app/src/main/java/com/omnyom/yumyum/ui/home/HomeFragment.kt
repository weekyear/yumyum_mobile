package com.omnyom.yumyum.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.omnyom.yumyum.TempRetrofitBuilder
import com.omnyom.yumyum.databinding.FoodListItemBinding
import com.omnyom.yumyum.databinding.FragmentHomeBinding
import com.omnyom.yumyum.helper.PreferencesManager
import com.omnyom.yumyum.interfaces.RetrofitService
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.model.like.LikeRequest
import com.omnyom.yumyum.model.like.LikeResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root = binding.root
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        homeViewModel.foodData.observe(viewLifecycleOwner, Observer {
            binding.viewPagerHome.adapter = FeedPagesAdapter(context, it)
            Log.d("HomFrag", "${it}")
        })

        binding.viewPagerHome.orientation = ViewPager2.ORIENTATION_VERTICAL

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    // 어탭터 형성
    class FeedPagesAdapter(val context: Context?,foodList: List<FeedData>) : RecyclerView.Adapter<FeedPagesAdapter.Holder>() {
        var item = foodList
        val userId = PreferencesManager.getLong(context!!, "userId").toString().toInt()


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : Holder {
            val innerBinding = FoodListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            Log.d("HomFrag", "${item}")

            return Holder(innerBinding)
        }

        override fun getItemCount(): Int = item.size

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.food.setVideoURI(item[position].videoPath.toUri())
            holder.foodName.text = item[position].title
            holder.detail.text = item[position].content
            holder.userName.text = item[position].userId.toString()
            holder.likeButton.setOnClickListener {
                var myRetrofitService: RetrofitService = TempRetrofitBuilder.buildService(RetrofitService::class.java)

                var Call = myRetrofitService.feedLike(LikeRequest(item[position].id, userId).get())
                Call.enqueue(object : Callback<LikeResponse> {
                    override fun onResponse(call: Call<LikeResponse>, response: Response<LikeResponse>) {
                        if (response.isSuccessful) {
                        }
                    }

                    override fun onFailure(call: Call<LikeResponse>, t: Throwable) {
                        t
                    }

                })
            }



            // 루프 설정!
            holder.food.setOnPreparedListener { mp -> //Start Playback
                holder.food.start()
                //Loop Video
                mp.setVolume(0f,0f)
                mp!!.isLooping = true;
            };

        }

        class Holder(private val innerBinding: FoodListItemBinding) : RecyclerView.ViewHolder(innerBinding.root) {

            init {
                innerBinding.root.setOnClickListener {
                    Toast.makeText(innerBinding.root.context, "클릭된 아이템 = ${innerBinding.textName.text}", Toast.LENGTH_SHORT).show()
                }
            }

            val food = innerBinding.foodVideo
            val foodName = innerBinding.textName
            val placeName = innerBinding.textPlacename
            val address = innerBinding.textAddress
            val detail = innerBinding.textDetail
            val userName = innerBinding.textUser
            val likeButton = innerBinding.btnLike

        }
    }


}