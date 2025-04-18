
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt.andorid)  // Use the corrected alias here
    alias(libs.plugins.ksp.android)   // Use the correct KSP plugin
    alias(libs.plugins.kotlin.serialize.plugin)
}

android {
    namespace = "com.example.hotel_mobile"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.hotel_mobile"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        multiDexEnabled = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }



}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation (libs.androidx.runtime.livedata)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    //hilt
    implementation(libs.android.hilt)
    debugImplementation(libs.android.hilt.test)
    ksp(libs.hilt.comiler)
    implementation(libs.hilt.nav)


    // Ktor dependencies
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.loggin)
    implementation(libs.ktor.auth)
    implementation(libs.ktor.cio)


    // Kotlinx Serialization JSON
    implementation(libs.kotlinx.serialization.json)

    // Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.android)

    //navigation
    implementation(libs.compose.navigation)

    //constrain
    implementation(libs.compose.constrin)


    //room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    annotationProcessor(libs.room.compiler)
    implementation(libs.room.ktx)
    implementation(libs.room.guava)
    testImplementation(libs.room.testing)

    //secureDataBase
    implementation (libs.android.database.sqlcipher)
    implementation (libs.sqlLight.android)

    //splash Screen
    implementation(libs.splash.android)


    //prevent minRequrire api gradle
    coreLibraryDesugaring(libs.tools.desugar.jdk.libs)

    //coil
    implementation (libs.coil.compose)


    //kotlin datetime
    implementation(libs.kotlinx.datetime)

    //workder
    implementation(libs.androidx.work.runtime.ktx)

    //core

    // Java language implementation
    implementation (libs.androidx.core)


   //location
    implementation(libs.play.services.location)

    //permission
    implementation(libs.accompanist.permissions)

    //coroutinTask
    implementation(libs.kotlinx.coroutines.play.services)

}