package com.keithsmyth.androidshowcase.view.search

import android.view.View
import com.keithsmyth.androidshowcase.R
import com.keithsmyth.androidshowcase.databinding.ItemSearchResultBinding
import com.keithsmyth.androidshowcase.domain.model.ListItemDomainModel
import com.keithsmyth.androidshowcase.view.BindingItem

class SearchResultBindingItem(
    val model: ListItemDomainModel,
    private val onItemClick: (id: Int) -> Unit,
) : BindingItem {

    override val layoutRes: Int
        get() = R.layout.item_search_result

    override fun bind(layout: View) {
        ItemSearchResultBinding.bind(layout).apply {
            searchResultTextView.text = model.name
            root.setOnClickListener { onItemClick(model.id) }
        }
    }

    override fun hasSamePrimaryKey(newItem: BindingItem): Boolean {
        return model.id == (newItem as SearchResultBindingItem).model.id
    }

    override fun hasSameContents(newItem: BindingItem): Boolean {
        return model == (newItem as SearchResultBindingItem).model
    }
}
