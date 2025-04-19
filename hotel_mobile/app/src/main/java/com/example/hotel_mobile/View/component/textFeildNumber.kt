package com.example.hotel_mobile.View.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


@Composable
fun TextFeildNumber(
     textValue: MutableState<String>,
     lable:String
){
    val pattern = remember { Regex("^\\d+\$") }

    OutlinedTextField(
        keyboardOptions = KeyboardOptions.Default
            .copy(keyboardType = KeyboardType.Number),
        value = textValue.value,
        onValueChange = {
            if(it.isNotEmpty()&&it.matches(pattern)){
                textValue.value =it
            }
        },
        label = {
            Text(lable)
        }
        , modifier = Modifier
            .padding(top = 10.dp)
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            disabledContainerColor = Color.White,
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.White
        )
    )
}