package com.tossdesu.bankcardinfo.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tossdesu.bankcardinfo.domain.entity.CardBin

class CardBinDiffCallback : DiffUtil.ItemCallback<CardBin>() {
    override fun areItemsTheSame(oldItem: CardBin, newItem: CardBin): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CardBin, newItem: CardBin): Boolean {
        return oldItem == newItem
    }
}