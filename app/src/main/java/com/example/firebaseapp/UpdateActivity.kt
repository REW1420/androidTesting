package com.example.firebaseapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.firebaseapp.dataClass.DataClass
import com.example.firebaseapp.databinding.ActivityUpdateBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UpdateActivity : AppCompatActivity() {


    private lateinit var binding: ActivityUpdateBinding

    lateinit var updateImage: ImageView
    lateinit var updateButton: Button
    lateinit var updateDesc: EditText
    lateinit var updateTitle: EditText
    lateinit var updateLang: EditText
    var title: String? = null
    var desc: String? = null
    var lang: String? = null
    var imageUrl: String? = null
    var key: String? = null
    var oldImageURL: String? = null
    lateinit var uri: Uri


    lateinit var databaseReference: DatabaseReference
    lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)


        updateImage = binding.uploadimg
        updateButton = binding.updateButton
        updateTitle = binding.updateTitle
        updateDesc = binding.updateDesc
        updateLang = binding.updateLang

        val activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                uri = data?.data!!
                updateImage.setImageURI(uri)
            } else {
                Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show()
            }
        }

        var bundle = intent.extras
        if (bundle != null) {

            updateDesc.text =
                Editable.Factory.getInstance().newEditable(bundle.getString("Description"))
            updateTitle.text =
                Editable.Factory.getInstance().newEditable(bundle.getString("Title"))
            updateLang.text =
                Editable.Factory.getInstance().newEditable(bundle.getString("Language"))
            key = bundle.getString("Key").toString()
            oldImageURL = bundle.getString("Image").toString()

            Glide.with(this@UpdateActivity)
                .load(bundle.getString("Image"))
                .into(updateImage)


        }

        databaseReference =
            FirebaseDatabase.getInstance().getReference("Android Tutorials").child(key!!)

        updateImage.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"

            activityResultLauncher.launch(photoPicker)
        }

        updateButton.setOnClickListener {
            saveData()
           val i = Intent(this,MainActivity::class.java)
            startActivity(i)
        }

    }

    private fun saveData() {
        storageReference = FirebaseStorage.getInstance().reference.child("Android Images")
            .child(uri.lastPathSegment!!)
        val builder = AlertDialog.Builder(this@UpdateActivity)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()
        storageReference.putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isComplete);
                val urlImage = uriTask.result
                imageUrl = urlImage.toString()
                updateData()
                dialog.dismiss()
            }
            .addOnFailureListener {
                dialog.dismiss()
            }
    }

    private fun updateData() {
        title = updateTitle.text.toString().trim()
        desc = updateDesc.text.toString().trim()
        lang = updateLang.text.toString()
        val dataClass = DataClass(title, desc, lang, imageUrl)
        databaseReference.setValue(dataClass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL!!)
                    reference.delete()
                    Toast.makeText(this@UpdateActivity, "Updated", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@UpdateActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }


}