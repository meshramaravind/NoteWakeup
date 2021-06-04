package com.arvind.notewakeup.view.dashboard

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.arvind.notewakeup.R
import com.arvind.notewakeup.adapter.CustomNoteAdapter
import com.arvind.notewakeup.databinding.FragmentDashboardBinding
import com.arvind.notewakeup.model.NoteModel
import com.arvind.notewakeup.utils.ViewState
import com.arvind.notewakeup.utils.hide
import com.arvind.notewakeup.utils.show
import com.arvind.notewakeup.view.base.BaseFragment
import com.arvind.notewakeup.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import java.lang.Math.abs

@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding, NoteViewModel>() {
    private lateinit var customNoteAdapter: CustomNoteAdapter
    override val viewModel: NoteViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRV()
        inits()
        observeNotes()
    }

    private fun setupRV() {
        customNoteAdapter = CustomNoteAdapter()
        rv_notedashboard.apply {
            setHasFixedSize(true)
            adapter = customNoteAdapter

                viewModel.getAllNote().observe(viewLifecycleOwner, { note ->
                    customNoteAdapter.differ.submitList(note)

                })

        }
    }

    private fun observeNotes() = lifecycleScope.launchWhenCreated {
        viewModel.uiState.collect { uiState ->
            when (uiState) {
                is ViewState.Loading -> {
                }
                is ViewState.Success -> {
                    showAllViews()
                    onNoteLoaded(uiState.notemodel)

                }
                is ViewState.Error -> {
                    toast("Error")
                }
                is ViewState.Empty -> {
                    hideAllViews()
                }
            }
        }
    }

    private fun onNoteLoaded(notemodel: List<NoteModel>) {
        customNoteAdapter.differ.submitList(notemodel)

    }

    private fun showAllViews() = with(binding) {
        dashboardGroup.show()
        rvNotedashboard.show()
    }

    private fun hideAllViews() = with(binding) {
        dashboardGroup.hide()
    }

    private fun inits() = with(binding) {
        btnAddNewnote.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_addNoteFragment)
        }

        mainDashboardScrollview.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { _, sX, sY, oX, oY ->
                if (abs(sY - oY) > 10) {
                    when {
                        sY > oY -> btnAddNewnote.hide()
                        oY > sY -> btnAddNewnote.show()
                    }
                }
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        lifecycleScope.launchWhenStarted {
            val isChecked = viewModel.getUIMode.first()
            val uiMode = menu.findItem(R.id.action_night_mode)
            uiMode.isChecked = isChecked
            setUIMode(uiMode, isChecked)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        return when (item.itemId) {
            R.id.action_night_mode -> {
                item.isChecked = !item.isChecked
                setUIMode(item, item.isChecked)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUIMode(item: MenuItem, isChecked: Boolean) {
        if (isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            viewModel.saveToDataStore(true)
            item.setIcon(R.drawable.ic_night)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            viewModel.saveToDataStore(false)
            item.setIcon(R.drawable.ic_day)
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDashboardBinding.inflate(inflater, container, false)

}