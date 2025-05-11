package com.chilly.android.presentation.screens.place

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavGraphBuilder
import androidx.navigation.toRoute
import com.chilly.android.R
import com.chilly.android.activityComponent
import com.chilly.android.di.screens.DaggerPlaceInfoComponent
import com.chilly.android.di.screens.PlaceInfoComponent
import com.chilly.android.presentation.common.components.ChillyButton
import com.chilly.android.presentation.common.components.ChillyButtonColor
import com.chilly.android.presentation.common.components.ChillyButtonType
import com.chilly.android.presentation.common.components.ErrorReloadPlaceHolder
import com.chilly.android.presentation.common.components.LoadingPlaceholder
import com.chilly.android.presentation.common.components.PlaceImagesPager
import com.chilly.android.presentation.common.components.PlaceRatingDialog
import com.chilly.android.presentation.common.structure.NewsCollector
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.common.structure.collectState
import com.chilly.android.presentation.navigation.Destination
import com.chilly.android.presentation.navigation.fadingComposable
import com.chilly.android.presentation.screens.place.PlaceInfoEvent.UiEvent
import com.chilly.android.presentation.theme.ChillyTheme
import com.chilly.android.presentation.theme.LinkColor
import com.chilly.android.presentation.theme.Yellow70

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlaceInfoScreen(
    state: PlaceUiState,
    padding: PaddingValues,
    onEvent: (UiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val place = state.place ?: return@TopAppBar
                    Text(text = place.name)
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onEvent(UiEvent.BackClicked)
                        }
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { scaffoldPadding ->
        when {
            state.place == null && !state.errorOccurred -> {
                LoadingPlaceholder(null) {
                    onEvent(UiEvent.ShownLoading)
                }
                return@Scaffold
            }
            state.place == null && state.errorOccurred -> {
                ErrorReloadPlaceHolder(null) {
                    onEvent(UiEvent.ReloadPlace)
                }
                return@Scaffold
            }
        }
        val place = state.place ?: return@Scaffold
        val mergedPadding = remember(padding, scaffoldPadding) {
            PaddingValues(
                top = maxOf(padding.calculateTopPadding(), scaffoldPadding.calculateTopPadding()),
                bottom = maxOf(padding.calculateBottomPadding(), scaffoldPadding.calculateBottomPadding())
            )
        }
        var showRateDialog by remember { mutableStateOf(false) }
        if (showRateDialog) {
            PlaceRatingDialog(
                place = place,
                onDismiss = { showRateDialog = false },
                ratingValue = state.ratingValue,
                onRatingChange = { onEvent(UiEvent.RatingChanged(it)) },
                commentValue = state.commentText,
                onCommentChange = { onEvent(UiEvent.CommentTextChanged(it)) },
                onConfirm = { onEvent(UiEvent.SendRatingClicked) }
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(mergedPadding)
                .padding(16.dp)
        ) {
            // images
            PlaceImagesPager(
                place = place
            )
            // title & isFavorite
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = place.name,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        onEvent(UiEvent.ToggleFavoriteClicked)
                    }
                ) {
                    val icon = if (state.isInFavorites) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Outlined.FavoriteBorder
                    }
                    val color = with(MaterialTheme.colorScheme) {
                        if (state.isInFavorites)  primary else onSurface
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color
                    )
                }
            }
            HorizontalDivider()

            Column {
                HeadlineText(stringResource(R.string.place_address_title))
                Text(place.address)
            }
            place.rating?.let { rating ->
                Row {
                    HeadlineText(stringResource(R.string.place_rating_title))
                    Text(rating.toString())
                }
            }

            val uriHandler = LocalUriHandler.current
            val context = LocalContext.current

            ChillyButton(
                textRes = R.string.place_maps_button,
                onClick = {
                    runCatching {
                        uriHandler.openUri(place.yandexMapsLink)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            ExpandableSection(
                expanded = Section.OPEN_HOURS in state.expandedSections,
                onToggle = {
                    onEvent(UiEvent.ToggleExpansion(Section.OPEN_HOURS))
                },
                title = {
                    HeadlineText(stringResource(R.string.place_open_hours_title))
                }
            ) {
                val dayMapping = stringArrayResource(R.array.week_days)
                place.openHours.forEachIndexed { index, openRange ->
                    Row {
                        HeadlineText("${dayMapping[index]} - ")
                        Text(openRange)
                    }
                }
            }

            ExpandableSection(
                expanded = Section.CONTACTS in state.expandedSections,
                onToggle = {
                    onEvent(UiEvent.ToggleExpansion(Section.CONTACTS))
                },
                title = {
                    HeadlineText(stringResource(R.string.place_contacts_title))
                }
            ) {
                place.phone?.let { phoneNumber ->
                    Row {
                        HeadlineText(stringResource(R.string.place_phone_title))
                        Text(
                            text = clickableText(phoneNumber) {
                                Intent(Intent.ACTION_DIAL).also {
                                    it.data = "tel:$phoneNumber".toUri()
                                    context.startActivity(it)
                                }
                            }
                        )
                    }
                }
                place.socials.forEach { link ->
                    val socialName = link.checkSocial() ?: return@forEach
                    Row {
                        HeadlineText("$socialName: ")
                        Text(
                            clickableText(link) {
                                runCatching {
                                    uriHandler.openUri(link)
                                }
                            }
                        )
                    }
                }
            }

            ExpandableSection(
                expanded = Section.COMMENTS in state.expandedSections,
                onToggle = {
                    onEvent(UiEvent.ToggleExpansion(Section.COMMENTS))
                },
                title = {
                    HeadlineText(stringResource(R.string.reviews_section_title))
                }
            ) {
                ChillyButton(
                    textRes = R.string.rate_place_button,
                    type = ChillyButtonType.Secondary,
                    onClick = {
                        showRateDialog = true
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                LaunchedEffect(Unit) {
                    if (state.comments.isEmpty()) {
                        onEvent(UiEvent.EmptyReviewsSectionExpanded)
                    }
                }

                if (state.isLoading) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }
                }
                if (state.comments.isEmpty()) {
                    Text(
                        text = stringResource(R.string.empty_comment_section),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.heightIn(max = 400.dp)
                    ) {
                        item {
                            Spacer(Modifier.height(4.dp))
                        }
                        items(state.comments, key = { it.id }) { comment ->
                            CommentCard(comment)
                        }
                        if (!state.allCommentsLoaded) {
                            item {
                                ChillyButton(
                                    textRes = R.string.load_comments_button,
                                    onClick = {
                                        onEvent(UiEvent.LoadNextCommentsPageClicked)
                                    },
                                    type = ChillyButtonType.Secondary,
                                    color = ChillyButtonColor.Gray,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        item {
                            Spacer(Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CommentCard(
    comment: CommentUiModel
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(
                        R.string.comment_card_title,
                        comment.timeString,
                        comment.rating
                    ),
                    style = MaterialTheme.typography.headlineSmall
                )
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Yellow70
                )
            }
            comment.text?.let {
                Text(text = it)
            }
        }
    }
}

private fun clickableText(
    url: String,
    text: String = url,
    onClick: () -> Unit
): AnnotatedString = buildAnnotatedString {
    withLink(
        LinkAnnotation.Url(url = url) { onClick() }
    ) {
        withStyle(SpanStyle(color = LinkColor, textDecoration = TextDecoration.Underline)) {
            append(text)
        }
    }
}

@Composable
private fun HeadlineText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Medium
    )
}

@Composable
private fun ExpandableSection(
    expanded: Boolean,
    onToggle: () -> Unit,
    title: @Composable () -> Unit,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            title()
            IconButton(
                onClick = onToggle
            ) {
                val icon = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
                Icon(imageVector = icon, contentDescription = null)
            }
        }
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(
                animationSpec = tween(500)
            ) + fadeIn(),
            exit = shrinkVertically(
                animationSpec = tween(500)
            ) + fadeOut()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .padding(contentPadding)
            ) {
                content()
            }
        }
    }
}

private fun String.checkSocial(): String? = when {
    "t.me" in this -> "Telegram"
    "vk.com" in this -> "VK"
    "wa.me" in this -> "WhatsApp"
    else -> null
}

fun NavGraphBuilder.installPlaceInfoScreen(padding: PaddingValues) {
    fadingComposable<Destination.PlaceInfo> { backStackEntry ->
        val route = backStackEntry.toRoute<Destination.PlaceInfo>()

        ScreenHolder<PlaceInfoStore, PlaceInfoComponent>(
            componentFactory = {
                DaggerPlaceInfoComponent.builder()
                    .appComponent(activityComponent)
                    .build()
            },
            storeFactory = { storeFactory.create(route.id) },
            route.id
        ) {
            val state = collectState(component.stateUiMapper)
            NewsCollector(component.newsCollector)
            PlaceInfoScreen(state.value, padding, store::dispatch)
        }
    }
}

@Composable
@PreviewLightDark
@Preview(name = "PlaceInfoScreen", showSystemUi = true, showBackground = true)
private fun PreviewPlaceInfoScreen() {
    ChillyTheme {
        PlaceInfoScreen(
            state = PlaceUiState(-1),
            padding = PaddingValues(),
            onEvent = {}
        )
    }
}

