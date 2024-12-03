package composables

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import utils.SignatureLine
import utils.SignatureState
import utils.toImageBitmap

@Composable
fun SignatureContainer(
    onSignatureCaptured: (ImageBitmap) -> Unit // Callback to pass the result
) {
    val state = rememberSignatureState()
    Column() {
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
                .drawWithContent {
                    drawContent()
                    state.updateSignature(
                        toImageBitmap(
                            width = size.width.toInt(),
                            height = size.height.toInt(),
                            signatureColor = Color.Black,
                            signatureSize = 5.dp,
                            signatureSignatureLines = state.signatureLines,
                        ),
                    )
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
        state.signature.let {
            if (it != null) {
               // it.toByteArray(ImageFormat.PNG)  convert for storage
                onSignatureCaptured(it)
            }
        }
    }
}


@Composable
fun rememberSignatureState(): SignatureState {
    return rememberSaveable(saver = SignatureState.Saver) {
        SignatureState()
    }
}








