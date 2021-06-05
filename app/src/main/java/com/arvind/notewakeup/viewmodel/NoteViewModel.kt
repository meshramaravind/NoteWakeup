package com.arvind.notewakeup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.arvind.notewakeup.model.NoteModel
import com.arvind.notewakeup.repository.NoteRepository
import com.arvind.notewakeup.storage.datastore.UIModeDataStore
import com.arvind.notewakeup.utils.DetailState
import com.arvind.notewakeup.utils.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    application: Application,
    private val noteRepository: NoteRepository
) :
    AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<ViewState>(ViewState.Loading)
    private val _detailState = MutableStateFlow<DetailState>(DetailState.Loading)

    // UI collect from this stateFlow to get the state updates
    val uiState: StateFlow<ViewState> = _uiState
    val detailState: StateFlow<DetailState> = _detailState

    private val uiModeDataStore = UIModeDataStore(application)

    // get ui mode
    val getUIMode = uiModeDataStore.uiMode

    // save ui mode
    fun saveToDataStore(isNightMode: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            uiModeDataStore.saveToDataStore(isNightMode)
        }
    }

    // insert  note
    fun insertNote(noteModel: NoteModel) = viewModelScope.launch {
        noteRepository.insert(noteModel)
    }

    // update  note
    fun updateNote(noteModel: NoteModel) = viewModelScope.launch {
        noteRepository.update(noteModel)
    }

    // delete note
    fun deleteNote(noteModel: NoteModel) = viewModelScope.launch {
        noteRepository.delete(noteModel)
    }

    //get all note
    fun getAllNote() = noteRepository.getAllNotes()

    //get all search note
    fun getAllSearchNote(query: String?) = noteRepository.getAllSearchNote(query)


}