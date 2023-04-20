package com.tossdesu.bankcardinfo.presentation

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.tossdesu.bankcardinfo.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setBackStackChangeListener()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFragmentManager.popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBackStackChangeListener() {
        supportFragmentManager.apply {
            addOnBackStackChangedListener {

                val isEnabled = backStackEntryCount > 0
                supportActionBar?.setDisplayHomeAsUpEnabled(isEnabled)

                val title = if (fragments[fragments.size - 1] is CardInfoFragment) {
                    R.string.card_info_fragment_label
                } else {
                    R.string.app_name
                }
                supportActionBar?.setTitle(title)
            }
        }
    }
}