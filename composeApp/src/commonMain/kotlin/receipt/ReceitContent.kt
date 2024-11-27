package receipt

import PdfDocData
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import composables.EntriesText
import kotlinx.coroutines.launch
import savePdfDoc
import utils.CustomSnackBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationContent(component: ReceiptComponent, modifier: Modifier = Modifier) {
    val focusname = remember { FocusRequester() }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var fileName by remember { mutableStateOf("") }
    var loc by remember { mutableStateOf("") }
    val saveDocuments = savePdfDoc(fileLocation = { filelocation ->
        loc=filelocation

    })
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBar(modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                title = {
                    Text(
                        "Receipt Preview",
                        fontSize = 12.sp
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
                    item {
                        EntriesText(
                            loadedEntry = "",
                            focusRequester = focusname,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text
                            ),
                            labelEntry = "First Name*",
                            callback = { loaded ->
                                firstName = loaded
                            },
                            resetSignal = firstName.isBlank()
                        )

                        EntriesText(
                            loadedEntry = "",
                            focusRequester = focusname,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text
                            ),
                            labelEntry = "Lastname*",
                            callback = { loaded ->
                                lastName = loaded
                            },
                            resetSignal = lastName.isBlank()
                        )

                        EntriesText(
                            loadedEntry = "",
                            focusRequester = focusname,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text
                            ),
                            labelEntry = "Email*",
                            callback = { loaded ->
                                email = loaded
                            },
                            resetSignal = email.isBlank()
                        )

                        EntriesText(
                            loadedEntry = "",
                            focusRequester = focusname,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text
                            ),
                            labelEntry = "File Name (without .pdf)",
                            callback = { loaded ->
                                fileName = loaded
                            },
                            resetSignal = fileName.isBlank()
                        )

                    }
                    item {
                        Button(
                            onClick = {
                                //save DOc
                                saveDocuments.launch(
                                    PdfDocData(
                                        firstname = firstName,
                                        lastname = lastName,
                                        email,
                                        fileName
                                    )
                                )
                                if (loc.isNotBlank()) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            CustomSnackBar(
                                                "Document saved Successfully",
                                                isError = false
                                            )
                                        )
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text("Save Doc")
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.padding(bottom = 200.dp))
                    }
                }
            }
        }
    )
}