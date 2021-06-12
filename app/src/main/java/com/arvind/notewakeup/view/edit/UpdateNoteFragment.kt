package com.arvind.notewakeup.view.edit

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.arvind.notewakeup.R
import com.arvind.notewakeup.databinding.FragmentUpdateNoteBinding
import com.arvind.notewakeup.model.NoteModel
import com.arvind.notewakeup.utils.hide
import com.arvind.notewakeup.utils.saveBitmap
import com.arvind.notewakeup.utils.show
import com.arvind.notewakeup.view.base.BaseFragment
import com.arvind.notewakeup.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateNoteFragment : BaseFragment<FragmentUpdateNoteBinding, NoteViewModel>() {
    private val args: UpdateNoteFragmentArgs by navArgs()
    override val viewModel: NoteViewModel by activityViewModels()
    private lateinit var noteModel: NoteModel


    // handle permission dialog
    private val requestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) shareImage()
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteModel = args.noteModel!!
        getdetailsnote()

    }

    private fun getdetailsnote() = with(binding) {
        binding.etNoteBodyUpdate.setText(noteModel.noteBody)
        binding.etNoteTitleUpdate.setText(noteModel.noteTitle)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_update_note, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_note_update -> {
                updateNote()
            }
            R.id.action_delete -> {
                deletenote()
            }

            R.id.action_share_text_update -> shareText()
            R.id.action_share_image_update -> shareImage()

        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareImage() {
        if (!isStoragePermissionGranted()) {
            requestLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return
        }

        // unHide the app logo and name
        showAppNameAndLogo()
        val imageURI = binding.detailView.drawToBitmap().let { bitmap ->
            hideAppNameAndLogo()
            saveBitmap(requireActivity(), bitmap)
        } ?: run {
            toast("Error occurred!")
            return
        }

        val intent = ShareCompat.IntentBuilder(requireActivity())
            .setType("image/jpeg")
            .setStream(imageURI)
            .intent

        startActivity(Intent.createChooser(intent, null))
    }

    private fun showAppNameAndLogo() = with(binding) {
        appIconForShare.show()
        appNameForShare.show()
    }

    private fun hideAppNameAndLogo() = with(binding) {
        appIconForShare.hide()
        appNameForShare.hide()
    }

    private fun isStoragePermissionGranted(): Boolean = ContextCompat.checkSelfPermission(
        requireContext(),
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    private fun deletenote() {

        AlertDialog.Builder(requireContext()).apply {
            setTitle("Delete Note")
            setMessage("Are you sure you want to permanently delete this note?")
            setPositiveButton("DELETE") { _, _ ->
                viewModel.deleteNote(noteModel)
                view?.findNavController()?.navigate(
                    R.id.action_updateNoteFragment_to_dashboardFragment
                )
            }
            setNegativeButton("CANCEL", null)
        }.create().show()

    }


    @SuppressLint("StringFormatMatches")
    private fun shareText() = with(binding) {
        val shareMsg = getString(
            R.string.share_message,
            binding.etNoteTitleUpdate.text.toString(),
            binding.etNoteBodyUpdate.text.toString(),
        )

        val intent = ShareCompat.IntentBuilder(requireActivity())
            .setType("text/plain")
            .setText(shareMsg)
            .intent

        startActivity(Intent.createChooser(intent, null))
    }

    private fun updateNote() {
        val title = binding.etNoteTitleUpdate.text.toString().trim()
        val body = binding.etNoteBodyUpdate.text.toString().trim()
        val date = binding.tvNoteDateUpdatenote.text.toString().trim()

        if (title.isNotEmpty()) {
            val note = NoteModel(noteModel.id, title, body, date)
            viewModel.updateNote(note)

            toast(getString(R.string.success_note_update))

            findNavController().navigate(
                R.id.action_updateNoteFragment_to_dashboardFragment
            )
        } else {
            toast(getString(R.string.enternotetitle))
        }


    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentUpdateNoteBinding.inflate(inflater, container, false)
}