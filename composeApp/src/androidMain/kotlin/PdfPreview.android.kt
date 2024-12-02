import PdfUtil.createAndSavePdf
import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.io.IOException


// Composable function to allow user to type and save a PDF
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
actual fun savePdfDoc(fileLocation: (url: String) -> Unit): Launcher {
    val launcherCustom: Launcher?
    val context = LocalContext.current
    var isReadExternalStoragePermissionGranted by remember { mutableStateOf(false) }
    var isWriteExternalStoragePermissionGranted by remember { mutableStateOf(false) }
    var isReadMediaGranted by remember { mutableStateOf(false) }
    val multiplePermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        isReadExternalStoragePermissionGranted =
            permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true
        isWriteExternalStoragePermissionGranted =
            permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true
        isReadMediaGranted = permissions[Manifest.permission.READ_MEDIA_IMAGES] == true

    }
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            multiplePermissionsLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            )

        } else {
            multiplePermissionsLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }

    }
    launcherCustom = remember {
        Launcher(onLaunch = { pdfretrived,_ ->
            println("loaded:::;; file " + pdfretrived?.firstname)
            if (pdfretrived?.fileName?.isBlank() == true) {
                Toast.makeText(context, "Please enter a valid file name.", Toast.LENGTH_SHORT)
                    .show()
                return@Launcher
            }
            try {
                if (isReadExternalStoragePermissionGranted || isWriteExternalStoragePermissionGranted) {
                    val pdfPath = createAndSavePdf(
                        pdfretrived?.firstname ?: "",
                        pdfretrived?.lastname?: "",
                        pdfretrived?.email?: "",
                        pdfretrived?.fileName?: "",
                        context = AndroidPlatformContext(context),
                        fileSavedStatus = { location ->
                            fileLocation(location)

                        })
                    Toast.makeText(context, "PDF saved to $pdfPath", Toast.LENGTH_LONG).show()
                } else {

                    multiplePermissionsLauncher.launch(
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    )
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && isReadMediaGranted) {
                    val pdfPath = createAndSavePdf(
                        pdfretrived?.firstname ?: "",
                        pdfretrived?.lastname?: "",
                        pdfretrived?.email?: "",
                        pdfretrived?.fileName?: "",
                        context = AndroidPlatformContext(context),
                        fileSavedStatus = { location ->
                            fileLocation(location)

                        })
                    Toast.makeText(context, "PDF saved to $pdfPath", Toast.LENGTH_LONG).show()


                }else{
                    multiplePermissionsLauncher.launch(
                        arrayOf(
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                    )
                }

            } catch (e: IOException) {
                Toast.makeText(context, "Error saving PDF: ${e.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
    return launcherCustom
}







