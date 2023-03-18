package com.example.firebaseapp

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseapp.AdapterHolder.CardAdaptapter
import com.example.firebaseapp.dataClass.DataClass
import com.example.firebaseapp.databinding.FragmentHomeBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList


class Home : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    var fab: FloatingActionButton? = null
    lateinit var databaseReference: DatabaseReference
    lateinit var eventListener: ValueEventListener
    lateinit var recyclerView: RecyclerView
    val dataList = mutableListOf<DataClass>()
    lateinit var adapter: CardAdaptapter
    lateinit var searchView: SearchView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el diseño utilizando viewBinding
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Habilitar la visualización del menú de opciones
        setHasOptionsMenu(true)

        //id asingment of every lateinit variable
        fab = binding.fab
        recyclerView = binding.recyclerView
        searchView = binding.search
        searchView.clearFocus()

        val gridLayoutManager = GridLayoutManager(context, 1)
        recyclerView.layoutManager = gridLayoutManager

        val builder = AlertDialog.Builder(context!!)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()

        val dataList = mutableListOf<DataClass>()

        adapter = CardAdaptapter(context!!, dataList)
        recyclerView.adapter = adapter

        databaseReference = FirebaseDatabase.getInstance().getReference("Android Tutorials")
        dialog.show()
        eventListener = databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (itemSnapshot in snapshot.children) {
                    val dataClass = itemSnapshot.getValue(DataClass::class.java)
                    dataClass?.key = itemSnapshot.key
                    dataClass?.let { dataList.add(it) }
                }
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
            }
        })




        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                searchList(newText!!)
                return true
            }
        })

        fab !!. setOnClickListener {
            val intent = Intent(context, UploadActivity::class.java)
            startActivity(intent)
        }




        // Devolver la vista raíz del diseño
            return binding.root
    }

    fun searchList(text: String) {
        val searchList = ArrayList<DataClass>()
        for (dataClass in dataList) {
            if (dataClass.dataTitle!!.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                searchList.add(dataClass)
            }
        }
        adapter.searchDataList(searchList)
    }


    fun onMapActivityClick() {
        val intent = Intent(context, MapActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mapIcon -> {
                onMapActivityClick()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}

