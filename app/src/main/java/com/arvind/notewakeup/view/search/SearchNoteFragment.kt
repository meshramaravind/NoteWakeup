package com.arvind.notewakeup.view.search

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.arvind.notewakeup.R
import com.arvind.notewakeup.adapter.CustomNoteAdapter
import com.arvind.notewakeup.databinding.FragmentSearchNoteBinding
import com.arvind.notewakeup.view.base.BaseFragment
import com.arvind.notewakeup.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_search_note.*

@AndroidEntryPoint
class SearchNoteFragment : BaseFragment<FragmentSearchNoteBinding, NoteViewModel>(),
    SearchView.OnQueryTextListener {

    override val viewModel: NoteViewModel by activityViewModels()
    private lateinit var customNoteAdapter: CustomNoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRV()

    }

    private fun setupRV() {
        customNoteAdapter = CustomNoteAdapter()
        rv_note_search.apply {
            setHasFixedSize(true)
            adapter = customNoteAdapter

            viewModel.getAllNote().observe(viewLifecycleOwner, { note ->
                customNoteAdapter.differ.submitList(note)
            })

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_search, menu)
        val mMenuSearch = menu.findItem(R.id.action_menu_search).actionView as SearchView
        mMenuSearch.isSubmitButtonEnabled = false
        mMenuSearch.setOnQueryTextListener(this)

    }


    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSearchNoteBinding.inflate(inflater, container, false)

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchNote(newText)
        }
        return true
    }

    private fun searchNote(query: String) {
        val searchQuery = "%$query%"
        viewModel.getAllSearchNote(searchQuery).observe(
            this, { list ->
                customNoteAdapter.differ.submitList(list)
            }
        )

    }
}