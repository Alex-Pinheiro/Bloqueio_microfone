package br.com.desabilitagravacao

import android.Manifest
import android.content.BroadcastReceiver
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import br.com.desabilitagravacao.ui.main.MainFragment
import br.com.desabilitagravacao.ui.main.REQUEST_RECORD_AUDIO_PERMISSION

class MainActivity : AppCompatActivity() {

    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)


    }
}