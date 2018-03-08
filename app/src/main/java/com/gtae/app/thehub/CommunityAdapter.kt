package com.gtae.app.thehub

/**
 * Created by A E on 01-Mar-18.
 */
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

import java.util.ArrayList
import java.util.HashMap
import com.google.firebase.storage.StorageReference
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener

class CommunityAdapter(internal var context: Context, internal var communityDatas: List<CommunityData>) : RecyclerView.Adapter<CommunityAdapter.ViewHolder>() {
    internal lateinit var rowView: View
    internal var result: ArrayList<HashMap<String, Any>>? = null
    internal var drw: Drawable? = null
    lateinit var storage: FirebaseStorage

    init {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        storage = FirebaseStorage.getInstance();
    }// TODO Auto-generated constructor stub

    inner class ViewHolder(rowView: View) : RecyclerView.ViewHolder(rowView) {
        internal var news_title: TextView
        internal var news_link: TextView
        internal var linearLayout: LinearLayout
        internal var img: ImageView

        init {
            news_title = rowView.findViewById(R.id.title_news) as TextView
            news_link = rowView.findViewById(R.id.link_news) as TextView
            linearLayout = rowView.findViewById(R.id.ad_layout) as LinearLayout
            img=rowView.findViewById(R.id.img) as ImageView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        rowView = inflater!!.inflate(R.layout.community_card, parent, false)
        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.news_title.text = communityDatas[position].getName()
        holder.news_link.text = communityDatas[position].getId()

        //firebase Storage access code
        val storageRef = storage.reference
        val spaceRef = storageRef.child("diamonds.jpg")

      //  var s="https://firebasestorage.googleapis.com/v0/b/thehub-55aa1.appspot.com/o/diamonds.jpg?alt=media&token=68f7d8f9-f8d6-4c57-993a-7dc903f07f70";






        spaceRef.getDownloadUrl().addOnSuccessListener(OnSuccessListener<Any> { uri ->
            // Got the download URL for 'users/me/profile.png'
            // Pass it to Picasso to download, show in ImageView and caching
            //Picasso.with(context).load(uri.toString()).into(holder.img)

            Picasso.with(context).load(uri.toString()).resize(400, 400).centerCrop().into(holder.img, object : Callback {
                override fun onSuccess() {
                    println("loaded Image")
                }

                override fun onError() {
                    println("Unable to load Image")
                }
            })

        }).addOnFailureListener(OnFailureListener {
        })



        holder.linearLayout.setOnClickListener {
            val intent = Intent(context, HomeActivity::class.java)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return communityDatas.size
    }

    companion object {
        private var inflater: LayoutInflater? = null
    }
}