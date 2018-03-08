package com.gtae.app.thehub

/**
 * Created by A E on 06-Mar-18.
 */
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.google.firebase.database.*

import java.util.ArrayList

class ForumHomePage : Fragment() {

    lateinit internal var context: Context
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private val forumDatas = ArrayList<ForumData>()
    internal lateinit var forumAdapter: ForumAdapter
    private var gm: StaggeredGridLayoutManager? = null
    private var mPostReference: DatabaseReference? = null




    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        context = activity

        val view = inflater!!.inflate(R.layout.photo_fragment, container, false)
        val recyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        recyclerView.layoutManager = GridLayoutManager(context, 2)
        forumAdapter = ForumAdapter(activity, forumDatas)
        recyclerView.adapter = forumAdapter

        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("forum")
        dataFetch()

        return view
    }
    internal fun dataFetch() {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                Log.i("xx", "Count : "+dataSnapshot.childrenCount)
                for(snapshot in dataSnapshot.children)
                {
                    val forumData  = snapshot.getValue<ForumData>(ForumData::class.java)
                    forumDatas.add(forumData!!)
                    //val post = dataSnapshot.getValue(CommunityData::class.java)
                    Log.i("xx", "Data : "+forumData!!.title)
                }
                forumAdapter.notifyDataSetChanged()

                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("xx", "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }
        mPostReference!!.addValueEventListener(postListener)

    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.title = "Forum"
    }
}