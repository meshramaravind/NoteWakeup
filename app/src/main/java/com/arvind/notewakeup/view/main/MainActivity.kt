package com.arvind.notewakeup.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.room.RoomDatabase
import com.arvind.notewakeup.R
import com.arvind.notewakeup.databinding.ActivityMainBinding
import com.arvind.notewakeup.repository.NoteRepository
import com.arvind.notewakeup.storage.db.NoteDatabase
import com.arvind.notewakeup.utils.hide
import com.arvind.notewakeup.utils.show
import com.arvind.notewakeup.utils.viewModelFactory
import com.arvind.notewakeup.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val repo by lazy { NoteRepository(NoteDatabase(this)) }
    private val viewModel: NoteViewModel by viewModels {
        viewModelFactory { NoteViewModel(this.application, repo) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel

        initViews(binding)
        observeNavElements(binding, navHostFragment.navController)
    }

    private fun observeNavElements(binding: ActivityMainBinding, navController: NavController) {

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {

                R.id.dashboardFragment -> {
                    supportActionBar!!.setDisplayShowTitleEnabled(false)
                    binding.tvToolbarTitle.show()

                }
                R.id.addNoteFragment -> {
                    supportActionBar!!.setDisplayShowTitleEnabled(true)
                    binding.tvToolbarTitle.hide()
                }

                R.id.updateNoteFragment -> {
                    supportActionBar!!.setDisplayShowTitleEnabled(true)
                    binding.tvToolbarTitle.hide()
                }

                R.id.searchNoteFragment -> {
                    supportActionBar!!.setDisplayShowTitleEnabled(false)
                    binding.tvToolbarTitle.hide()
                }

                else -> {
                    supportActionBar!!.setDisplayShowTitleEnabled(true)
                }
            }
        }

    }

    private fun initViews(binding: ActivityMainBinding) {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
            ?: return

        with(navHostFragment.navController) {
            appBarConfiguration = AppBarConfiguration(graph)
            setupActionBarWithNavController(this, appBarConfiguration)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        navHostFragment.navController.navigateUp()
        return super.onSupportNavigateUp()
    }

}