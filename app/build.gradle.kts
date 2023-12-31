import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    kotlin("plugin.serialization")
}

android {
    namespace = "com.kamikadze328.smssender"
    compileSdk = 34

    defaultConfig {
        val localProperties: Properties = gradleLocalProperties(rootDir)
        val tgChatId: String = localProperties.getProperty("tg.chat.id", "")
        val tgBotId: String = localProperties.getProperty("tg.bot.id", "")
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
        compose = true
    }
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.composeKotlinCompiler.get()
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    // Android
    implementation(libs.androidx.core)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.viewmodel)
    implementation(libs.lifecycle.service)
    implementation(libs.lifecycle.viewmodel)

    // Compose
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.material)
    implementation(libs.compose.tooling)
    debugImplementation(libs.compose.preview)
    implementation(libs.compose.livedata)

    // Kotlin
    implementation(libs.coroutines.android)
    implementation(libs.kotlin.serialization)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    annotationProcessor(libs.room.compiler)
    ksp(libs.room.compiler)

    // 3rd party
    implementation(platform(libs.ktor.bom))
    implementation(libs.ktor.andoird)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.logging)
    implementation(libs.ktor.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin)
    implementation(libs.koin.core)
    implementation(libs.koin.core.coroutines)
}