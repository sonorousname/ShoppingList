package com.example.shoppinglist.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter
    private var shopItemContainer: FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        shopItemContainer = findViewById(R.id.shop_item_container)

        val buttonAddItem = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        buttonAddItem.setOnClickListener {
            if (shopItemContainer == null) {
                val intent = ShopItemActivity.newIntentAddItem(this)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstance())
            }
        }

        setupRecyclerView()

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.shopList.observe(this) {
            //Обновляем список с помощью submitList!!!
            shopListAdapter.submitList(it)
        }
    }

    override fun onEditingFinished() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            //Либо используем .add, тогда будут копиться фрагменты и при желании на кнопку назад
            //можно будет перейти на предыдущий фрагмент. Нам не нужно этого, нам нужно заменять
            //Чтобы при кнопке назад мы ушли с фрагментов
            .replace(R.id.shop_item_container, fragment)
            //Добавляем в стэк, указываем имя при необходимости
            .addToBackStack(null)
            //Обязательно коммитим, то есть приминяем все изменения!
            .commit()
    }

    private fun setupRecyclerView() {
        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)
        shopListAdapter = ShopListAdapter()
        rvShopList.adapter = shopListAdapter

        //Настраиваем пул
        rvShopList.recycledViewPool.setMaxRecycledViews(
            ShopListAdapter.ENABLE_TYPE,
            ShopListAdapter.MAX_POOL_SIZE
        )
        rvShopList.recycledViewPool.setMaxRecycledViews(
            ShopListAdapter.DISABLE_TYPE,
            ShopListAdapter.MAX_POOL_SIZE
        )

        //Говорим, что делать при долгом клике
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }

        //Говорим, что делать при обычном клике
        shopListAdapter.onShopItemClickListener = {
            if (shopItemContainer == null) {
                val intent = ShopItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstance(it.id))
            }
        }

        //Вынесли в отдельный метод удаление свайпом
        setupSwipeListener(rvShopList)
    }

    //Удаление свайпом
    fun setupSwipeListener(rvShopList: RecyclerView) {
        // В скобочках мы должны передать два параметра
        // 1-ый параметр: направление перемещения (метод onMove), мы его никак не используем, поэтому 0
        // 2-ой параметр: направление свайпа (только вправа, влево или в обе стороны)
        // ItemTouchHelper.LEFT / ItemTouchHelper.RIGHT - это пример в обе стороны ("or" значение или)
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            //метод onMove вызывается когда мы хотим переместить элементы с одного места на другое
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                //Так как нам не пригодится, то оставляем возвращаем false
                return false
            }

            //метод onSwiped вызывается при свайпе, то есть когда мы перемещаем элемент вправо или влево
            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {
                //viewHolder прилетает в качестве параметра и получаем позицию элемента, по которому был свайп
                val position = viewHolder.adapterPosition //Получаем позицию элемента
                //С помощью currentList получаем текущий список в адаптере!
                val shopItem = shopListAdapter.currentList[position]
                viewModel.deleteShopItem(shopItem)
            }
        }
        // Привязываем к RecyclerView
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }
}