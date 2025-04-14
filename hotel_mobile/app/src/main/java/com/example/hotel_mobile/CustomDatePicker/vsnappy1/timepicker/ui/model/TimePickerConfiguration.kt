package com.example.hotel_mobile.CustomDatePicker.vsnappy1.timepicker.ui.model

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hotel_mobile.CustomDatePicker.vsnappy1.theme.Size
import com.example.hotel_mobile.CustomDatePicker.vsnappy1.theme.grayLight
import com.example.hotel_mobile.CustomDatePicker.vsnappy1.timepicker.data.DefaultTimePickerConfig

class TimePickerConfiguration private constructor(
    val height: Dp,
    val timeTextStyle: TextStyle,
    val selectedTimeTextStyle: TextStyle,
    val numberOfTimeRowsDisplayed: Int,
    val selectedTimeScaleFactor: Float,
    val selectedTimeAreaHeight: Dp,
    val selectedTimeAreaColor: Color,
    val selectedTimeAreaShape: Shape
) {
    class Builder {
        private var height: Dp = DefaultTimePickerConfig.height
        private var timeTextStyle: TextStyle = DefaultTimePickerConfig.timeTextStyle
        private var selectedTimeTextStyle: TextStyle = DefaultTimePickerConfig.selectedTimeTextStyle
        private var numberOfTimeRowsDisplayed: Int =
            DefaultTimePickerConfig.numberOfTimeRowsDisplayed
        private var selectedTimeScaleFactor: Float = DefaultTimePickerConfig.selectedTimeScaleFactor
        private var selectedTimeAreaHeight: Dp = DefaultTimePickerConfig.selectedTimeAreaHeight
        private var selectedTimeAreaColor: Color = DefaultTimePickerConfig.selectedTimeAreaColor
        private var selectedTimeAreaShape: Shape = DefaultTimePickerConfig.selectedTimeAreaShape

        fun height(height: Dp) =
            apply { this.height = height }

        fun build(): TimePickerConfiguration {
            return TimePickerConfiguration(
                height = height,
                timeTextStyle = timeTextStyle,
                selectedTimeTextStyle = selectedTimeTextStyle,
                numberOfTimeRowsDisplayed = numberOfTimeRowsDisplayed,
                selectedTimeScaleFactor = selectedTimeScaleFactor,
                selectedTimeAreaHeight = selectedTimeAreaHeight,
                selectedTimeAreaColor = selectedTimeAreaColor,
                selectedTimeAreaShape = selectedTimeAreaShape,
            )
        }
    }
}