package xyz.tcbuildmc.common.dependency.runtime.util;
public class Properties {
    public static final String MAVEN_URL = "https://maven.aliyun.com/repository/public/";
    public static final String LIBS_DIR = "rtlibs";
    public static final int TIMEOUT = 30;

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

    public static int getTimeout() {
        String timeout = System.getProperty("rtd.timeout");

        if (timeout == null) {
            return TIMEOUT;
        }

        return Integer.parseInt(timeout);
    }
}
