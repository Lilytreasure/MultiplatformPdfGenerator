package utils

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap

@Stable
class SignatureState {
    private val _signatureLines = mutableStateListOf<SignatureLine>()
    val signatureLines: List<SignatureLine> get() = _signatureLines.toList()

    private val _signature = mutableStateOf<ImageBitmap?>(null)
    val signature: ImageBitmap? get() = _signature.value

    fun addSignatureLine(signatureLine: SignatureLine) {
        _signatureLines.add(signatureLine)
    }

    fun clearSignatureLines() {
        _signatureLines.clear()
    }

    fun updateSignature(bitmap: ImageBitmap) {
        _signature.value = bitmap
    }

    companion object {
        val Saver: Saver<SignatureState, *> = Saver(
            save = {
                it.signatureLines to it.signature
            },
            restore = {
                SignatureState().apply {
                    _signatureLines.addAll(it.first)
                    _signature.value = it.second
                }
            },
        )
    }
}

@Immutable
data class SignatureLine(
    val start: Offset,
    val end: Offset,
)