package com.example.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.AddShopItemUseCase
import com.example.shoppinglist.domain.EditShopItemUseCase
import com.example.shoppinglist.domain.GetShopItemUseCase
import com.example.shoppinglist.domain.ShopItem

class ShopItemViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val getShopItemUseCase = GetShopItemUseCase(repository)

    private val _errorInput = MutableLiveData<Boolean>()
    val errorInput: LiveData<Boolean>
        get() = _errorInput

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = inputName?.trim() ?: ""
        val count = try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
        if (validateInput(name, count)) {
            val shopItem = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(shopItem)
            _shouldCloseScreen.value = Unit
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = inputName?.trim() ?: ""
        val count = try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
        if (validateInput(name, count)) {
            _shopItem.value?.let {
                    val item = it.copy(name = name, count = count)
                    editShopItemUseCase.editShopItem(item)
                    _shouldCloseScreen.value = Unit
                }
        }
    }

    fun getShopItem(shopItemId: Int) {
        val item = getShopItemUseCase.getShopItem(shopItemId)
        _shopItem.value = item
    }

    private fun validateInput(name: String, count: Int): Boolean {
        if (name.isBlank() or (count <= 0)) {
            _errorInput.value = true
            return false
        }
        return true
    }

    fun resetErrorInputName() {
        _errorInput.value = false
    }

}