package com.sample.zxing.activity

import android.os.Bundle
import android.util.Base64
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.camera.CameraSettings
import com.sample.zxing.CustomCaptureManager
import com.sample.zxing.R
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.security.spec.X509EncodedKeySpec

/**
 * @author Niharika.Arora
 * Scan Activity to scan different qr codes and parse result
 */
class ScanActivity : AppCompatActivity(),
    CustomCaptureManager.CaptureViewListener {

    private lateinit var camSettings: CameraSettings
    private lateinit var barcodeScanner: DecoratedBarcodeView
    private var capture: CustomCaptureManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_custom_scanner)
        barcodeScanner = findViewById(R.id.barcode_scanner)

        // Camera settings
        camSettings = CameraSettings()
        camSettings.isAutoFocusEnabled = true
        camSettings.focusMode = CameraSettings.FocusMode.MACRO

        initCapture(savedInstanceState)
    }

    private fun initCapture(savedInstanceState: Bundle?) {
        capture = CustomCaptureManager(this, barcodeScanner)
        barcodeScanner.barcodeView.cameraSettings = camSettings
        capture?.setViewCaptureListener(this)
        capture?.initializeFromIntent(intent, savedInstanceState)
        capture?.decode()
    }

    //Parse a jjwt token after getting the result
    override fun onResultFetched(json: String) {
        //show your result here
        val claimsJws: Jws<Claims?>?
        try {
            claimsJws = decryptFile(json)
            //extract your keys from claimJws
            val stringKey = claimsJws?.body?.get("your key name", String::class.java)
            val intKey = claimsJws?.body?.get("your key name", Integer::class.java)
        } catch (e: Exception) {
            //handle exception
        }
    }

    @Throws(
        NoSuchAlgorithmException::class,
        InvalidKeySpecException::class,
        JwtException::class
    )
    fun decryptFile(jwtToken: String?): Jws<Claims?>? {
        val publicKeyBytes =
            Base64.decode("your public key", Base64.DEFAULT)
        // create a key object from the bytes
        val keySpec =
            X509EncodedKeySpec(publicKeyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        val publicKey = keyFactory.generatePublic(keySpec)
        return Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(jwtToken)
    }

}