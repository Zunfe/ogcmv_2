// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    // Se añade la dependencia para los servicios de Google
    id("com.google.gms.google-services") version "4.4.1" apply false
}