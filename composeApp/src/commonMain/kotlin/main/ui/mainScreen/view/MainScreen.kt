package main.ui.mainScreen.view

import PIZZA_ANIM_EDGE_SIZE_COEF
import PIZZA_PAGER_HORIZONTAL_PADDING
import PIZZA_PAGER_PADDING
import PIZZA_PLATE_PADDING
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
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
import androidx.compose.runtime.MutableIntState
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
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.aymendev.pizzaorder.data.Pizza
import com.aymendev.pizzaorder.data.PizzaSupplement
import com.aymendev.pizzaorder.ui.utils.PagerUtils
import com.aymendev.pizzaorder.ui.utils.PagerUtils.calculatePageOffset
import com.aymendev.pizzaorder.ui.utils.PagerUtils.getPizzaItemRotationValue
import main.data.MockPizzaData
import org.jetbrains.compose.resources.painterResource
import pizzaoderkmp.composeapp.generated.resources.Res
import pizzaoderkmp.composeapp.generated.resources.capres
import pizzaoderkmp.composeapp.generated.resources.champinion
import pizzaoderkmp.composeapp.generated.resources.gree_olive_leaf
import pizzaoderkmp.composeapp.generated.resources.half_tomato
import pizzaoderkmp.composeapp.generated.resources.plate
import main.ui.core.AnimatedCircularImages

import main.ui.core.DraggableBox
import main.ui.core.PizzaBox
import main.ui.mainScreen.MainScreenUiState
import main.ui.mainScreen.PizzaPagerListener
import main.ui.shared.BackPressHandler
import main.ui.shared.SharedBackPressHandler
import main.ui.theme.Orange40
import main.ui.theme.Orange60
import main.ui.theme.Pink40
import main.ui.theme.Yellow40
import main.ui.theme.Yellow50
import main.ui.theme.Yellow60
import main.ui.theme.Yellow70
import main.ui.viewModel.MainScreenViewModel

enum class MainScreenTestTags {
    PAGER,
    DETAILS_PIZZA,
    PIZZA_ITEM,
    MAIN_HEADER,
    ADD_TO_CART_BUTTON,
    PIZZA_COUNT,
    DETAILS_HEADER,
}

const val PIZZA_ADDITION_ITEM_SIZE = 60
const val DETAILS_PAGE_CONTAINER_SIZE_DP = 250
const val MAIN_PAGE_CONTAINER_SIZE_DP = 200
const val DETAILS_PAGE_CORNER_RADIUS_DP = 10
const val MAIN_PAGE_CORNER_RADIUS_DP = 100
const val SELECTED_ROTATION_ANGLE = -25f
const val DEFAULT_ROTATION_ANGLE = 0f
const val ANIMATION_DURATION_MS = 500
const val ROTATION_ANIMATION_DURATION_MS = 500
const val ADD_TO_CART_BUTTON_PADDING = 10
const val ADD_TO_CART_BUTTON_SIZE = 80
const val ADDITION_INSTRUCTION_TEXT_PADDING = 10
const val ADDITION_INSTRUCTION_TEXT_SIZE = 50
const val PIZZA_DETAILS_PADDING = ADD_TO_CART_BUTTON_SIZE + ADD_TO_CART_BUTTON_PADDING * 2 +
        PIZZA_ADDITION_ITEM_SIZE + ADDITION_INSTRUCTION_TEXT_PADDING +
        ADDITION_INSTRUCTION_TEXT_SIZE + 20
const val PIZZA_DETAILS_PADDING_ON_PAGER = ADD_TO_CART_BUTTON_SIZE + ADD_TO_CART_BUTTON_PADDING + 20

@Composable
fun MainScreen(mainScreenViewModel: MainScreenViewModel) {
    val uiState by mainScreenViewModel.uiState.collectAsState()
    val pizzaCartCount = remember { mutableIntStateOf(0) }
    val rotation = remember { mutableFloatStateOf(DEFAULT_ROTATION_ANGLE) }
    SharedBackPressHandler.backPress = object : BackPressHandler {
        override fun onBackPressed() {
            mainScreenViewModel.setPizzaSelection(false)
        }
    }
    val containerSize = animateDpAsState(
        targetValue = if (uiState.isPizzaSelected || uiState.isPizzaBoxVisible) DETAILS_PAGE_CONTAINER_SIZE_DP.dp else MAIN_PAGE_CONTAINER_SIZE_DP.dp,
        label = "containerSize",
        animationSpec = tween(ANIMATION_DURATION_MS)
    )
    val cornerRadiusBg = animateDpAsState(
        targetValue = if (!uiState.isPizzaSelected && !uiState.isPizzaBoxVisible) MAIN_PAGE_CORNER_RADIUS_DP.dp else DETAILS_PAGE_CORNER_RADIUS_DP.dp,
        label = "cornerRadiusBg",
        animationSpec = tween(ANIMATION_DURATION_MS)
    )
    val selectedRotation = animateFloatAsState(
        targetValue = if (!uiState.isPizzaSelected) DEFAULT_ROTATION_ANGLE else SELECTED_ROTATION_ANGLE,
        label = "selectedRotation",
        animationSpec = tween(durationMillis = ROTATION_ANIMATION_DURATION_MS),
    )
    val pagerListener = object : PizzaPagerListener {
        override fun onPizzaClicked(int: Int) {
            mainScreenViewModel.onPizzaItemClicked()
        }

        override fun onScrollToPage(index: Int) {
            mainScreenViewModel.setCurrentPizza(MockPizzaData.pizzas[index])
        }

        override fun onScrollUpdate(fraction: Float) {
            if (fraction != 1f)
                mainScreenViewModel.setRotation(mutableStateOf(100 * -fraction))
        }
    }
    mainScreenViewModel.apply {
        setSelectedRotation(selectedRotation)
        setContainerSize(containerSize)
        setRotation(rotation)
        setCornerRadiusBg(cornerRadiusBg)
    }
    LaunchedEffect(uiState.isPizzaSelected) {
        if (!uiState.isPizzaSelected) {
            mainScreenViewModel.clearSupplements()
        }
    }
    Scaffold(topBar = {
        TopBar(
            uiState = uiState,
            pizzaCartCount = pizzaCartCount,
        ) {
            mainScreenViewModel.setPizzaSelection(false)
        }
    }) {
        MainContent(
            mainScreenUiState = uiState,
            onPizzaBoxDisappear = {
                pizzaCartCount.value++
                mainScreenViewModel.hidePizzaBox()
            },
            onPizzaGloballyPositioned = {
                mainScreenViewModel.setPizzaUiCoordinate(it)
            },
            onClickOnPizzaFromDetails = {
                if (uiState.currentSupplements.isNotEmpty()) {
                    mainScreenViewModel.removeLastSupplement()
                }
            },
            onSupplementDragged = {
                if (uiState.currentSupplements.size < 2) {
                    mainScreenViewModel.addSupplement(it)
                }
            },
            onAddToCarClicked = {
                mainScreenViewModel.showPizzaBox()
            },
            pagerListener = pagerListener
        )
    }

}

@Composable
private fun TopBar(
    uiState: MainScreenUiState,
    pizzaCartCount: MutableIntState,
    onBackClicked: () -> Unit
) {
    if (uiState.isPizzaSelected)
        DetailTopBar(pizzaCartCount, onBackClicked)
    else MainTopBar(pizzaCartCount)
}

@Composable
private fun MainContent(
    mainScreenUiState: MainScreenUiState,
    pagerListener: PizzaPagerListener,
    onPizzaGloballyPositioned: (layoutCoordinates: LayoutCoordinates) -> Unit,
    onClickOnPizzaFromDetails: () -> Unit,
    onAddToCarClicked: () -> Unit,
    onSupplementDragged: (pizzaSupplement: PizzaSupplement) -> Unit,
    onPizzaBoxDisappear: () -> Unit,
) {
    Column(
        modifier = Modifier.background(brush = Brush.verticalGradient(listOf(Yellow60, Yellow50)))
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PizzaButton(isHidden = mainScreenUiState.isPizzaSelected, modifier = Modifier)
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            RoundBackground(
                mainScreenUiState = mainScreenUiState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
            //Plate IMAGE
            Plate(
                mainScreenUiState = mainScreenUiState,
                modifier = Modifier
                    .align(Alignment.TopCenter)

            ) {
                onPizzaGloballyPositioned(it)
            }
            PizzaBox(
                modifier = Modifier.fillMaxHeight(0.5f).fillMaxWidth().align(Alignment.TopCenter),
                pizzaImage = mainScreenUiState.currentPizza.image,
                isBoxOpenedToClosed = mainScreenUiState.isPizzaBoxClosed,
                isBoxHiddenToVisible = mainScreenUiState.isPizzaBoxVisible,
            ) {
                onPizzaBoxDisappear()
            }
            if (mainScreenUiState.isPizzaSelected) {
                DetailsPagePizzaImage(
                    mainScreenUiState = mainScreenUiState,
                    modifier = Modifier.align(Alignment.TopCenter),
                    onClickOnPizzaFromDetails = onClickOnPizzaFromDetails
                )
                Supplements(
                    mainScreenUiState = mainScreenUiState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )

                Additions(
                    uiState = mainScreenUiState,
                    isEnableDrag = mainScreenUiState.currentSupplements.size < 2,
                    modifier = Modifier.padding(
                        bottom =
                        (ADD_TO_CART_BUTTON_PADDING + ADD_TO_CART_BUTTON_SIZE).dp
                    ).fillMaxWidth()
                        .align(Alignment.BottomCenter),
                ) {

                    onSupplementDragged(MockPizzaData.supplements[it])
                }
            }
            AnimatedEdge(
                state = mainScreenUiState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
            )

            if (!mainScreenUiState.isPizzaSelected
                && !mainScreenUiState.isPizzaBoxVisible
            ) {
                PizzaPager(
                    Modifier
                        .padding(start = PIZZA_PAGER_PADDING.dp, top = PIZZA_PLATE_PADDING.dp)
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .testTag(MainScreenTestTags.PAGER.name),
                    pizzas = MockPizzaData.pizzas,
                    pizzaSize = mainScreenUiState.plateSize.value,
                    listener = pagerListener,
                    externalRotation = mainScreenUiState.selectedRotation.value,
                    contentHorizontalPadding = PIZZA_PAGER_HORIZONTAL_PADDING
                )
            }
            PizzaDetails(
                modifier = Modifier
                    .padding(
                        bottom = if (mainScreenUiState.isPizzaSelected)
                            PIZZA_DETAILS_PADDING.dp
                        else PIZZA_DETAILS_PADDING_ON_PAGER.dp
                    ).align(Alignment.BottomCenter),
                isPizzaSelected = mainScreenUiState.isPizzaSelected,
                pizza = mainScreenUiState.currentPizza
            )
            AddToCartButton(
                mainScreenUiState = mainScreenUiState,
                modifier = Modifier.align(Alignment.BottomCenter),
                onAddToCarClicked
            )
        }
    }
}

@Composable
private fun DetailsPagePizzaImage(
    mainScreenUiState: MainScreenUiState,
    modifier: Modifier,
    onClickOnPizzaFromDetails: () -> Unit
) {
    if (!mainScreenUiState.isPizzaBoxVisible) {
        Image(
            modifier = modifier
                .padding(top = PIZZA_PLATE_PADDING.dp)
                .size(mainScreenUiState.plateSize.value)
                .clip(CircleShape)
                .rotate(mainScreenUiState.selectedRotation.value)
                .clickable {
                    onClickOnPizzaFromDetails()
                }
                .testTag(MainScreenTestTags.DETAILS_PIZZA.name),
            painter = painterResource(
                mainScreenUiState.currentPizza.image
            ), contentScale = ContentScale.Crop, contentDescription = ""
        )
    }
}

@Composable
private fun RoundBackground(mainScreenUiState: MainScreenUiState, modifier: Modifier) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 0.5.dp, shape = RoundedCornerShape(
                    mainScreenUiState.cornerRadiusBg.value
                )
            ).background(
                color = Yellow60,
                shape = RoundedCornerShape(mainScreenUiState.cornerRadiusBg.value)
            )
            .fillMaxSize(if (!mainScreenUiState.isPizzaSelected) 0.9f else 1f)

    )
}


@Composable
private fun Supplements(mainScreenUiState: MainScreenUiState, modifier: Modifier) {
    if (mainScreenUiState.isDisplaySupplementsAllowed) {
        mainScreenUiState.currentSupplements.forEachIndexed { index, supplement ->
            AnimatedCircularImages(
                modifier = modifier
                    .padding(top = PIZZA_PLATE_PADDING.dp)
                    .size(mainScreenUiState.plateSize.value),
                supplement.image,
                radius = 40 + index * 20f
            )
        }
    }
}

@Composable
private fun Plate(
    mainScreenUiState: MainScreenUiState,
    modifier: Modifier,
    onPizzaGloballyPositioned: (LayoutCoordinates) -> Unit
) {
    Image(
        modifier = modifier
            .padding(top = PIZZA_PLATE_PADDING.dp)
            .size(mainScreenUiState.plateSize.value)
            .alpha(if (mainScreenUiState.isPizzaBoxVisible) 0f else 1f).shadow(
                8.dp,
                shape = CircleShape,
                ambientColor = Color.Black,
                spotColor = Color.Black
            ).rotate(mainScreenUiState.selectedRotation.value).onGloballyPositioned {
                onPizzaGloballyPositioned(it)
            },
        contentScale = ContentScale.Crop,
        painter = painterResource(Res.drawable.plate),
        contentDescription = ""
    )
}


@Composable
fun MainTopBar(itemCount: MutableState<Int>) {
    Box(
        Modifier.height(90.dp).fillMaxWidth().background(Yellow60).padding(horizontal = 20.dp)
            .testTag(MainScreenTestTags.MAIN_HEADER.name)
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = "Order Manually",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            color = Pink40
        )
        Row(Modifier, verticalAlignment = Alignment.CenterVertically) {
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
            .testTag(MainScreenTestTags.DETAILS_HEADER.name)
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

    BadgedBox(
        modifier = modifier,
        badge = {
            if (itemCount.value > 0) {
                Badge(
                    backgroundColor = Color.Red,
                    contentColor = Color.White
                ) {
                    Text(
                        text = "${itemCount.value}",
                        modifier = Modifier.testTag(MainScreenTestTags.PIZZA_COUNT.name)
                    )
                }
            }
        }
    ) {
        Icon(
            modifier = Modifier.size(35.dp),
            imageVector = Icons.Outlined.ShoppingCart,
            contentDescription = "Shopping cart",
            tint = Pink40
        )
    }

}

@Composable
private fun AnimatedEdge(state: MainScreenUiState, modifier: Modifier) {

    if (!state.isPizzaBoxVisible) {

        val radius = remember { mutableStateOf(0F) }
        val images = listOf(
            Res.drawable.champinion,
            Res.drawable.half_tomato,
            Res.drawable.capres,
            Res.drawable.gree_olive_leaf,
            Res.drawable.champinion,
            Res.drawable.half_tomato,
            Res.drawable.capres,
            Res.drawable.gree_olive_leaf,
        )
        AnimatedVisibility(
            modifier = modifier.clip(CircleShape)
                .rotate(state.rotation.value)
                .size(state.plateSize.value * PIZZA_ANIM_EDGE_SIZE_COEF),
            visible = !state.isPizzaSelected,
            exit = fadeOut()
        ) {
            AnimatedCircularImages(
                modifier = Modifier.onGloballyPositioned {
                    radius.value = it.size.width / 10f
                },
                images = images,
                numberOfImages = images.size,
                radius = radius.value,
                isAnimated = false,
                isSingleImage = false
            )
        }
    }
}

@Composable
private fun AddToCartButton(
    mainScreenUiState: MainScreenUiState,
    modifier: Modifier,
    onAddToCarClicked: () -> Unit
) {
    Box(modifier.size(ADD_TO_CART_BUTTON_SIZE.dp)
        .padding(ADD_TO_CART_BUTTON_PADDING.dp)
        .shadow(
            2.dp,
            shape = RoundedCornerShape(20.dp),
            ambientColor = Orange40
        )
        .background(
            brush = Brush.verticalGradient(
                if (!mainScreenUiState.isPizzaBoxVisible && !mainScreenUiState.isPizzaBoxClosed) listOf(
                    Yellow40, Orange40
                ) else listOf(Yellow60, Orange60)
            ), shape = RoundedCornerShape(20.dp)
        )
        .clickable(enabled = !mainScreenUiState.isPizzaBoxVisible && !mainScreenUiState.isPizzaBoxClosed) {
            onAddToCarClicked()
        }.testTag(MainScreenTestTags.ADD_TO_CART_BUTTON.name)
    )

    {
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
    uiState: MainScreenUiState,
    isEnableDrag: Boolean,
    onDragged: (Int) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AdditionsText(
            modifier = Modifier.padding(ADDITION_INSTRUCTION_TEXT_PADDING.dp)
                .height(ADDITION_INSTRUCTION_TEXT_SIZE.dp), uiState.isPizzaSelected
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (i in 0 until MockPizzaData.supplements.size) {
                DraggableBox(enableDrag = isEnableDrag,
                    modifier = Modifier.background(color = Yellow70, shape = CircleShape)
                        .size(PIZZA_ADDITION_ITEM_SIZE.dp),
                    targetLayoutCoordinates = uiState.pizzaLayoutCoordinates as LayoutCoordinates,
                    onDragged = {
                        onDragged(i)
                    }) {
                    Box(
                        Modifier.matchParentSize().align(Alignment.Center),
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
    modifier: Modifier, pizza: Pizza, isPizzaSelected: Boolean
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(enter = fadeIn() + slideInVertically { -it },
            exit = fadeOut(animationSpec = tween()),
            visible = !isPizzaSelected,
            content = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(20.dp),
                        text = pizza.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Pink40,
                        textAlign = TextAlign.Center
                    )
                    Stars(pizza.rate)
                }
            })
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            modifier = Modifier.zIndex(1f),
            text = "$${pizza.price}",
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
private fun PizzaButton(isHidden: Boolean, modifier: Modifier) {
    AnimatedVisibility(
        modifier = modifier,
        visible = !isHidden,
        enter = fadeIn(tween(100)) + expandIn()
    ) {
        Column(
            Modifier.fillMaxWidth(),
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
    listener: PizzaPagerListener,
    contentHorizontalPadding: Dp = 105.dp,
    externalRotation: Float? = null,
    pizzas: List<Pizza>
) {
    // Remember the pager state with the initial page set from PagerUtils
    val pagerState = rememberPagerState(
        pageCount = { pizzas.size },
        initialPage = PagerUtils.currentIndex
    )

    // Notify on scroll if it's in progress
    if (pagerState.isScrollInProgress) {
        listener.onScrollUpdate(pagerState.currentPageOffsetFraction)
    }

    // HorizontalPager to display pizzas
    HorizontalPager(
        state = pagerState,
        verticalAlignment = Alignment.CenterVertically,
        pageSize = PageSize.Fill,
        contentPadding = PaddingValues(horizontal = contentHorizontalPadding),
        modifier = modifier
    ) { page ->
        // Calculate page offset for animation
        val pageOffset = calculatePageOffset(
            currentPageOffsetFraction = pagerState.currentPageOffsetFraction,
            currentPage = pagerState.currentPage,
            page = page
        )
        val offset = 70.dp * pageOffset

        // Notify about current page scroll
        listener.onScrollToPage(pagerState.currentPage)

        // Animate the scale for the current pizza page
        val scale = animateFloatAsState(
            targetValue = if (pageOffset < 0.4f) 1f else 0.6f,
            label = "Pizza scale animation"
        )
        // Determine the rotation for the pizza based on the scroll state
        // Render the pizza page with calculated values
        PizzaPage(
            offset = offset,
            pageOffset = pageOffset,
            scale = scale,
            index = pagerState.currentPage,
            pizza = pizzas[page],
            size = pizzaSize,
            rotation = getPizzaItemRotationValue(
                isScrollInProgress = pagerState.isScrollInProgress,
                pageOffset = pageOffset,
                rotation = externalRotation
            )
        ) {
            PagerUtils.currentIndex = pagerState.currentPage
            if (page == pagerState.currentPage) {

                listener.onPizzaClicked(pagerState.currentPage)
            }
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
    index: Int,
    onClickAction: () -> Unit
) {
    println("index::$index")
    Image(
        contentScale = ContentScale.Crop,
        modifier = Modifier.offset(y = offset).size(size).clip(CircleShape)
            .background(Color.Transparent, shape = CircleShape)
            .scale(scale.value).clickable {
                onClickAction()
            }.rotate(rotation)
            .testTag(
                if (pageOffset == 0f)
                    "${MainScreenTestTags.PIZZA_ITEM.name}_${index}" else ""
            ),
        painter = painterResource(pizza.image),
        contentDescription = ""
    )
}

