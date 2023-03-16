package com.assessment3.calendarappmodify

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import org.json.JSONArray
import org.json.JSONObject
import java.text.DateFormat
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    private lateinit var appBarconfguration: AppBarConfiguration
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    lateinit var Calenderrecycle: RecyclerView
    var holidaysarraycurrent = JSONArray()
    var holidaysByMonthList = JSONArray()
    private lateinit var textholidy:TextView
    private val homeActivity = GetYearLocation::class.java
    private val nextActivity = FilterByAllCountry::class.java
    private val reminder =getnotification::class.java
    var currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val calendar = Calendar.getInstance()
    val currentMonthNumber = calendar.get(Calendar.MONTH) + 1
    val currentMonthName = DateFormatSymbols().months[currentMonthNumber - 1]

    val filteredHolidays = JSONArray()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.setTitle("DashBoard")
        drawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        getCalendarData()
        textholidy=findViewById(R.id.txt_holiday)
        val calendar=Calendar.getInstance().time
        val dateformat=DateFormat.getDateInstance(DateFormat.FULL).format(calendar)
        val timeformat=DateFormat.getTimeInstance().format(calendar)

        val textdate=findViewById<TextView>(R.id.currentdate)
        val texttime=findViewById<TextView>(R.id.currenttime)

        navView.itemIconTintList= null;

        textdate.text=dateformat
        texttime.text=timeformat

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, homeActivity)
                    startActivity(intent)
                    true
                }
                R.id.nav_next -> {
                    val intent = Intent(this, nextActivity)
                    startActivity(intent)
                    true
                }
                R.id.logout->{
                      logout()
                    true
                }

                R.id.reminder->{
                    val intent=Intent(this,reminder)
                    startActivity(intent)
                    true
                }
                else -> false

            }

        }


    }
    private fun logout() {
        // Perform logout operation here
        // ...

        // Exit the application
        finishAffinity()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun getCalendarData() {
        val url = "https://calendarific.com/api/v2/holidays?api_key=f6740c4eafcdc2a625e5d59965249cf4ec41fca4&country=LK&year="+currentYear

        val request = StringRequest(
            Request.Method.GET, url,
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val jsonResponse = jsonObject.getJSONObject("response")
                    val holidaysArray = jsonResponse.getJSONArray("holidays")

                    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                    // Iterate over the holidays array to find the holiday for the current date
                    var holidayName = "Today is not  a holiday"
                    for (i in 0 until holidaysArray.length()) {
                        val holidayObject = holidaysArray.getJSONObject(i)
                        val dateObject = holidayObject.getJSONObject("date")
                        val holidayDate = dateObject.getString("iso")
                        if (holidayDate == currentDate) {
                            holidayName = holidayObject.getString("name")
                            break
                        }
                    }

                    // Update the UI with the result of the holiday check
                    runOnUiThread {
                        textholidy.text = holidayName
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
            })

        Volley.newRequestQueue(applicationContext).add(request)
    }


}