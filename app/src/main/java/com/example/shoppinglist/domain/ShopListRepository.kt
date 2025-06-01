package com.example.shoppinglist.domain

import androidx.lifecycle.LiveData

interface ShopListRepository {

    fun getShopList(): LiveData<List<ShopItem>>

    fun getShopItem(shopItemId: Int): ShopItem

    fun editShopItem(shopItem: ShopItem)

    fun deleteShopItem(shopItem: ShopItem)

    fun addShopItem(shopItem: ShopItem)
}