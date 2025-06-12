package com.example.shoppinglist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository
import kotlin.random.Random

class ShopListRepositoryImpl(application: Application) : ShopListRepository {

    private val shopListDao = AppDatabase.getInstance(application).shopListDao()
    private val mapper  = ShopListMapper()

    override fun getShopList(): LiveData<List<ShopItem>> {
        //В треугольных скобках указывается возвращаемый тип
        return MediatorLiveData<List<ShopItem>>().apply {
            //Добавляем источник с помощью addSource, он принимает LiveData, которую он будет перехватывать
            //Функция после addSource будет вызвана при каждом изменении в shopListDao.getShopList()
            //shopListDao.getShopList() возвращает LiveData
            addSource(shopListDao.getShopList()) {
                value = mapper.mapListDbModelToListEntity(it)
            }
        }
    }

    override suspend fun getShopItem(shopItemId: Int): ShopItem {
        val dbModel = shopListDao.getShopItem(shopItemId)
        return mapper.mapDbModelToEntity(dbModel)
    }

    override suspend fun editShopItem(shopItem: ShopItem) {
        //Делаем add, так как у нас перезаписывает добавление, если id элементов совпадает
        addShopItem(shopItem)
    }

    override suspend fun deleteShopItem(shopItem: ShopItem) {
        shopListDao.deleteShopItem(shopItem.id)
    }

    override suspend fun addShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }
}