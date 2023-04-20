package com.tossdesu.bankcardinfo.presentation

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tossdesu.bankcardinfo.R
import com.tossdesu.bankcardinfo.databinding.FragmentCardInfoBinding
import com.tossdesu.bankcardinfo.domain.entity.CardInfo

class CardInfoFragment : Fragment() {

    private var _binding: FragmentCardInfoBinding? = null
    private val binding: FragmentCardInfoBinding
        get() = _binding ?: throw RuntimeException("FragmentCardInfoBinding = null")

    private lateinit var binString: String
    private lateinit var cardInfo: CardInfo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parsArgs()
        bindViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parsArgs() {
        val args = requireArguments()
        args.containsKey(EXTRA_BIN_NUMBER)
        if (!args.containsKey(EXTRA_BIN_NUMBER))
            throw RuntimeException("EXTRA_BIN_NUMBER param is absent")
        if (!args.containsKey(EXTRA_CARD))
            throw RuntimeException("EXTRA_CARD param is absent")

        binString = args.getString(EXTRA_BIN_NUMBER).toString()
        cardInfo = (if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            args.getParcelable(EXTRA_CARD, CardInfo::class.java)
        } else {
            @Suppress("DEPRECATION")
            args.getParcelable<CardInfo>(EXTRA_CARD)
        }) ?: throw RuntimeException("CardInfo contains null")
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
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_500))
    }

    companion object {

        private const val NO_INFO = "нет данных"

        private const val EXTRA_CARD = "extra_card"
        private const val EXTRA_BIN_NUMBER = "extra_bin_number"

        fun newInstance(bin: String, cardInfo: CardInfo) =
            CardInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_BIN_NUMBER, bin)
                    putParcelable(EXTRA_CARD, cardInfo)
                }
            }
    }
}