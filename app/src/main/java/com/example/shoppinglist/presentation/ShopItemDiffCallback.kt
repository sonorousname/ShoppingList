package com.example.shoppinglist.presentation

import androidx.recyclerview.widget.DiffUtil
import com.example.shoppinglist.domain.ShopItem

class ShopItemDiffCallback: DiffUtil.ItemCallback<ShopItem>() {

    //Принимает старый и новый элементы для сравнения
    //Сначала сравниваем id. Если id одинаковые, то это один и тот же элемент
    override fun areItemsTheSame(
        oldItem: ShopItem,
        newItem: ShopItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    //А этот метод вызывается если id одинаковые и уже сравнивает все поля, чтобы понять какие изменились
    override fun areContentsTheSame(
        oldItem: ShopItem,
        newItem: ShopItem
    ): Boolean {
        return oldItem == newItem
    }

}