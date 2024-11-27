import PdfUtil.createAndSavePdf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun savePdfDoc(fileLocation: (url: String) -> Unit): Launcher {

    val launcherCustom: Launcher?
    launcherCustom = remember {
        Launcher(onLaunch = {pdfretrived->
            try {
                createAndSavePdf(pdfretrived.firstname,pdfretrived.lastname,pdfretrived.email,pdfretrived.fileName, fileSavedStatus = {location->
                   fileLocation(location)
                })

            }catch (e : Exception){
                println("error saving;;;;" + e)

            }

        })
    }

 return  launcherCustom
}

