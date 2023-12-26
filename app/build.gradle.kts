import java.util.Properties
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("androidx.room")
}

val localProperties: Properties = gradleLocalProperties(rootDir)
val tgChatId: String = localProperties.getProperty("tg.chat.id", "")
val tgBotId: String = localProperties.getProperty("tg.bot.id", "")

android {
    namespace = "com.kamikadze328.smssender"
    compileSdk = 34

    defaultConfig {
        buildConfigField("String", "TG_CHAT_ID", tgChatId)
        buildConfigField("String", "TG_BOT_ID", tgBotId)

        applicationId = "com.kamikadze328.smssender"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        buildConfig = true
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(libs.androidx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)

    implementation(libs.coroutines.android)
    implementation(libs.coroutines.core)
    implementation(libs.serialization)

    implementation(libs.ktor.andoird)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.logging)


    implementation(libs.viewmodel)

    implementation(libs.lifecycle.service)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.compose)


    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    annotationProcessor(libs.room.compiler)
    ksp(libs.room.compiler)
}