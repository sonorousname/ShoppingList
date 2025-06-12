package com.example.shoppinglist.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment : androidx.fragment.app.Fragment() {

    private lateinit var viewModel: ShopItemViewModel

    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var buttonSave: Button
    private var shopItemId: Int = UNDEFINED_ID

    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditingFinishedListener) {
            onEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must implement OnEditingFinishedListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]

        //В методе onViewCreated вызываем метод requireArguments, получаем все значение
        val args = requireArguments()
        //А уже из всех значение получим по ключу нужное, если его нет, то по-умолчанию UNDEFINED_ID
        shopItemId = args.getInt(KEY_SHOP_ITEM_ID, UNDEFINED_ID)
        when (shopItemId) {
            UNDEFINED_ID -> launchAddMode()
            else -> launchEditMode()
        }

        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        viewModel.errorInput.observe(viewLifecycleOwner) {
            if (it) {
                tilName.error = "Неправильный ввод"
                tilCount.error = "Неправильный ввод"
            } else {
                tilName.error = null
                tilCount.error = null
            }
        }

        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
//            activity?.onBackPressed() //Вместо finish() во фрагменте
//            finish()
        }
    }


    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(viewLifecycleOwner) {
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        buttonSave.setOnClickListener {
            viewModel.editShopItem(etName.text.toString(), etCount.text.toString())
        }
    }

    private fun launchAddMode() {
        buttonSave.setOnClickListener {
            viewModel.addShopItem(etName.text.toString(), etCount.text.toString())
        }
    }

    private fun initViews(view: View) {
        tilName = view.findViewById(R.id.til_name)
        tilCount = view.findViewById(R.id.til_count)
        etName = view.findViewById(R.id.et_text)
        etCount = view.findViewById(R.id.et_count)
        buttonSave = view.findViewById(R.id.save_button)
    }

    interface OnEditingFinishedListener {
        fun onEditingFinished()
    }

    companion object {

        private const val UNDEFINED_ID = -1
        private const val KEY_SHOP_ITEM_ID = "shopItemId"

        //Создаём метод создание экземпляра прямо во Фрагменте, который возвращает себя же, то есть Фрагмент
        //Мы хотим передать shopItemId фрагменту. Если ничего не передать, то по-умолчанию он будет -1
        fun newInstance(shopItemId: Int = UNDEFINED_ID): ShopItemFragment {
            //В методе создаём фрагмент и вписываем туда аргументы
            return ShopItemFragment().apply {
                //Аргументы должны быть типа Bundle, поэтому создаём их (КЛЮЧ - Значение)
                arguments = Bundle().apply {
                    putInt(KEY_SHOP_ITEM_ID, shopItemId)
                }
            }
        }

    }

}