plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.eva.reminders"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.eva.reminders"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.eva.reminders.utils.AppCustomRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
    arg("room.generateKotlin", "true")
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class:1.1.2")
    // dependency injection
    implementation("com.google.dagger:hilt-android:2.48")
    ksp("com.google.dagger:hilt-android-compiler:2.48")
    //navigation
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    //room
    implementation("androidx.room:room-ktx:2.6.0")
    implementation("androidx.room:room-runtime:2.6.0")
    annotationProcessor("androidx.room:room-compiler:2.6.0")
    ksp("androidx.room:room-compiler:2.6.0")
    //icons
    implementation("androidx.compose.material:material-icons-extended:1.5.4")
    // lifecycle utils for  for Compose
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    //datastore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    //splash
    implementation("androidx.core:core-splashscreen:1.1.0-alpha02")
    //room -testing
    testImplementation("androidx.room:room-testing:2.6.0")
    //hilt testing
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.48")
    //tests
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    //tests-rules
    androidTestImplementation("androidx.test:rules:1.5.0")
    //mockito
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
    // espresso
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // compose tests
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    //tooling
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    // turbine
    testImplementation("app.cash.turbine:turbine:1.0.0")
    androidTestImplementation("app.cash.turbine:turbine:1.0.0")
    //kotlin tests
    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}