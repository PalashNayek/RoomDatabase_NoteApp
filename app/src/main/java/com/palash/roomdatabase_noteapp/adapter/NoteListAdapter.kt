package com.palash.roomdatabase_noteapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.palash.roomdatabase_noteapp.databinding.ItemCardBinding
import com.palash.roomdatabase_noteapp.room_db.NoteDataClass

class NoteListAdapter(private val onNoteClicked: (NoteDataClass) -> Unit) :
    ListAdapter<NoteDataClass, NoteListAdapter.NoteViewHolder>(DiffUtilsCallback()) {

    inner class NoteViewHolder(private val binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NoteDataClass) {
            binding.tvTitle.text = item.title
            binding.tvDesc.text = item.content
            binding.tvDateTime.text = item.createdDate

            binding.root.setOnClickListener {
                onNoteClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class DiffUtilsCallback : DiffUtil.ItemCallback<NoteDataClass>() {
        override fun areItemsTheSame(oldItem: NoteDataClass, newItem: NoteDataClass): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteDataClass, newItem: NoteDataClass): Boolean {
            return oldItem == newItem
        }

    }
}