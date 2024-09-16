package com.palash.roomdatabase_noteapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.palash.roomdatabase_noteapp.R
import com.palash.roomdatabase_noteapp.adapter.NoteListAdapter
import com.palash.roomdatabase_noteapp.databinding.FragmentHomeBinding
import com.palash.roomdatabase_noteapp.room_db.DataBaseName
import com.palash.roomdatabase_noteapp.room_db.NoteDataClass
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NoteListAdapter
    private lateinit var dataBase: DataBaseName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.title = "Home"

        dataBase = DataBaseName.getDataBase(requireContext())
        adapter = NoteListAdapter(::onNoteClicked)
        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showRecyclerView()

        //search ...........................
        binding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {
                // No implementation needed here
            }

            override fun onTextChanged(chr: CharSequence?, i: Int, i1: Int, i2: Int) {
                // Check if char sequence is not null and not empty
                chr?.let {
                    if (it.isNotEmpty()) {
                        dataBase.interfaceDao().searchRecordByTitle(it.toString())
                            .observe(viewLifecycleOwner) { item ->
                                if (item.isEmpty()){
                                    binding.recyclerView.visibility = View.GONE
                                    binding.listEmptyTxt.visibility = View.VISIBLE
                                } else{
                                    binding.listEmptyTxt.visibility = View.GONE
                                    binding.recyclerView.visibility = View.VISIBLE
                                    adapter.submitList(item)
                                }
                            }
                    } else {
                        showRecyclerView()
                    }
                }
            }

            override fun afterTextChanged(editable: Editable?) {
                // No implementation needed here
            }
        })

        //Add (fab) button click
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
        }
    }

    private fun showRecyclerView() {

        dataBase.interfaceDao().getAllRecord().observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)

        }

    }

    private fun onNoteClicked(noteItem: NoteDataClass) {
        val bundle = Bundle()
        bundle.putString("noteDetails", Gson().toJson(noteItem))
        Log.d("Hello",noteItem.createdDate)
        findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}