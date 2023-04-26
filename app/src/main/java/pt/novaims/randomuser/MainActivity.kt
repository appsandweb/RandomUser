package pt.novaims.randomuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var getUserButton: Button
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var queue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getUserButton = findViewById(R.id.getUserButton)
        nameTextView = findViewById(R.id.nameTextView)
        emailTextView = findViewById(R.id.emailTextView)
        phoneTextView = findViewById(R.id.phoneTextView)
        imageView = findViewById(R.id.imageView)

        queue = Volley.newRequestQueue(this)
        getUserButton.setOnClickListener {
            fetchUser()
        }
    }
    private fun fetchUser() {
        val url = "https://randomuser.me/api/"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    val results = jsonResponse.getJSONArray("results")
                    if (results.length() > 0) {
                        val user = results.getJSONObject(0)
                        val name = user.getJSONObject("name")
                        val firstName = name.getString("first")
                        val lastName = name.getString("last")
                        val email = user.getString("email")
                        val phone = user.getString("phone")
                        nameTextView.text = "$firstName $lastName"
                        emailTextView.text = email
                        phoneTextView.text = phone

                        val picture = user.getJSONObject("picture")
                        val pictureUrl = picture.getString("large")
                        Glide.with(this)
                            .load(pictureUrl)
                            .into(imageView)
                    }
                } catch (e: JSONException) {
                    Log.e(TAG, "Error parsing JSON", e)
                }
            },
            Response.ErrorListener { error ->
                Log.e(TAG, "Error fetching user data", error)
            })
        queue.add(stringRequest)
    }
    companion object {
        private const val TAG = "MainActivity"
    }
}