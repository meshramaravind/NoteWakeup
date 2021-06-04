package com.arvind.notewakeup.utils

import com.arvind.notewakeup.model.NoteModel

sealed class ViewState {
    object Loading : ViewState()
    object Empty : ViewState()
    data class Success(val notemodel: List<NoteModel>) : ViewState()
    data class Error(val exception: Throwable) : ViewState()
}
