package cgy.memoriz.share.profile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment.getExternalStorageDirectory
import android.provider.MediaStore
import android.support.v4.content.PermissionChecker
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cgy.memoriz.R
import cgy.memoriz.SharedPref
import cgy.memoriz.URLEndpoint
import cgy.memoriz.VolleySingleton
import cgy.memoriz.fragment.MainActivityBaseFragment
import cgy.memoriz.upload.Utility
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_profile_edit.view.*
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.util.*


class EditProfile : MainActivityBaseFragment() {
    private lateinit var spDialog : Dialog
    private lateinit var encodedImage : String
    private lateinit var userChosenTask : String

    private val requestCamera = 0
    private val selectFile = 1
    var thumbnail : Bitmap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_edit, container, false)

        setTitle("Edit Profile")

        spDialog = SpotsDialog.Builder()
                .setContext(context)
                .setMessage("Uploading...")
                .build()

        view.profile_photo.setOnTouchListener { _, motionEvent ->
            selectImage()
            false
        }

        view.saveProfileBtn.setOnClickListener {
//            switchFragment(StudentQSolver())

            val image = getStringImage(this.thumbnail!!)

            val imageObject = JSONObject()
            try {
                imageObject.put("size", "1000")
                imageObject.put("type", "image/jpeg")
                imageObject.put("data", image)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            updateProfile(imageObject.toString())
        }

        return view
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (userChosenTask.equals("Take Photo"))
                    cameraIntent()
                else if (userChosenTask.equals("Choose from Library"))
                    galleryIntent()
            } else {
                //code for deny
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == selectFile)
                onSelectFromGalleryResult(data)
            else if (requestCode == requestCamera)
                onCaptureImageResult(data!!)
        }
    }

    private fun onCaptureImageResult(data: Intent) {
        thumbnail = data.extras!!.get("data") as Bitmap
        val bytes = ByteArrayOutputStream()
        thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 90, bytes)

        val destination = File(getExternalStorageDirectory(),
                System.currentTimeMillis().toString() + ".jpg")

        val fo: FileOutputStream
        try {
            destination.createNewFile()
            fo = FileOutputStream(destination)
            fo.write(bytes.toByteArray())
            fo.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        view!!.profile_photo!!.setImageBitmap(thumbnail)
    }

    private fun onSelectFromGalleryResult(data: Intent?) {
        if (data != null) {
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(context!!.contentResolver, data.data)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        view!!.profile_photo!!.setImageBitmap(thumbnail)
    }

    private fun updateProfile(image: String) {
        showDialog()

        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlUploadImage,
                Response.Listener<String> { response ->
                    try {
                        hideDialog()
//                      get the feedback message from the php and show it on the app by using Toast
                        Log.d("response", response)
                        val obj = JSONObject(response)
                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()

                        if (obj.getString("message") == "Profile photo upload successfully") {
//                            super.getBaseActivity()!!.onBackPressed()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { volleyError -> Toast.makeText(context, volleyError.message, Toast.LENGTH_LONG).show() }) {

            //          pack the registration info to POST it
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()

                params["image"] = image
                params["email"] = SharedPref.userEmail
                Log.e("image string", image)

                return params
            }
        }
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }

    private fun selectImage() {
        val items = arrayOf<CharSequence>("Take Photo", "Choose from Library", "Cancel")

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Add Photo!")
        builder.setItems(items) { dialog, item ->
            val result = Utility.checkPermission(context!!)

            if (items[item] == "Take Photo") {
                userChosenTask = "Take Photo"

                if (PermissionChecker.checkSelfPermission(context!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    requestPermissions(arrayOf(Manifest.permission.CAMERA), requestCamera)
                else
                    cameraIntent()
            }
            else if (items[item] == "Choose from Library") {
                userChosenTask = "Choose from Library"
                if (result)
                    galleryIntent()

            }
            else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun getStringImage(bmp: Bitmap): String {
        try {
            val baos = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, 60, baos)
            val imageBytes = baos.toByteArray()
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT)
            return encodedImage
        } catch (e: Exception) {

        }
        return encodedImage
    }

    private fun galleryIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select File"), selectFile)
    }

    private fun cameraIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, requestCamera)
    }



    private fun showDialog() {
        if (!spDialog.isShowing)
            spDialog.show()
    }

    private fun hideDialog() {
        if (spDialog.isShowing)
            spDialog.dismiss()
    }
}