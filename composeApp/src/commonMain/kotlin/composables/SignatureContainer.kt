package composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.preat.peekaboo.image.picker.toImageBitmap

@Composable
fun SignatureContainer() {
    val state = rememberSignatureState()
    val signatureByteArray by rememberSaveable { mutableStateOf<ByteArray?>(null) }
    Column(){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .border(
                    BorderStroke(
                        width = 0.5.dp,
                        color = MaterialTheme.colorScheme.primary,
                    ),
                    shape = RoundedCornerShape(8.dp),
                )
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        val signatureLine = SignatureLine(
                            start = change.position - dragAmount,
                            end = change.position,
                        )
                        state.addSignatureLine(signatureLine)
                    }
                }
        ) {
            androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) { // Draw all the signature lines in real time
                state.signatureLines.forEach { line ->
                    drawLine(
                        color = Color.Black,
                        start = line.start,
                        end = line.end,
                        strokeWidth = 4.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }
        }
        //image preview
        signatureByteArray?.let { byteArray ->
            val bitmap = byteArray.toImageBitmap()
            Image(
                bitmap = bitmap,
                contentDescription = "Signature Preview",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .border(
                        BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                        shape = RoundedCornerShape(8.dp)
                    )
            )
        }
    }
}

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

@Composable
fun rememberSignatureState(): SignatureState {
    return rememberSaveable(saver = SignatureState.Saver) {
        SignatureState()
    }
}

@Immutable
data class SignatureLine(
    val start: Offset,
    val end: Offset,
)


