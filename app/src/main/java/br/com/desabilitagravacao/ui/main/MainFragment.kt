package br.com.desabilitagravacao.ui.main


import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.findNavController
import br.com.desabilitagravacao.ForegroundService
import br.com.desabilitagravacao.R

const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class MainFragment : Fragment() {

    lateinit var sharedPreferences: SharedPreferences
    var block: Boolean = false

    private var permissionToRecordAccepted = false

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionToRecordAccepted) {
            //finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("preferencias", Context.MODE_PRIVATE)

        val button: Button? = view?.findViewById(R.id.button)
        button?.setOnClickListener {
            Log.e("MediaRecorder", "Botao ok")
            val recorder = MediaRecorder()
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            recorder.setOutputFile("/dev/null")
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
            recorder.prepare()
            try {
                recorder.start()
                //recorder.stop()
                recorder.release()
                ForegroundService.startService(requireContext(), "intent")
                view?.findNavController()?.navigate(R.id.action_mainFragment_to_segundoFragment)
            } catch (e: IllegalStateException) {
                Toast.makeText(requireContext(), "O microfone está em uso", Toast.LENGTH_SHORT).show()
                //recorder.stop()
                //recorder.release()
                // Show alert dialogs to user.
                // Ask him to stop audio record in other app.
                // Stay in pause with your streaming because MIC is busy.

                //isMicFree = false;
            }
            /*ForegroundService.startService(requireContext(), "intent")
            view?.findNavController()?.navigate(R.id.action_mainFragment_to_segundoFragment)*/
        }

    }

    override fun onResume() {
        super.onResume()

        block = sharedPreferences.getBoolean("block", false)
        if(block){
            view?.findNavController()?.navigate(R.id.action_mainFragment_to_segundoFragment)
        }
    }

}