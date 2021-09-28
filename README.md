# Eremit Malaysia SDK
This is a Eremit Malaysia SDK sample application with integration steps.

Requirements
============

* Minimum Android SDK Version 21
* Working front and back camera in device

Size Changes in Dynamic Feature Module
------------
* Around 5 MB in main application module
* Around 35 MB in dynamic feature module

Integration Steps
=================

1. Add it in your root build.gradle at the end of repositories
  ```gradle
  allprojects {
    repositories {
      maven {
            url "http://maven.eightsquare.co:8081/artifactory/libs-release-local"
            credentials {
                username = "####"
                password = "####"
            }
        }
        maven {
            url "https://mi-artifactory.otlabs.fr/artifactory/smartsdk"
            credentials {
                username "######"
                password "######"
            }
        }
    }
}
```
2. Add the below dependency in main application module's build.gradle
```gradle
   implementation ("morpho.mph_bio_sdk.android:SmartSDK:4.24.0") {
        exclude group:'com.idemia.smartsdk', module:'*'
    }
```

3. Add below code in main application's manifest file
```xml
 <provider
            android:name="com.morpho.lkms.android.sdk.lkms_core.content_provider.LkmsStoreProvider"
            android:authorities="${applicationId}.bio_store"
            android:readPermission="${applicationId}.NEW_READ_LKMS_LICENSE_PROVIDER"
            android:writePermission="${applicationId}.NEW_WRITE_LKMS_LICENSE_PROVIDER"
            tools:replace="android:authorities, android:readPermission, android:writePermission" />

        <provider
            android:name="com.morpho.mph_bio_sdk.android.sdk.content_provider.BioStoreProvider"
            android:authorities="${applicationId}.lkms"
            android:readPermission="${applicationId}.NEW_READ_MPH_BIO_SDK_PROVIDER"
            android:writePermission="${applicationId}.NEW_WRITE_MPH_BIO_SDK_PROVIDER"
            tools:replace="android:authorities, android:readPermission, android:writePermission" />
```

4. Add below dependency in dynamic feature module's build.gradle
```gradle
implementation "com.eightsquarei.eremit:eremitsdk:0.0.1-alpha29"
````

5. Add below resources in main application:
styles.xml
```xml
    <style name="EmsAppThemeSuccessActivity" />
    <style name="Theme.AppCompat.Light.NoActionBar.FullScreen" />
    <style name="EmsPasscodeViewTheme" />
    <style name="EmsAppThemeHome" />
    <style name="EmsBlueScreenActivtyTheme" />
    <style name="EmsDialogActivityTheme" />
```

strings.xml
```xml
    <string name="ems_app_name"/>
    <string name="DeeplinkUrl"/>
    <string name="ems_facebook_app_id"/>
    <string name="ems_fb_login_protocol_scheme"/>
    <string name="google_play_services_version"/>
```

6. Use below code to start eRemit SDK from DFM
```kotlin
EremitSdk.Builder()
            .apiKey("API_KEY_HERE")
            .envType(EnvType.SIT)
            .build().start(this)
```
  **Parameters**  
  * `apiKey` - Get valid license key from support team
  * `envType`
    1. `EnvType.SIT`
    2. `EnvType.UAT`
    3. `EnvType.PREPROD`
    4. `EnvType.PROD`
  

  
Notes
=======

1. Migrate to AndroidX, if current application is in support library. 
   Use the link below to migrate.
     [https://developer.android.com/jetpack/androidx/migrate](https://developer.android.com/jetpack/androidx/migrate)

2. Add below inside in android block if Java 8 incompatible error or receive crash log `java.lang.BootstrapMethodError: Exception from call site #5 bootstrap method....`
```gradle
   compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
   }
```
3. If you receive `Manifest merger failed : Attribute application@.....` error during application build, add the code below in application block in `AndroidManifest.xml`.
```xml
 tools:replace="android:icon,android:roundIcon,android:name"
```

4. If you receive error `Attribute application@allowBackup value=(false) from AndroidManifest.xml:7:9-36
	is also present at AndroidManifest.xml:10:9-35 value=(true).
	Suggestion: add 'tools:replace="android:allowBackup"' to <application> element at AndroidManifest.xml:6:5-22:19 to override.`
    Use the below code:
    ```xml
    tools:replace="android:allowBackup"
    ```
5. If you receive error `Modules 'base' and 'dynamicfeature' contain entry 'root/META-INF/kotlinx-coroutines-core.kotlin_module' with different content.`, apply below code in dynamic feature module's build.gradle
```gradle
    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }
```
 
 6. If you receive error `All modules with native libraries must support the same set of ABIs, but module 'base' supports '[ARM64_V8A, ARMEABI_V7A, ARMEABI, X86, X86_64]' and module 'dynamicfeature' supports '[ARM64_V8A, ARMEABI_V7A, ARMEABI, MIPS, MIPS64, X86, X86_64]'.`
 Add supported NDK architecture in application module's build.gradle and sync project
```gradle
  android {
    defaultConfig {
      ndk {
            abiFilters 'armeabi-v7a', 'arm64-v8a'
      }
    }
  }
  ```
