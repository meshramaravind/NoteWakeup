package com.arvind.notewakeup.utils

import com.arvind.notewakeup.model.NoteModel

sealed class DetailState {
    object Loading : DetailState()
    object Empty : DetailState()
    data class Success(val noteModel: NoteModel) : DetailState()
    data class Error(val exception: Throwable) : DetailState()
}
