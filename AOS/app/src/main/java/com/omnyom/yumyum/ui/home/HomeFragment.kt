package com.omnyom.yumyum.ui.home

import android.content.Context
import android.os.Bundle
import android.os.Handler
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
import com.omnyom.yumyum.model.place.GetPlaceDataResponse
import com.omnyom.yumyum.model.place.PlaceData
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


            return Holder(innerBinding)
        }

        override fun getItemCount(): Int = item.size

        override fun onBindViewHolder(holder: Holder, position: Int) {
            var myRetrofitService: RetrofitService = TempRetrofitBuilder.buildService(RetrofitService::class.java)
            Log.d("HomFrag", "${item[position].placeId}")

            // 장소 불러오기
            fun getPlaceData(placeId : Long) {
                var call = myRetrofitService.getPlaceData(placeId)
                call.enqueue(object : Callback<GetPlaceDataResponse> {
                    override fun onResponse(call: Call<GetPlaceDataResponse>, response: Response<GetPlaceDataResponse>) {
                        if (response.isSuccessful) {
                            Log.d("placeData", "오나?")
                            val placeData = response.body()!!.data
                            holder.placeName.text = placeData.name
                            holder.address.text = placeData.address
                        }
                    }

                    override fun onFailure(call: Call<GetPlaceDataResponse>, t: Throwable) {
                        t
                        Log.d("placeData", "${t}")
                    }

                })
            }

            // 좋아요!
            fun likeFeed() {
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

            // 안좋아요!
            fun unlikeFeed() {
                var Call = myRetrofitService.cancelFeedLike(item[position].id.toLong(), userId.toLong())
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

            getPlaceData(item[position].placeId.toLong())

            holder.food.setVideoURI(item[position].videoPath.toUri())
            holder.foodName.text = item[position].title
            holder.detail.text = item[position].content
            holder.userName.text = item[position].userId.toString()

            holder.thumbUp.setMaxFrame(15)
            holder.thumbUp2.setMinFrame(15)

            // 버튼 구현
            if (item[position].isLike) {
                holder.thumbUp.visibility = View.INVISIBLE
            } else {
                holder.thumbUp2.visibility = View.INVISIBLE
            }

            holder.thumbUp.setOnClickListener {
                likeFeed()
                Log.d("nanta", "쪼아요")
                holder.thumbUp.playAnimation()
                Handler().postDelayed({
                    holder.thumbUp.progress = 0.0f
                    holder.thumbUp.visibility = View.INVISIBLE
                    holder.thumbUp2.visibility = View.VISIBLE
                }, 800)
            }

            holder.thumbUp2.setOnClickListener {
                unlikeFeed()
                Log.d("nanta", "씨러요")
                holder.thumbUp2.playAnimation()
                Handler().postDelayed({
                    holder.thumbUp2.progress = 0.5f
                    holder.thumbUp2.visibility = View.INVISIBLE
                    holder.thumbUp.visibility = View.VISIBLE
                }, 800)
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
            val food = innerBinding.foodVideo
            val foodName = innerBinding.textName
            val placeName = innerBinding.textPlacename
            val address = innerBinding.textAddress
            val detail = innerBinding.textDetail
            val userName = innerBinding.textUser
            val thumbUp = innerBinding.avThumbUp
            val thumbUp2 = innerBinding.avThumbUp2
        }
    }


}