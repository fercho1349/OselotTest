package com.tlilektik.oselottest.iu

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.transition.Explode
import android.view.Window
import com.squareup.picasso.Picasso
import com.tlilektik.oselottest.R
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.card_view_detail.*

class DetailActivity: AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            // set an exit transition
            exitTransition = Explode()
        }

        setContentView(R.layout.activity_detail)

        val toolbar: Toolbar = findViewById(R.id.toolbars)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val b = intent.extras
        Picasso.get().load(b.getString("thumbnail")).into(picture)
        name.text = "Name: "+b.getString("name")
        age.text = "Age: "+b.getString("age")
        weight.text = "Weight: "+b.getString("weight")
        height.text = "Height: "+b.getString("height")
        hair_color.text = "Hair Color: "+b.getString("hair_color")
        professions.text = "Professions: "+b.getString("professions")
            .replace("[", "").replace("]","")
        friends.text = "Friends: " + b.getString("friends")
            .replace("[", "").replace("]","")
        gender.text = "Gender: Male"

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}