package com.example.test

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SwitchExample()
            AccelerometerSensorReader()
            //CloseWithBackPress(activity = this)
        }
    }

    @Composable
    fun CloseWithBackPress(activity: MainActivity) {
        activity.onBackPressedDispatcher.onBackPressed()
        Text("Exit")
    }

    @Composable
    private fun AccelerometerSensorReader() {
        val context = LocalContext.current
        val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        var xValue by remember { mutableStateOf(0f) }
        var yValue by remember { mutableStateOf(0f) }
        var zValue by remember { mutableStateOf(0f) }

        val sensorEventListener = remember {
            object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    event?.let {
                        xValue = it.values[0]
                        yValue = it.values[1]
                        zValue = it.values[2]
                    }
//                    if(yValue < 0)
//                    {
//                        toggleFlashlight(context, turnOn = true)
//                    }
                }
                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }
    }
        DisposableEffect(Unit) {
            sensorManager.registerListener(sensorEventListener, accelerometer, android.hardware.SensorManager.SENSOR_DELAY_UI)
            onDispose {
                sensorManager.unregisterListener(sensorEventListener)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Accelerometer Data:", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text("X: $xValue", fontSize = 18.sp)
            Text("Y: $yValue", fontSize = 18.sp)
            Text("Z: $zValue", fontSize = 18.sp)
        }
}

@Composable
fun SwitchExample() {
    var isChecked by remember { mutableStateOf(false) }
    //var isFlashOn by remember { mutableStateOf(false) }
    val context = LocalContext.current

//    Box(
//        modifier = Modifier.fillMaxSize(),
//        //contentAlignment = (Alignment.TopEnd Alignment.Center)
//    ){
//        Button(
//            onClick = {
//                isFlashOn = !isFlashOn
//                toggleFlashlight(context, isFlashOn)
//            },
//            modifier = Modifier
//                .offset(x = 50.dp, y = 100.dp)
//        ) {
//            Text(if (isFlashOn) "Turn OFF Flashlight" else "Turn ON Flashlight" , fontSize = 30.sp , textAlign =  TextAlign.Right)
//        }
//    }
    Button(
        onClick = { repeat(3) {vibrateDevice(context) }},
        modifier = Modifier.offset(x = 70.dp, y = 100.dp)
    ) {
        Text("Vibrate")
    }
}

private fun vibrateDevice(context: Context) {

    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
    {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    }else
    {
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }


    val tim = longArrayOf(0L, 750L, 100L, 750L, 100L, 750L, 100L, 750L,100L, 750L,
        0L, 750L, 100L, 750L, 100L, 750L, 100L, 750L,100L, 750L,
        0L, 750L, 100L, 750L, 100L, 750L, 100L, 750L,100L, 750L,
        0L, 750L, 100L, 750L, 100L, 750L, 100L, 750L,100L, 750L)
    val amp = intArrayOf(0, 255, 0, 255, 0, 255, 0, 255 , 0, 255,
        0, 255, 0, 255, 0, 255, 0, 255 , 0, 255,
        0, 255, 0, 255, 0, 255, 0, 255 , 0, 255,
        0, 255, 0, 255, 0, 255, 0, 255 , 0, 255)

    vibrator.vibrate(VibrationEffect.createWaveform(tim, amp, 3))

    //vibrator.vibrate(  VibrationEffect.createOneShot(1000, 255))

    //vibrator.vibrate(  VibrationEffect.createWaveform(tim, -1))

    //val tim = longArrayOf(1000L, 500L, 1000L, 500L)
    //val amp = intArrayOf(70,60,70,60)
    //vibrator.vibrate(VibrationEffect.createWaveform(tim, amp, -1))
    //val vibrator = null
    //vibrator.vibrate(vibrationEffect!!)
}


// Function to toggle the flashlight
fun toggleFlashlight(context: Context, turnOn: Boolean) {
    val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    try {
        val cameraId = cameraManager.cameraIdList[0]
        run {
            cameraManager.setTorchMode(cameraId, turnOn) // Toggle flashlight
        }
    } catch (e: Exception) {
        Log.e("Flashlight", "Error toggling flashlight: ${e.message}")
    }
}
    }

