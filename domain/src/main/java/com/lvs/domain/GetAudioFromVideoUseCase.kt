package com.lvs.domain

import android.content.Context
import android.net.Uri
import com.lvs.data.util.AudioExtractor
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class GetAudioFromVideoUseCase(context: Context) {

    private val cacheDir = context.cacheDir

    @Throws(IOException::class)
    operator fun invoke(inputVideoUri: Uri) {

        val fileOutput = File(cacheDir.path + "/audio_from_video.mp3")
        //clean up temp file
        val fileOutputStream = FileOutputStream(fileOutput)
        fileOutputStream.write(ByteArray(0))
        fileOutputStream.close()

        AudioExtractor().genVideoUsingMuxer(
            inputVideoUri.path,
            fileOutput.path,
            -1,
            -1,
            true,
            false
        )
    }
}

