package cn.tesseract.patcher;

import net.fabricmc.mappingio.MappedElementKind;
import net.fabricmc.mappingio.MappingVisitor;
import net.fabricmc.mappingio.tree.MappingTreeView;
import net.fabricmc.mappingio.tree.MemoryMappingTree;
import net.fabricmc.mappingio.tree.VisitOrder;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class MappingUtils {
    public static MemoryMappingTree buildNamedMappings(MemoryMappingTree obfToInt, MemoryMappingTree intToNamed) throws IOException {
        MemoryMappingTree merged = new MemoryMappingTree();
        merged.visitNamespaces("source", Collections.singletonList("target"));

        obfToInt.accept(new MappingVisitor() {
            private MappingTreeView.ClassMappingView namedClass;
            private String fieldDesc;
            private String methodDesc;

            @Override
            public void visitNamespaces(String srcNs, List<String> dstNs) {
            }

            @Override
            public boolean visitClass(String srcName) {
                MappingTreeView.ClassMappingView obfClass = obfToInt.getClass(srcName);
                String intName = obfClass != null ? obfClass.getDstName(0) : null;
                namedClass = intName != null ? intToNamed.getClass(intName) : null;
                return merged.visitClass(srcName);
            }

            @Override
            public void visitDstName(MappedElementKind kind, int ns, String intName) {
                String finalName = intName;
                if (namedClass != null) {
                    if (kind == MappedElementKind.CLASS && namedClass.getDstName(0) != null) {
                        finalName = namedClass.getDstName(0);
                    } else if (kind == MappedElementKind.FIELD) {
                        MappingTreeView.FieldMappingView nf = namedClass.getField(intName, fieldDesc);
                        if (nf != null && nf.getDstName(0) != null) finalName = nf.getDstName(0);
                    } else if (kind == MappedElementKind.METHOD) {
                        MappingTreeView.MethodMappingView nm = namedClass.getMethod(intName, methodDesc);
                        if (nm != null && nm.getDstName(0) != null) finalName = nm.getDstName(0);
                    }
                }
                merged.visitDstName(kind, 0, finalName);
            }

            @Override
            public boolean visitField(String srcName, String srcDesc) {
                fieldDesc = srcDesc;
                return merged.visitField(srcName, srcDesc);
            }

            @Override
            public boolean visitMethod(String srcName, String srcDesc) {
                methodDesc = srcDesc;
                return merged.visitMethod(srcName, srcDesc);
            }

            @Override
            public boolean visitMethodArg(int lvIndex, int argIndex, String srcName) {
                return merged.visitMethodArg(lvIndex, argIndex, srcName);
            }

            @Override
            public boolean visitMethodVar(int lvIndex, int startOpIdx, int endOpIdx, int scopeStartOpIdx, String srcName) {
                return merged.visitMethodVar(lvIndex, startOpIdx, endOpIdx, scopeStartOpIdx, srcName);
            }

            @Override
            public boolean visitElementContent(MappedElementKind kind) throws IOException {
                return merged.visitElementContent(kind);
            }

            @Override
            public boolean visitEnd() {
                return merged.visitEnd();
            }

            @Override
            public void visitComment(MappedElementKind kind, String comment) {
                merged.visitComment(kind, comment);
            }
        }, VisitOrder.createByInputOrder());

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
                if (isNew) {
                    result.visitClass(srcName);
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

                if (isNew) {
                    result.visitElementContent(MappedElementKind.CLASS);
                }
            }
        }

        return result;
    }
}
