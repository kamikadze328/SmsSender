import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.kamikadze328.smssender"
    compileSdk = 35

    defaultConfig {
        val localProperties = gradleLocalProperties(rootDir, providers)
        val tgChatId = localProperties.getProperty("tg.chat.id").orEmpty()
        val tgBotId = localProperties.getProperty("tg.bot.id").orEmpty()
        buildConfigField("String", "TG_CHAT_ID", tgChatId)
        buildConfigField("String", "TG_BOT_ID", tgBotId)

        applicationId = "com.kamikadze328.smssender"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    // Android
    implementation(libs.androidx.core)
    implementation(libs.appcompat)
    implementation(libs.material3)
    implementation(libs.activity)
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
    implementation(libs.collections.immutable)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
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