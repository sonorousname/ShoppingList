package com.example.shoppinglist.data

import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository

object ShopListRepositoryImpl : ShopListRepository {

    private val shopList = mutableListOf<ShopItem>()

    private var autoIncrementId = 0

    override fun getShopList(): List<ShopItem> {
        return shopList.toList()
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList[shopItemId] ?: throw RuntimeException("Not found")
    }

    override fun editShopItem(shopItem: ShopItem) {
        shopList.remove(shopList[shopItem.id])
        addShopItem(shopItem)
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
    }

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) shopItem.id = autoIncrementId++
        shopList.add(shopItem)
    }
}