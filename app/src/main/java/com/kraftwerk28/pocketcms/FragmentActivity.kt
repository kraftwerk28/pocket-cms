package com.kraftwerk28.pocketcms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI

class FragmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)
        val nav = this.findNavController(R.id.nav_graph)
        NavigationUI.setupActionBarWithNavController(this, nav)
    }

    override fun onSupportNavigateUp(): Boolean {
        val nav = this.findNavController(R.id.nav_graph)
        return nav.navigateUp()
    }
}
