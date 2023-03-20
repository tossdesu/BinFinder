package com.tossdesu.bankcardinfo.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.tossdesu.bankcardinfo.R
import com.tossdesu.bankcardinfo.databinding.ActivityBinInfoBinding
import com.tossdesu.bankcardinfo.domain.entity.CardInfo

class CardInfoActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityBinInfoBinding.inflate(layoutInflater)
    }

    private lateinit var binString: String
    private lateinit var cardInfo: CardInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.card_info_activity_label)

        parsIntent()
        bindViews()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun parsIntent() {
        if (!intent.hasExtra(EXTRA_BIN_NUMBER))
            throw RuntimeException("EXTRA_BIN_NUMBER param is absent")
        if (!intent.hasExtra(EXTRA_CARD))
            throw RuntimeException("EXTRA_CARD param is absent")

        binString = intent.getStringExtra(EXTRA_BIN_NUMBER).toString()
        cardInfo = intent.getParcelableExtra(EXTRA_CARD)
            ?: throw RuntimeException("CardInfo contains null")
    }

    private fun bindViews() {
        with(binding) {
            //Bin number
            tvBinNumber.text = binString

            //Bank
            tvBankName.text = cardInfo.bankName ?: NO_INFO
            tvUrl.text = cardInfo.bankUrl ?: NO_INFO
            cardInfo.bankUrl?.let {
                setOnUrlClickListener(it)
                setClickableTextColor(tvUrl)
            }
            tvPhone.text = cardInfo.bankPhone ?: NO_INFO
            cardInfo.bankPhone?.let {
                setOnPhoneClickListener(it)
                setClickableTextColor(tvPhone)
            }
            tvCity.text = cardInfo.bankCity ?: NO_INFO

            //Country
            tvCountryAlpha2.text = cardInfo.countryAlpha2
            tvCountry.text = cardInfo.countryName
            tvCoordinates.text = getCoordinatesString()
            setOnCoordinatesClickListener(cardInfo.latitude, cardInfo.longitude)
            setClickableTextColor(tvCoordinates)
            tvCurrency.text = cardInfo.currency

            //Additional information
            tvScheme.text = cardInfo.cardScheme
            tvCardType.text = cardInfo.cardType ?: NO_INFO
            tvBrand.text = cardInfo.cardBrand ?: NO_INFO
            tvPrepaid.text = cardInfo.isCardPrepaid?.toString() ?: NO_INFO
            tvCardNumberLength.text = cardInfo.cardNumberLength?.toString() ?: NO_INFO
            tvCardNumberLuhn.text = cardInfo.isCardLuhn?.toString() ?: NO_INFO
        }
    }

    private fun getCoordinatesString() =
        "(latitude: ${cardInfo.latitude}, longitude: ${cardInfo.longitude})"

    private fun setOnPhoneClickListener(phoneNumber: String) {
        binding.tvPhone.setOnClickListener {
            val phoneIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
            startActivity(phoneIntent)
        }
    }

    private fun setOnUrlClickListener(url: String) {
        binding.tvUrl.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://$url"))
            startActivity(browserIntent)
        }
    }

    private fun setOnCoordinatesClickListener(latitude: Float, longitude: Float) {
        binding.tvCoordinates.setOnClickListener {
            val mapIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("geo:$latitude,$longitude")
            )
            startActivity(mapIntent)
        }
    }

    private fun setClickableTextColor(textView: TextView) {
        textView.setTextColor(ContextCompat.getColor(this, R.color.blue_500))
    }

    companion object {

        private const val NO_INFO = "нет данных"

        private const val EXTRA_CARD = "extra_card"
        private const val EXTRA_BIN_NUMBER = "extra_bin_number"

        fun newIntent(context: Context, bin: String, cardInfo: CardInfo): Intent {
            return Intent(context, CardInfoActivity::class.java).apply {
                putExtra(EXTRA_BIN_NUMBER, bin)
                putExtra(EXTRA_CARD, cardInfo)
            }
        }
    }
}