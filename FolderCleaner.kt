
import java.nio.file.Files.delete
import java.io.File


class FolderWork{
    companion object {
        fun deleteFromFolder(path: String) {
            for (myFile in File(path).listFiles()!!)
                if (myFile.isFile) myFile.delete()
        }
    }
}
