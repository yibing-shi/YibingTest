package com.yibing.classloader;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Scanner;

/**
 * JarClassLoader provides a convenient abstraction for
 * for loading both classes and locating resources from jar
 * files whose locations are specified as URLs. It makes
 * extensive use of the Java 2 URLClassLoader.
 *
 * @see java.net.URLClassLoader
 */
public class MultiJarClassLoader extends URLClassLoader {
    public MultiJarClassLoader(URL url) {
        super(new URL[]{url});
    }

    /**
     * Creates a JarClassLoader object a URL string
     *
     * @param urlString from which to load classes and locating resources
     * @throws MalformedURLException reports problems converting string into a URL
     */
    public MultiJarClassLoader(String urlString) throws MalformedURLException {
        this(new URL("jar:" + urlString + "!/"));
    }

    /**
     * Adds urls to the JarClassLoader search path
     *
     * @param urlStrings from which to load classes and locating resources
     * @throws MalformedURLException reports problems converting strings into a URLs
     */
    public void addJarURLs(String[] urlStrings) throws MalformedURLException {
        if (urlStrings != null)
            for (int i = 0; i < urlStrings.length; i++)
                addJarURL(urlStrings[i]);
    }

    /**
     * Adds url to the JarClassLoader search path
     *
     * @param urlString from which to load classes and locating resources
     * @throws MalformedURLException reports problems converting string into a URL
     */
    public void addJarURL(String urlString) throws MalformedURLException {
        if (urlString != null)
            addURL(new URL("jar:" + urlString + "!/"));
    }

    public static void main(String[] args)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException, MalformedURLException {
        ClassLoader cl = new MultiJarClassLoader("file:/E:/Dropbox/programming/java/JarClassLoader/src/main/resource/Data.jar");
        Class c = cl.loadClass("Data");
        System.out.println(c.newInstance());
        InputStream in = cl.getResourceAsStream("version.txt");
        Scanner scanner = new Scanner(in);
        System.out.println("version: " + scanner.nextLine());
    }
}
