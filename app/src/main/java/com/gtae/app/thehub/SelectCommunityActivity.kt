package com.gtae.app.thehub

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import com.google.firebase.database.*

import java.util.ArrayList


class SelectCommunityActivity : AppCompatActivity() {

   private val mLayoutManager: RecyclerView.LayoutManager? = null
   private val communityDatas = ArrayList<CommunityData>()
   internal lateinit var communityAdapter: CommunityAdapter
    private var gm: StaggeredGridLayoutManager? = null
    private var mPostReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_fragment)

        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("community");

        val recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        communityAdapter = CommunityAdapter(this, communityDatas)

        gm = StaggeredGridLayoutManager(2, 1)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = communityAdapter

        dataFetch()
    }

    internal fun dataFetch() {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                Log.i("xx", "Count : "+dataSnapshot.childrenCount)
                for(snapshot in dataSnapshot.children)
                {
                    val communityData  = snapshot.getValue<CommunityData>(CommunityData::class.java)
                    communityDatas.add(communityData!!)
                    //val post = dataSnapshot.getValue(CommunityData::class.java)
                     Log.i("xx", "Data : "+communityData!!.name)
                }
                communityAdapter.notifyDataSetChanged()

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

