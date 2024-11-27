package composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun EntriesText(
    loadedEntry: String,
    focusRequester: FocusRequester,
    keyboardOptions: KeyboardOptions,
    labelEntry: String,
    callback: (userInput: String) -> Unit = {},
    resetSignal: Boolean = false
) {
    var desiredEntry by remember { mutableStateOf(TextFieldValue()) }

    LaunchedEffect(loadedEntry) {
        desiredEntry = TextFieldValue(loadedEntry)
        callback(loadedEntry)
    }
    LaunchedEffect(resetSignal) {
        if (resetSignal) {
            desiredEntry = TextFieldValue("")
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .shadow(0.5.dp, RoundedCornerShape(10.dp))
            .background(
                color = MaterialTheme.colorScheme.inverseOnSurface,
                shape = RoundedCornerShape(10.dp)
            ),
    ) {
        BasicTextField(
            modifier = Modifier
                .focusRequester(focusRequester)
                .height(65.dp)
                .padding(top = 20.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                .absoluteOffset(y = 2.dp),
            enabled = true,
            keyboardOptions = keyboardOptions,
            value = desiredEntry,
            onValueChange = {
                if (it.text.isEmpty() ||  it.text.isNotEmpty()) {
                    desiredEntry = it
                    callback(it.text)
                }
            },
            singleLine = true,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = MaterialTheme.typography.bodySmall.fontWeight,
                fontSize = 13.sp,
                fontStyle = MaterialTheme.typography.bodySmall.fontStyle,
                letterSpacing = MaterialTheme.typography.bodySmall.letterSpacing,
                lineHeight = MaterialTheme.typography.bodySmall.lineHeight,
                fontFamily = MaterialTheme.typography.bodySmall.fontFamily
            ),
            decorationBox = { innerTextField ->
                if (desiredEntry.text.isEmpty()
                ) {
                    Text(
                        modifier = Modifier.alpha(.3f),
                        text = labelEntry,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                AnimatedVisibility(
                    visible = desiredEntry.text.isNotEmpty(),
                    modifier = Modifier.absoluteOffset(y = -(16).dp),
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically(),
                ) {
                    Text(
                        text = labelEntry,
                        color = Color.Blue,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        innerTextField()
                    }
                }
            }
        )
    }
}