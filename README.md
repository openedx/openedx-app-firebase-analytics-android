# OpenEdX Analytics Integration

This project provides an easy way to manage and initialize analytics services for OpenEdX applications
using `PluginManager`. With `PluginManager`, you can dynamically add and configure analytics services without
modifying `AnalyticsManager`.

## Adding OEXFirebaseAnalytics

To include `OEXFirebaseAnalytics` in your project, follow these steps:

### Step 1: Add Dependency

Add it to your root build.gradle at the end of repositories:

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

Include the `OEXFirebaseAnalytics` library in your project. Open your project's `build.gradle` file for the app
module and add the following dependency:

```groovy
implementation 'com.github.openedx:openedx-app-firebase-analytics-android:1.0.0'
```

### Step 2: Initialize in OpenEdXApp

Next, update the `initPlugins` function in your `OpenEdXApp` class to include `OEXFirebaseAnalytics`. Here’s how to do it:

```kotlin
private fun initPlugins() {
    pluginManager.addPlugin(OEXFirebaseAnalytics(context = this))
}
```

Now, PluginManager will handle the integration of OEXFirebaseAnalytics with AnalyticsManager. This ensures that
AnalyticsManager remains decoupled from specific analytics implementations, making your app’s analytics easily
configurable and extensible.
