package cn.tesseract.patcher;

import net.fabricmc.mappingio.MappedElementKind;
import net.fabricmc.mappingio.tree.MappingTreeView;
import net.fabricmc.mappingio.tree.MemoryMappingTree;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class MappingUtils {
    public static MemoryMappingTree buildNamedMappings(MemoryMappingTree obfToInt, MemoryMappingTree intToNamed) throws IOException {
        MemoryMappingTree merged = new MemoryMappingTree();
        merged.visitNamespaces("source", Collections.singletonList("target"));

        for (MappingTreeView.ClassMappingView obfClass : obfToInt.getClasses()) {
            String obfName = obfClass.getSrcName();
            if (obfName == null) continue;

            String intClassName = obfClass.getDstName(0);
            MappingTreeView.ClassMappingView namedClass = intClassName == null ? null : intToNamed.getClass(intClassName);
            String finalClassName = namedClass != null && namedClass.getDstName(0) != null ? namedClass.getDstName(0) : (intClassName != null ? intClassName : obfName);

            merged.visitClass(obfName);
            merged.visitDstName(MappedElementKind.CLASS, 0, finalClassName);

            for (MappingTreeView.FieldMappingView field : obfClass.getFields()) {
                String obfFieldName = field.getSrcName(), obfFieldDesc = field.getSrcDesc(), intFieldName = field.getDstName(0);
                if (obfFieldName == null) continue;
                String finalFieldName = intFieldName != null ? intFieldName : obfFieldName;
                if (namedClass != null && intFieldName != null) {
                    MappingTreeView.FieldMappingView namedField = namedClass.getField(intFieldName, field.getDstDesc(0));
                    if (namedField != null && namedField.getDstName(0) != null) finalFieldName = namedField.getDstName(0);
                }
                merged.visitField(obfFieldName, obfFieldDesc);
                merged.visitDstName(MappedElementKind.FIELD, 0, finalFieldName);
                merged.visitElementContent(MappedElementKind.FIELD);
            }

            for (MappingTreeView.MethodMappingView method : obfClass.getMethods()) {
                String obfMethodName = method.getSrcName(), obfMethodDesc = method.getSrcDesc(), intMethodName = method.getDstName(0);
                if (obfMethodName == null) continue;
                String finalMethodName = intMethodName != null ? intMethodName : obfMethodName;
                if (namedClass != null && intMethodName != null) {
                    MappingTreeView.MethodMappingView namedMethod = namedClass.getMethod(intMethodName, method.getDstDesc(0));
                    if (namedMethod != null && namedMethod.getDstName(0) != null) finalMethodName = namedMethod.getDstName(0);
                }
                merged.visitMethod(obfMethodName, obfMethodDesc);
                merged.visitDstName(MappedElementKind.METHOD, 0, finalMethodName);
                merged.visitElementContent(MappedElementKind.METHOD);
            }

            merged.visitElementContent(MappedElementKind.CLASS);
        }

        return merged;
    }

    public static MemoryMappingTree buildSharedMappings(
            MemoryMappingTree firstInt,
            MemoryMappingTree secondInt,
            MemoryMappingTree firstNamed,
            MemoryMappingTree secondNamed
    ) throws IOException {
        MemoryMappingTree result = new MemoryMappingTree();
        result.visitNamespaces("source", Collections.singletonList("target"));

        // Collect intermediate names from desktop (reverse index)
        Set<String> desktopClasses = new HashSet<>();
        Map<String, Set<String>> desktopFields = new HashMap<>();
        Map<String, Set<String>> desktopMethods = new HashMap<>();

        for (MappingTreeView.ClassMappingView cls : secondInt.getClasses()) {
            String intName = cls.getDstName(0);
            if (intName == null) continue;
            desktopClasses.add(intName);

            Set<String> fields = new HashSet<>();
            for (MappingTreeView.FieldMappingView f : cls.getFields()) {
                String n = f.getDstName(0);
                if (n != null) fields.add(n + ";" + f.getDstDesc(0));
            }
            desktopFields.put(intName, fields);

            Set<String> methods = new HashSet<>();
            for (MappingTreeView.MethodMappingView m : cls.getMethods()) {
                String n = m.getDstName(0);
                if (n != null) methods.add(n + ";" + m.getDstDesc(0));
            }
            desktopMethods.put(intName, methods);
        }

        // Intersect with android and build result
        for (MappingTreeView.ClassMappingView cls : firstInt.getClasses()) {
            String intName = cls.getDstName(0);
            if (intName == null || !desktopClasses.contains(intName)) continue;

            MappingTreeView.ClassMappingView namedCls = firstNamed.getClass(intName);
            if (namedCls == null || namedCls.getDstName(0) == null)
                namedCls = secondNamed.getClass(intName);
            String actualName = namedCls != null ? namedCls.getDstName(0) : null;
            result.visitClass(intName);
            result.visitDstName(MappedElementKind.CLASS, 0, actualName != null ? actualName : intName);

            Set<String> dFields = desktopFields.getOrDefault(intName, Collections.emptySet());
            for (MappingTreeView.FieldMappingView f : cls.getFields()) {
                String fn = f.getDstName(0);
                String fd = f.getDstDesc(0);
                if (fn == null || !dFields.contains(fn + ";" + fd)) continue;

                String actualFn = null;
                for (MemoryMappingTree nt : new MemoryMappingTree[]{firstNamed, secondNamed}) {
                    MappingTreeView.ClassMappingView nc = nt.getClass(intName);
                    if (nc != null) {
                        MappingTreeView.FieldMappingView nf = nc.getField(fn, fd);
                        if (nf != null && nf.getDstName(0) != null) {
                            actualFn = nf.getDstName(0);
                            break;
                        }
                    }
                }
                result.visitField(fn, fd);
                result.visitDstName(MappedElementKind.FIELD, 0, actualFn != null ? actualFn : fn);
                result.visitElementContent(MappedElementKind.FIELD);
            }

            Set<String> dMethods = desktopMethods.getOrDefault(intName, Collections.emptySet());
            for (MappingTreeView.MethodMappingView m : cls.getMethods()) {
                String mn = m.getDstName(0);
                String md = m.getDstDesc(0);
                if (mn == null || !dMethods.contains(mn + ";" + md)) continue;

                String actualMn = null;
                for (MemoryMappingTree nt : new MemoryMappingTree[]{firstNamed, secondNamed}) {
                    MappingTreeView.ClassMappingView nc = nt.getClass(intName);
                    if (nc != null) {
                        MappingTreeView.MethodMappingView nm = nc.getMethod(mn, md);
                        if (nm != null && nm.getDstName(0) != null) {
                            actualMn = nm.getDstName(0);
                            break;
                        }
                    }
                }
                result.visitMethod(mn, md);
                result.visitDstName(MappedElementKind.METHOD, 0, actualMn != null ? actualMn : mn);
                result.visitElementContent(MappedElementKind.METHOD);
            }

            result.visitElementContent(MappedElementKind.CLASS);
        }

        return result;
    }

    public static MemoryMappingTree buildMergedMappings(
            MemoryMappingTree a,
            MemoryMappingTree b
    ) throws IOException {
        MemoryMappingTree result = new MemoryMappingTree();
        result.visitNamespaces("source", Collections.singletonList("target"));

        Set<String> seenClasses = new HashSet<>();
        Map<String, Set<String>> seenFields = new HashMap<>();
        Map<String, Set<String>> seenMethods = new HashMap<>();

        for (MemoryMappingTree tree : new MemoryMappingTree[]{a, b}) {
            for (MappingTreeView.ClassMappingView cls : tree.getClasses()) {
                String srcName = cls.getSrcName();
                String dstName = cls.getDstName(0);
                if (srcName == null) continue;

                boolean isNew = seenClasses.add(srcName);
                result.visitClass(srcName);
                if (isNew) {
                    result.visitDstName(MappedElementKind.CLASS, 0, dstName != null ? dstName : srcName);
                }

                Set<String> classFields = seenFields.computeIfAbsent(srcName, k -> new HashSet<>());
                for (MappingTreeView.FieldMappingView f : cls.getFields()) {
                    String fn = f.getSrcName();
                    String fd = f.getSrcDesc();
                    if (fn == null || !classFields.add(fn + ";" + fd)) continue;

                    String fDst = f.getDstName(0);
                    result.visitField(fn, fd);
                    result.visitDstName(MappedElementKind.FIELD, 0, fDst != null ? fDst : fn);
                    result.visitElementContent(MappedElementKind.FIELD);
                }

                Set<String> classMethods = seenMethods.computeIfAbsent(srcName, k -> new HashSet<>());
                for (MappingTreeView.MethodMappingView m : cls.getMethods()) {
                    String mn = m.getSrcName();
                    String md = m.getSrcDesc();
                    if (mn == null || !classMethods.add(mn + ";" + md)) continue;

                    String mDst = m.getDstName(0);
                    result.visitMethod(mn, md);
                    result.visitDstName(MappedElementKind.METHOD, 0, mDst != null ? mDst : mn);
                    result.visitElementContent(MappedElementKind.METHOD);
                }

                result.visitElementContent(MappedElementKind.CLASS);
            }
        }

        return result;
    }
}
