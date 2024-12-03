import androidx.compose.runtime.Composable


//Todo----- Work on file upload and  download on Android/iOs
data class PdfDocData(
    var firstname: String,
    var lastname: String,
    var email: String,
    var fileName: String,
    var signature: ByteArray?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PdfDocData

        if (firstname != other.firstname) return false
        if (lastname != other.lastname) return false
        if (email != other.email) return false
        if (fileName != other.fileName) return false
        if (signature != null) {
            if (other.signature == null) return false
            if (!signature.contentEquals(other.signature)) return false
        } else if (other.signature != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = firstname.hashCode()
        result = 31 * result + lastname.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + fileName.hashCode()
        result = 31 * result + (signature?.contentHashCode() ?: 0)
        return result
    }
}

@Composable
expect fun savePdfDoc(fileLocation: (url: String) -> Unit): Launcher



