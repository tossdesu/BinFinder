package com.tossdesu.bankcardinfo.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tossdesu.bankcardinfo.R
import com.tossdesu.bankcardinfo.databinding.ActivityMainBinding
import com.tossdesu.bankcardinfo.domain.entity.CardInfo
import com.tossdesu.bankcardinfo.presentation.MainActivityUiState.*
import com.tossdesu.bankcardinfo.presentation.adapter.CardBinsAdapter
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var cardBinsAdapter: CardBinsAdapter

    private val component by lazy {
        (application as BinFinderApp).component
    }

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    var searchBinQuery: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initAdapter()
        observeBinSearching()
        observeSearchView()
    }

    private fun initAdapter() {
        binding.rvBinsHistory.adapter = cardBinsAdapter
    }

    private fun observeBinSearching() {
        viewModel.uiState.observe(this) {
            when(it) {
                is CardData -> {
                    startCardInfoActivity(it.cardInfo)
                    binding.searchView.setQuery("", false)
                    hideProgressBar()
                }
                is BinSearchHistoryData -> {
                    if (it.cardBins.isNotEmpty()) {
                        binding.tvHistoryEmpty.visibility = View.GONE
                        binding.rvBinsHistory.visibility = View.VISIBLE
                        cardBinsAdapter.submitList(it.cardBins)
                    }
                }
                is Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is FatalError -> {
                    hideProgressBar()
                    showDialog(it.title, it.message)
                }
                is NoConnectionError -> {
                    hideProgressBar()
                    val message = resources.getText(R.string.no_internet_connection).toString()
                    showNoConnectionError(message)
                }
                is ValidateError -> {
                    hideProgressBar()
                    val message = resources.getText(R.string.bin_validate_error).toString()
                    showError(message)
                }
                is NothingFoundNotification -> {
                    hideProgressBar()
                    val message = resources.getText(R.string.nothing_found).toString()
                    showDialog(message = message)
                }
            }
        }
    }

    private fun observeSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                hideKeyBoard()
                searchBinQuery = query
                viewModel.getCardInfo(searchBinQuery)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun startCardInfoActivity(cardInfo: CardInfo) {
        val intent = CardInfoActivity.newIntent(
            this,
            searchBinQuery ?: "",
            cardInfo
        )
        startActivity(intent)
    }

    private fun showError(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun showNoConnectionError(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(resources.getText(R.string.reload_button)) {
                viewModel.getCardInfo(searchBinQuery)
            }.show()
    }

    private fun showDialog(title: String? = null, message: String) {
        val builder = AlertDialog.Builder(this)
        with(builder)
        {
            title?.let { setTitle(title) }
            setMessage(message)
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    private fun hideKeyBoard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }
}