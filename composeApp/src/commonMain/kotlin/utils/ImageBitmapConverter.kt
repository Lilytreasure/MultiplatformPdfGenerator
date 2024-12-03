package utils

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

internal fun toImageBitmap(
    width: Int,
    height: Int,
    signatureColor: Color,
    signatureSize: Dp,
    signatureSignatureLines: List<SignatureLine>,
): ImageBitmap {
    val imgBitmap = ImageBitmap(width, height)

    Canvas(imgBitmap).apply {
        CanvasDrawScope().draw(
            density = Density(1f, 1f),
            layoutDirection = LayoutDirection.Ltr,
            canvas = this,
            size = Size(width.toFloat(), height.toFloat()),
        ) {
            signatureSignatureLines.forEach { line ->
                drawLine(
                    color = signatureColor,
                    start = line.start,
                    end = line.end,
                    strokeWidth = (signatureSize * 3).toPx(),
                    cap = StrokeCap.Round,
                )
            }
        }
    }
    return imgBitmap
}