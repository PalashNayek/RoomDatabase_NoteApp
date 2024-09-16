package com.palash.roomdatabase_noteapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.palash.roomdatabase_noteapp.R
import com.palash.roomdatabase_noteapp.Utils
import com.palash.roomdatabase_noteapp.databinding.FragmentAddNoteBinding
import com.palash.roomdatabase_noteapp.room_db.DataBaseName
import com.palash.roomdatabase_noteapp.room_db.NoteDataClass
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AddNoteFragment : Fragment() {
    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!



    private lateinit var dataBase: DataBaseName
    var valueId: Long = 0
    private var isInsert = true

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        setIntialData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = (activity as AppCompatActivity).supportActionBar

        // Set dynamic left navigation icon
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.round_arrow_back_24)
        // Dynamically update the toolbar title
        toolbar?.title = "Add note"

        //initialize dataBase var
        dataBase = DataBaseName.getDataBase(requireContext())

        binding.tvDate.text = Utils().getCurrentDateTime("dd MMMM yyyy hh:mm a")

        /*valueId = intent.getLongExtra("id", 0)

        if(valueId>0)
        {
            isInsert=false
            fetchData(valueId)
            binding.fabDelete.visibility = View.VISIBLE
        }*/



        binding.fabSave.setOnClickListener {
            validationChecker()
        }

    }

    private fun setIntialData() {
        val jsonNote = arguments?.getString("noteDetails")
        if (jsonNote != null) {
            val noteItem = Gson().fromJson(jsonNote, NoteDataClass::class.java)
            noteItem?.let {
                binding.tvDate.text = noteItem.createdDate
                binding.edTitle.setText(noteItem.title)
                binding.edDesc.setText(noteItem.content)

                binding.fabSave.setText("")
                binding.fabSave.setText("Update")
                binding.fabSave.setOnClickListener {
                    scope.launch {
                        dataBase.interfaceDao()
                            .updateData(NoteDataClass(noteItem.id, binding.edTitle.text.toString(), binding.edDesc.text.toString(), noteItem.tags, binding.tvDate.text.toString()))
                    }
                }
            }
        }
    }

    private fun validationChecker() {
        val mDate = binding.tvDate.text.toString()
        val edTitle = binding.edTitle.text.toString()
        val tags = ""

        if (edTitle.isNotEmpty()) {
            val edDesc = binding.edDesc.text.toString()
            if (edDesc.isNotEmpty()) {
                binding.tLayoutDesc.error = null
                insertUpdateNote(edTitle, edDesc, tags, mDate)

            } else {
                binding.tLayoutDesc.error = "Please Enter Content"
                binding.tLayoutDesc.requestFocus()
                binding.tLayoutTitle.error = null
            }
        } else {
            binding.tLayoutTitle.error = "Please Enter Title"
            binding.tLayoutTitle.requestFocus()
        }
    }

    private fun insertUpdateNote(edTitle: String, edDesc: String, tags: String, mDate: String) {

        scope.launch {
            //val result = if (isInsert) {
                dataBase.interfaceDao().insertData(NoteDataClass(0, edTitle, edDesc, tags, mDate))
                    .toInt()
            //}
            /*else {
                dataBase.interfaceDao()
                    .updateData(NoteDataClass(valueId, edTitle, edDesc, tags, mDate))
            }*/

            /*if (result > 0) {
                //when we run background that time we use withContext(dispatcher)
                withContext(Dispatchers.Main) {
                    Utils().showToast(
                        requireContext(),
                        if (isInsert) "Insert Successful" else "Update Successful"
                    )
                }
            } else {
                withContext(Dispatchers.Main) {
                    Utils().showToast(
                        requireContext(),
                        if (isInsert) "Already Present" else "Not Update"
                    )
                }
            }*/
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}