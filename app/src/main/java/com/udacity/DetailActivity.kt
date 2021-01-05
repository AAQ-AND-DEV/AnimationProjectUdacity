package com.udacity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.udacity.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)
//        val motionLayout = findViewById<MotionLayout>(R.id.motion_layout)
//        motionLayout.transitionToEnd()
        setSupportActionBar(detailBinding.toolbar)
    }

}
