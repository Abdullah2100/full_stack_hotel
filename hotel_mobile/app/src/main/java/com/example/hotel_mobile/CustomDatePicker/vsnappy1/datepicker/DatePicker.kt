package  com.example.hotel_mobile.CustomDatePicker.vsnappy1.datepicker

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.hotel_mobile.CustomDatePicker.vsnappy1.extension.toDp
import com.example.hotel_mobile.CustomDatePicker.vsnappy1.theme.Size.medium
import com.example.hotel_mobile.Util.General
import kotlinx.coroutines.launch
import kotlin.math.ceil


@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    onDateSelected: (Int, Int, Int) -> Unit,
    date: DatePickerDate = DefaultDate.defaultDate,
    selectionLimiter: SelectionLimiter = SelectionLimiter(),
    configuration: DatePickerConfiguration = DatePickerConfiguration.Builder().build(),
    id: Int = 1,
    month: Int = General.getCurrentMonth(),
    year: Int = General.getCurrentYear(),
    alreadyBookedList: Map<Int, Int> = mapOf(1 to 1)
) {
    date.year = year;
    date.month = month-1;
    if(!(year==General.getCurrentYear()&&month==General.getCurrentMonth()))
        date.day=0



    val viewModel: DatePickerViewModel = viewModel(key = "DatePickerViewModel$id")
    val uiState by viewModel.uiState.observeAsState(
        DatePickerUiState(
            selectedYear = year,
            selectedMonth = Constant.getMonths(date.year)[month-1],
            selectedDayOfMonth = if(year==General.getCurrentYear()&&month==General.getCurrentMonth()) date.day else 0
        )
    )
    Log.d("selectedNumber","${date}")

    // Key is Unit because I want this to run only once not every time when is composable is recomposed.
    LaunchedEffect(key1 = Unit) { viewModel.setDate(date) }

    var height by remember { mutableStateOf(configuration.height) }
    Box(modifier = modifier.onGloballyPositioned {
        if (it.size.height == 0) return@onGloballyPositioned
        height = it.size.height.toDp() - configuration.headerHeight// Update the height
    }) {
        // TODO add sliding effect when next or previous arrow is pressed
        CalendarHeader(
            title = "${uiState.currentVisibleMonth.name} ${uiState.selectedYear}",
            onMonthYearClick = { viewModel.toggleIsMonthYearViewVisible() },
            onNextClick = { viewModel.moveToNextMonth() },
            onPreviousClick = { viewModel.moveToPreviousMonth() },
            isPreviousNextVisible = !uiState.isMonthYearViewVisible,
            themeColor = configuration.selectedDateBackgroundColor,
            configuration = configuration,
        )
        Box(
            modifier = Modifier
                .padding(top = configuration.headerHeight)
                .height(height)
        ) {
            AnimatedFadeVisibility(
                visible = !uiState.isMonthYearViewVisible
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
                        onDateSelected(
                            uiState.selectedYear,
                            uiState.selectedMonth.number,
                            uiState.selectedDayOfMonth
                        )
                    },
                    configuration = configuration,
                    alreadyBookedList = alreadyBookedList,
                    date = date
                )
            }
        }
    }
    // Call onDateSelected when composition is completed
    LaunchedEffect(key1 = Unit) {
        onDateSelected(
            uiState.selectedYear,
            uiState.selectedMonth.number,
            uiState.selectedDayOfMonth
        )
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
//
        val day = value - currentVisibleMonth.firstDayOfMonth.number + 2

        val isSelected = day == selectedDayOfMonth  && selectedMonth == currentVisibleMonth




        val canSelectDate = General.getCurrentYear()<= date.year &&
                General.getCurrentMonth()<=date.month &&
                day > General.getCurrentStartDayAtMonth()-1

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
                    if (!alreadyBookedList.containsKey(day)&&canSelectDate)
                        onDaySelected(day)
                }
                .background(
                    if (isSelected) configuration.selectedDateBackgroundColor
                    else if (alreadyBookedList.containsKey(day)) Color.Red
                    else
                        Color.Transparent
                )
        ) {
            Text(
                text = "$day",
                textAlign = TextAlign.Center,
                style =
                if (isSelected) configuration.selectedDateTextStyle
                    .copy(color =
                    if (canSelectDate) configuration.selectedDateTextStyle.color
                    else configuration.disabledDateColor
                    )

                else
                    configuration.dateTextStyle.copy(
                    color =//if (isWithinRange) {
//                        if (value % 7 == 0) configuration.sundayTextColor
                         if (alreadyBookedList.containsKey(day)||isSelected) Color.White
                        else if(!canSelectDate)Color.Gray.copy(0.80f)
                        else configuration.dateTextStyle.color
                    //} else {
                   //     configuration.disabledDateColor
                   // }
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
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onMonthYearClick: () -> Unit,
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