package composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import theme.backArrowColor

@Composable
fun  PdfViewCard(label: String, description: String? = null, onClickContainer: () -> Unit){
        Box(modifier = Modifier
            .background(color = MaterialTheme.colorScheme.inverseOnSurface)
            .clickable {
                onClickContainer()
            }) {
            Row(
                modifier = Modifier.padding(top = 9.dp, bottom = 9.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically){
                Row(verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        Icons.Outlined.PictureAsPdf,
                        contentDescription = null
                    )
                    Column {
                        Text(
                            text = label,
                            modifier = Modifier.padding(start = 15.dp),
                            fontSize = 14.sp,
                            color= MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        if (description != null) {
                            Text(
                                text = description,
                                modifier = Modifier.padding(start = 15.dp),
                                color= MaterialTheme.colorScheme.outline,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                    }
                }
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = "share file",
                        tint = backArrowColor
                    )
                    Spacer(modifier = Modifier.padding(end = 15.dp))
                }
            }
        }
}