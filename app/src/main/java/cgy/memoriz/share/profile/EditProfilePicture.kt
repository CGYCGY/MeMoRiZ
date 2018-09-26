package cgy.memoriz.share.profile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment.getExternalStorageDirectory
import android.provider.MediaStore
import android.support.v4.content.PermissionChecker
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import cgy.memoriz.R
import cgy.memoriz.SharedPref
import cgy.memoriz.URLEndpoint
import cgy.memoriz.VolleySingleton
import cgy.memoriz.fragment.MainActivityBaseFragment
import cgy.memoriz.others.SDialog
import cgy.memoriz.others.rotate
import cgy.memoriz.upload.Utility
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile_photo_edit.view.*
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.util.*


class EditProfilePicture : MainActivityBaseFragment() {
    private lateinit var uploadingDialog : SDialog
    private lateinit var encodedImage : String
    private lateinit var userChosenTask : String

    private val requestCamera = 0
    private val selectFile = 1
    var thumbnail : Bitmap? = null

    fun newInstance(file: File): EditProfilePicture{
        val args = Bundle()
        args.putSerializable("file", file)
        val fragment = EditProfilePicture()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_photo_edit, container, false)

        setTitle("Edit Profile Picture")

        uploadingDialog = SDialog("Uploading...", context!!)

        val bundle = arguments
        if (bundle != null) {
            val file = bundle.getSerializable("file") as File
            val loadingDialog = SDialog("Uploading...", context!!)

            if (file.exists()) {
                val uri = Uri.fromFile(file)

                loadingDialog.showDialog()

                //load image from phone storage
                Picasso.get()
                        .load(uri)
                        .into(view!!.profile_edit_photo)

                loadingDialog.hideDialog()
            }
        }

        view.profile_edit_photo.setOnTouchListener { _, _ ->
            selectImage()
            false
        }

        view.saveProfilePhotoBtn.setOnClickListener {

            if (thumbnail == null) {
                Toast.makeText(context, "No changes detected", Toast.LENGTH_LONG).show()
            }
            else {
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
        }

        view!!.rotateProfilePhotoBtn.visibility = GONE

        view.rotateProfilePhotoBtn.setOnClickListener {
            thumbnail = thumbnail!!.rotate(90.toFloat())
            view.profile_edit_photo.setImageBitmap(thumbnail)
        }

        return view
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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

        view!!.rotateProfilePhotoBtn.visibility = VISIBLE
        view!!.profile_edit_photo!!.setImageBitmap(thumbnail)
    }

    private fun onSelectFromGalleryResult(data: Intent?) {
        if (data != null) {
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(context!!.contentResolver, data.data)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        view!!.rotateProfilePhotoBtn.visibility = VISIBLE
        view!!.profile_edit_photo!!.setImageBitmap(thumbnail)
    }

    private fun updateProfile(image: String) {
        uploadingDialog.showDialog()

        val stringRequest = object : StringRequest(Request.Method.POST, URLEndpoint.urlUploadImage,
                Response.Listener<String> { response ->
                    try {
                        uploadingDialog.hideDialog()
//                      get the feedback message from the php and show it on the app by using Toast
                        Log.d("response", response)
                        val obj = JSONObject(response)
                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()

                        if (obj.getString("message") == "Profile photo upload successfully") {
                            getBaseActivity()!!.onBackPressed()
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
}