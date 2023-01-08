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

class EditGroupActivity : AppCompatActivity() {
    var etName: EditText? = null
    var etDescription: EditText? = null
    var btnUpdate: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_group)

        etName = findViewById(R.id.etName)
        etDescription = findViewById(R.id.etDescription)
        btnUpdate = findViewById(R.id.btnEdit)

        etName?.setText(intent.getStringExtra("name"))
        etDescription?.setText(intent.getStringExtra("description"))

        btnUpdate?.setOnClickListener{
            val myUrl = "https://demo.resthapi.com/group/" + intent.getStringExtra("id")
            val myRequest: StringRequest =
                object : StringRequest(
                    Method.PUT, myUrl,
                    Response.Listener {
                        Toast.makeText(this@EditGroupActivity, "Succesfully Edit Group", Toast.LENGTH_SHORT).show()
                        val a = Intent(this@EditGroupActivity, MainActivity::class.java)
                        startActivity(a)
                        finish()
                    },
                    Response.ErrorListener { volleyError: VolleyError ->
                        Toast.makeText(this@EditGroupActivity, volleyError.message, Toast.LENGTH_SHORT).show()
                    }
                ){
                    override fun getParams(): Map<String, String>{
                        val params: MutableMap<String, String> = HashMap()
                        params["name"] = etName?.text.toString()
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