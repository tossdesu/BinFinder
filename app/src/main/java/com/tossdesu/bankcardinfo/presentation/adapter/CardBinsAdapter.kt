package com.tossdesu.bankcardinfo.presentation.adapter

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tossdesu.bankcardinfo.databinding.ItemCardBinBinding
import com.tossdesu.bankcardinfo.domain.entity.CardBin
import javax.inject.Inject

class CardBinsAdapter @Inject constructor(
    val application: Application
) :
    ListAdapter<CardBin, CardBinHolder>(CardBinDiffCallback()) {

    var onHistoryBinClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardBinHolder {
        val binding = ItemCardBinBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CardBinHolder(binding)
    }

    override fun onBindViewHolder(holder: CardBinHolder, position: Int) {
        val item = getItem(holder.adapterPosition)
        with(holder.binding) {
            tvCardBin.text = item.bin
            root.setOnClickListener {
                onHistoryBinClick?.invoke(item.bin)
            }
        }
    }
}
