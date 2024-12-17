package com.keithsmyth.androidshowcase.view.search

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.keithsmyth.androidshowcase.R
import com.keithsmyth.androidshowcase.databinding.FragmentSearchComposeBinding
import com.keithsmyth.androidshowcase.domain.model.ListItemDomainModel
import com.keithsmyth.androidshowcase.view.MainNavigation
import com.keithsmyth.androidshowcase.view.theme.AndroidShowcaseTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchComposeFragment : Fragment(R.layout.fragment_search_compose) {

    @Inject
    lateinit var mainNavigation: MainNavigation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSearchComposeBinding.bind(view)
        val viewModel: SearchViewModel by viewModels()

        viewModel.ensureRefreshList()

        binding.searchComposeView.setContent {
            AndroidShowcaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SearchScreen(
                        viewModel,
                        navigateToDetail = { id ->
                            mainNavigation.navigateToDetail(
                                id,
                                findNavController()
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun SearchScreen(viewModel: SearchViewModel, navigateToDetail: (id: Int) -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Column {
        SearchBox(
            searchTerm = state.searchTerm,
            onSearchTermUpdated = viewModel::updateSearchTerm,
            modifier = Modifier.fillMaxWidth(),
        )
        SearchList(
            results = state.resultListItems,
            navigateToDetail = navigateToDetail,
            modifier = Modifier.fillMaxHeight(),
        )
    }
}

@Composable
private fun SearchBox(
    searchTerm: String,
    onSearchTermUpdated: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextField(
        value = searchTerm,
        onValueChange = onSearchTermUpdated,
        modifier = modifier,
    )
}

@Composable
private fun SearchList(
    results: List<ListItemDomainModel>,
    navigateToDetail: (id: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(results) { listItem ->
            SearchResultItem(
                listItem = listItem,
                navigateToDetail = navigateToDetail,
                modifier = Modifier.fillParentMaxWidth(),
            )
        }
    }
}

@Composable
private fun SearchResultItem(
    listItem: ListItemDomainModel,
    navigateToDetail: (id: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = listItem.name,
        modifier = modifier.then(
            Modifier
                .padding(8.dp)
                .clickable { navigateToDetail(listItem.id) }
        )
    )
}
