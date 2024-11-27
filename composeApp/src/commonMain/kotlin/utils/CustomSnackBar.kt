package utils

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals


class CustomSnackBar(override val message: String, val isError: Boolean) :
    SnackbarVisuals {
    override val actionLabel: String
        get() = if (isError) "Error" else "OK"

    override val withDismissAction: Boolean
        get() = false

    override val duration: SnackbarDuration
        get() = SnackbarDuration.Short
}