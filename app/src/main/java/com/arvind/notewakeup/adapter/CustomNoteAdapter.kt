package com.arvind.notewakeup.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arvind.notewakeup.databinding.ItemsNoteBinding
import com.arvind.notewakeup.model.NoteModel
import com.arvind.notewakeup.view.dashboard.DashboardFragmentDirections
import java.util.*

class CustomNoteAdapter : RecyclerView.Adapter<CustomNoteAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            ItemsNoteBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val noteModel = differ.currentList[position]
        holder.bind(noteModel)
    }

    inner class NoteViewHolder(val itemsNoteBinding: ItemsNoteBinding) :
        RecyclerView.ViewHolder(itemsNoteBinding.root) {
        fun bind(noteModel: NoteModel) {
            itemsNoteBinding.apply {
                itemsNoteBinding.note = noteModel
                itemsNoteBinding.executePendingBindings()


                tvNoteDate.text = noteModel.createdAtDateFormat
                tvNoteDate.visibility = View.VISIBLE

                val random = Random()
                val color =
                    Color.argb(
                        255, random.nextInt(256),
                        random.nextInt(256), random.nextInt(256)
                    )
                ibColor.setBackgroundColor(color)



                root.setOnClickListener { v ->
                    val direction = DashboardFragmentDirections
                        .actionDashboardFragmentToUpdateNoteFragment(noteModel)
                    v.findNavController().navigate(direction)
                }

            }
        }
    }

    private val differCallback =
        object : DiffUtil.ItemCallback<NoteModel>() {
            override fun areItemsTheSame(oldItem: NoteModel, newItem: NoteModel): Boolean {
                return oldItem.id == newItem.id &&
                        oldItem.noteBody == newItem.noteBody &&
                        oldItem.noteTitle == newItem.noteTitle
            }

            override fun areContentsTheSame(oldItem: NoteModel, newItem: NoteModel): Boolean {
                return oldItem == newItem
            }

        }

    val differ = AsyncListDiffer(this, differCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}


