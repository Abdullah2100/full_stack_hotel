package com.example.hotel_mobile.View.Pages

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.hotel_mobile.Dto.LoginDto
import com.example.hotel_mobile.Modle.Request.RoomImageCreation
import com.example.hotel_mobile.Modle.enNetworkStatus
import com.example.hotel_mobile.Util.General
import com.example.hotel_mobile.Util.General.toCustomFil
import com.example.hotel_mobile.View.component.CustomErrorSnackBar
import com.example.hotel_mobile.View.component.TextFeildNumber
import com.example.hotel_mobile.ViewModle.HomeViewModle
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("SuspiciousIndentation")
@Composable
fun RoomCreate(
    homeViewModel: HomeViewModle,
    nav: NavHostController,
){


    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scop = rememberCoroutineScope()
    val stateus = homeViewModel.statusChange.collectAsState()
    val roomdata = homeViewModel.roomData.collectAsState()
    val roomtTypeData = homeViewModel.roomTypesData.collectAsState()
    val theumanil = roomdata.value.images?.firstOrNull { it.isThumbnail==true }
    val roomImages = roomdata.value.images?.filter { it.isThumbnail==false }

    val pricePerOnNigth = remember{mutableStateOf("")}
    val capacity = remember{mutableStateOf("")}
    val bedNumber = remember{mutableStateOf("")}
    val isDropDownClicked = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val  onImageSelection =rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val fileHolder =  uri.toCustomFil(context = context);
            if(fileHolder!=null)
            homeViewModel.setRoomThumnail(
                RoomImageCreation(
                    null,
                    fileHolder,
                    null,
                    false,
                    true
                )
            )

        } else {
        }
    }

    val selectMutipleImages = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(10)) { uris ->
            // Callback is invoked after the user selects media items or closes the
            // photo picker.
            if (uris.isNotEmpty()) {
                uris.forEach {roomImageHolder->
                    homeViewModel.setRoomThumnail(
                        RoomImageCreation(
                            null,
                            roomImageHolder.toCustomFil(context)!!,
                            null,
                            false,
                           false
                        )
                    )
                }
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

   val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val arePermissionsGranted = permissionsMap.values.reduce { acc, next ->
            acc && next
        }

        if (arePermissionsGranted) {
            scop.launch(Dispatchers.Main) {
                val data = fusedLocationClient.lastLocation.await()
                data?.let {
                    location->

                    homeViewModel.setRoomData(
                        longitude = location.longitude,
                        latitude = location.latitude
                    )
                }
            }



        } else {
        }
    }


    LaunchedEffect(theumanil!=null) {
        Log.d("imageResultData","${theumanil?.data}")
    }

    CustomErrorSnackBar(
        authViewModel = null,
        homeViewModel = homeViewModel,
        page = {

            Scaffold(
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(top = 50.dp)
                    .background(Color.Red),
                topBar = {
                    Box(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .height(30.dp)
                            .width(30.dp)
                            .background(Color.Blue, shape = RoundedCornerShape(15.dp))
                            .clickable {
                                nav.popBackStack()
                            }) {
                        Image(
                            Icons.Outlined.KeyboardArrowLeft,
                            "",
                            colorFilter = ColorFilter.tint(color = Color.White),
                            modifier = Modifier.size(40.dp)
                        )
                    }
                },

                ) {
                it.calculateTopPadding()
                it.calculateBottomPadding()

                Column(modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()))
                {
                    Box(Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .padding(horizontal = 20.dp)
                        .height(220.dp)
                        .fillMaxWidth()
                        .drawBehind {
                            drawRoundRect(color = Color.Black, style = General.dashStrock())
                        }
                        .clickable {
                            onImageSelection.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }
                        ,
                        contentAlignment = Alignment.Center
                        ) {
                        when(theumanil==null){
                            true->{

                                Text("صورة الاساسية للغرفة")
                            }
                            else->{
                                ConstraintLayout{
                               val (iconRef) = createRefs()

                                    SubcomposeAsyncImage(
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .padding(horizontal = 20.dp, vertical = 20.dp)
                                            .height(250.dp)
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp)),
                                        model =
                                        ImageRequest.Builder(context)
                                            .data(theumanil.data.path)
                                            .build(),
                                        contentDescription = "",
                                        loading = {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize(),
                                                contentAlignment = Alignment.Center // Ensures the loader is centered and doesn't expand
                                            ) {
                                                CircularProgressIndicator(
                                                    color = Color.Black,
                                                    modifier = Modifier.size(54.dp) // Adjust the size here
                                                )
                                            }
                                        },
                                    )
                                    Box(
                                        modifier = Modifier
                                            .padding(
                                                top = 5.dp,
                                                end = 15.dp
                                            )
                                            .background(
                                                Color.Red,
                                                shape = RoundedCornerShape(20.dp)
                                            )
                                            .constrainAs(iconRef) {
                                                end.linkTo(parent.end)
                                            }
                                            .clickable {
                                                homeViewModel.setRoomThumnail(
                                                    theumanil,
                                                    isDeleted = true
                                                )
                                            }
                                    ) {
                                        Icon(Icons.Default.Clear, contentDescription = "",
                                            tint = Color.White)
                                    }
                                }




                            }
                        }
                    }

                    Box(Modifier
                        .padding(top = 20.dp)
                        .padding(horizontal = 20.dp)
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .drawBehind {

                            drawRoundRect(color = Color.Black, style = General.dashStrock())
                        }
                        .clickable {
                            selectMutipleImages.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }, contentAlignment = Alignment.Center)
                    {
                        when(roomImages.isNullOrEmpty()){
                            true->{

                                Column (
                                    modifier = Modifier.height(200.dp),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text("تحديد صور الغرفة",
                                        )
                                }

                            }
                            else->{

                                  FlowRow (
                                      modifier = Modifier
                                          .padding(
                                              horizontal = 10.dp,
                                              vertical = 10.dp
                                          )
                                          .fillMaxHeight()
                                          .fillMaxWidth(),
                                      horizontalArrangement = Arrangement.Center

                                  ) {
                                      roomImages.forEach {roomImage->
                                          Column {
                                              ConstraintLayout{
                                                  val (iconRef) = createRefs()

                                                  SubcomposeAsyncImage(
                                                      contentScale = ContentScale.Crop,
                                                      modifier = Modifier
                                                          .padding(5.dp)
                                                          .height(150.dp)
                                                          .width(150.dp)
                                                          .clip(RoundedCornerShape(8.dp)),
                                                      model =
                                                      ImageRequest.Builder(context)
                                                          .data(roomImage.data.path)
                                                          .build(),
                                                      contentDescription = "",
                                                      loading = {
                                                          Box(
                                                              modifier = Modifier
                                                                  .fillMaxSize(),
                                                              contentAlignment = Alignment.Center // Ensures the loader is centered and doesn't expand
                                                          ) {
                                                              CircularProgressIndicator(
                                                                  color = Color.Black,
                                                                  modifier = Modifier.size(54.dp) // Adjust the size here
                                                              )
                                                          }
                                                      },
                                                  )
                                                  Box(
                                                      modifier = Modifier
                                                          .padding(
                                                              top = 5.dp,
                                                              end = 5.dp
                                                          )
                                                          .background(
                                                              Color.Red,
                                                              shape = RoundedCornerShape(20.dp)
                                                          )
                                                          .constrainAs(iconRef) {
                                                              end.linkTo(parent.end)
                                                          }
                                                          .clickable {
                                                              homeViewModel.setRoomThumnail(
                                                                  roomImage,
                                                                  isDeleted = true
                                                              )
                                                          }
                                                  ) {
                                                      Icon(Icons.Default.Clear, contentDescription = "",
                                                          tint = Color.White)
                                                  }
                                              }

                                          }
                                      }

                                  }
                            }
                        }
                    }


if(!roomtTypeData.value.isNullOrEmpty())
{

   Column(
       modifier = Modifier
           .padding(top = 20.dp)
       .padding(horizontal = 20.dp)
       .fillMaxWidth()
   ) {
       Text("تحديد نوع الغرفة",
           fontWeight = FontWeight.Bold,
           fontSize = 19.sp)
       Box(
           modifier = Modifier
                  .height(50.dp)
               .fillMaxWidth()

               .border(
                   width = 1.dp,
                   color = Color.Black,
                   shape = RoundedCornerShape(8.dp)
               )
               .padding(start = 10.dp)
               .clickable {
                   isDropDownClicked.value=true
               },
           contentAlignment = Alignment.CenterStart
       ) {
           Text(
               if(roomdata.value.roomtypeid==null)
                   roomtTypeData.value!!.get(0).roomTypeName
               else
                   roomtTypeData.value!!.first { it.roomTypeID==roomdata.value.roomtypeid }.roomTypeName
           )
       }
       DropdownMenu(expanded =isDropDownClicked.value ,
           onDismissRequest = {
               isDropDownClicked.value=false;

           }) {
           roomtTypeData.value!!.forEachIndexed { index, roomTypeModel ->
               DropdownMenuItem(text = {
                   Text(roomTypeModel.roomTypeName)
               }, onClick = {
                   homeViewModel.setRoomData(
                       roomtypeid = roomTypeModel.roomTypeID
                   )
                   isDropDownClicked.value=false;

               })
           }
       }
   }

}

                    TextFeildNumber(pricePerOnNigth,"سعر اليلة الواحدة")
                    TextFeildNumber(capacity,"سعة الغرفة")
                    TextFeildNumber(bedNumber,"عدد السرائر")
                    Button(

                        modifier = Modifier
                            .padding(top = 10.dp)
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth()
                          ,
                        shape = RoundedCornerShape(8.dp)
                        ,
                        onClick = {
                            locationPermissionLauncher.launch(arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ))
                        }
                    ) {
                        Text("تعيين الموقع الحالي للغرفة")
                    }

                    Button(
                        enabled = stateus.value != enNetworkStatus.Loading,

                        onClick = {
                            keyboardController?.hide();
                            homeViewModel.setRoomData(
                                pricePerNight =if(pricePerOnNigth.value.isNullOrEmpty())null else pricePerOnNigth.value?.trim()?.toDouble(),
                                capacity= if(capacity.value.isNullOrEmpty())null else capacity.value?.trim()?.toInt(),
                                bedNumber =if(bedNumber.value.isNullOrEmpty())null else bedNumber.value?.trim()?.toInt()
                            )
                            homeViewModel.createRoom(
                                snackbarHostState = snackbarHostState,
                                navController = nav
                            )
                        },
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth()
                            .height(35.dp)
                    ) {
                        when (stateus.value) {
                            enNetworkStatus.Loading -> {
                                CircularProgressIndicator(
                                    color = Color.Blue, modifier = Modifier
                                        .offset(y = -3.dp)
                                        .height(25.dp)
                                        .width(25.dp)
                                )
                            }

                            else -> {
                                Text(
                                    "انشاء",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }


                    }
                    Box(modifier=Modifier.height(90.dp))
                }
            }

        }
    )

}