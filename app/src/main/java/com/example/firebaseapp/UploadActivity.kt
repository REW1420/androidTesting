package com.example.firebaseapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseapp.dataClass.DataClass
import com.example.firebaseapp.databinding.ActivityUploadBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.DateFormat
import java.util.*


class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding

    var uploadImage: ImageView? = null
    var saveButton: Button? = null
    var uploadTopic: EditText? = null
    var uploadDesc: EditText? = null
    var uploadLang: EditText? = null
    var imageURL: String? = null
    var uri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uploadImage = binding.uploadImage
        saveButton = binding.btnUpload
        uploadTopic = binding.uploadTopic
        uploadDesc = binding.uploadDesc
        uploadLang = binding.uploadLang


        val activityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                uri = data?.data
                uploadImage!!.setImageURI(uri)
            } else {
                Toast.makeText(this, "No se a selecionnado una imagen", Toast.LENGTH_SHORT).show()
            }
        }

        uploadImage!!.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }

        saveButton!!.setOnClickListener {
            saveData()
        }

    }

    private fun saveData() {
        val storageReference =
            uri!!.lastPathSegment?.let {
                FirebaseStorage.getInstance().reference.child("Android Images")
                    .child(it)
            }
        val builder = AlertDialog.Builder(this@UploadActivity)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()
        storageReference!!.putFile(uri!!).addOnSuccessListener { taskSnapshot ->
            val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
            while (!uriTask.isComplete);
            val urlImage = uriTask.result
            imageURL = urlImage.toString()
            uploadData()
            dialog.dismiss()
        }.addOnFailureListener { e ->
            dialog.dismiss()
        }
    }

    fun uploadData() {
        val title = uploadTopic?.text.toString()
        val desc = uploadDesc?.text.toString()
        val lang = uploadLang?.text.toString()
        val dataClass = DataClass(title, desc, lang, imageURL)
        //We are changing the child from title to currentDate,
        // because we will be updating title as well and it may affect child value.
        val currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)
        FirebaseDatabase.getInstance().getReference("Android Tutorials").child(currentDate)
            .setValue(dataClass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@UploadActivity, "Saved", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@UploadActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }



}

