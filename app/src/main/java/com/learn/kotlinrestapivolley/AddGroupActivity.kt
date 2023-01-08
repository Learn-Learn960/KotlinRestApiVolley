package com.learn.kotlinrestapivolley

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class AddGroupActivity : AppCompatActivity() {
    var etNameGroup: EditText? = null
    var etDescription: EditText? = null
    private var btnSave: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_group)

        etNameGroup = findViewById(R.id.etNameGroup)
        etDescription = findViewById(R.id.etDescription)
        btnSave = findViewById(R.id.btnSave)

        btnSave?.setOnClickListener{
            val myUrl = "https://demo.resthapi.com/group"
            val myRequest: StringRequest =
                object : StringRequest(
                    Method.POST, myUrl,
                    Response.Listener {
                        Toast.makeText(this@AddGroupActivity, "Succesfully Add Group", Toast.LENGTH_SHORT).show()
                        val a = Intent(this@AddGroupActivity, MainActivity::class.java)
                        startActivity(a)
                        finish()
                    },
                    Response.ErrorListener { volleyError: VolleyError->
                        Toast.makeText(this@AddGroupActivity, volleyError.message, Toast.LENGTH_SHORT).show()
                    }
                ){
                    override fun getParams(): Map<String, String>{
                        val params: MutableMap<String, String> = HashMap()
                        params["name"] = etNameGroup?.text.toString()
                        params["description"] = etDescription?.text.toString()
                        return  params
                    }

                    override fun getHeaders(): Map<String, String> {
                        val params: MutableMap<String, String> = HashMap()
                        params["content-type"] = "application/json"
                        params["Content-Type"] = "application/x-www-form-urlencoded"
                        return  params
                    }
                }
            val requestQueue = Volley.newRequestQueue(this)
            requestQueue.add(myRequest)
        }
    }
}