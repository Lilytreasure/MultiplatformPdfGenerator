package receipt

import PdfDocData
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Draw
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import composables.AlertDialogExample
import composables.EntriesText
import composables.SignAction
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
        loc = filelocation
    })
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val openAlertDialog = remember { mutableStateOf(false) }
    var signature by remember { mutableStateOf<ImageBitmap?>(null) }
    when {
        openAlertDialog.value -> {
            AlertDialogExample(
                onDismissRequest = { openAlertDialog.value = false },
                onConfirmation = {capturedSignature ->
                    signature = capturedSignature
                    openAlertDialog.value = false
                },
                dialogTitle = "Sign  the document",
                icon = Icons.Outlined.Draw
            )
        }
    }
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .consumeWindowInsets(WindowInsets.systemBars),
        topBar = {
            TopAppBar(modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                title = {
                    Text(
                        "Receipt Preview",
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
                        //Add signature
                        //SignatureContainer()
                        if (signature == null){
                            Row(modifier = Modifier.padding(top = 10.dp)){
                                SignAction(label = "Sign Document", onClickContainer = {
                                    //Launch Pop up to sign
                                    openAlertDialog.value = true
                                })
                            }
                        }
                        signature?.let {
                            Box(
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .size(400.dp, 200.dp) // Set the size for the image
                                    .border(
                                        BorderStroke(
                                            width = 2.dp, // Border thickness
                                            color = MaterialTheme.colorScheme.primary // Border color
                                        ),
                                        shape = RoundedCornerShape(8.dp) // Optional rounded corners
                                    )
                            ) {
                                Image(
                                    bitmap = it,
                                    contentDescription = "Signature",
                                    modifier = Modifier.fillMaxSize() // Ensure the image fills the Box
                                )
                                IconButton(
                                    onClick = {
                                        openAlertDialog.value = true
                                    },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd) // Align button to the top-end of the Box
                                        .padding(8.dp) // Optional padding for the button
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit, // Use the edit icon
                                        contentDescription = "Edit Signature",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
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