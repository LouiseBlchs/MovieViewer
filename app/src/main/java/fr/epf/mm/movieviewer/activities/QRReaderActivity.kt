package fr.epf.mm.movieviewer.activities


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import fr.epf.mm.movieviewer.R
import io.github.g00fy2.quickie.ScanQRCode


class QRReaderActivity : AppCompatActivity(){


    val scanQrCodeLauncher = registerForActivityResult(ScanQRCode()) { result ->
        // handle QRResult
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<Button>(R.id.action_qrscanner).setOnClickListener{scanQrCodeLauncher.launch(null)}



    }








}


