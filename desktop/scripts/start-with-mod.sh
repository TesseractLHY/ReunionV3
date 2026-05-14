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

mkdir -p "mods/cache"
PATCHED_JAR="mods/cache/game-lib.patched.jar"

echo "Patching game-lib.jar..."
"$JAVA" \
  -Dfile.encoding=UTF-8 \
  -cp "mods/lib/*" \
  cn.tesseract.patcher.Patcher \
  game-lib.jar "$PATCHED_JAR" --platform desktop

echo "=== Soviet Desktop Mod ==="
echo "Game dir: $(pwd)"
echo "JVM: $JAVA"
echo

"$JAVA" \
  -Djava.net.preferIPv4Stack=true \
  -Xmx1000M \
  -Dfile.encoding=UTF-8 \
  -Djava.library.path=. \
  -cp "$PATCHED_JAR:mods/lib/*:mods/*:libs/*" \
  cn.tesseract.soviet.desktop.DesktopLauncher
