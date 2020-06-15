## ZxingPlusJjwtSample

A demo application for generating QR code and scanning <a href="https://github.com/jwtk/jjwt">JJWT</a> token.

using <a href=""> zxing-android-embedded</a> and <a href="">zxing</a> for generating and scanning QR code.

Some Features of **Zxing** library:

1. Can be used via Intents (little code required).
2. Can be embedded in an Activity, for advanced customization of UI and logic.
3. Scanning can be performed in landscape or portrait mode.
4. Camera is managed in a background thread, for fast startup time.

**JJWT** aims to be the easiest to use and understand library for creating and verifying JSON Web Tokens (JWTs) on the JVM
and Android.

JJWT is a pure Java implementation based 
exclusively on the [JWT](https://tools.ietf.org/html/rfc7519), 
[JWS](https://tools.ietf.org/html/rfc7515), [JWE](https://tools.ietf.org/html/rfc7516), 
[JWK](https://tools.ietf.org/html/rfc7517) and [JWA](https://tools.ietf.org/html/rfc7518) RFC specifications and 
open source under the terms of the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0).

### Usage

## QR code generation

```java
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
```
<br/>

## Decrypting JJWT token

```java
        val publicKeyBytes = Base64.decode("your public key", Base64.DEFAULT)
        // create a key object from the bytes
        val keySpec =
            X509EncodedKeySpec(publicKeyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        val publicKey = keyFactory.generatePublic(keySpec)
        return Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(jwtToken)
```
<br/>

## Scanning and Extracting result from QR code

```java
        capture = CustomCaptureManager(this, barcodeScanner)
        capture?.setShowMissingCameraPermissionDialog(true)
        barcodeScanner.barcodeView.cameraSettings = camSettings
        capture?.setViewCaptureListener(this)
        capture?.initializeFromIntent(intent, savedInstanceState)
        capture?.decode()
```
<br/>
In other lifecycle methods,add this-<br/>

```java
 override fun onResume() {
        super.onResume()
        capture?.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        capture?.onDestroy()
    }
```
<br/>

On getting the result,you can extract keys like this-
```java

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
``` 
<br/>

Happy Coding!!
