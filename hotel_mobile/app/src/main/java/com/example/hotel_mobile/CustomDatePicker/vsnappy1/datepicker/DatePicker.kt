package  com.example.hotel_mobile.CustomDatePicker.vsnappy1.datepicker

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hotel_mobile.CustomDatePicker.vsnappy1.component.AnimatedFadeVisibility
import  com.example.hotel_mobile.CustomDatePicker.vsnappy1.datepicker.ui.model.DatePickerConfiguration
import  com.example.hotel_mobile.CustomDatePicker.vsnappy1.datepicker.data.Constant
import  com.example.hotel_mobile.CustomDatePicker.vsnappy1.datepicker.data.model.DatePickerDate
import  com.example.hotel_mobile.CustomDatePicker.vsnappy1.datepicker.data.model.DefaultDate
import  com.example.hotel_mobile.CustomDatePicker.vsnappy1.datepicker.data.model.Month
import  com.example.hotel_mobile.CustomDatePicker.vsnappy1.datepicker.data.model.SelectionLimiter
import  com.example.hotel_mobile.CustomDatePicker.vsnappy1.datepicker.enums.Days
import  com.example.hotel_mobile.CustomDatePicker.vsnappy1.datepicker.ui.model.DatePickerUiState
import  com.example.hotel_mobile.CustomDatePicker.vsnappy1.datepicker.ui.viewmodel.DatePickerViewModel
import com.example.hotel_mobile.CustomDatePicker.vsnappy1.extension.noRippleClickable
import com.example.hotel_mobile.CustomDatePicker.vsnappy1.extension.spToDp
import com.example.hotel_mobile.CustomDatePicker.vsnappy1.extension.toDp
import com.example.hotel_mobile.CustomDatePicker.vsnappy1.theme.Size.medium
import com.example.hotel_mobile.Util.General
import com.example.hotel_mobile.View.component.CustomSizer
import kotlinx.coroutines.launch
import kotlin.math.ceil


@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    onDateSelected: (Int, Int, Int) -> Unit,
    selectionLimiter: SelectionLimiter = SelectionLimiter(),
    configuration: DatePickerConfiguration = DatePickerConfiguration.Builder().build(),
    month: Int = General.getCurrentMonth(),
    year: Int = General.getCurrentYear(),
    alreadyBookedList: Map<Int, Int>?=null,
    isYear: Boolean = false,
    isMonth: Boolean = false,
    dialogState:MutableState<Boolean> = mutableStateOf(false)
) {
    Log.d("bookedList",alreadyBookedList.toString())

    val isShownDay = isMonth == false && isYear == false;

    val dateHolder = DatePickerDate(year, month, 0);
    val currentMonth = Constant.getMonths(year)[month];


    val viewModel: DatePickerViewModel = viewModel()
    val uiState by viewModel.uiState.observeAsState(
        DatePickerUiState(
            selectedYear = year,
            selectedMonth = currentMonth ,
            selectedDayOfMonth = 0
        )
    )

    // Key is Unit because I want this to run only once not every time when is composable is recomposed.
    LaunchedEffect(key1 = Unit) {
        viewModel.setDate(
            dateHolder
        )
    }



    var height by remember { mutableStateOf(configuration.height) }
    Column(
        horizontalAlignment = Alignment.End
    ) {


        Box(modifier = modifier.onGloballyPositioned {
            if (it.size.height == 0) return@onGloballyPositioned
            height = it.size.height.toDp() - configuration.headerHeight// Update the height
        })
        {

            // TODO add sliding effect when next or previous arrow is pressed

            CalendarHeader(
                title = "${uiState.currentVisibleMonth.name} ${uiState.selectedYear}",
                isPreviousNextVisible = false,// !uiState?.isMonthYearViewVisible,
                themeColor = configuration.selectedDateBackgroundColor,
                configuration = configuration,
            )



//            if(viewModel.monthChange.value==true)
            Box(
                modifier = Modifier
                    .padding(top = configuration.headerHeight)
                    .height(height)
            ) {
                AnimatedFadeVisibility(
                    visible = isShownDay// !uiState.isMonthYearViewVisible
                ) {
                    DateView(
                        currentVisibleMonth = uiState.currentVisibleMonth,
                        selectedYear = uiState.selectedYear,
                        selectedMonth = uiState.selectedMonth,
                        selectedDayOfMonth = uiState.selectedDayOfMonth,
                        selectionLimiter = selectionLimiter,
                        height = height,
                        onDaySelected = {
                            viewModel.updateSelectedDayAndMonth(
                                it
                            )
                        },
                        configuration = configuration,
                        alreadyBookedList =if(alreadyBookedList==null) emptyMap() else alreadyBookedList,
                        date = dateHolder
                    )
                }

                AnimatedFadeVisibility(
                    visible = !isShownDay // uiState.isMonthYearViewVisible
                ) {

                    MonthAndYearView(
                        modifier = Modifier.align(Alignment.Center),
                        selectedMonth = uiState.selectedMonthIndex,
                        onMonthChange = {
                            viewModel.updateSelectedMonthIndex(it)
                        },
                        selectedYear = uiState.selectedYearIndex,
                        onYearChange = {
                            viewModel.updateSelectedYearIndex(it)
                        },
                        years = uiState.years,
                        months = Constant.getMonths(year).map { it.name } ,
                        height = height,
                        configuration = configuration,
                        isYear = isYear,
                    )
                }
            }
        }
        Row(
            modifier= Modifier
                .padding(horizontal = 19.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            Button(onClick = {
                dialogState.value=false
            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                )) {
                Text(text = "الغاء")
            }
            CustomSizer(width = 10.dp)

            Button(onClick = {
                onDateSelected(
                    if(isYear)uiState.selectedYear else 0,
                    uiState.selectedMonthIndex,
                    if(uiState.selectedDayOfMonth!=0)uiState.selectedDayOfMonth else 0
                )
            },
            ) {
                Text(text = "تم")
            }
        }


    }
   }

@Composable
private fun MonthAndYearView(
    modifier: Modifier = Modifier,
    selectedMonth: Int,
    onMonthChange: (Int) -> Unit,
    selectedYear: Int,
    onYearChange: (Int) -> Unit,
    years: List<String>,
    months: List<String>,
    height: Dp,
    configuration: DatePickerConfiguration,
    isYear: Boolean = false,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize(),
    ) {
        Box(
            modifier = modifier
                .padding(horizontal = medium)
                .fillMaxWidth()
                .height(configuration.selectedMonthYearAreaHeight)
                .background(
                    color = configuration.selectedMonthYearAreaColor,
                    shape = configuration.selectedMonthYearAreaShape
                )
        )
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (isYear) {
                true -> {
                    SwipeLazyColumn(
                        modifier = Modifier.weight(0.5f),
                        selectedIndex = selectedYear,
                        onSelectedIndexChange = onYearChange,
                        items = years,
                        height = height,
                        configuration = configuration
                    )
                }


                else -> {
                    SwipeLazyColumn(
                        modifier = Modifier.weight(0.5f),
                        selectedIndex = selectedMonth,
                        onSelectedIndexChange = onMonthChange,
                        items = months,
                        height = height,
                        configuration = configuration
                    )
                }
            }
        }
    }
}

@Composable
private fun SwipeLazyColumn(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    items: List<String>,
    configuration: DatePickerConfiguration,
    height: Dp,

    ) {
    val coroutineScope = rememberCoroutineScope()
    var isAutoScrolling by remember { mutableStateOf(false) }
    val listState = rememberLazyListState(selectedIndex)
    com.example.hotel_mobile.CustomDatePicker.vsnappy1.component.SwipeLazyColumn(
        modifier = modifier,
        selectedIndex = selectedIndex,
        onSelectedIndexChange = onSelectedIndexChange,
        isAutoScrolling = isAutoScrolling,
        height = height,
        numberOfRowsDisplayed = configuration.numberOfMonthYearRowsDisplayed,
        listState = listState,
        onScrollingStopped = {}
    ) {
        // I add some empty rows at the beginning and end of list to make it feel that it is a center focused list
        val count = items.size + configuration.numberOfMonthYearRowsDisplayed - 1
        items(count) {
            SliderItem(
                value = it,
                selectedIndex = selectedIndex,
                items = items,
                configuration = configuration,
                height = height,
                onItemClick = { index ->
                    onSelectedIndexChange(index)
                    coroutineScope.launch {
                        isAutoScrolling = true
                        onSelectedIndexChange(index)
                        listState.animateScrollToItem(index)
                        isAutoScrolling = false
                    }
                }
            )
        }
    }
}

@Composable
private fun SliderItem(
    value: Int,
    selectedIndex: Int,
    items: List<String>,
    onItemClick: (Int) -> Unit,
    configuration: DatePickerConfiguration,
    height: Dp,
) {
    // this gap variable helps in maintaining list as center focused list
    val gap = configuration.numberOfMonthYearRowsDisplayed / 2
    val isSelected = value == selectedIndex + gap
    val scale by animateFloatAsState(targetValue = if (isSelected) configuration.selectedMonthYearScaleFactor else 1f)
    Box(
        modifier = Modifier
            .height(height / (configuration.numberOfMonthYearRowsDisplayed))
    ) {
        if (value >= gap && value < items.size + gap) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .noRippleClickable {
                        onItemClick(value - gap)
                    },
                contentAlignment = Alignment.Center
            ) {
                configuration.selectedMonthYearTextStyle.fontSize
                Box(
                    modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = items[value - gap],
                        modifier = Modifier
                            //.align(alignment)
                            .scale(scale),
                        style = if (isSelected) configuration.selectedMonthYearTextStyle
                        else configuration.monthYearTextStyle
                    )
                }
            }
        }
    }
}


@Composable
private fun DateView(
    modifier: Modifier = Modifier,
    selectedYear: Int,
    currentVisibleMonth: Month,
    selectedDayOfMonth: Int?,
    selectionLimiter: SelectionLimiter,
    onDaySelected: (Int) -> Unit,
    selectedMonth: Month,
    height: Dp,
    configuration: DatePickerConfiguration,
    alreadyBookedList: Map<Int, Int>,
    date: DatePickerDate = DefaultDate.defaultDate
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        userScrollEnabled = false,
        modifier = modifier
    ) {
        items(Constant.days) {
            DateViewHeaderItem(day = it, configuration = configuration)
        }
        // since I may need few empty cells because every month starts with a different day(Monday, Tuesday, ..)
        // that's way I add some number X into the count
        val count =
            currentVisibleMonth.numberOfDays + currentVisibleMonth.firstDayOfMonth.number - 1
        val topPaddingForItem =
            getTopPaddingForItem(
                count,
                height - configuration.selectedDateBackgroundSize * 2, // because I don't want to count first two rows
                configuration.selectedDateBackgroundSize
            )
        items(count) {
            if (it < currentVisibleMonth.firstDayOfMonth.number - 1) return@items // to create empty boxes
            DateViewBodyItem(
                value = it,
                currentVisibleMonth = currentVisibleMonth,
                selectedYear = selectedYear,
                selectedMonth = selectedMonth,
                selectedDayOfMonth = selectedDayOfMonth,
                selectionLimiter = selectionLimiter,
                onDaySelected = onDaySelected,
                topPaddingForItem = topPaddingForItem,
                configuration = configuration,
                alreadyBookedList = alreadyBookedList,
                date = date
            )
        }
    }
}

@Composable
private fun DateViewBodyItem(
    value: Int,
    currentVisibleMonth: Month,
    selectedYear: Int,
    selectedMonth: Month,
    selectedDayOfMonth: Int?,
    selectionLimiter: SelectionLimiter,
    onDaySelected: (Int) -> Unit,
    topPaddingForItem: Dp,
    configuration: DatePickerConfiguration,
    alreadyBookedList: Map<Int, Int>,
    date: DatePickerDate = DefaultDate.defaultDate
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        val day = value - currentVisibleMonth.firstDayOfMonth.number + 2

        val isSelected = day == selectedDayOfMonth // && selectedMonth == currentVisibleMonth

        val isInTheSameYearAndMonth = General.getCurrentYear() == date.year &&
                General.getCurrentMonth() == date.month

        val canSelectDate = General.getCurrentYear() <= date.year &&
                General.getCurrentMonth() <= date.month &&
                (if (!isInTheSameYearAndMonth) day >= 0
                else
                    day > General.getCurrentStartDayAtMonth() - 1)
                      //  )

        val isWithinRange = selectionLimiter.isWithinRange(
            DatePickerDate(
                selectedYear,
                currentVisibleMonth.number,
                day
            )
        )
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
                .padding(top = if (value < 7) 0.dp else topPaddingForItem) // I don't want first row to have any padding
                .size(configuration.selectedDateBackgroundSize)
                .clip(configuration.selectedDateBackgroundShape)
                .noRippleClickable(enabled = isWithinRange) {

                    if (!alreadyBookedList.containsKey(day) && canSelectDate) {
                        onDaySelected(day)
                    }
                }
                .background(

                    if (alreadyBookedList.containsKey(day)) Color.Red
                    else if (isSelected) configuration.selectedDateBackgroundColor
                    else
                        Color.Transparent
                )
        ) {
            Text(
                text = "$day",
                textAlign = TextAlign.Center,
                style =
                if (isSelected) configuration.selectedDateTextStyle
                    .copy(
                        color =
                        if (canSelectDate) configuration.selectedDateTextStyle.color
                        else configuration.disabledDateColor
                    )
                else
                    configuration.dateTextStyle.copy(
                        color =
                        if (alreadyBookedList.containsKey(day) || isSelected) Color.White
                        else if (!canSelectDate) Color.Gray.copy(0.80f)
                        else configuration.dateTextStyle.color
                    ),
            )
        }


    }
}

@Composable
private fun DateViewHeaderItem(
    configuration: DatePickerConfiguration,
    day: Days
) {
    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .size(configuration.selectedDateBackgroundSize)
    ) {
        Text(
            text = day.abbreviation,
            textAlign = TextAlign.Center,
            style = configuration.daysNameTextStyle.copy(
                color = if (day.number == 1) configuration.sundayTextColor else configuration.daysNameTextStyle.color
            ),
        )
    }
}

// Not every month has same number of weeks, so to maintain the same size for calender I add padding if there are less weeks
private fun getTopPaddingForItem(
    count: Int,
    height: Dp,
    size: Dp
): Dp {
    val numberOfRowsVisible = ceil(count.toDouble() / 7) - 1
    val result =
        (height - (size * numberOfRowsVisible.toInt()) - medium) / numberOfRowsVisible.toInt()
    return if (result > 0.dp) result else 0.dp
}

@Composable
private fun CalendarHeader(
    modifier: Modifier = Modifier,
    title: String,
    isPreviousNextVisible: Boolean,
    configuration: DatePickerConfiguration,
    themeColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(configuration.headerHeight)
    ) {
        val textColor by
        animateColorAsState(
            targetValue = if (isPreviousNextVisible) configuration.headerTextStyle.color else themeColor,
            animationSpec = tween(durationMillis = 400, delayMillis = 100)
        )
        Text(
            text = title,
            style = configuration.headerTextStyle.copy(color = textColor),
            modifier = modifier
                .padding(start = medium)
                .align(Alignment.CenterStart),
        )
    }
}


@Preview
@Composable
fun DefaultDatePicker() {
    DatePicker(onDateSelected = { _: Int, _: Int, _: Int -> })
}