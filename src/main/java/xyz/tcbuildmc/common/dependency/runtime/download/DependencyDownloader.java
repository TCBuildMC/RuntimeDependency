package xyz.tcbuildmc.common.dependency.runtime.download;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class DependencyDownloader {
    private final String url;
    private final File dest;

    @Contract(pure = true)
    public DependencyDownloader(String url, File dest) {
        this.url = url;
        this.dest = dest;
    }

    public void download() throws URISyntaxException, IOException {
        System.out.println("Downloading " + this.dest.getName());

        FileUtils.copyURLToFile(new URI(this.url).toURL(), this.dest);
    }
}
