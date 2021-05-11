package com.omnyom.yumyum.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.FragmentHomeBinding
import com.omnyom.yumyum.databinding.ListItemFoodBinding
import com.omnyom.yumyum.helper.PreferencesManager
import com.omnyom.yumyum.helper.RetrofitManager.Companion.retrofitService
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.model.like.LikeRequest
import com.omnyom.yumyum.model.like.LikeResponse
import com.omnyom.yumyum.model.place.GetPlaceDataResponse
import com.omnyom.yumyum.model.place.PlaceData
import com.omnyom.yumyum.ui.search.SearchFragment
import com.omnyom.yumyum.ui.userfeed.UserFeedActivity
import com.omnyom.yumyum.ui.useroption.MyOptionActivity
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
        })

        binding.viewPagerHome.orientation = ViewPager2.ORIENTATION_VERTICAL

        binding.icSearch.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, SearchFragment())
            transaction.commit()
        }

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
            val innerBinding = ListItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            // assigning that animation to
            // the image and start animation
            return Holder(innerBinding)
        }

        override fun getItemCount(): Int = item.size

        override fun onBindViewHolder(holder: Holder, position: Int) {

            // 좋아요!
            fun likeFeed() {
                var Call = retrofitService.feedLike(LikeRequest(item[position].id, userId).get())
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
                var Call = retrofitService.cancelFeedLike(item[position].id.toLong(), userId.toLong())
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

            fun goUserFeed() {
                val intent = Intent(context, UserFeedActivity::class.java)
                val authorId = item[position].user.id.toString()
                intent.putExtra("authorId", authorId)
                context?.startActivity(intent)
            }

            val clkRotate = AnimationUtils.loadAnimation(context, R.anim.rotate_clockwise)
            holder.progressBar.startAnimation(clkRotate)

            if (holder.expendable.lineCount == 1) {
                holder.btnExpend.visibility = View.GONE
            }

            if (item[position].place == null) {
                holder.placeName.text = "장소를 추가해주세요"
            } else {
                holder.placeName.text = item[position].place.name + " | " + item[position].place.address
            }
            holder.food.setVideoURI(item[position].videoPath.toUri())
            holder.foodName.text = item[position].title
            holder.detail.text = item[position].content
            holder.userName.text = "@" + item[position].user.nickname
            holder.likeNum.text = item[position].likeCount.toString()
            holder.userName.setOnClickListener{
                goUserFeed()
            }

            holder.thumbUp.setMaxFrame(15)
            holder.thumbUp2.setMinFrame(15)

            // 버튼 구현
            if (item[position].isLike) {
                holder.thumbUp.visibility = View.INVISIBLE
                holder.thumbUp2.visibility = View.VISIBLE
            } else {
                holder.thumbUp2.visibility = View.INVISIBLE
                holder.thumbUp.visibility = View.VISIBLE
            }

            when(item[position].score) {
                1 -> {
                    holder.star1.playAnimation()
                    holder.star1.loop(true)
                    holder.star1.visibility = View.VISIBLE
                    holder.star2.visibility = View.GONE
                    holder.star3.visibility = View.GONE
                    holder.star4.visibility = View.GONE
                    holder.star5.visibility = View.GONE
                }
                2 -> {
                    holder.star2.playAnimation()
                    holder.star2.loop(true)
                    holder.star1.visibility = View.GONE
                    holder.star2.visibility = View.VISIBLE
                    holder.star3.visibility = View.GONE
                    holder.star4.visibility = View.GONE
                    holder.star5.visibility = View.GONE
                }
                3 -> {
                    holder.star3.playAnimation()
                    holder.star3.loop(true)
                    holder.star1.visibility = View.GONE
                    holder.star2.visibility = View.GONE
                    holder.star3.visibility = View.VISIBLE
                    holder.star4.visibility = View.GONE
                    holder.star5.visibility = View.GONE
                }
                4 -> {
                    holder.star4.playAnimation()
                    holder.star4.loop(true)
                    holder.star1.visibility = View.GONE
                    holder.star2.visibility = View.GONE
                    holder.star3.visibility = View.GONE
                    holder.star4.visibility = View.VISIBLE
                    holder.star5.visibility = View.GONE
                }
                5 -> {
                    holder.star5.playAnimation()
                    holder.star5.loop(true)
                    holder.star1.visibility = View.GONE
                    holder.star2.visibility = View.GONE
                    holder.star3.visibility = View.GONE
                    holder.star4.visibility = View.GONE
                    holder.star5.visibility = View.VISIBLE
                }
            }

            holder.thumbUp.setOnClickListener {
                likeFeed()
                Log.d("nanta", "쪼아요")
                holder.likeNum.text = (item[position].likeCount + 1).toString()
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
                holder.likeNum.text = item[position].likeCount.toString()
                holder.thumbUp2.playAnimation()
                Handler().postDelayed({
                    holder.thumbUp2.progress = 0.5f
                    holder.thumbUp2.visibility = View.INVISIBLE
                    holder.thumbUp.visibility = View.VISIBLE
                }, 800)
            }

            holder.food.setOnPreparedListener { mp ->
                holder.food.start()
                mp.setVolume(0f,0f)
                mp!!.isLooping = true;
                holder.progressBar.clearAnimation()
                holder.progressBar.visibility = View.GONE
            };
        }

        class Holder(private val innerBinding: ListItemFoodBinding) : RecyclerView.ViewHolder(innerBinding.root) {
            val expendable = innerBinding.expandableText
            val btnExpend = innerBinding.expandCollapse
            val food = innerBinding.foodVideo
            val foodName = innerBinding.textName
            val placeName = innerBinding.textPlacename
            val detail = innerBinding.textDetail
            val userName = innerBinding.textUser
            val thumbUp = innerBinding.avThumbUp
            val thumbUp2 = innerBinding.avThumbUp2
            val likeNum = innerBinding.tvLikeNum
            val progressBar = innerBinding.progressBar
            val star1 = innerBinding.avStar1
            val star2 = innerBinding.avStar2
            val star3 = innerBinding.avStar3
            val star4 = innerBinding.avStar4
            val star5 = innerBinding.avStar5
        }
    }
}