package com.sample.zxing

import android.app.Activity
import android.graphics.Bitmap
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import java.io.File
import java.io.FileOutputStream

/**
 * @author Niharika.Arora
 * Custom Capture manager for returning scan result
 */
class CustomCaptureManager(private val activity: Activity, barcodeView: DecoratedBarcodeView?) :
    CaptureManager(activity, barcodeView) {
    private var captureViewListener: CaptureViewListener? = null

    interface CaptureViewListener {
        fun onResultFetched(json: String)
    }

    override fun returnResult(rawResult: BarcodeResult) {
        val barcodeImagePath = getBarcodeImagePath(rawResult)
        if (barcodeImagePath != null) {
            val intent = resultIntent(rawResult, barcodeImagePath)
            val contents =
                intent.getStringExtra(Intents.Scan.RESULT)
            if (contents == null) {
                //do nothing
            } else {
                captureViewListener?.onResultFetched(rawResult.text)
            }
        }
    }

    private fun getBarcodeImagePath(rawResult: BarcodeResult): String? {
        var barcodeImagePath: String? = null
        val bmp = rawResult.bitmap
        try {
            val bitmapFile = File.createTempFile(
                "barcodeimage",
                ".jpg",
                activity.cacheDir
            )
            val outputStream = FileOutputStream(bitmapFile)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.close()
            barcodeImagePath = bitmapFile.absolutePath
        } catch (e: Exception) {

        }
        return barcodeImagePath
    }

    fun setViewCaptureListener(captureViewListener: CaptureViewListener?) {
        this.captureViewListener = captureViewListener
    }
}