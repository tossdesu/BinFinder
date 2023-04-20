package com.tossdesu.bankcardinfo.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.allViews
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tossdesu.bankcardinfo.R
import com.tossdesu.bankcardinfo.databinding.FragmentCardSearchBinding
import com.tossdesu.bankcardinfo.domain.entity.CardInfo
import com.tossdesu.bankcardinfo.presentation.adapter.CardBinsAdapter
import javax.inject.Inject

class CardSearchFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var cardBinsAdapter: CardBinsAdapter

    private val component by lazy {
        (requireActivity().application as BinFinderApp).component
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[CardSearchViewModel::class.java]
    }

    private var _binding: FragmentCardSearchBinding? = null
    private val binding: FragmentCardSearchBinding
        get() = _binding ?: throw RuntimeException("FragmentCardSearchBinding = null")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        getBinSearchHistory()
        observeBinSearching()
        observeSearchView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getBinSearchHistory() {
        viewModel.getHistory()
    }

    private fun initAdapter() {
        binding.rvBinsHistory.adapter = cardBinsAdapter
        // Set lambda for on adapter item click event
        cardBinsAdapter.onHistoryBinClick = { bin ->
            viewModel.getCardInfo(bin, true)
            binding.searchView.setQuery(bin, false)
        }
    }

    private fun observeBinSearching() {
        with(binding) {
            viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
                // Enable SearchView and RecyclerView if they were disabled when loading data
                if (!searchView.isEnabled)
                    enableSearchView()
                if (!rvBinsHistory.isEnabled)
                    enableRecyclerView()
                // Hide progressBar in all cases except when loading search history of bin numbers
                if (progressBar.isShown && uiState !is CardSearchFragmentUiState.BinSearchHistoryData)
                    progressBar.visibility = View.GONE
                // Analysing what we should do with fragment UI
                when (uiState) {
                    is CardSearchFragmentUiState.CardData -> {
                        // Collect data from ViewModel when fragment view is in RESUMED state only
                        if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                            launchCardInfoFragment(uiState.cardInfo)
                            searchView.setQuery("", false)
                        }
                    }
                    is CardSearchFragmentUiState.BinSearchHistoryData -> {
                        if (uiState.cardBins.isNotEmpty()) {
                            if (tvHistoryEmpty.isShown) {
                                tvHistoryEmpty.visibility = View.GONE
                                rvBinsHistory.visibility = View.VISIBLE
                            }
                            cardBinsAdapter.submitList(uiState.cardBins) {
                                rvBinsHistory.scrollToPosition(0)
                            }
                        }
                    }
                    is CardSearchFragmentUiState.Loading -> {
                        // Disable SearchView and RecyclerView when loading data
                        enableSearchView(false)
                        enableRecyclerView(false)
                        // Show progressBar
                        progressBar.visibility = View.VISIBLE
                    }
                    is CardSearchFragmentUiState.Error -> {
                        val message =
                            resources.getText(uiState.messageStringResource).toString()
                        showError(message)
                    }
                    is CardSearchFragmentUiState.NoConnectionError -> {
                        showNoConnectionError()
                    }
                    is CardSearchFragmentUiState.NothingFoundNotification -> {
                        val message = resources.getText(R.string.nothing_found).toString()
                        showDialog(message = message)
                    }
                    is CardSearchFragmentUiState.FatalError -> {
                        showDialog(uiState.title, uiState.message)
                    }
                }
            }
        }
    }

    private fun observeSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    hideKeyBoard()
                    viewModel.getCardInfo(query)
                    return true
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun launchCardInfoFragment(cardInfo: CardInfo) {
        val infoFragment = CardInfoFragment.newInstance(getSearchBinString(), cardInfo)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, infoFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showError(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun showNoConnectionError() {
        Snackbar.make(
            binding.root,
            resources.getText(R.string.no_internet_connection).toString(),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(resources.getText(R.string.reload_button)) {
                viewModel.getCardInfo(getSearchBinString())
            }.show()
    }

    private fun showDialog(title: String? = null, message: String) {
        val builder = AlertDialog.Builder(requireContext())
        with(builder) {
            title?.let { setTitle(title) }
            setMessage(message)
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    private fun hideKeyBoard() {
        (requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE)
                as InputMethodManager).apply {
            hideSoftInputFromWindow(binding.root.windowToken, 0)
        }
    }

    private fun getSearchBinString(): String = binding.searchView.query.toString()

    private fun enableSearchView(isEnable: Boolean = true) {
        binding.searchView.allViews.forEach { it.isEnabled = isEnable }
    }

    private fun enableRecyclerView(isEnable: Boolean = true) {
        binding.rvBinsHistory.allViews.forEach { it.isEnabled = isEnable }
    }
}