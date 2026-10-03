# Builds a release APK signed with your key.
# The keystore password is asked for every time and only lives in this process's environment.
$ErrorActionPreference = 'Stop'

$jdk = 'C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot'
if (Test-Path "$jdk\bin\java.exe") { $env:JAVA_HOME = $jdk }

$secure = Read-Host 'Keystore password' -AsSecureString
$env:SHIZUKU_KEYSTORE_PASSWORD = [System.Net.NetworkCredential]::new('', $secure).Password

try {
    & "$PSScriptRoot\gradlew.bat" --no-daemon :manager:assembleRelease
    if ($LASTEXITCODE -ne 0) { throw "Gradle failed with exit code $LASTEXITCODE" }
} finally {
    Remove-Item Env:SHIZUKU_KEYSTORE_PASSWORD -ErrorAction SilentlyContinue
}

Get-ChildItem "$PSScriptRoot\out\apk\*-release.apk" |
    Sort-Object LastWriteTime -Descending |
    Select-Object -First 1 |
    ForEach-Object { "`nAPK: $($_.FullName)" }
