package com.example.shoppinglist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

//Передаём уже не RecyclerView, а ListAdapter: в УГЛОВЫХ скобках передаётся два параметра
//1-ый - тип элементов, который отображает RecyclerView
//2-ой - класс ViewHolder
//А дальше уже в КРУГЛЫХ скобках передаём класс DiffUtil
class ShopListAdapter : ListAdapter<ShopItem, ShopItemViewHolder>(
    ShopItemDiffCallback()
) {

    private var count = 1

    //То есть функция, которая принимает ShopItem и ничего не возвращает
    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    companion object {
        const val ENABLE_TYPE = 1
        const val DISABLE_TYPE = 0

        const val MAX_POOL_SIZE = 30
    }

    override fun getItemViewType(position: Int): Int {
        val shopItem = getItem(position)
        if (shopItem.enable) {
            return ENABLE_TYPE
        } else {
            return DISABLE_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        Log.d("TestCountViewHolders", "Номер Холдера: ${count++}")
        val layout: Int
        if (viewType == ENABLE_TYPE) {
            layout = R.layout.item_shop_enabled
        } else {
            layout = R.layout.item_shop_disabled
        }

        val view = LayoutInflater.from(parent.context).inflate(
            layout,
            parent,
            false
        )
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ShopItemViewHolder,
        position: Int
    ) {
        val shopItem = getItem(position)
        //Долгое нажатие, LONG
        holder.view.setOnLongClickListener {
            //Так как у нас нулебальная переменная, то мы должны использовать invoke
            //Если переменная не null, то только в этом случае вызовется метод
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        holder.view.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }
        holder.tvName.text = shopItem.name
        holder.tvCount.text = shopItem.count.toString()
    }
}