package com.assessment3.calendarappmodify

import android.os.Bundle
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity

class DisplayDetailsworld : AppCompatActivity() {

    private lateinit var title: TextView

    private lateinit var year: TextView
    private lateinit var description: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       // supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        this.setTitle("Display Details")

        title=findViewById(R.id.txt_title12)
        year=findViewById(R.id.txt_year12)
        description=findViewById(R.id.txt_description)

        val ti1=intent.getStringExtra("title")
        val ti2=intent.getStringExtra("date")
        val ti3=intent.getStringExtra("description")

        title.text=ti1
        year.text=ti2
        description.text=ti3


        }
    }


