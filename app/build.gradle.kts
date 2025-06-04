plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
  //  alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.xequal2.kotlincompilerplugin"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.xequal2.kotlincompilerplugin"
        minSdk = 26
        targetSdk = 35
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
        val pluginJar = project(":logcomposableplugin").layout.buildDirectory.file("libs/logcomposableplugin.jar")
        freeCompilerArgs += listOf("-Xplugin=${project(":logcomposableplugin").layout.buildDirectory.file("libs/logcomposableplugin.jar").get().asFile.absolutePath}")
    }
    buildFeatures {
        compose = true
    }
    packagingOptions {
        exclude("kotlin/internal/internal.kotlin_builtins")
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11" // or newer
    }
    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.jetbrains.kotlin") {
                useVersion("1.9.23") // Change to match your plugin/compiler version
                because("Kotlin plugin, stdlib, and embeddable compiler MUST match!")
            }
        }
    }
    packagingOptions {
        exclude("**/*.kotlin_builtins")
    }
}

dependencies {
  //  implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.23")
    implementation(libs.androidx.core.ktx)
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
    implementation(project(":logcomposableplugin"))
}