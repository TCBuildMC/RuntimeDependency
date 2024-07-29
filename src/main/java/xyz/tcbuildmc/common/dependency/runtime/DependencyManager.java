package xyz.tcbuildmc.common.dependency.runtime;

import me.lucko.jarrelocator.JarRelocator;
import me.lucko.jarrelocator.Relocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import xyz.tcbuildmc.common.dependency.runtime.download.DependencyDownloader;
import xyz.tcbuildmc.common.dependency.runtime.maven.MavenDependency;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DependencyManager {
    public static String MAVEN_URL = "https://maven.aliyun.com/repository/public/";
    public static String LIBS_DIR = "rtlibs";

    @NotNull
    private final List<Relocation> relocationRules;

    @Contract(pure = true)
    public DependencyManager(@NotNull List<Relocation> relocationRules) {
        this.relocationRules = relocationRules;
    }

    public DependencyManager() {
        this(new ArrayList<>());
    }

    public void addRelocationRule(Relocation relocation) {
        this.relocationRules.add(relocation);
    }

    public void addRelocationRule(String from, String to) {
        this.relocationRules.add(new Relocation(from, to));
    }

    public void download(@NotNull String url, File dest) {
        if (!url.endsWith(".jar") || !dest.getName().endsWith(".jar")) {
            throw new IllegalArgumentException("Only jar file can be added to classpath.");
        }

        try {
            new DependencyDownloader(url, dest).download();

        } catch (URISyntaxException |
                 IOException e) {
            throw new RuntimeException("Failed to download.", e);
        }
    }

    public void download(@NotNull MavenDependency dependency) {
        this.download(dependency, new File(LIBS_DIR, dependency.getMavenFileName()));
    }

    public void download(@NotNull MavenDependency dependency, @NotNull File dest) {
        if (!dest.getName().endsWith(".jar")) {
            throw new IllegalArgumentException("Only jar file can be downloaded.");
        }

        try {
            new DependencyDownloader(getMavenUrl() + dependency.getDependencyURL(), dest).download();

        } catch (URISyntaxException |
                 IOException e) {
            throw new RuntimeException("Failed to download.", e);
        }
    }

    public void relocate(File from, File to) {
        try {
            new JarRelocator(from, to, this.relocationRules).run();
        } catch (IOException e) {
            throw new RuntimeException("Failed to relocate file " + from.getName(), e);
        }
    }

    public void addToClasspath(@NotNull File file) {
        if (file.isDirectory()) {
            for (File subFile : Objects.requireNonNull(file.listFiles())) {
                this.addToClasspath(subFile);
            }

            return;
        }

        if (!file.getName().endsWith(".jar")) {
            throw new IllegalArgumentException("Only jar file can be added to classpath.");
        }

        try {
            Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            boolean canAccess = addURL.isAccessible();

            if (!canAccess) {
                addURL.setAccessible(true);
            }

            URLClassLoader cl = (URLClassLoader) ClassLoader.getSystemClassLoader();
            URL url = file.toURI().toURL();

            System.out.println("Loading " + file.getName());
            addURL.invoke(cl, url);

            addURL.setAccessible(canAccess);

        } catch (MalformedURLException |
                 InvocationTargetException |
                 NoSuchMethodException |
                 IllegalAccessException e) {

            throw new RuntimeException("Failed to load.", e);
        }
    }

    public static String getMavenUrl() {
        String url = System.getProperty("rtd.maven_url");

        if (url == null) {
            return MAVEN_URL;
        }

        if (url.indexOf(url.length() - 1) != '/') {
            return url + '/';
        }

        return url;
    }
}
