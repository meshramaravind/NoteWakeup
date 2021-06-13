package com.arvind.notewakeup.view.add

import android.os.Bundle
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.arvind.notewakeup.R
import com.arvind.notewakeup.databinding.FragmentAddNoteBinding
import com.arvind.notewakeup.model.NoteModel
import com.arvind.notewakeup.view.base.BaseFragment
import com.arvind.notewakeup.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.content_add_note_layout.view.*
import kotlinx.android.synthetic.main.fragment_add_note.*

@AndroidEntryPoint
class AddNoteFragment : BaseFragment<FragmentAddNoteBinding, NoteViewModel>() {

    override val viewModel: NoteViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_note, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_attachfile -> {

            }

            R.id.action_done -> {
                saveNote()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveNote() = with(binding) {
        val noteTitle = addNoteLayout.etNoteTitle.text.toString().trim()
        val noteBody = addNoteLayout.etNoteBody.text.toString().trim()
        val date = addNoteLayout.tvNoteDate_addnote.text.toString().trim()

        if (noteTitle.isNotEmpty()) {
            val note = NoteModel(0, noteTitle, noteBody, date)

            viewModel.insertNote(note)
            toast(getString(R.string.success_note_saved))

            findNavController().navigate(
                R.id.action_addNoteFragment_to_dashboardFragment
            )
        } else {
            toast(getString(R.string.enternotetitle))
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAddNoteBinding.inflate(inflater, container, false)
}