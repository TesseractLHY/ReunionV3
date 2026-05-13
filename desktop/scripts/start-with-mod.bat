@echo off
REM === Soviet Desktop Mod ===
REM Extract this ZIP into your Rusted Warfare game directory, then run this script.
REM Prerequisite: Java must be available (bundled JVM is auto-detected).

cd /d "%~dp0"

REM Detect bundled JVM (64-bit preferred, fall back to 32-bit)
if exist "jvm64\bin\java.exe" (
    set JAVA=jvm64\bin\java
) else if exist "jvm\bin\java.exe" (
    set JAVA=jvm\bin\java
) else (
    echo ERROR: No bundled JVM found. Please install Java and add it to PATH.
    pause
    exit /b 1
)

echo === Soviet Desktop Mod ===
echo Game dir: %cd%
echo JVM: %JAVA%
echo.

%JAVA% ^
  -Djava.net.preferIPv4Stack=true ^
  -Xmx1000M ^
  -Dfile.encoding=UTF-8 ^
  -Djava.library.path=. ^
  -cp "game-lib.jar;libs\*;mods\*;mods\lib\*" ^
  cn.tesseract.soviet.desktop.DesktopLauncher

pause
