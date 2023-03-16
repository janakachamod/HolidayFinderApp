package com.assessment3.calendarappmodify

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.text.DateFormatSymbols

class GetAllTheData : AppCompatActivity() {
    lateinit var Calenderrecycle: RecyclerView
    var holidaysarraycurrent= JSONArray()
    var holidaysByMonthList = JSONArray()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setTitle("Get All Countries Data")
        setContentView(R.layout.activity_get_all_the_data)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        getCalendarData()
        Calenderrecycle = findViewById(R.id.recycleview)
        Calenderrecycle.adapter = CalenderAdapter()
        Calenderrecycle.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

    }

    fun getCalendarData() {
        val url = "https://calendarific.com/api/v2/holidays?api_key=f6740c4eafcdc2a625e5d59965249cf4ec41fca4&country=" + intent.getStringExtra("country") + "&year=" + intent.getStringExtra("year") + ""

        val request = StringRequest(
            Request.Method.GET, url,
            Response.Listener { response ->
                try {
                    val jsonobject = JSONObject(response)
                    val getJsonObjectResponse = jsonobject.getJSONObject("response")

                    holidaysarraycurrent = getJsonObjectResponse.getJSONArray("holidays")

                    val holidaysByMonth = HashMap<Int, ArrayList<JSONObject>>()
                    for (i in 0 until holidaysarraycurrent.length()) {
                        val holiday = holidaysarraycurrent.getJSONObject(i)
                        val date = holiday.getJSONObject("date")
                        val datetime = date.getJSONObject("datetime")
                        val monthNumber = datetime.getString("month").toInt()

                        // Add holiday to corresponding ArrayList based on month
                        if (holidaysByMonth.containsKey(monthNumber)) {
                            holidaysByMonth[monthNumber]?.add(holiday)
                        } else {
                            holidaysByMonth[monthNumber] = arrayListOf(holiday)
                        }
                    }

                    // Sort holidays by month
                    val sortedHolidaysByMonth = holidaysByMonth.toSortedMap()

                    // Create a new JSONArray with headers for each month followed by the holidays for that month
                    for ((monthNumber, holidays) in sortedHolidaysByMonth) {
                        val monthName = DateFormatSymbols().months[monthNumber - 1]

                        // Add header for month
                        val header = JSONObject()
                        header.put("name", monthName)
                        header.put("color", getColorForMonth(monthName))
                        holidaysByMonthList.put(header)

                        // Add holidays for month
                        for (holiday in holidays) {
                            holidaysByMonthList.put(holiday)
                        }
                    }

                    Calenderrecycle.adapter?.notifyDataSetChanged()
                } catch (e: Error) {

                }
            },
            Response.ErrorListener { error -> })

        Volley.newRequestQueue(applicationContext).add(request)
    }
    inner class CalenderAdapter : RecyclerView.Adapter<CalendarViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)


            return CalendarViewHolder(view)
        }

        override fun getItemCount(): Int {

            return holidaysarraycurrent.length()

        }

        override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
            try {


                val holiday = holidaysByMonthList.getJSONObject(position)
                if (holiday.has("name")) {
                    holder.countryname.text = holiday.getString("name")
                    holder.countryname.visibility = View.VISIBLE
                    holder.date.visibility = View.GONE
                    val color = holiday.getString("name")
                    holder.itemView.setBackgroundColor(getColorForMonth(color))

                } else {
                    holder.countryname.visibility = View.GONE
                    holder.date.visibility = View.VISIBLE
                    holder.date.text = holiday.getJSONObject("date")
                        .getJSONObject("datetime").getString("month")
                }

                holder.itemView.setOnClickListener{
                    val intent = Intent(holder.itemView.context, DisplayDetails::class.java)
                    val holiday = holidaysarraycurrent.getJSONObject(position)
                    val date = holiday.getJSONObject("date")

                    intent.putExtra("title", holidaysByMonthList.getJSONObject(position).getString("name"))
                    intent.putExtra("date",date.getString("iso"))
                    intent.putExtra("description", holidaysByMonthList.getJSONObject(position).getString("description"))

                    holder.itemView.context.startActivity(intent)
                }




            }
            catch (e:Error)
            {

            }


        }

    }
    inner class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val countryname: TextView = itemView.findViewById(R.id.title)
        val date: TextView = itemView.findViewById(R.id.month)

    }

    private fun getColorForMonth(monthName: String): Int {
        return when (monthName.toLowerCase()) {
            "january" -> Color.parseColor("#FFCDD2")
            "february" -> Color.parseColor("#F8BBD0")
            "march" -> Color.parseColor("#E1BEE7")
            "april" -> Color.parseColor("#D1C4E9")
            "may" -> Color.parseColor("#C5CAE9")
            "june" -> Color.parseColor("#BBDEFB")
            "july" -> Color.parseColor("#B3E5FC")
            "august" -> Color.parseColor("#B2EBF2")
            "september" -> Color.parseColor("#B2DFDB")
            "october" -> Color.parseColor("#C8E6C9")
            "november" -> Color.parseColor("#DCEDC8")
            "december" -> Color.parseColor("#F0F4C3")
            else -> Color.WHITE
        }
    }

}