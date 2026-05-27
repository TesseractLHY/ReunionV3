package cn.tesseract.patcher;

import net.fabricmc.mappingio.format.enigma.EnigmaDirReader;
import net.fabricmc.mappingio.tree.MappingTreeView;
import net.fabricmc.mappingio.tree.MemoryMappingTree;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SourceRemapper {
    public static void main(String[] args) throws Exception {
        Path projectRoot = Paths.get(".");

        Map<String, String> mappings = new LinkedHashMap<>();
        Map<String, String> importMappings = new LinkedHashMap<>();
        Map<String, String> slashMappings = new LinkedHashMap<>();
        parseMappings(mappings, importMappings, slashMappings);

        System.out.println("Loaded " + mappings.size() + " name mappings, " + importMappings.size() + " import mappings and " + slashMappings.size() + " slash mappings");

        // Build a regex pattern for simple name replacements (whole word match)
        // Sort by length descending to ensure longer names match first
        List<String> keys = new ArrayList<>(mappings.keySet());
        keys.sort((a, b) -> b.length() - a.length());

        StringBuilder patternBuilder = new StringBuilder();
        for (String key : keys) {
            if (patternBuilder.length() > 0) patternBuilder.append("|");
            patternBuilder.append(Pattern.quote(key));
        }
        Pattern pattern = Pattern.compile("\\b(" + patternBuilder + ")\\b");

        // Build a regex pattern for full import path replacements
        List<String> importKeys = new ArrayList<>(importMappings.keySet());
        importKeys.sort((a, b) -> b.length() - a.length());

        StringBuilder importPatternBuilder = new StringBuilder();
        for (String key : importKeys) {
            if (importPatternBuilder.length() > 0) importPatternBuilder.append("|");
            importPatternBuilder.append(Pattern.quote(key));
        }
        Pattern importPattern = importPatternBuilder.length() > 0
                ? Pattern.compile("(" + importPatternBuilder + ")")
                : null;

        // Build a regex pattern for slash-separated bytecode-style path replacements
        List<String> slashKeys = new ArrayList<>(slashMappings.keySet());
        slashKeys.sort((a, b) -> b.length() - a.length());

        StringBuilder slashPatternBuilder = new StringBuilder();
        for (String key : slashKeys) {
            if (slashPatternBuilder.length() > 0) slashPatternBuilder.append("|");
            slashPatternBuilder.append(Pattern.quote(key));
        }
        Pattern slashPattern = slashPatternBuilder.length() > 0
                ? Pattern.compile("(" + slashPatternBuilder + ")")
                : null;

        String[] sourceDirs = {"patcher", "core", "android", "desktop"};
        for (String dir : sourceDirs) {
            Path dirPath = projectRoot.resolve(dir);
            if (!Files.exists(dirPath)) continue;
            Files.walkFileTree(dirPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toString().endsWith(".java") && !file.toString().contains("SourceRemapper")) {
                        processJavaFile(file, pattern, mappings, importPattern, importMappings, slashPattern, slashMappings);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }

        System.out.println("Done!");
    }

    static void parseMappings(Map<String, String> mappings, Map<String, String> importMappings, Map<String, String> slashMappings) throws IOException {
        MemoryMappingTree androidNamed = new MemoryMappingTree();
        EnigmaDirReader.read(Paths.get("libs/mappings/android_named"), "source", "target", androidNamed);
        MemoryMappingTree desktopNamed = new MemoryMappingTree();
        EnigmaDirReader.read(Paths.get("libs/mappings/desktop_named"), "source", "target", desktopNamed);
        MemoryMappingTree tree = MappingUtils.buildMergedMappings(androidNamed, desktopNamed);

        for (MappingTreeView.ClassMappingView cls : tree.getClasses()) {
            String srcName = cls.getSrcName();
            String dstName = cls.getDstName(0);

            if (srcName == null || dstName == null || srcName.equals(dstName)) continue;

            String srcSimple = srcName.substring(srcName.lastIndexOf('/') + 1);
            String dstSimple = dstName.substring(dstName.lastIndexOf('/') + 1);

            // Map full dotted paths (e.g. com.corrodinggames.rts.game.class_329 -> ...TeamUnitStats)
            String srcDotted = srcName.replace('/', '.');
            String dstDotted = dstName.replace('/', '.');
            importMappings.put(srcDotted, dstDotted);

            // Map slash-separated paths (e.g. com/corrodinggames/rts/game/class_329 -> .../TeamUnitStats)
            slashMappings.put(srcName, dstName);

            // Map simple class names: class_XXX -> NamedClass
            if (srcSimple.matches("class_\\d+(\\$class_\\d+)*")) {
                mappings.put(srcSimple, dstSimple);
            }

            // Map fields: field_XXX -> namedFieldName
            for (MappingTreeView.FieldMappingView f : cls.getFields()) {
                String fSrc = f.getSrcName();
                String fDst = f.getDstName(0);
                if (fSrc != null && fDst != null && !fSrc.equals(fDst) && fSrc.matches("field_\\d+")) {
                    mappings.put(fSrc, fDst);
                }
            }

            // Map methods: method_XXX -> namedMethodName
            for (MappingTreeView.MethodMappingView m : cls.getMethods()) {
                String mSrc = m.getSrcName();
                String mDst = m.getDstName(0);
                if (mSrc != null && mDst != null && !mSrc.equals(mDst) && mSrc.matches("method_\\d+")) {
                    mappings.put(mSrc, mDst);
                }
            }
        }
    }

    static void processJavaFile(Path file, Pattern pattern, Map<String, String> mappings,
                                Pattern importPattern, Map<String, String> importMappings,
                                Pattern slashPattern, Map<String, String> slashMappings) throws IOException {
        String content = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
        int replaceCount = 0;

        // First pass: replace full dotted import paths (import statements and dotted string literals)
        if (importPattern != null) {
            Matcher importMatcher = importPattern.matcher(content);
            StringBuffer importSb = new StringBuffer();
            while (importMatcher.find()) {
                String matched = importMatcher.group(1);
                String replacement = importMappings.get(matched);
                if (replacement != null) {
                    importMatcher.appendReplacement(importSb, Matcher.quoteReplacement(replacement));
                    replaceCount++;
                } else {
                    importMatcher.appendReplacement(importSb, Matcher.quoteReplacement(matched));
                }
            }
            importMatcher.appendTail(importSb);
            content = importSb.toString();
        }

        // Second pass: replace slash-separated bytecode-style paths (e.g. in methodInsn.owner string literals)
        if (slashPattern != null) {
            Matcher slashMatcher = slashPattern.matcher(content);
            StringBuffer slashSb = new StringBuffer();
            while (slashMatcher.find()) {
                String matched = slashMatcher.group(1);
                String replacement = slashMappings.get(matched);
                if (replacement != null) {
                    slashMatcher.appendReplacement(slashSb, Matcher.quoteReplacement(replacement));
                    replaceCount++;
                } else {
                    slashMatcher.appendReplacement(slashSb, Matcher.quoteReplacement(matched));
                }
            }
            slashMatcher.appendTail(slashSb);
            content = slashSb.toString();
        }

        // Third pass: replace simple intermediary names (class_NNN, method_NNN, field_NNN)
        Matcher matcher = pattern.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String matched = matcher.group(1);
            String replacement = mappings.get(matched);
            if (replacement != null) {
                matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
                replaceCount++;
            } else {
                matcher.appendReplacement(sb, Matcher.quoteReplacement(matched));
            }
        }
        matcher.appendTail(sb);
        content = sb.toString();

        if (replaceCount > 0) {
            Files.write(file, content.getBytes(StandardCharsets.UTF_8));
            System.out.println("Replaced " + replaceCount + " occurrences in " + file);
        }
    }
}
