package com.omnyom.yumyum.ui.eureka

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.util.Log.d
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.example.messengerapp.Notifications.EurekaData
import com.example.messengerapp.Notifications.Sender
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.FragmentEurekaBinding
import com.omnyom.yumyum.helper.KakaoMapUtils
import com.omnyom.yumyum.helper.PreferencesManager.Companion.userId
import com.omnyom.yumyum.interfaces.APISerivce
import com.omnyom.yumyum.model.eureka.Chat
import com.omnyom.yumyum.model.eureka.Client
import com.omnyom.yumyum.model.eureka.EurekaResponse
import com.omnyom.yumyum.ui.base.BaseBindingFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class EurekaFragment : BaseBindingFragment<FragmentEurekaBinding> (R.layout.fragment_eureka) {
    private val eurekaVM: EurekaViewModel by viewModels()
    val db = Firebase.firestore

    override fun extraSetupBinding() {
        binding.apply {
            vm = eurekaVM
        }
    }

    override fun setup() {
        KakaoMapUtils.initLocationFragmentManager(context)

        val chatsRef = db.collection("Chats")
        chatsRef.addSnapshotListener { snapshot, e ->
            eurekaVM.getGeoHashes()
        }

    }

    override fun setupViews() {
        binding.btnSendMessage.setOnClickListener {
            val message = binding.editTextMessage.text.toString()
            val userId = userId.toString().toInt()
            if (message == "") {
                Toast.makeText(context, "메세지를 입력하세요", Toast.LENGTH_SHORT).show()
            } else {
                eurekaVM.sendMessage(message, userId)
            }
            binding.editTextMessage.setText("")
        }



    }

    override fun onSubscribe() {
        eurekaVM.matchingDocs.observe(this) {
            val constraintLayout : ConstraintLayout = binding.clMessage
            val docs = eurekaVM.matchingDocs.value!!
            constraintLayout.removeAllViews()


            for (doc in docs){
//                val authorId = doc.userId.toString().toLong()
//                d("author", "$authorId")
//                eurekaVM.getMessageAuthor(authorId)
//                val userData = eurekaVM.authorData.value!!
//                d("author", "$userData")
                viewChat(requireContext(), doc)
            }

        }
    }

    override fun release() {
    }


    fun viewChat(context: Context, doc: Chat) {
        val constraintLayout : ConstraintLayout = binding.clMessage
        val random = Random()
        val lng = eurekaVM.positions!![0]
        val lat = eurekaVM.positions!![1]

        val message = TextView(context)
        message.text = doc.message
        TextViewCompat.setTextAppearance(
                message,
                android.R.style.TextAppearance_DeviceDefault_Large
        )
        message.typeface = Typeface.MONOSPACE
        message.background = ColorDrawable(Color.parseColor("#9FD3FA"))
        message.setPadding(
                5.toDp(context),
                3.toDp(context),
                5.toDp(context),
                3.toDp(context)
        )
        message.setTextColor(Color.parseColor("#26428B"))
        message.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10F)
        message.id = View.generateViewId()
        constraintLayout.addView(message)

        // 유저네임
        val username = TextView(context)
        username.text = doc.userId.toString()
        TextViewCompat.setTextAppearance(
                username,
                android.R.style.TextAppearance_DeviceDefault_Large
        )
        username.typeface = Typeface.MONOSPACE
        username.setTextColor(Color.parseColor("#26428B"))
        username.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10F)
        username.id = View.generateViewId()
        constraintLayout.addView(username)

        //프로필
        val profile = ImageView(context)
        Glide.with(context).load(R.drawable.ic_profile).override(50, 50).into(profile)
        profile.id = View.generateViewId()
        constraintLayout.addView(profile)

        //
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels;
        val width = displayMetrics.widthPixels

        // constraint
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        val leftGap = random.nextInt(340) + 30
        val bottomGap = random.nextInt(500) + 50
        val leftBylat : Double = (lat - doc.lat) * 100000
        val bottomBylng : Double = (lng - doc.lng) * 100000
        d("geoeeeleftBylat", "$leftBylat")
        d("geoeeebottomBylng", "$bottomBylng")

        if (doc.userId == userId.toString().toInt()) {
            constraintSet.connect(
                    message.id,
                    ConstraintSet.START,
                    R.id.cl_message,
                    ConstraintSet.START,
                    0.toDp(context)
            )
            constraintSet.connect(
                    message.id,
                    ConstraintSet.BOTTOM,
                    R.id.cl_message,
                    ConstraintSet.BOTTOM,
                    0.toDp(context)
            )
            constraintSet.connect(
                    message.id,
                    ConstraintSet.END,
                    R.id.cl_message,
                    ConstraintSet.END,
                    0.toDp(context)
            )
            constraintSet.connect(
                    message.id,
                    ConstraintSet.TOP,
                    R.id.cl_message,
                    ConstraintSet.TOP,
                    0.toDp(context)
            )

        } else {
            constraintSet.connect(
                    message.id,
                    ConstraintSet.START,
                    R.id.cl_message,
                    ConstraintSet.START,
                    leftGap.toDp(context)
            )
            constraintSet.connect(
                    message.id,
                    ConstraintSet.BOTTOM,
                    R.id.cl_message,
                    ConstraintSet.BOTTOM,
                    bottomGap.toDp(context)
            )
        }

        constraintSet.connect(
                profile.id,
                ConstraintSet.START,
                message.id,
                ConstraintSet.START,
                0.toDp(context)
        )

        constraintSet.connect(
                profile.id,
                ConstraintSet.END,
                message.id,
                ConstraintSet.END,
                0.toDp(context)
        )


        constraintSet.connect(
                profile.id,
                ConstraintSet.TOP,
                message.id,
                ConstraintSet.BOTTOM,
                10.toDp(context)
        )

        constraintSet.connect(
                username.id,
                ConstraintSet.START,
                message.id,
                ConstraintSet.START,
                0.toDp(context)
        )

        constraintSet.connect(
                username.id,
                ConstraintSet.END,
                message.id,
                ConstraintSet.END,
                0.toDp(context)
        )

        constraintSet.connect(
                username.id,
                ConstraintSet.TOP,
                profile.id,
                ConstraintSet.BOTTOM,
                10.toDp(context)
        )

        constraintSet.applyTo(constraintLayout)
    }


    fun Int.toDp(context: Context):Int = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,this.toFloat(),context.resources.displayMetrics
    ).toInt()

}