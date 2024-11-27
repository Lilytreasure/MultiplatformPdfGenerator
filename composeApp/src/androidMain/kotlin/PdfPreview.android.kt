import PdfUtil.createAndSavePdf
import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.io.IOException


// Composable function to allow user to type and save a PDF
@Composable
actual fun savePdfDoc(fileLocation: (url: String) -> Unit): Launcher {
    val launcherCustom: Launcher?
    val context = LocalContext.current
    var  isReadExternalStoragePermissionGranted by remember { mutableStateOf(false) }
    var    isWriteExternalStoragePermissionGranted  by remember { mutableStateOf(false) }
    val multiplePermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        isReadExternalStoragePermissionGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true
        isWriteExternalStoragePermissionGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true

    }
    LaunchedEffect(Unit){
        multiplePermissionsLauncher.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
    }
    launcherCustom = remember {
        Launcher(onLaunch = {pdfretrived->
            println("loaded:::;; file " +pdfretrived.firstname)
            if (pdfretrived.fileName.isBlank()) {
                Toast.makeText(context, "Please enter a valid file name.", Toast.LENGTH_SHORT).show()
                return@Launcher
            }
            try {
                if(isReadExternalStoragePermissionGranted || isWriteExternalStoragePermissionGranted ){
                    val pdfPath = createAndSavePdf(pdfretrived.firstname,pdfretrived.lastname, pdfretrived.email,pdfretrived.fileName, fileSavedStatus = {location->
                        fileLocation(location)

                    })
                    Toast.makeText(context, "PDF saved to $pdfPath", Toast.LENGTH_LONG).show()
                }else{
                    multiplePermissionsLauncher.launch(
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    )
                }
            } catch (e: IOException) {
                Toast.makeText(context, "Error saving PDF: ${e.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
    return  launcherCustom
}


