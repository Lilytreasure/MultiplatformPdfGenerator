package pdfView

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import composables.PdfViewCard
import getAllFilesInDirectory
import getPlatformContext
import openPdfDoc
import utils.CustomSnackBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfViewContent(component: PdfViewComponent, modifier: Modifier = Modifier) {
    val snackbarHostState = remember { SnackbarHostState() }
    val files: List<String> = getAllFilesInDirectory(getPlatformContext())
    val openDocs = openPdfDoc(getPlatformContext())
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .consumeWindowInsets(WindowInsets.systemBars),
        topBar = {
            TopAppBar(modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                title = {
                    Text(
                        "Documents View",
                        style = MaterialTheme.typography.titleMedium
                    )
                })
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                // custom snackbar with the custom action button color and border
                val isError = (data.visuals as? CustomSnackBar)?.isError ?: false
                val buttonColor =
                    if (isError) {
                        ButtonDefaults.textButtonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    } else {
                        ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.inversePrimary
                        )
                    }
                Snackbar(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    action = {
                        TextButton(
                            onClick = { if (isError) data.dismiss() else data.performAction() },
                            colors = buttonColor
                        ) {
                            Text(data.visuals.actionLabel ?: "")
                        }
                    }
                ) {
                    Text(data.visuals.message)
                }
            }

        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
            ) {
                LazyColumn(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                    items(files) { fileName ->  // items expects a List
                        PdfViewCard(label = fileName,
                            description = "12/2/22",
                            onClickContainer = {
                                try {
                                    openDocs.launch(filename = fileName)
                                } catch (e: Exception) {
                                    println("Eror opening pdf" + e)
                                }
                            })
                    }
                    item {
                        Spacer(modifier = Modifier.padding(bottom = 200.dp))
                    }
                }
            }
        }
    )
}