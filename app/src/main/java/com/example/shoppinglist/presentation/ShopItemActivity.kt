package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem.Companion.UNDEFINED_ID
import com.example.shoppinglist.presentation.MainActivity
import com.google.android.material.textfield.TextInputLayout

class ShopItemActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    private var shopItemId = UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_shop_item)

        shopItemId = intent.getIntExtra(KEY_SHOP_ITEM_ID, UNDEFINED_ID)

        if (savedInstanceState == null) {
            val fragment = ShopItemFragment.newInstance(shopItemId)
            supportFragmentManager.beginTransaction()
                .replace(R.id.shop_item_container, fragment)
                .commit()
        }

    }

    override fun onEditingFinished() {
        finish()
    }

    companion object {
        private const val UNDEFINED_ID = -1
        private const val KEY_SHOP_ITEM_ID = "shopItemId"

        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            //Создаём в классе ShopItemActivity
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(KEY_SHOP_ITEM_ID, shopItemId)
            return intent
        }

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            return intent
        }

    }
}