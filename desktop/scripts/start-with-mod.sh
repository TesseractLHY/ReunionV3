#!/bin/bash
# === Soviet Desktop Mod ===
# Extract this ZIP into your Rusted Warfare game directory, then run this script.

cd "$(dirname "$0")"

# Detect bundled JVM (64-bit preferred, fall back to 32-bit)
if [ -f "jvm64/bin/java" ]; then
    JAVA="jvm64/bin/java"
elif [ -f "jvm/bin/java" ]; then
    JAVA="jvm/bin/java"
else
    echo "ERROR: No bundled JVM found. Please install Java and add it to PATH."
    exit 1
fi

echo "=== Soviet Desktop Mod ==="
echo "Game dir: $(pwd)"
echo "JVM: $JAVA"
echo

"$JAVA" \
  -Djava.net.preferIPv4Stack=true \
  -Xmx1000M \
  -Dfile.encoding=UTF-8 \
  -Djava.library.path=. \
  -cp "game-lib.jar:libs/*:mods/*:mods/lib/*" \
  cn.tesseract.soviet.desktop.DesktopLauncher
