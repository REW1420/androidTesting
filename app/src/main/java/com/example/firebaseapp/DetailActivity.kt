package com.example.firebaseapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.firebaseapp.databinding.ActivityDetailBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class DetailActivity : AppCompatActivity() {


    private lateinit var binding: ActivityDetailBinding

    var detailImage: ImageView? = null
    var detailTitle: TextView? = null
    var detailDesc: TextView? = null
    var detailLang: TextView? = null


    var key: String? = null
    var imageUrl: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        detailDesc = findViewById(R.id.detailDesc);
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);

        detailLang = findViewById(R.id.detailLang);

        //extras extraction
        val bundle = intent.extras
        if (bundle != null) {
            detailDesc!!.text = (bundle.getString("Description") as String)
            detailTitle!!.text = bundle.getString("Title").toString()
            detailLang!!.text = bundle.getString("Language") as String
            key = bundle.getString("Key").toString()
            imageUrl = bundle.getString("Image").toString()

            Glide.with(this).load(imageUrl).into(detailImage!!)


        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.actionDelete -> {
                onDeleteClick()
                true
            }
            R.id.actionEdit -> {
                onEditClick()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    private fun onDeleteAction() {
        val reference = FirebaseDatabase.getInstance().getReference("Android Tutorials")
        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.getReferenceFromUrl(imageUrl!!)
        storageReference.delete().addOnSuccessListener {
            reference.child(key!!).removeValue()
            Toast.makeText(this@DetailActivity, "Deleted", Toast.LENGTH_SHORT).show()
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }

    private fun onDeleteClick() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alerta")
            .setMessage("Esta seguroo de eliminar el elemento")
            .setPositiveButton("Aceptar") { _, _ ->
                onDeleteAction()
            }
            .setNegativeButton("Cancelar") { _, _ ->
                return@setNegativeButton
            }
        val alertDialog = builder.create()
        alertDialog.show()

    }

    private fun onEditClick() {
        val intent = Intent(this, UpdateActivity::class.java)
        intent.putExtra("Title", detailTitle!!.text.toString())
        intent.putExtra("Description", detailDesc!!.text.toString())
        intent.putExtra("Language",detailLang!!.text.toString())
        intent.putExtra("Image",imageUrl)
        intent.putExtra("Key",key)
        startActivity(intent)
    }
}