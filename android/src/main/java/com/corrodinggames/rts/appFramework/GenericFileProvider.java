package com.corrodinggames.rts.appFramework;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes.dex */
public class GenericFileProvider extends ContentProvider {
    public static final String[] field_130 = {"_display_name", "_size"};
    public static final File field_131 = new File("/");
    public static HashMap field_132 = new HashMap();
    public class_12 field_133;

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        return true;
    }

    @Override // android.content.ContentProvider
    public void attachInfo(Context context, ProviderInfo providerInfo) {
        super.attachInfo(context, providerInfo);
        if (providerInfo.exported) {
            throw new SecurityException("Provider must not be exported");
        }
        if (!providerInfo.grantUriPermissions) {
            throw new SecurityException("Provider must grant uri permissions");
        }
        this.field_133 = method_97(context, providerInfo.authority);
    }

    public static Uri method_98(Context context, String str, File file) {
        return method_97(context, str).method_102(file);
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        File fileMethod_101 = this.field_133.method_101(uri);
        if (strArr == null) {
            strArr = field_130;
        }
        String[] strArr3 = new String[strArr.length];
        Object[] objArr = new Object[strArr.length];
        int i = 0;
        for (String str3 : strArr) {
            if ("_display_name".equals(str3)) {
                strArr3[i] = "_display_name";
                objArr[i] = fileMethod_101.getName();
                i++;
            } else if ("_size".equals(str3)) {
                strArr3[i] = "_size";
                objArr[i] = Long.valueOf(fileMethod_101.length());
                i++;
            }
        }
        String[] strArr4 = new String[i];
        System.arraycopy(strArr3, 0, strArr4, 0, i);
        Object[] objArr2 = new Object[i];
        System.arraycopy(objArr, 0, objArr2, 0, i);
        MatrixCursor matrixCursor = new MatrixCursor(strArr4, 1);
        matrixCursor.addRow(objArr2);
        return matrixCursor;
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        File fileMethod_101 = this.field_133.method_101(uri);
        int iLastIndexOf = fileMethod_101.getName().lastIndexOf(46);
        if (iLastIndexOf >= 0) {
            String mimeTypeFromExtension = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileMethod_101.getName().substring(iLastIndexOf + 1));
            if (mimeTypeFromExtension != null) {
                return mimeTypeFromExtension;
            }
        }
        return "application/octet-stream";
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        throw new UnsupportedOperationException("No external inserts");
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        throw new UnsupportedOperationException("No external updates");
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        return this.field_133.method_101(uri).delete() ? 1 : 0;
    }

    @Override // android.content.ContentProvider
    public ParcelFileDescriptor openFile(Uri uri, String str) throws FileNotFoundException {
        int i;
        File fileMethod_101 = this.field_133.method_101(uri);
        if ("r".equals(str)) {
            i = 268435456;
        } else if ("w".equals(str) || "wt".equals(str)) {
            i = 738197504;
        } else if ("wa".equals(str)) {
            i = 704643072;
        } else if ("rw".equals(str)) {
            i = 939524096;
        } else if ("rwt".equals(str)) {
            i = 1006632960;
        } else {
            throw new IllegalArgumentException("Invalid mode: ".concat(String.valueOf(str)));
        }
        return ParcelFileDescriptor.open(fileMethod_101, i);
    }

    public static class_12 method_97(Context context, String str) {
        class_12 class_12Var;
        synchronized (field_132) {
            class_12 class_12Var2 = (class_12) field_132.get(str);
            class_12Var = class_12Var2;
            if (class_12Var2 == null) {
                try {
                    try {
                        class_13 class_13Var = new class_13(str);
                        XmlResourceParser xmlResourceParserLoadXmlMetaData = context.getPackageManager().resolveContentProvider(str, 128).loadXmlMetaData(context.getPackageManager(), "android.support.FILE_PROVIDER_PATHS");
                        if (xmlResourceParserLoadXmlMetaData == null) {
                            throw new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
                        }
                        while (true) {
                            int next = xmlResourceParserLoadXmlMetaData.next();
                            if (next == 1) {
                                field_132.put(str, class_13Var);
                                class_12Var = class_13Var;
                                break;
                            }
                            if (next == 2) {
                                String name = xmlResourceParserLoadXmlMetaData.getName();
                                File fileMethod_99 = null;
                                String attributeValue = xmlResourceParserLoadXmlMetaData.getAttributeValue(null, "name");
                                String attributeValue2 = xmlResourceParserLoadXmlMetaData.getAttributeValue(null, "path");
                                if ("root-path".equals(name)) {
                                    fileMethod_99 = method_99(field_131, attributeValue2);
                                } else if ("files-path".equals(name)) {
                                    fileMethod_99 = method_99(context.getFilesDir(), attributeValue2);
                                } else if ("cache-path".equals(name)) {
                                    fileMethod_99 = method_99(context.getCacheDir(), attributeValue2);
                                } else if ("external-path".equals(name)) {
                                    fileMethod_99 = method_99(Environment.getExternalStorageDirectory(), attributeValue2);
                                }
                                if (fileMethod_99 == null) {
                                    continue;
                                } else {
                                    if (TextUtils.isEmpty(attributeValue)) {
                                        throw new IllegalArgumentException("Name must not be empty");
                                    }
                                    try {
                                        class_13Var.field_161.put(attributeValue, fileMethod_99.getCanonicalFile());
                                    } catch (IOException e) {
                                        throw new IllegalArgumentException("Failed to resolve canonical path for ".concat(String.valueOf(fileMethod_99)), e);
                                    }
                                }
                            }
                        }
                    } catch (XmlPullParserException e2) {
                        throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", e2);
                    }
                } catch (IOException e3) {
                    throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", e3);
                }
            }
        }
        return class_12Var;
    }

    public static File method_99(File file, String... strArr) {
        for (int i = 0; i <= 0; i++) {
            String str = strArr[0];
            if (str != null) {
                file = new File(file, str);
            }
        }
        return file;
    }
}
