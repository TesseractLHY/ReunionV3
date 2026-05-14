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

if not exist "mods\cache" mkdir "mods\cache"

set PATCHED_JAR=mods\cache\game-lib.patched.jar

echo Patching game-lib.jar...
%JAVA% ^
  -Dfile.encoding=UTF-8 ^
  -cp "mods\lib\*" ^
  cn.tesseract.patcher.Patcher ^
  game-lib.jar "%PATCHED_JAR%" --platform desktop

echo === Soviet Desktop Mod ===
echo Game dir: %cd%
echo JVM: %JAVA%
echo.

%JAVA% ^
  -Djava.net.preferIPv4Stack=true ^
  -Xmx1000M ^
  -Dfile.encoding=UTF-8 ^
  -Djava.library.path=. ^
  -cp "%PATCHED_JAR%;mods\lib\*;mods\*;libs\*" ^
  cn.tesseract.soviet.desktop.DesktopLauncher

pause
