package com.ultikits.ultitools.utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClassUtils {

    /**
     * 根据传入的根包名，扫描该包下所有类
     *
     * @param thiz            this
     * @param rootPackageName 包名
     */
    public static List<String> scanClasses(Object thiz, String rootPackageName) {
        return scanClasses(thiz.getClass(), rootPackageName);
    }

    /**
     * 根据传入的根包名，扫描该包下所有类
     *
     * @param thisClass       所在类
     * @param rootPackageName 包名
     */
    public static List<String> scanClasses(Class<?> thisClass, String rootPackageName) {
        return scanClasses(Objects.requireNonNull(thisClass.getClassLoader()), rootPackageName);
    }

    /**
     * 根据传入的根包名和对应classloader，扫描该包下所有类
     */
    public static List<String> scanClasses(ClassLoader classLoader, String packageName) {
        try {
            String packageResource = packageName.replace(".", "/");
            URL url = classLoader.getResource(packageResource);
            File root = new File(url.toURI());
            List<String> classList = new ArrayList<>();
            scanClassesInner(root, packageName, classList);
            return classList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 遍历文件夹下所有.class文件，并转换成包名字符串的形式保存在结果List中。
     */
    private static void scanClassesInner(File root, String packageName, List<String> result) {
        for (File child : Objects.requireNonNull(root.listFiles())) {
            String name = child.getName();
            if (child.isDirectory()) {
                scanClassesInner(child, packageName + "." + name, result);
            } else if (name.endsWith(".class")) {
                String className = packageName + "." + name.replace(".class", "");
                result.add(className);
            }
        }
    }
}
