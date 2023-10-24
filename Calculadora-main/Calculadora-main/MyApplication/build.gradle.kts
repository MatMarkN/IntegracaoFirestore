plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
}

buildscript {
    repositories {
        google()
        // Adicione outros repositórios, se necessário
    }
    dependencies {
        classpath("com.google.gms:google-services:4.3.10")
        // Adicione outras dependências, se necessário
    }
}
