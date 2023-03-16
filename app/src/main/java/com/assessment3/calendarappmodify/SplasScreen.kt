package com.assessment3.calendarappmodify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView

class SplasScreen : AppCompatActivity() {

    lateinit var logo: ImageView
    lateinit var description: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splas_screen)


        val  topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        val  bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);


        logo = findViewById(R.id.logo)
        description = findViewById(R.id.description)

        logo.startAnimation(topAnim);
        description.startAnimation(bottomAnim)

        Handler().postDelayed({
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        ,3000)
    }
}