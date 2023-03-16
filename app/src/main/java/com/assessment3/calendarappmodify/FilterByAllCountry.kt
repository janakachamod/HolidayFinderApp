package com.assessment3.calendarappmodify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class FilterByAllCountry : AppCompatActivity() {
    var countryarray = JSONArray()
    val years = arrayOf("2023", "2022", "2021", "2020")
    private lateinit var fetchdata: Button
    var selectedCountry = ""
    private lateinit var yearsdrop: Spinner
    private lateinit var spinner: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setTitle("Filter")
        setContentView(R.layout.activity_filter_by_all_country)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        yearsdrop = findViewById(R.id.sp_year)
        fetchdata = findViewById(R.id.button)
        spinner = findViewById(R.id.sp_country)



        getcountrydata()
        val adapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearsdrop.adapter = adapter1

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedCountry = parent?.getItemAtPosition(position).toString()
                // do something with the selected country
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // handle nothing selected
            }
        }

        fetchdata.setOnClickListener {


            val countryCode = getCountryCode(selectedCountry)
            val selectedYear = yearsdrop.selectedItem.toString()


            val intent = Intent(this, GetAllTheData::class.java)
            intent.putExtra("country", countryCode)
            intent.putExtra("year", selectedYear)
            startActivity(intent)
        }


    }

    fun getcountrydata() {
        val url =
            "https://calendarific.com/api/v2/countries?api_key=f6740c4eafcdc2a625e5d59965249cf4ec41fca4"
        val request = StringRequest(
            Request.Method.GET, url,
            Response.Listener { response ->
                try {
                    val rootJson = JSONObject(response)
                    val getJsonObjectResponse = rootJson.getJSONObject("response")
                    countryarray = getJsonObjectResponse.getJSONArray("countries")
                    val list: MutableList<String> = ArrayList()
                    for (i in 0 until countryarray.length()) {
                        val jobj2 = countryarray.getJSONObject(i)
                        val name = jobj2.getString("country_name")
                        list.add(name)
                    }

                    val adapter12 = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
                    adapter12.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.setAdapter(adapter12)


                } catch (exception: Exception) {

                }
            },
            Response.ErrorListener { error -> })

        Volley.newRequestQueue(applicationContext).add(request)
    }

    fun getCountryCode(countryName: String): String {
        var countryCode = ""
        for (i in 0 until countryarray.length()) {
            val jobj2 = countryarray.getJSONObject(i)
            if (jobj2.getString("country_name") == countryName) {
                countryCode = jobj2.getString("iso-3166")
                break
            }
        }
        return countryCode
    }

}