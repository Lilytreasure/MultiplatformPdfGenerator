package notifications

import PdfDocData
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import savePdfDoc

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationContent(component: NotificationComponent, modifier: Modifier = Modifier) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var fileName by remember { mutableStateOf("") }
    val saveDocuments=savePdfDoc()
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("PDF Preview",
                fontSize = 12.sp) })
        },
        content = { padding ->
            Column(modifier = Modifier
                .padding(padding)) {
                LazyColumn(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                    item {
                        TextField(
                            value = firstName,
                            onValueChange = { firstName = it },
                            label = { Text("First Name") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp, top = 5.dp)
                        )

                        TextField(
                            value = lastName,
                            onValueChange = { lastName = it },
                            label = { Text("Last Name") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        )

                        TextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        )

                        TextField(
                            value = fileName,
                            onValueChange = { fileName = it },
                            label = { Text("File Name (without .pdf)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            maxLines = 1
                        )

                    }
                    item {
                        Button(
                            onClick = {
                                //save DOc
                                println("current;;" + firstName)
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