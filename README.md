Xebia Essentials for Android
============================

This project is a complete rewrite of the `Xebia Essentials` Android application.


Scraper
-------

The scraper gets its data from `http://essentials.xebia.com/` and generates an XML file.

Run it with:

```bash
./scripts/run-scraper.sh
```

Tests use the port `8090`. Make sure it is available before building and running the tests.

You can change the local test port modifying the `AppTest.WIREMOCK_PORT` variable.

When running the tests through IDEA, make sure your test running configuration's "Working directory" is `$MODULE_DIR$`.


Android app
-----------


## How to

- Install dependencies (until next release of novoda test plugin, we use our own custom version)

```bash
./scripts/install-custom-gradle-test-plugin.sh
```

- Run tests (attach a device first)

```bash
./scripts/run-tests-unit.sh
./scripts/run-tests-spoon.sh
```

- Import in IntelliJ IDEA

- Launch IntelliJ
- Import Project
- Select the root `build.gradle` file during the import
- Use default gradle wrapper

At this point, you should be able to launch the app via IntelliJ but the tests should not work yet.

- Edit the default 'Android Tests' build configuration
- Select the `essentials-android` module
- Specific instrumentation runner: `com.google.android.apps.common.testing.testrunner.GoogleInstrumentationTestRunner`
- Apply

You must run unit tests from the gradle testDebug task


## Checklist before releasing

- change the `versionCode` and `versionName` in `essentials-android/build.gradle`
- tag a version in your SCM system.


## Generate a signed apk for production

Make sure you have these environment variables:

```bash
export ESSENTIALS_KEYSTORE="/path/to/xebia-essentials.keystore"
export ESSENTIALS_KEYSTORE_PWD="keystore-password"
export ESSENTIALS_KEY_ALIAS="key-alias"
export ESSENTIALS_KEY_PWD="key-password"
```

Run the following:

```bash
./gradlew :essentials-android:assembleRealRelease
```

The signed and zip-aligned apk build will be located in `essentials-android/build/apk/essentials-android-release.apk`.

You can make sure it is properly signed and aligned, running the following commands:

```bash
./gradlew signingReport
jarsigner -verify -certs -verbose /path/to/signed-aligned.apk
# jar verified.
zipalign -c -v 4 /path/to/signed-aligned.apk
# Verification succesful
```
