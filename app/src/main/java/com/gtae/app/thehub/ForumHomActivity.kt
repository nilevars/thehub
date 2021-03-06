package com.gtae.app.thehub

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import com.google.firebase.database.*
import java.util.ArrayList

class ForumHomActivity : AppCompatActivity() {

    private val mLayoutManager: RecyclerView.LayoutManager? = null
    private val forumDatas = ArrayList<ForumData>()
    internal lateinit var forumAdapter: ForumAdapter
    private var gm: StaggeredGridLayoutManager? = null
    private var mPostReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_fragment)

        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("forum");

        val recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        forumAdapter = ForumAdapter(this, forumDatas)

        gm = StaggeredGridLayoutManager(2, 1)
        recyclerView.layoutManager = gm
        recyclerView.adapter = forumAdapter

        dataFetch()
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

}
