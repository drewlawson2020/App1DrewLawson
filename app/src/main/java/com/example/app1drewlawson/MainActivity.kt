package com.example.app1drewlawson

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    //Variables to store data
    private var mFirstName: String? = null
    private var mMidName: String? = null
    private var mLastName: String? = null

    // UI Variables
    // Edit text vars
    private var mEtFirstName: EditText? = null
    private var mEtMidName: EditText? = null
    private var mEtLastName: EditText? = null

    // Button vars
    private var mButtonSubmit: Button? = null
    private var mButtonCamera: Button? = null

    // Store image for profile here
    private var mIvPic: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Get the buttons
        mButtonSubmit = findViewById(R.id.button_submit)
        mButtonCamera = findViewById(R.id.picture_button)
        // Set up listeners
        mButtonSubmit!!.setOnClickListener(this)
        mButtonCamera!!.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_submit -> {
                mEtFirstName = findViewById(R.id.et_fn_data)
                mEtMidName = findViewById(R.id.et_mn_data)
                mEtLastName = findViewById(R.id.et_ln_data)

                mFirstName = mEtFirstName!!.text.toString();
                mMidName = mEtMidName!!.text.toString();
                mLastName = mEtLastName!!.text.toString();
                // Check if all text is present. If it isn't, make a toast warning. If it is, send to next activity.
                if (mFirstName.isNullOrBlank() || mMidName.isNullOrBlank() || mLastName.isNullOrBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Missing data in one or all fields!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else
                {
                    var nameToSend = mFirstName + " " + mLastName;
                    val messageIntent = Intent(this, LoginPage::class.java)
                    messageIntent.putExtra("ET_STRING", nameToSend)
                    this.startActivity(messageIntent)
                }
            }
            R.id.picture_button-> {
                //Open camera activity
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try{
                    cameraActivity.launch(cameraIntent)
                }catch(ex: ActivityNotFoundException){
                    //Do error handling here
                }
            }
        }
    }
    private val cameraActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == RESULT_OK) {
            mIvPic = findViewById<View>(R.id.iv_picture_data) as ImageView
            val thumbnailImage = result.data!!.getParcelableExtra("data", Bitmap::class.java)
            mIvPic!!.setImageBitmap(thumbnailImage)
            if(isExternalStorageWritable)
            {
                saveImage(thumbnailImage)
            }
            else
            {
                Toast.makeText(this, "External storage not writable.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun saveImage(finalBitmap: Bitmap?) {
        val root = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val myDir = File("$root/saved_images")
        myDir.mkdirs()
        val fname = "Thumbnail.jpg"
        val file = File(myDir, fname)
        if (file.exists())
            file.delete()
        try
        {
            val out = FileOutputStream(file)
            finalBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
            Toast.makeText(this, "Picture saved!", Toast.LENGTH_SHORT).show()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        if(mFirstName!=null)
            outState.putString("FirstName",mFirstName)
        if(mMidName!=null)
            outState.putString("MidName",mMidName)
        if(mLastName!=null)
            outState.putString("LastName",mLastName)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.getString("FirstName") != null)
            mFirstName = savedInstanceState.getString("FirstName")
        if (savedInstanceState.getString("LastName") != null)
            mLastName = savedInstanceState.getString("LastName")
        if (savedInstanceState.getString("LastName") != null)
            mLastName = savedInstanceState.getString("LastName")
        mIvPic = findViewById<View>(R.id.iv_picture_data) as ImageView
        val root = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        mIvPic?.setImageURI(Uri.fromFile(File("$root/saved_images", "Thumbnail.jpg")));

    }
    private val isExternalStorageWritable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state
        }

}

