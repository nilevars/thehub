package com.gtae.app.thehub

/**
 * Created by A E on 04-Mar-18.
 */
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso


class ForumAdapter(internal var context: Context, internal var forumDatas: List<ForumData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    internal var itemView: View? = null
    private var inflater: LayoutInflater? = null

    private val QUE = 0
    private val ARTICLE = 1
    private val PHOTO = 2
    private val JOKES = 3


    init {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getItemCount(): Int {
        return this.forumDatas.size
    }

    override fun getItemViewType(position: Int): Int {
        if (forumDatas[position].getType() === 0) {
            return QUE
        } else if (forumDatas[position].getType() === 1) {
            return ARTICLE
        } else if (forumDatas[position].getType() === 2) {
            return PHOTO
        } else if (forumDatas[position].getType() === 3) {
            return JOKES
        }
        return -1
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(viewGroup.context)

        when (viewType) {

            ARTICLE -> {
                val v2 = inflater.inflate(R.layout.article_card, viewGroup, false)
                viewHolder = ArticleCardHolder(v2)
            }
            PHOTO -> {
                val v3 = inflater.inflate(R.layout.feed_card, viewGroup, false)
                viewHolder = PhotoCardHolder(v3)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder.itemViewType) {

            ARTICLE -> {
                val holder1 = viewHolder as ArticleCardHolder
                holder1.title.text = forumDatas[position].getTitle()
                holder1.desc.text = forumDatas[position].getDescription()
                holder1.time.text = forumDatas[position].getTime()
                holder1.post_id.text = forumDatas[position].getId()
            }
            PHOTO -> {
                val holder2 = viewHolder as PhotoCardHolder
                holder2.title.text = forumDatas[position].getTitle()
                holder2.desc.text = forumDatas[position].getDescription()
                holder2.time.text = forumDatas[position].getTime()
                val hub = context.getSharedPreferences("com.example.ae.yourhub", Context.MODE_PRIVATE)
                holder2.comm.text = hub.getString("community", "")
                holder2.post_id.text = forumDatas[position].getId()
                holder2.url = forumDatas[position].getImage()
                Picasso.with(context).load(forumDatas[position].getImage()).resize(400, 400).centerCrop().into(holder2.imageView, object : Callback {
                    override fun onSuccess() {
                        println("loaded Image")
                    }

                    override fun onError() {
                        println("Unable to load Image")
                    }
                })
                setScaleAnimation(holder2.imageView)
            }
        }
    }

    internal inner class PhotoCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var desc: TextView
        var comment: TextView
        var post_id: TextView
        var time: TextView
        var comm: TextView
        var url = ""
        var imageView: ImageView
        var imageButton: ImageButton

        init {
            title = itemView.findViewById(R.id.title) as TextView
            desc = itemView.findViewById(R.id.description) as TextView
            comment = itemView.findViewById(R.id.comment) as TextView
            imageView = itemView.findViewById(R.id.thumbnail) as ImageView
            comm = itemView.findViewById(R.id.community) as TextView
            imageView.setOnClickListener {

            }
            imageButton = itemView.findViewById(R.id.like) as ImageButton
            post_id = itemView.findViewById(R.id.post_id) as TextView
            time = itemView.findViewById(R.id.time) as TextView

            title.setOnClickListener {

            }
            imageButton.setOnClickListener {

            }

        }
    }

    internal inner class ArticleCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var desc: TextView
        var comment: TextView
        var post_id: TextView
        var time: TextView
        var imageView: ImageView
        var imageButton: ImageButton
        var like: Boolean? = false
        var l: LinearLayout

        init {
            title = itemView.findViewById(R.id.title) as TextView
            desc = itemView.findViewById(R.id.description) as TextView
            comment = itemView.findViewById(R.id.comment) as TextView
            post_id = itemView.findViewById(R.id.post_id) as TextView
            time = itemView.findViewById(R.id.time) as TextView
            l = itemView.findViewById(R.id.article) as LinearLayout
            l.setOnClickListener {

            }
            imageView = itemView.findViewById(R.id.thumbnail) as ImageView
            imageButton = itemView.findViewById(R.id.like) as ImageButton
            imageButton.setOnClickListener {

            }


        }
    }

    private fun setScaleAnimation(view: View) {
        val anim = ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        anim.duration = FADE_DURATION.toLong()
        view.startAnimation(anim)
    }

    companion object {
        private val FADE_DURATION = 1000
    }
}
