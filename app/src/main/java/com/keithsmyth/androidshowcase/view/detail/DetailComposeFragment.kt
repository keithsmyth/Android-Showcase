package com.keithsmyth.androidshowcase.view.detail

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.keithsmyth.androidshowcase.R
import com.keithsmyth.androidshowcase.databinding.FragmentDetailComposeBinding
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
    Text("Detail")
}

@Preview
@Composable
private fun Preview() {
    Text("Preview")
}
