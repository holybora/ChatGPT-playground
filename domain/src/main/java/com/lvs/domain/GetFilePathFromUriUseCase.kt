package com.lvs.domain

import android.content.Context
import android.net.Uri
import com.lvs.data.util.FileHelper
import javax.inject.Inject

class GetFilePathFromUriUseCase @Inject constructor(val context: Context) {

    operator fun invoke(uri: Uri): String = FileHelper.getRealPathFromURI(context, uri)
}