<div align="center">

# Shizuku · Vuriko fork

An Android app that allows other apps to use system-level APIs that require ADB/root privileges.

</div>

## ⚠️ Disclaimer

This is a **fork of a fork**:

- [RikkaApps/Shizuku](https://github.com/RikkaApps/Shizuku) — the original Shizuku by RikkaW
- [thedjchi/Shizuku](https://github.com/thedjchi/Shizuku) — adds start on boot, TCP mode, watchdog, automation intents and more (maintenance paused in July 2026)
- **Vuriko** (this repo) — keeps thedjchi's fork working on new Android releases, starting with Android 17 QPR1

Vuriko keeps the package name `moe.shizuku.privileged.api`, so apps that use Shizuku work with it as usual. It is signed with a different key, so uninstall any other Shizuku build first.

## 🆕 Changes in Vuriko

- **Android 17 QPR1:** fixed the false "USB debugging is not enabled" error. Since QPR1, `Settings.Global.ADB_ENABLED` always reads 0 for third-party apps.
- Builds with AGP 8.13.2 and NDK 30 against the current API 37 SDK.
- Shizuku-API is included as a git subtree (`api/`) instead of a submodule.
- The in-app updater checks this fork's releases and installs an update only if it is signed with the release certificate below (also in stealth mode, which re-signs the APK locally).
- Fixed a crash when an update download failed.
- Server: sees all users on Android 17 (Private Space, work profiles) and keeps app grants when packages can't be read early in boot.
- The keystore password is never stored on disk (see [Building](#building)).

## ⬇️ Download

Get the latest APK from [Releases](https://github.com/x0ka1n3/Shizuku/releases), or [build it yourself](#building).

Release APKs are signed with this certificate (SHA-256):

```
76ccdd7450445654754197fe0c749eb53dc57f1046eeb3d0f156b4d0afbd8cfd
```

Check an APK with `apksigner verify --print-certs <file>.apk`.

## ✨ Features from thedjchi's fork

* **More robust "start on boot":** waits for a Wi-Fi connection before starting the Shizuku service
* **TCP mode:** (i.e., the `adb tcpip` command) once Shizuku successfully starts with Wi-Fi after a reboot, you can stop/restart Shizuku without a Wi-Fi connection
* **Watchdog service:** automatically restarts Shizuku if it stops unexpectedly, and can alert you of crashes/potential fixes
* **Start/stop intents:** toggle Shizuku on-demand using automation apps (e.g., Tasker, MacroDroid, Automate)
* **[BETA] Stealth mode:** hide Shizuku from other apps that don't work when Shizuku is installed
* **Android/Google TV and VR headset support**
* **MediaTek support**

## 📝 User Guide

thedjchi's [wiki](https://github.com/thedjchi/Shizuku/wiki) still applies for setup, info, and troubleshooting.

## ☑️ Requirements

**Minimum Version: Android 7+**
- **Root mode:** Requires a rooted device
- **Wireless Debugging mode:** Works on Android 11+ and all Android TVs
- **PC mode:** Works on all devices
- **Start on boot:** Available only when using Wireless Debugging or Root mode

## 🔒 Privacy

* No tracking, analytics or telemetry
* No proprietary libraries, no Google Play Services
* Open-source codebase
* Internet access is only used for wireless debugging connections and to check this fork's GitHub releases (can be turned off in settings)

### Permissions

* **INTERNET:** required for the wireless debugging start mode, and to check for updates
* **ACCESS_LOCAL_NETWORK, USE_LOOPBACK_INTERFACE, NEARBY_WIFI_DEVICES, CHANGE_WIFI_MULTICAST_STATE:** finding and connecting to the wireless debugging port (Android 17 requires the first two)
* **ACCESS_NETWORK_STATE:** used to determine when Wi-Fi is available for background start via wireless debugging
* **POST_NOTIFICATIONS:** required for pairing notification and other alerts
* **RECEIVE_BOOT_COMPLETED:** required for start on boot
* **FOREGROUND_SERVICE, FOREGROUND_SERVICE_SPECIAL_USE, WAKE_LOCK:** keep the watchdog and background start alive
* **REQUEST_IGNORE_BATTERY_OPTIMIZATIONS:** prevents start on boot and watchdog services from being killed
* **WRITE_SECURE_SETTINGS:** used to toggle USB and wireless debugging in the background when starting/stopping Shizuku
* **REQUEST_DELETE_PACKAGES, REQUEST_INSTALL_PACKAGES:** used to install app updates and to uninstall/install the Shizuku stub in stealth mode

## 📱 Developer Guide

The API and a demo project live in [`api/`](api) (from [Shizuku-API](https://github.com/RikkaApps/Shizuku-API)).

1. Shizuku has different permissions in root and ADB mode. You can see permissions granted to ADB [here](https://cs.android.com/android/platform/superproject/main/+/main:frameworks/base/packages/Shell/AndroidManifest.xml).
   If your app requires root permission, use `ShizukuService#getUid` to check if Shizuku is running as root or ADB, or use `ShizukuService#checkPermission` to check if the server has sufficient permissions.
2. On devices running Android 8 or lower, if you need to use Shizuku in a Service or Broadcast Receiver that might not be started by an Activity, please trigger the send binder by starting a transparent activity.
3. Please prefer using `ShizukuBinderWrapper` instead of directly using `transactRemote` when possible, as API calls can change across Android versions.

## Building

Requirements: JDK 21, Android SDK platform 37, NDK 30.0.16248370, CMake 3.31+.

```
git clone https://github.com/x0ka1n3/Shizuku.git
./gradlew :manager:assembleDebug
```

Release builds are signed only when a password is provided:

1. Create `signing.properties` in the repo root with `KEYSTORE_FILE=<path to .jks>` and `KEYSTORE_ALIAS=<alias>` (no password).
2. On Windows run `.\build-release.ps1`, which asks for the password. Elsewhere, set `SHIZUKU_KEYSTORE_PASSWORD` and run `./gradlew :manager:assembleRelease`.

Without a password the release APK comes out unsigned. The APK is copied to `out/apk/`.

## 🙏 Credits

- [RikkaW](https://github.com/RikkaW) and the Shizuku contributors
- [thedjchi](https://github.com/thedjchi) and contributors — consider [supporting thedjchi](https://www.buymeacoffee.com/thedjchi)
- Android 17 support ported from [Nightzuku](https://github.com/kerneldroid/Nightzuku) in [thedjchi/Shizuku#226](https://github.com/thedjchi/Shizuku/pull/226)
- QPR1 `ADB_ENABLED` diagnosis by the community in [thedjchi/Shizuku#301](https://github.com/thedjchi/Shizuku/issues/301)

## 📃 License

All code files in this project are licensed under [Apache 2.0](LICENSE)
