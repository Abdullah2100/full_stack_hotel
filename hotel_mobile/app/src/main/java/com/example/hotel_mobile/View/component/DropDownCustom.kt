package com.example.hotel_mobile.View.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.hotel_mobile.Modle.enDropDownDateType


@Composable
fun CustomDropDownShape(
    listData:List<Int>,
    selectedValue: MutableState<Int>,
    title:String,
    dropDownType: enDropDownDateType,
    selectedDropType:MutableState<enDropDownDateType>,

    ){
    var isDropDownExpanded = remember { MutableTransitionState(initialState = false) }

    Row(
        modifier = Modifier

            .fillMaxWidth()
            .clickable {
                selectedDropType.value = dropDownType
                isDropDownExpanded.targetState = !isDropDownExpanded.targetState
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(selectedValue.value.toString())
        Text(title)

    }
//if(dropDownType===selectedDropType.value)
    AnimatedVisibility(
        visibleState = isDropDownExpanded
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(bottom = 15.dp)
                .background(Color.White.copy(0.16f))

        ) {

            items(listData) { year ->
                Box(
                    modifier = Modifier
                        .padding(bottom = 2.dp)
                        .fillMaxWidth()
                        .height(20.dp)
                        .clickable {
                            selectedValue.value = year;
                            isDropDownExpanded.targetState = false
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(year.toString())
                }
            }
        }


    }


}