package com.tossdesu.bankcardinfo.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tossdesu.bankcardinfo.databinding.ItemCardBinBinding
import com.tossdesu.bankcardinfo.domain.entity.CardBin
import javax.inject.Inject

class CardBinsAdapter @Inject constructor() :
    ListAdapter<CardBin, CardBinHolder>(CardBinDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardBinHolder {
        val binding = ItemCardBinBinding.inflate(LayoutInflater.from(parent.context))
        return CardBinHolder(binding)
    }

    override fun onBindViewHolder(holder: CardBinHolder, position: Int) {
        val item = getItem(holder.adapterPosition)
        holder.binding.tvCardBin.text = item.bin
    }
}
