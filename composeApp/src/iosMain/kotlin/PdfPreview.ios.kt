import PdfUtil.createAndSavePdf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
@Composable
actual fun savePdfDoc(): Launcher {

    val launcherCustom: Launcher?
    launcherCustom = remember {
        Launcher(onLaunch = {pdfretrived->
            try {
                createAndSavePdf(pdfretrived.firstname,pdfretrived.lastname,pdfretrived.email,pdfretrived.fileName)

            }catch (e : Exception){
                println("error saving;;;;" + e)
            }

        })
    }

 return  launcherCustom
}

