package home

import PdfDocData
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import composables.EntriesText
import savePdfDoc

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    component: HomeComponent,
    modifier: Modifier = Modifier
) {
    val focusname = remember { FocusRequester() }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var fileName by remember { mutableStateOf("") }
    val saveDocuments=savePdfDoc()
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBar(modifier = Modifier.background(color =  MaterialTheme.colorScheme.background),
                title = { Text("PDF Preview",
                fontSize = 12.sp) })
        },
        content = { padding ->
            Column(modifier = Modifier
                .padding(padding)) {
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
                                firstName= loaded
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
                                saveDocuments.launch(PdfDocData(firstname = firstName, lastname = lastName,email, fileName))

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

