package com.gtae.app.thehub

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.R.attr.name
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_community.*


class CommunityActivity : AppCompatActivity() {

    private var mDatabase: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        submit.setOnClickListener {
            var name = cname.text.toString()
            if(name.equals(""))
            {
                Toast.makeText(this@CommunityActivity, "Please Enter Community Name", Toast.LENGTH_LONG).show()
            }
            else
            {
                mDatabase = FirebaseDatabase.getInstance().getReference()
                mAuth= FirebaseAuth.getInstance()
                val user = mAuth!!.getCurrentUser()
                writeNewUser(user!!.uid,name ,user!!.email);
            }
        }


    }

    private fun writeNewUser(userId: String, name: String,admin :String?) {
        var communityData = CommunityData(userId,name,false,false)
        var ref=mDatabase!!.child("community").push().setValue(communityData)
        if(ref!=null)
        {
            Toast.makeText(this@CommunityActivity, "Created", Toast.LENGTH_LONG).show()
            cname.setText("")

        }

        //mDatabase!!.child("community").child(userId).setValue(communityData)
    }
}
