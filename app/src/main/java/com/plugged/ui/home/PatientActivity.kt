package com.plugged.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.plugged.R
import com.plugged.utils.MyPreferences
import com.plugged.viewmodel.PluggedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_patient.*
@AndroidEntryPoint
class PatientActivity : AppCompatActivity() {
    private val viewModel: PluggedViewModel by viewModels()

    lateinit var toolbar: Toolbar
    lateinit var navView: NavigationView

    private val navController by lazy {
        Navigation.findNavController(this, R.id.nav_host_fragment)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, 0, 0
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        setupDrawerLayout()

        val logOut = navView.menu.findItem(R.id.logOut)
        logOut.setOnMenuItemClickListener {

            viewModel.deletePatient()
            MyPreferences(this).is_staff = false
            MyPreferences(this).logged_in = false
            this.finishAffinity()
            return@setOnMenuItemClickListener true
        }


    }
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setupDrawerLayout() {
        nav_view.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)
    }
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawer_layout)
    }
}