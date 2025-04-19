package com.example.hotel_mobile.View.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.hotel_mobile.Dto.RoomDto
import com.example.hotel_mobile.View.Pages.LoginPage
import com.example.hotel_mobile.Modle.Screens
import com.example.hotel_mobile.Util.RoomNavType
import com.example.hotel_mobile.View.Pages.BookingForMyRoom
import com.example.hotel_mobile.View.Pages.BookingPage
import com.example.hotel_mobile.View.Pages.HomePage
import com.example.hotel_mobile.View.Pages.MyRoom
import com.example.hotel_mobile.View.Pages.RoomCreate
import com.example.hotel_mobile.View.Pages.RoomPage
import com.example.hotel_mobile.View.Pages.SignUpPage
import com.example.hotel_mobile.ViewModle.AuthViewModle
import com.example.hotel_mobile.ViewModle.HomeViewModle
import kotlin.reflect.typeOf


@Composable
fun NavController(
    navController: NavHostController,
    isLogin: Boolean?,
    authViewModle: AuthViewModle = hiltViewModel(),
    homeViewModle: HomeViewModle = hiltViewModel()
) {


    NavHost(
        navController = navController,
        startDestination = if (isLogin == true)
            Screens.homeGraph
        else Screens.authGraph
    ) {

        navigation<Screens.authGraph>(

            startDestination = Screens.login
        ) {

            composable<Screens.login> {
                LoginPage(navController,authViewModle)
            }

            composable<Screens.signUp> {
                SignUpPage(navController,authViewModle)
            }

        }

        navigation<Screens.homeGraph>(
            startDestination = Screens.home
        ) {
            composable<Screens.home> {
                HomePage(navController,homeViewModle)
            }

            composable<Screens.booking> {
                BookingPage(nav = navController, homeViewModel = homeViewModle)
            }
            composable<Screens.myRooms> {
                MyRoom(nav = navController, homeViewModel = homeViewModle)
            }

            composable<Screens.bookingForMyRoom> {
                BookingForMyRoom(nav = navController, homeViewModel = homeViewModle)
            }

            composable<Screens.createNewRoom> {
                RoomCreate(nav = navController, homeViewModel = homeViewModle)
            }


            composable<Screens.Room>(
                typeMap = mapOf(
                    typeOf<RoomDto>() to RoomNavType.RoomType
                )
            ) {
                 result ->
                val args = result.toRoute<Screens.Room>()
                RoomPage(
                    args.roomdata,
                    homeViewModle,
                    navController
                )
            }


        }
    }

}