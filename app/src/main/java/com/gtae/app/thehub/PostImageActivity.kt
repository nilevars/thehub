package com.gtae.app.thehub

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class PostImageActivity : AppCompatActivity() {

    internal var type = 0
    internal var PICK_IMAGE = 10
    internal var PICK_CAMERA = 5
    lateinit var imageView: ImageView
    lateinit var gallery: ImageButton
    lateinit var camera: ImageButton
    lateinit var title: EditText
    lateinit var desc: EditText
    lateinit var linearLayout: LinearLayout
    lateinit var progressDialog: ProgressDialog
    internal var pathImage = " "
    private val output: File? = null
    lateinit var mCurrentPhotoPath: String
    lateinit var imageFileName: String
    internal var photoFile: File? = null
    var storageRef : StorageReference? = null
    var storage : FirebaseStorage? = null


    /***Permission Requests */
    /*----------------------*/
    //permission is automatically granted on sdk<23 upon installation
    val isStoragePermissionGranted: Boolean
        get() {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Log.v("PERMISSION", "Permission is granted")
                    return true
                } else {

                    Log.v("PERMISSION", "Permission is revoked")
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 23)
                    return false
                }
            } else {
                Log.v("PERMISSION", "Permission is granted")
                return true
            }
        }
    //permission is automatically granted on sdk<23 upon installation
    val isMediaPermissionGranted: Boolean
        get() {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Log.v("PERMISSION", "Permission is granted")
                    return true
                } else {

                    Log.v("PERMISSION", "Permission is revoked")
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 24)
                    return false
                }
            } else {
                Log.v("PERMISSION", "Permission is granted")
                return true
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_image)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.write)
        val upArrow = resources.getDrawable(R.drawable.ic_clear)
        upArrow.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP)
        toolbar.navigationIcon = upArrow
        toolbar.setNavigationOnClickListener { onBackPressed() }



        // Create a storage reference from our app
        storage = FirebaseStorage.getInstance()
         storageRef = storage!!.getReference();


        title = findViewById(R.id.title) as EditText
        desc = findViewById(R.id.desc) as EditText
        linearLayout = findViewById(R.id.l1) as LinearLayout
        imageView = findViewById(R.id.photo) as ImageView



        gallery = findViewById(R.id.gallery) as ImageButton
        gallery.setOnClickListener {
            if (isMediaPermissionGranted) {
                accessGallery()
            }
        }
        camera = findViewById(R.id.camera) as ImageButton
        camera.setOnClickListener {
            if (isStoragePermissionGranted) {
                takepic()
            }
        }
        progressDialog = ProgressDialog(this, ProgressDialog.STYLE_SPINNER)
    }

    /***Option Menu Creation and Actions */
    /*-----------------------------------*/

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_answer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.done) {
            progressDialog.isIndeterminate = true
            progressDialog.setTitle("Uploading....")
            progressDialog.show()
            val t = title.text.toString()
            val d = desc.text.toString()
            if(!t.equals(""))
            {
                val image = (imageView.drawable as BitmapDrawable).bitmap
                val byteArrayOutputStream = ByteArrayOutputStream()
                image.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream)
                val encodeImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)

                var data = byteArrayOutputStream .toByteArray();

                val tsLong = System.currentTimeMillis() / 1000
                var timestamp = tsLong.toString()


                var mAuth= FirebaseAuth.getInstance()
                val user = mAuth!!.getCurrentUser()
                var uid = user!!.uid
                var filename="IMG _"+ timestamp + ".png"
                // Create a reference to "mountains.jpg"
                var mountainsRef = storageRef!!.child("forum/"+uid+"/"+filename)



                var uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener {
                    Log.i("status","Failed")
                    Toast.makeText(this@PostImageActivity,"Failed",Toast.LENGTH_LONG).show()
                }
                uploadTask.addOnSuccessListener {
                    Log.i("status","Success")
                    Toast.makeText(this@PostImageActivity,"Success",Toast.LENGTH_LONG).show()
                    progressDialog.dismiss()
                }
            }
            else
            {
                Toast.makeText(this@PostImageActivity,"Title cannot be blank",Toast.LENGTH_LONG).show()
            }


           
        }
        return super.onOptionsItemSelected(item)
    }


    /***Gallery Access */
    /*------------------*/
    fun accessGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE)
    }

    /***Take Photo Code */
    /*----------------------*/
    fun takepic() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go

            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {

            }

            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(this@PostImageActivity,
                        "com.gtae.app.thehub.fileprovider",
                        photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, PICK_CAMERA)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )
        Log.e("hi", "im here")
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        Log.e("path:", mCurrentPhotoPath)
        return image
    }

    /***Intent Request Returns */
    /*---------------------------*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            val selectedImageURI = data.data
            Picasso.with(this).load(selectedImageURI).resize(400, 400).centerCrop().into(imageView, object : Callback {
                override fun onSuccess() {
                    imageView.visibility = View.VISIBLE
                    linearLayout.visibility = View.GONE
                    println("loaded Image")
                }

                override fun onError() {
                    println("Unable to load Image")
                }
            })

        }
        if (requestCode == PICK_CAMERA && resultCode == RESULT_OK) {


            Picasso.with(this).load(File(mCurrentPhotoPath)).resize(400, 400).centerCrop().into(imageView, object : Callback {
                override fun onSuccess() {
                    imageView.visibility = View.VISIBLE
                    linearLayout.visibility = View.GONE
                    println("loaded Image")
                    Log.e("picasso", "loaded")
                }

                override fun onError() {
                    Log.e("picasso", "not loaded")
                    println("Unable to load Image")
                }
            })
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 23 && grantResults != null) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Log.v("PERMISSION", "Permission is granted")
                    takepic()
                }
            }
        }
        if (requestCode == 24 && grantResults != null) {
            if (Build.VERSION.SDK_INT >= 24) {
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Log.v("Media PERMISSION", "Permission is granted")
                    accessGallery()
                }
            }
        }
    }


}