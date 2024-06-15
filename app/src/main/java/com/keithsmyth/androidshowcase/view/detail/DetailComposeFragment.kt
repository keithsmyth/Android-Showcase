package com.keithsmyth.androidshowcase.view.detail

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keithsmyth.androidshowcase.R
import com.keithsmyth.androidshowcase.databinding.FragmentDetailComposeBinding
import com.keithsmyth.androidshowcase.domain.model.PokemonDomainModel
import com.keithsmyth.androidshowcase.view.theme.AndroidShowcaseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailComposeFragment : Fragment(R.layout.fragment_detail_compose) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDetailComposeBinding.bind(view)
        val viewModel: DetailViewModel by viewModels()

        binding.detailComposeView.setContent {
            AndroidShowcaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DetailScreen(viewModel)
                }

            }
        }
    }
}

@Composable
private fun DetailScreen(viewModel: DetailViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Box {
        if (state.isLoading) DetailLoading()
        state.detail?.let { detail -> DetailBody(detail) }
    }
}

@Composable
fun DetailLoading() {
    CircularProgressIndicator()
}

@Composable
private fun DetailBody(detail: PokemonDomainModel) {
    Column {
        DetailHeader(
            name = detail.name,
            number = detail.id.toString(),
        )
    }
}

@Composable
private fun DetailHeader(
    name: String,
    number: String,
) {
    Row {
        Column {
            Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(name, modifier = Modifier.weight(1F))
                Text(number)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    DetailHeader(
        name = "Oshawott",
        number = "7",
    )
}
