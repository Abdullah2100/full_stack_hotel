package com.example.hotel_mobile.View.Pages

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.example.hotel_mobile.ViewModle.AuthViewModle
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hotel_mobile.Modle.Screens
import java.nio.file.WatchEvent



@Composable
fun LoginPage(
    nav: NavHostController,
    finalScreenViewModel: AuthViewModle = hiltViewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val userNameOrEmail = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }
    Scaffold{
        it.calculateTopPadding()
        it.calculateBottomPadding()


        ConstraintLayout{
            val (goToReiginster,form) = createRefs();

            Box(modifier = Modifier
                .constrainAs(goToReiginster){
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            ) {
              Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.Center
              ) {
                  Text(text = "ليس لديك حساب")
                  Box(
                  modifier = Modifier
                      .padding(start = 5.dp)
                      .clickable {
                          nav.navigate(Screens.signUp)
                      }
                  ) {
                      Text(text = "انشاء", color = Color.Blue )

                  }
              }
            }
            Column(
                modifier = Modifier
                    .constrainAs(form) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxHeight(0.9f)
                    .fillMaxWidth()
                    .padding(top = 50.dp)
                ,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                OutlinedTextField(
                    maxLines = 1,
                    value = userNameOrEmail.value,
                    onValueChange = { userNameOrEmail.value = it },
                    placeholder = {
                        Text(
                            "اسم المستخدم / ايميل",
                            color = Color.Gray.copy(alpha = 0.66f)
                        )
                    },
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .padding(horizontal = 50.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(19.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.46f)
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                )


                OutlinedTextField(
                    maxLines = 1,
                    value = password.value,
                    onValueChange = { password.value = it },
                    placeholder = {
                        Text(
                            "كملة المرور",
                            color = Color.Gray.copy(alpha = 0.66f)
                        )
                    },
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .padding(horizontal = 50.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(19.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.46f),
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {

                    })

                )

                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .padding(horizontal = 50.dp)
                        .fillMaxWidth()
                        .height(35.dp)
                ) {
                    Text(
                        "دخول",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }


        }


    }


}