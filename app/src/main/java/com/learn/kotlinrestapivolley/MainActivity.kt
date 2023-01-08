package com.learn.kotlinrestapivolley

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    var rvGroups: RecyclerView? = null
    var btnAdd: FloatingActionButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvGroups = findViewById(R.id.rvGroup)
        btnAdd = findViewById(R.id.btnAdd)

        getAllGroups()

        btnAdd?.setOnClickListener{
            val a = Intent(this@MainActivity, AddGroupActivity::class.java)
            startActivity(a)
        }
    }

    fun getAllGroups() {
        val groupList: MutableList<GroupModel> = ArrayList()
        val myUrl = "https://demo.resthapi.com/group"
        val myRequest = StringRequest(
            Request.Method.GET, myUrl,
            { response: String? ->
                var myJsonObject: JSONObject? = null
                try {
                    myJsonObject = response?.let { JSONObject(it) }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                try {
                    for (i in 0 until myJsonObject?.getJSONArray("docs")?.length()!!) {
                        val groupModel = GroupModel()
                        groupModel._id =
                            myJsonObject.getJSONArray("docs").getJSONObject(i).getString("_id")
                        groupModel.name =
                            myJsonObject.getJSONArray("docs").getJSONObject(i).getString("name")
                        groupModel.description = myJsonObject.getJSONArray("docs").getJSONObject(i)
                            .getString("description")
                        groupModel.createdAt = myJsonObject.getJSONArray("docs").getJSONObject(i)
                            .getString("createdAt")
                        groupList.add(groupModel)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                val groupAdapter = GroupAdapter(groupList, this)
                val mLayout: RecyclerView.LayoutManager = LinearLayoutManager(this@MainActivity)
                rvGroups?.layoutManager = mLayout
                rvGroups?.itemAnimator = DefaultItemAnimator()
                rvGroups?.adapter = groupAdapter
            }
        ) { volleyError: VolleyError ->
            Toast.makeText(this@MainActivity, volleyError.message, Toast.LENGTH_SHORT).show()
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(myRequest)
    }

}

internal class GroupAdapter(groups: List<GroupModel>, context: Context) :
    RecyclerView.Adapter<GroupAdapter.ViewHolder>() {
    private val groups: List<GroupModel>
    private var context: Context

    init {
        this.groups = groups
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.group_adapter, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group: GroupModel = groups[position]
        holder.tvnama.text = group.name
        holder.tvDescription.text = group.description
        holder.btnEdit.setOnClickListener {
            val a= Intent(context,EditGroupActivity::class.java)
            a.putExtra("id", group._id)
            a.putExtra("name", group.name)
            a.putExtra("description", group.description)
            context.startActivity(a)
        }

        holder.btnDelete.setOnClickListener {
            val myUrl = "https://demo.resthapi.com/group/" + group._id
            val myRequest: StringRequest =
                object : StringRequest(
                    Method.DELETE, myUrl,
                    Response.Listener {
                        Toast.makeText(context, "Succesfully Delete Group", Toast.LENGTH_SHORT).show()
                        (context as MainActivity).getAllGroups()
                    },
                    Response.ErrorListener { volleyError: VolleyError ->
                        Toast.makeText(context, volleyError.message, Toast.LENGTH_SHORT).show()
                    }
                ){
                    override fun getHeaders(): Map<String, String> {
                        val params: MutableMap<String, String> = HashMap()
                        params["content-type"] = "application/json"
                        params["Content-Type"] = "application/x-www-form-urlencoded"
                        return  params
                    }
                }
            val requestQueue = Volley.newRequestQueue(context)
            requestQueue.add(myRequest)
        }
    }

    override fun getItemCount(): Int {
        return groups.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvnama: TextView
        var tvDescription: TextView
        var btnEdit: ImageButton
        var btnDelete: ImageButton

        init {
            tvnama = itemView.findViewById(R.id.tvName)
            tvDescription = itemView.findViewById(R.id.tvDescription)
            btnEdit = itemView.findViewById(R.id.btnEdit)
            btnDelete = itemView.findViewById(R.id.btnHapus)
        }
    }
}