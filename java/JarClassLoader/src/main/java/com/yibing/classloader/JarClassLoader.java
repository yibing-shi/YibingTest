package com.yibing.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by IntelliJ IDEA.
 * User: yibing
 * Date: 11-11-26
 * Time: 上午12:33
 * @author Yibing
 * @version 1.0
 * To load classes or resource from a JAR file
 */
public class JarClassLoader extends ClassLoader {
    private String jarFilePath; //Path to the jar file
    private Hashtable<String, Class> classes = new Hashtable<String, Class>(); //used to cache already defined classes

    public JarClassLoader(String jarFilePath) {
        super(JarClassLoader.class.getClassLoader()); //calls the parent class loader's constructor
        this.jarFilePath = jarFilePath;
    }

    public Class<?> loadClass(String className) throws ClassNotFoundException {
        return findClass(className);
    }

    public Class<?> findClass(String className) throws ClassNotFoundException {
        byte classByte[];
        Class result = classes.get(className); //checks in cached classes
        if (result != null) {
            return result;
        }

        try {
            return findSystemClass(className);
        } catch (ClassNotFoundException e) {
            //ignore
        }

        try {
            JarFile jar = new JarFile(jarFilePath);
            JarEntry entry = jar.getJarEntry(className + ".class");
            InputStream is = jar.getInputStream(entry);
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            int nextValue = is.read();
            while (-1 != nextValue) {
                byteStream.write(nextValue);
                nextValue = is.read();
            }

            classByte = byteStream.toByteArray();
            result = defineClass(className, classByte, 0, classByte.length, null);
            classes.put(className, result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ClassNotFoundException();
        }
    }

    public URL findResource(String name) {
        try {
            File file = new File(jarFilePath);
            String url = file.toURI().toURL().toString();
            return new URL("jar:"+url+"!/"+name);
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ClassLoader cl = new JarClassLoader("src\\main\\resource\\Data.jar");
        Class c = cl.loadClass("Data");
        System.out.println(c.newInstance());
        InputStream in = cl.getResourceAsStream("version.txt");
        Scanner scanner = new Scanner(in);
        System.out.println("version: " + scanner.nextLine());
    }

}
