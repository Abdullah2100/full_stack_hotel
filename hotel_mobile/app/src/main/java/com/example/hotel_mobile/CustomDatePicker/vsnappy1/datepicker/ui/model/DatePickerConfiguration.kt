package  com.example.hotel_mobile.CustomDatePicker.vsnappy1.datepicker.ui.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import  com.example.hotel_mobile.CustomDatePicker.vsnappy1.datepicker.data.DefaultDatePickerConfig

class DatePickerConfiguration private constructor(
    val height: Dp,
    val headerHeight: Dp,
    val headerTextStyle: TextStyle,

    val daysNameTextStyle: TextStyle,
    val dateTextStyle: TextStyle,
    val selectedDateTextStyle: TextStyle,
    val sundayTextColor: Color,
    val disabledDateColor: Color,
    val selectedDateBackgroundSize: Dp,
    val selectedDateBackgroundColor: Color,
    val selectedDateBackgroundShape: Shape,
    val monthYearTextStyle: TextStyle,
    val selectedMonthYearTextStyle: TextStyle,
    val selectedMonthYearScaleFactor: Float,
    val numberOfMonthYearRowsDisplayed: Int,
    val selectedMonthYearAreaHeight: Dp,
    val selectedMonthYearAreaColor: Color,
    val selectedMonthYearAreaShape: Shape
) {
    class Builder {
        private var height: Dp = DefaultDatePickerConfig.height
        private var headerHeight: Dp = DefaultDatePickerConfig.headerHeight
        private var headerTextStyle: TextStyle = DefaultDatePickerConfig.headerTextStyle
        private var headerArrowSize: Dp = DefaultDatePickerConfig.headerArrowSize
        private var headerArrowColor: Color = DefaultDatePickerConfig.headerArrowColor
        private var daysNameTextStyle: TextStyle = DefaultDatePickerConfig.daysNameTextStyle
        private var dateTextStyle: TextStyle = DefaultDatePickerConfig.dateTextStyle
        private var selectedDateTextStyle: TextStyle = DefaultDatePickerConfig.selectedDateTextStyle
        private var sundayTextColor: Color = DefaultDatePickerConfig.sundayTextColor
        private var disabledDateColor: Color = DefaultDatePickerConfig.disabledDateColor
        private var selectedDateBackgroundSize: Dp =
            DefaultDatePickerConfig.selectedDateBackgroundSize
        private var selectedDateBackgroundColor: Color =
            DefaultDatePickerConfig.selectedDateBackgroundColor
        private var selectedDateBackgroundShape: Shape =
            DefaultDatePickerConfig.selectedDateBackgroundShape
        private var monthYearTextStyle: TextStyle = DefaultDatePickerConfig.monthYearTextStyle
        private var selectedMonthYearTextStyle: TextStyle =
            DefaultDatePickerConfig.selectedMonthYearTextStyle
        private var numberOfMonthYearRowsDisplayed: Int =
            DefaultDatePickerConfig.numberOfMonthYearRowsDisplayed
        private var selectedMonthYearScaleFactor: Float =
            DefaultDatePickerConfig.selectedMonthYearScaleFactor
        private var selectedMonthYearAreaHeight: Dp =
            DefaultDatePickerConfig.selectedMonthYearAreaHeight
        private var selectedMonthYearAreaColor: Color =
            DefaultDatePickerConfig.selectedMonthYearAreaColor
        private var selectedMonthYearAreaShape: Shape =
            DefaultDatePickerConfig.selectedMonthYearAreaShape

        fun height(height: Dp) =
            apply { this.height = height }



        fun build(): DatePickerConfiguration {
            return DatePickerConfiguration(
                height = height,
                headerHeight = headerHeight,
                headerTextStyle = headerTextStyle,
                daysNameTextStyle = daysNameTextStyle,
                dateTextStyle = dateTextStyle,
                selectedDateTextStyle = selectedDateTextStyle,
                sundayTextColor = sundayTextColor,
                disabledDateColor = disabledDateColor,
                selectedDateBackgroundSize = selectedDateBackgroundSize,
                selectedDateBackgroundColor = selectedDateBackgroundColor,
                selectedDateBackgroundShape = selectedDateBackgroundShape,
                monthYearTextStyle = monthYearTextStyle,
                selectedMonthYearTextStyle = selectedMonthYearTextStyle,
                selectedMonthYearScaleFactor = selectedMonthYearScaleFactor,
                numberOfMonthYearRowsDisplayed = numberOfMonthYearRowsDisplayed,
                selectedMonthYearAreaHeight = selectedMonthYearAreaHeight,
                selectedMonthYearAreaColor = selectedMonthYearAreaColor,
                selectedMonthYearAreaShape = selectedMonthYearAreaShape
            )
        }
    }
}