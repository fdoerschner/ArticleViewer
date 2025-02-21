package de.schwarz.it.feature.addArticle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import de.schwarz.it.core.common.PaddingDefaults

/**
 * Article screen to display a fullscreen dialog with adaptive properties for a phone or a tablet.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddArticleScreen(onDismiss: () -> Unit, viewModel: AddArticleViewModel = hiltViewModel()) {
    Dialog(
        onDismissRequest = {
            onDismiss()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = LocalConfiguration.current.screenWidthDp > 600,
        ),
    ) {
        val viewState by viewModel.viewState.collectAsState()
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = onDismiss,
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "")
                        }
                    },
                    title = {
                        Text(stringResource(R.string.add_article_top_bar_title))
                    },
                )
            },
        ) {
            AddArticleContent(viewState, Modifier.padding(it))
        }
    }
}

@Composable
private fun AddArticleContent(viewState: AddArticleViewState, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .padding(PaddingDefaults.defaultPadding)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(PaddingDefaults.defaultPadding),
    ) {
        items(viewState.inputs) { input ->
            val error = input.inputError
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = input.value,
                label = {
                    Text(input.label.asString())
                },
                onValueChange = { newValue ->
                    input.onChange(newValue)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = when (input.inputType) {
                        InputType.Number -> KeyboardType.Decimal
                        InputType.Text -> KeyboardType.Text
                    },
                ),
                supportingText = {
                    when (error) {
                        InputError.InputOutOfBounds -> Text(stringResource(R.string.input_out_of_bound_error))
                        InputError.MandatoryNotFilled -> Text(stringResource(R.string.input_is_mandatory))
                        InputError.CodeAlreadyTaken -> Text(stringResource(R.string.input_is_not_unique))
                        null -> Unit
                    }
                },
                isError = error != null,
            )
        }
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Button(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = viewState.onSave,
                ) {
                    Text(stringResource(R.string.save))
                }
            }
        }
    }
}