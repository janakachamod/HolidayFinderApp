package com.assessment3.calendarappmodify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class DisplayDetails : AppCompatActivity() {

    private lateinit var title: TextView

    private lateinit var year: TextView
    private lateinit var description:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_details)

      supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        this.setTitle("Display Details")

        title=findViewById(R.id.txt_title)
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