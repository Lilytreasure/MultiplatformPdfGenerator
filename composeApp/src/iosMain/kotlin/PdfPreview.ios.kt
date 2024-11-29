import PdfUtil.createAndSavePdf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

object IOSPlatformContext : PlatformContext

@Composable
actual fun savePdfDoc(fileLocation: (url: String) -> Unit): Launcher {

    val launcherCustom: Launcher?
    launcherCustom = remember {
        Launcher(onLaunch = {pdfretrived->
            try {
                createAndSavePdf(pdfretrived.firstname,pdfretrived.lastname,pdfretrived.email,pdfretrived.fileName, context =IOSPlatformContext , fileSavedStatus = {location->
                   fileLocation(location)
                })

            }catch (e : Exception){
                println("error saving;;;;" + e)

            }

        })
    }

 return  launcherCustom
}

