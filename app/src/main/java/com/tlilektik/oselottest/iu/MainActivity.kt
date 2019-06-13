package com.tlilektik.oselottest.iu

import android.app.ActivityOptions
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.support.v4.widget.DrawerLayout
import android.support.design.widget.NavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import com.tlilektik.oselottest.service.APIService
import com.tlilektik.oselottest.entity.ItemObject
import com.tlilektik.oselottest.view.list.RecyclerViewAdapter
import com.tlilektik.oselottest.service.Response
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.SearchView
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.tlilektik.oselottest.utils.CircleTransform
import android.widget.Toast
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi
import android.transition.Explode
import android.view.Window
import com.tlilektik.oselottest.R


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    RecyclerViewAdapter.RecyclerViewClickListener {

    lateinit var arrayList: ArrayList<ItemObject>


    override fun recyclerViewListClicked(v: ImageView?, position: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //val intent : Intent = DetailActivity.makeIntent(applicationContext, album)
            val intent = Intent(applicationContext, DetailActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this, v,"OselotTest")
            intent.putExtra("thumbnail",arrayList[position].thumbnail)
            intent.putExtra("name",arrayList[position].name)
            intent.putExtra("age",arrayList[position].age.toString())
            intent.putExtra("weight",arrayList[position].weight.toString())
            intent.putExtra("height",arrayList[position].height.toString())
            intent.putExtra("hair_color",arrayList[position].hair_color)
            intent.putExtra("professions",arrayList[position].professions.toString())
            intent.putExtra("friends",arrayList[position].friends.toString())
            startActivity(intent, options!!.toBundle())
        }
    }

    lateinit var rvAdapter : RecyclerViewAdapter

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            // set an exit transition
            exitTransition = Explode()
        }

        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        getRetrofit()
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
        val headerView = navView.getHeaderView(0)
        val imageViewNavHeader = headerView.findViewById<ImageView>(R.id.imageViewNavHeader)
        Picasso.get().load(R.mipmap.foto).transform(CircleTransform()).into(imageViewNavHeader)

    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_share -> {
                val pm = packageManager
                try {
                    val waIntent = Intent(Intent.ACTION_SEND)
                    waIntent.type = "text/plain"
                    val text = "https://github.com/fercho1349/OselotTest"

                    val info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA)
                    //Check if package exists or not. If not then code
                    //in catch block will be called
                    waIntent.setPackage("com.whatsapp")

                    waIntent.putExtra(Intent.EXTRA_TEXT, text)
                    startActivity(Intent.createChooser(waIntent, "Share with"))

                } catch (e: PackageManager.NameNotFoundException) {
                    Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                        .show()
                }

            }
            R.id.nav_send -> {
                try {
                    packageManager.getPackageInfo("com.linkedin.android", 0)
                    intent = Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://profile/fernando-gálvez-castillo-a8aa21a4"))
                } catch (e: Exception) {
                    Toast.makeText(this, "LinkedIn not Installed", Toast.LENGTH_SHORT)
                        .show()
                } finally {
                    startActivity(intent)
                }
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initCharacter(array: ArrayList<ItemObject>) {

        arrayList = array

        if(arrayList.isNotEmpty()){

            val recyclerView = findViewById<RecyclerView>(R.id.mi_recicler)
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

            rvAdapter =
                RecyclerViewAdapter(
                    arrayList,
                    applicationContext,
                    arrayList,
                    this
                )
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = rvAdapter

        }else{
            showErrorDialog()
        }

    }

    private fun getRetrofit() {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(APIService::class.java)
        val call = api.getList()
        call.enqueue(object : Callback<Response> {
            override fun onFailure(call: Call<Response>?, t: Throwable?) {
              t!!.printStackTrace()
            }

            override fun onResponse(call: Call<Response>?, response: retrofit2.Response<Response>?) {
                searchByName(response!!.body()!!.array)
            }
        })

    }


    private fun searchByName(array: ArrayList<ItemObject>) {
        if(array.isNotEmpty()) {
            initCharacter(array)
        }else{
            showErrorDialog()
        }
        hideKeyboard()
    }


    private fun hideKeyboard(){
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(drawer_layout.windowToken, 0)
    }

    private fun showErrorDialog() {
      Toast.makeText(this, "Ha ocurrido un error, inténtelo de nuevo.", Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        val search = menu!!.findItem(R.id.search)
        val searchView = MenuItemCompat.getActionView(search) as SearchView
        search(searchView)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }

    fun search(searchView: SearchView){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                rvAdapter.filter.filter(newText)
                return true
            }
        })
    }



}
