package ui.mainScreen.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.aymendev.pizzaorder.data.Pizza
import com.aymendev.pizzaorder.ui.utils.PagerUtils
import com.aymendev.pizzaorder.ui.utils.PagerUtils.calculatePageOffset
import data.MockPizzaData
import getPagerStartPadding
import getPagerTopPadding
import getPlatform
import org.jetbrains.compose.resources.painterResource
import pizzaoderkmp.composeapp.generated.resources.Res
import pizzaoderkmp.composeapp.generated.resources.circle_img
import pizzaoderkmp.composeapp.generated.resources.plate
import ui.core.AnimatedCircularImages
import ui.core.DraggableBox
import ui.core.PizzaBox
import ui.shared.BackPressHandler
import ui.shared.SharedBackPressHandler
import ui.theme.Orange40
import ui.theme.Orange60
import ui.theme.Pink40
import ui.theme.Yellow40
import ui.theme.Yellow50
import ui.theme.Yellow60
import ui.theme.Yellow70
import ui.mainScreen.viewModel.MainScreenViewModel

enum class MainScreenTestTags {
    PAGER,
    DETAILS_PIZZA,
}

const val DEFAULT_ROOT_WIDTH_DP = 10
const val DETAILS_PAGE_CONTAINER_SIZE_DP = 250
const val MAIN_PAGE_CONTAINER_SIZE_DP = 200
const val DETAILS_PAGE_CORNER_RADIUS_DP = 10
const val MAIN_PAGE_CORNER_RADIUS_DP = 100
const val SELECTED_ROTATION_ANGLE = -25f
const val DEFAULT_ROTATION_ANGLE = 0f
const val ANIMATION_DURATION_MS = 500
const val ROTATION_ANIMATION_DURATION_MS = 600

@Composable
fun MainScreen(mainScreenViewModel: MainScreenViewModel) {

    val isDetailsPage by mainScreenViewModel.isPizzaSelected.collectAsState()
    val isBoxVisible by mainScreenViewModel.isPizzaBoxVisible.collectAsState()
    val pizzaCartCount = remember { mutableIntStateOf(0) }
    val rotation = remember { mutableFloatStateOf(DEFAULT_ROTATION_ANGLE) }
    val selectedRotationFinished = remember { mutableStateOf(false) }

    SharedBackPressHandler.backPress = object : BackPressHandler {
        override fun onBackPressed() {
            mainScreenViewModel.setPizzaSelection(false)
        }
    }
    val rootWidth = remember { mutableStateOf(DEFAULT_ROOT_WIDTH_DP.dp) }
    val containerSize = animateDpAsState(
        targetValue = if (isDetailsPage || isBoxVisible) DETAILS_PAGE_CONTAINER_SIZE_DP.dp else MAIN_PAGE_CONTAINER_SIZE_DP.dp,
        label = "containerSize",
        animationSpec = tween(ANIMATION_DURATION_MS)
    )
    val cornerRadiusBg = animateDpAsState(
        targetValue = if (!isDetailsPage && !isBoxVisible) MAIN_PAGE_CORNER_RADIUS_DP.dp else DETAILS_PAGE_CORNER_RADIUS_DP.dp,
        label = "cornerRadiusBg",
        animationSpec = tween(ANIMATION_DURATION_MS)
    )
    val selectedRotation = animateFloatAsState(
        targetValue = if (!isDetailsPage) DEFAULT_ROTATION_ANGLE else SELECTED_ROTATION_ANGLE,
        label = "selectedRotation",
        animationSpec = tween(durationMillis = ROTATION_ANIMATION_DURATION_MS),
        finishedListener = { selectedRotationFinished.value = true }
    )

    mainScreenViewModel.apply {
        this.containerSize = containerSize
        this.cornerRadiusBg = cornerRadiusBg
        this.selectedRotation = selectedRotation
        this.rootWidth = rootWidth
        this.rotation = rotation
    }

    Scaffold(topBar = {
        if (isDetailsPage) DetailTopBar(pizzaCartCount) {
            mainScreenViewModel.setPizzaSelection(false)
        }
        else MainTopBar(pizzaCartCount)
    }) {
        MainContent(
            mainScreenViewModel = mainScreenViewModel,
            onAddToCard = { pizzaCartCount.value++ },
            onPizzaItemClicked = {
                mainScreenViewModel.onPizzaItemClicked()
            })
    }

}

@Composable
private fun MainContent(
    mainScreenViewModel: MainScreenViewModel,
    onPizzaItemClicked: (pizza: Pizza) -> Unit,
    onAddToCard: (pizza: Pizza) -> Unit
) {
    val isPizzaBoxClosed = mainScreenViewModel.isPizzaBoxClosed.collectAsState()
    val isPizzaBoxVisible = mainScreenViewModel.isPizzaBoxVisible.collectAsState()
    val currentSupplements = mainScreenViewModel.currentSupplements.collectAsState()
    val pizzaSize = mainScreenViewModel.containerSize.value
    val isPizzaSelected = mainScreenViewModel.isPizzaSelected.collectAsState()
    val additionBottomPadding = 120.dp

    LaunchedEffect(isPizzaSelected.value, isPizzaBoxVisible) {
        if (!isPizzaSelected.value) {
            mainScreenViewModel.clearSupplements()
        }
    }
    Column(
        modifier = Modifier.background(brush = Brush.verticalGradient(listOf(Yellow60, Yellow50)))
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PizzaButton(isPizzaSelected)
        Box(
            modifier = Modifier.fillMaxSize().onGloballyPositioned {
                mainScreenViewModel.rootWidth.value = it.size.width.toFloat().dp
            },
        ) {
            Box(
                modifier = Modifier.shadow(
                    elevation = 0.5.dp, shape = RoundedCornerShape(
                        mainScreenViewModel.cornerRadiusBg.value
                    )
                ).background(
                    color = Yellow60,
                    shape = RoundedCornerShape(mainScreenViewModel.cornerRadiusBg.value)
                ).fillMaxSize(if (!mainScreenViewModel.isPizzaSelected.value) 0.9f else 1f)
                    .align(Alignment.TopCenter)
            )

            Box(
                modifier = Modifier.padding(top = 22.dp)
                    .size(mainScreenViewModel.containerSize.value)
                    .alpha(if (isPizzaBoxVisible.value) 0f else 1f).shadow(
                        8.dp,
                        shape = CircleShape,
                        ambientColor = Color.Black,
                        spotColor = Color.Black
                    ).rotate(mainScreenViewModel.selectedRotation.value).onGloballyPositioned {
                        mainScreenViewModel.setPizzaUiCoordinate(it)
                    }.align(Alignment.TopCenter)
            ) {
                Plate(Modifier.size(mainScreenViewModel.containerSize.value))
            }

            PizzaBox(
                modifier = Modifier.padding(bottom = 300.dp).align(Alignment.TopCenter),
                pizzaImage = mainScreenViewModel.currentPizza.value.image,
                isBoxOpenedToClosed = isPizzaBoxClosed.value,
                isBoxHiddenToVisible = isPizzaBoxVisible.value,
            ) {
                onAddToCard(mainScreenViewModel.currentPizza.value)
                mainScreenViewModel.clearSupplements()
                mainScreenViewModel.hidePizzaBox()

            }
            if (isPizzaSelected.value) {
                if (!isPizzaBoxVisible.value) {
                    Image(
                        modifier = Modifier
                            .padding(top = 26.dp).size(pizzaSize)
                            .clip(CircleShape)
                            .rotate(mainScreenViewModel.selectedRotation.value)
                            .align(Alignment.TopCenter)
                            .clickable {
                                if (currentSupplements.value.size > 0) {
                                    mainScreenViewModel.removeLastSupplement()
                                }
                            }
                            .testTag(MainScreenTestTags.DETAILS_PIZZA.name),
                        painter = painterResource(
                            mainScreenViewModel.currentPizza.value.image
                        ), contentScale = ContentScale.Crop, contentDescription = ""
                    )
                }
                if (mainScreenViewModel.isDisplaySupplementsAllowed()) {
                    for (i in 0 until currentSupplements.value.size)
                        AnimatedCircularImages(
                            modifier = Modifier.padding(top = 20.dp).size(250.dp)
                                .align(Alignment.TopCenter),
                           currentSupplements.value[i].image,
                            radius = mainScreenViewModel.getSupplementCircularImagesRadius(i)
                        )
                }

                Additions(
                    uiState = mainScreenViewModel,
                    isEnableDrag = currentSupplements.value.size < 2,
                    modifier = Modifier.padding(bottom = additionBottomPadding).fillMaxWidth()
                        .align(Alignment.BottomCenter),
                ) {

                    if (currentSupplements.value.size < 2) {
                        mainScreenViewModel.addSupplement(MockPizzaData.supplements[it])
                    }
                }
            }

            if (!isPizzaBoxVisible.value)
                AnimatedEdge(
                    modifier = Modifier.align(Alignment.TopCenter)
                        .size(mainScreenViewModel.containerSize.value * 1.25f).clip(CircleShape)
                        .rotate(mainScreenViewModel.rotation.value),
                    isPizzaSelected = isPizzaSelected
                )

            if (!isPizzaSelected.value && !isPizzaBoxVisible.value) {
                PizzaPager(
                    Modifier.padding(start = getPagerStartPadding(), top = getPagerTopPadding())
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .testTag(MainScreenTestTags.PAGER.name),
                    pizzas = MockPizzaData.pizzas,
                    pizzaSize = pizzaSize,
                    onPizzaClicked = {
                        onPizzaItemClicked(mainScreenViewModel.currentPizza.value)
                    },
                    onPageScroll = {
                        mainScreenViewModel.currentPizza.value = MockPizzaData.pizzas[it]
                    },
                    onScroll = {
                        mainScreenViewModel.rotation.floatValue = 100 * -it
                    },
                    rotation = mainScreenViewModel.selectedRotation.value
                )
            }

            PizzaDetails(
                modifier = Modifier.padding(bottom = if (isPizzaSelected.value) 205.dp else 80.dp)
                    .align(Alignment.BottomCenter),
                isPizzaSelected = isPizzaSelected,
                pizza = mainScreenViewModel.currentPizza
            )

            AddToCartButton(
                modifier = Modifier.size(80.dp).padding(10.dp)
                    .shadow(2.dp, shape = RoundedCornerShape(20.dp), ambientColor = Orange40)
                    .background(
                        brush = Brush.verticalGradient(
                            if (!isPizzaBoxVisible.value && !isPizzaBoxClosed.value) listOf(
                                Yellow40, Orange40
                            ) else listOf(Yellow60, Orange60)
                        ), shape = RoundedCornerShape(20.dp)
                    )
                    .clickable(enabled = !isPizzaBoxVisible.value && !isPizzaBoxClosed.value) {
                        mainScreenViewModel.showPizzaBox()
                    }.align(Alignment.BottomCenter)
            )
        }
    }
}


@Composable
fun MainTopBar(itemCount: MutableState<Int>) {
    Box(
        Modifier.height(90.dp).fillMaxWidth().background(Yellow60).padding(horizontal = 20.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = "Order Manually",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            color = Pink40
        )
        Row(Modifier.align(Alignment.BottomStart), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = "", tint = Pink40)
            Text(
                text = "Paris",
                fontWeight = FontWeight.W400,
                fontSize = 12.sp,
                color = Pink40,
            )
        }
        CartIcon(Modifier.align(Alignment.CenterEnd), itemCount)
    }
}

@Composable
fun DetailTopBar(pizzaCartCount: MutableState<Int>, onBackClicked: () -> Unit) {
    Box(
        Modifier.height(90.dp).fillMaxWidth().background(Yellow60).padding(horizontal = 20.dp)
    ) {
        Icon(
            modifier = Modifier.size(30.dp).align(Alignment.CenterStart).clip(CircleShape)
                .clickable {
                    onBackClicked()
                },
            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
            tint = Pink40,
            contentDescription = ""
        )
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "Order Manually",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            color = Pink40
        )
        CartIcon(Modifier.align(Alignment.CenterEnd), pizzaCartCount)
    }
}


@Composable
private fun CartIcon(modifier: Modifier, itemCount: MutableState<Int>) {
    Box(modifier) {
        Icon(
            contentDescription = "",
            modifier = Modifier.padding(5.dp).size(35.dp).align(Alignment.Center),
            imageVector = Icons.Outlined.ShoppingCart,
            tint = Pink40
        )
        if (itemCount.value > 0) Box(
            Modifier.size(22.dp).background(Color.Red, shape = CircleShape).align(Alignment.TopEnd)
        ) {

            Text(
                modifier = Modifier.padding(
                    bottom = if (getPlatform().name.lowercase().contains("ios")) 5.dp else 0.dp
                ).align(Alignment.TopCenter),
                fontSize = 13.sp,
                color = Color.White,
                text = itemCount.value.toString()
            )
        }
    }
}

@Composable
private fun AnimatedEdge(modifier: Modifier, isPizzaSelected: State<Boolean>) {
    AnimatedVisibility(modifier = modifier, visible = !isPizzaSelected.value, exit = fadeOut()) {
        Image(
            painter = painterResource(Res.drawable.circle_img),
            contentScale = ContentScale.Inside,
            contentDescription = ""
        )
    }
}

@Composable
private fun Plate(modifier: Modifier) {
    Image(
        modifier = modifier,
        contentScale = ContentScale.Crop,
        painter = painterResource(Res.drawable.plate),
        contentDescription = ""
    )
}

@Composable
private fun AddToCartButton(modifier: Modifier) {
    Box(modifier) {
        Icon(
            modifier = Modifier.align(Alignment.Center),
            imageVector = Icons.Outlined.ShoppingCart,
            contentDescription = "",
            tint = Color.White
        )
    }
}

@Composable
private fun Additions(
    modifier: Modifier,
    uiState: MainScreenViewModel,
    isEnableDrag: Boolean,
    onDragged: (Int) -> Unit,
) {
    val pizzaCoordinates = uiState.pizzaLayoutCoordinates.collectAsState()
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AdditionsText(modifier = Modifier, uiState.isPizzaSelected.value)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (i in 0 until MockPizzaData.supplements.size) {
                DraggableBox(enableDrag = isEnableDrag,
                    modifier = Modifier.background(color = Yellow70, shape = CircleShape)
                        .size(60.dp),
                    targetLayoutCoordinates = pizzaCoordinates,
                    onDragged = {
                        onDragged(i)
                    }) {
                    Box(
                        Modifier.size(60.dp).align(Alignment.Center),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            contentScale = ContentScale.Inside,
                            painter = painterResource(MockPizzaData.supplements[i].image),
                            contentDescription = ""
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AdditionsText(modifier: Modifier, isPizzaSelected: Boolean) {
    AnimatedVisibility(modifier = modifier, visible = isPizzaSelected) {
        Text(
            textAlign = TextAlign.Center,
            text = "Tapping Must be 2",
            fontWeight = FontWeight.W400,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun PizzaDetails(
    modifier: Modifier, pizza: MutableState<Pizza>, isPizzaSelected: State<Boolean>
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(enter = fadeIn() + slideInVertically { -it },
            exit = fadeOut(animationSpec = tween()),
            visible = !isPizzaSelected.value,
            content = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(20.dp),
                        text = pizza.value.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Pink40,
                        textAlign = TextAlign.Center
                    )
                    Stars(pizza.value.rate)
                }
            })
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            modifier = Modifier.zIndex(1f),
            text = "$${pizza.value.price}",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = Pink40
        )
        Spacer(modifier = Modifier.height(10.dp))
        PizzaSizes()
    }
}

@Composable
private fun PizzaSizes() {
    Row(
        modifier = Modifier.zIndex(1f).fillMaxWidth(0.6f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(
            modifier = Modifier.size(40.dp).background(Color.White, shape = CircleShape)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "S",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.W400,
                fontSize = 16.sp,
                color = Pink40
            )
        }
        Box(
            modifier = Modifier.size(60.dp).shadow(3.dp, shape = CircleShape)
                .background(Color.White, shape = CircleShape).clip(CircleShape)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "M",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.W400,
                fontSize = 16.sp,
                color = Pink40
            )
        }
        Box(
            modifier = Modifier.size(40.dp).background(Color.White, shape = CircleShape)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "L",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.W400,
                fontSize = 16.sp,
                color = Pink40
            )
        }
    }
}

@Composable
private fun PizzaButton(isPizzaSelected: State<Boolean>) {
    AnimatedVisibility(visible = !isPizzaSelected.value, enter = fadeIn(tween(100)) + expandIn()) {
        Column(
            Modifier.padding(5.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier.padding(10.dp).size(120.dp, 50.dp)
                    .background(shape = RoundedCornerShape(20.dp), color = Yellow40),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Pizza",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = Pink40,
                    fontSize = 22.sp
                )
            }
        }
    }
}

@Composable
private fun Stars(rate: Int) {
    Row {
        for (i in 1..5) {
            Icon(
                imageVector = Icons.Default.Star,
                tint = if (i <= rate) Orange40 else Color.LightGray,
                contentDescription = ""
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PizzaPager(
    modifier: Modifier,
    pizzaSize: Dp,
    onPizzaClicked: (Int) -> Unit,
    rotation: Float? = null,
    onScroll: (Float) -> Unit,
    onPageScroll: (Int) -> Unit,
    pizzas: List<Pizza>
) {
    val pagerState =
        rememberPagerState(pageCount = { pizzas.size }, initialPage = PagerUtils.currentIndex)

    if (pagerState.isScrollInProgress) {
        onScroll(pagerState.currentPageOffsetFraction)
    }
    HorizontalPager(
        state = pagerState,
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(horizontal = 100.dp),
        modifier = modifier
    ) { page ->
        val pageOffset = calculatePageOffset(
            currentPageOffsetFraction = pagerState.currentPageOffsetFraction,
            currentPage = pagerState.currentPage,
            page
        )
        val offset = 70.dp * pageOffset
        onPageScroll(pagerState.currentPage)
        val scale =
            animateFloatAsState(targetValue = if (pageOffset < 0.4) 1.0F else 0.6f, label = "")
        PizzaPage(
            offset,
            pageOffset,
            scale,
            pizza = pizzas[page],
            size = pizzaSize,
            rotation = if (pagerState.isScrollInProgress && (pageOffset < 0.5)) (100 * -pageOffset) else if ((pageOffset < 0.5)) rotation
                ?: 0f else 0f
        ) {
            PagerUtils.currentIndex = pagerState.currentPage
            if (page == pagerState.currentPage) onPizzaClicked(pagerState.currentPage)
        }
    }
}

@Composable
fun PizzaPage(
    offset: Dp,
    pageOffset: Float,
    scale: State<Float>,
    size: Dp = 240.dp,
    rotation: Float = 100 * -pageOffset,
    pizza: Pizza,
    onClickAction: () -> Unit
) {
    Image(
        contentScale = ContentScale.Crop,
        modifier = Modifier.offset(y = offset).size(size).clip(CircleShape)
            .background(Color.Transparent, shape = CircleShape).scale(scale.value).clickable {
                onClickAction()
            }.rotate(rotation),
        painter = painterResource(pizza.image),
        contentDescription = ""
    )
}

