package com.sample.zxing.activity

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.sample.zxing.R

/**
 * @author Niharika.Arora
 * QrCodeGeneratorActivity to generate Qr code and show in imageView
 */
class QrCodeGeneratorActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var scanNow: TextView
    private var bitMatrix: BitMatrix? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById(R.id.scan_code)
        setImage()
        scanNow.setOnClickListener {
            launchScanScreen()
        }
    }

    private fun launchScanScreen() {
        startActivity(Intent(this, ScanActivity::class.java))
    }

    private fun setImage() {
        val qrText = "Your qr code hash here"
        val multiFormatWriter = MultiFormatWriter()
        try {
            bitMatrix?.clear()
            val size = convertDpToPixel()
            bitMatrix = multiFormatWriter.encode(
                    qrText,
                    BarcodeFormat.QR_CODE,
                    size,
                    size
            )
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.createBitmap(bitMatrix)
            imageView.setImageBitmap(bitmap)
        } catch (e: WriterException) { //do nothing
        }
    }

    private fun convertDpToPixel(): Int {
        return (250f * (resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT.toFloat())).toInt()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}