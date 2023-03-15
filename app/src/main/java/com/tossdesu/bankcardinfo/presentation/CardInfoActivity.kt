package com.tossdesu.bankcardinfo.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.tossdesu.bankcardinfo.R
import com.tossdesu.bankcardinfo.databinding.ActivityBinInfoBinding
import com.tossdesu.bankcardinfo.domain.entity.CardInfo

class CardInfoActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityBinInfoBinding.inflate(layoutInflater)
    }

    private lateinit var bin: String
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
            throw RuntimeException("Param EXTRA_BIN_NUMBER is absent")
        if (!intent.hasExtra(EXTRA_CARD))
            throw RuntimeException("Param EXTRA_CARD is absent")

        bin = intent.getStringExtra(EXTRA_BIN_NUMBER).toString()
        intent.getParcelableExtra<CardInfo>(EXTRA_CARD)?.let {
            cardInfo = it
        }
    }

    private fun bindViews() {
        with(binding) {
            //Bin number
            tvBinNumber.text = bin
            //Bank
            tvBankName.text = cardInfo.bankName ?: NO_INFO
            tvUrl.text = cardInfo.bankUrl ?: NO_INFO
            tvPhone.text = cardInfo.bankPhone ?: NO_INFO
            tvCity.text = cardInfo.bankCity ?: NO_INFO
            //Country
            tvCountryAlpha2.text = cardInfo.countryAlpha2
            tvCountry.text = cardInfo.countryName
            tvCoordinates.text = getCoordinatesString()
            tvCurrency.text = cardInfo.currency
            //Card
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

    companion object {

        private const val NO_INFO = "нет данных"

        private const val EXTRA_CARD = "extra_card"
        private const val EXTRA_BIN_NUMBER = "extra_bin_number"

        fun newIntent(context: Context, bin: String, cardInfo: CardInfo): Intent {
            val intent = Intent(context, CardInfoActivity::class.java)
            intent.putExtra(EXTRA_BIN_NUMBER, bin)
            intent.putExtra(EXTRA_CARD, cardInfo)
            return intent
        }
    }
}