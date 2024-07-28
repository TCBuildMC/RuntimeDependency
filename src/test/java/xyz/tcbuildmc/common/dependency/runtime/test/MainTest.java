package xyz.tcbuildmc.common.dependency.runtime.test;

import org.junit.jupiter.api.Test;
import xyz.tcbuildmc.common.dependency.runtime.DependencyManager;
import xyz.tcbuildmc.common.dependency.runtime.maven.MavenDependency;
import xyz.tcbuildmc.common.dependency.runtime.util.Properties;

import java.io.File;

public class MainTest {
    @Test
    void download() {
        DependencyManager manager = new DependencyManager();

        manager.download(new MavenDependency("commons-io:commons-io:2.16.1"));
    }

    @Test
    void relocate() {
        DependencyManager manager = new DependencyManager();

        manager.addRelocationRule("org.apache.commons.io", "com.example.lib.commons_io");
        manager.relocate(new File(Properties.LIBS_DIR, "commons-io-2.16.1.jar"), new File(Properties.LIBS_DIR, "commons-io-2.16.1-relocated.jar"));
    }
}
