package com.lsenpuraehc.accelerationtestapp

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var sManager : SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tvSensor = findViewById<TextView>(R.id.tvSensor)
        val lRotation = findViewById<LinearLayout>(R.id.lRotation)

        var magnetic = FloatArray(9)
        var gravity = FloatArray(9)

        var accrs = FloatArray(3)
        var magd = FloatArray(3)
        var values = FloatArray(3)

        sManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val sensor2 = sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        val sListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                when (event?.sensor?.type) {
                    Sensor.TYPE_ACCELEROMETER -> accrs = event.values.clone()
                    Sensor.TYPE_MAGNETIC_FIELD -> magd = event.values.clone()
                }
                SensorManager.getRotationMatrix(gravity, magnetic, accrs, magd)
                val outGravity = FloatArray(9)
                SensorManager.remapCoordinateSystem(gravity,
                SensorManager.AXIS_X,
                SensorManager.AXIS_Z,
                outGravity)
                SensorManager.getOrientation(outGravity, values)

                val degree = values[2] * 57.2958f
                val rotation = degree -90
                lRotation.rotation =  - rotation
                val rData = (degree + 90).toInt()
                val color = if (rData == 0)
                    Color.GREEN
                 else
                    Color.RED
                lRotation.setBackgroundColor(color)
                tvSensor.text = rData.toString()

            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

            }

        }
        sManager.registerListener(sListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        sManager.registerListener(sListener, sensor2, SensorManager.SENSOR_DELAY_NORMAL)
    }
}