package xyz.tcbuildmc.common.dependency.runtime.maven;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class MavenDependency {
    public static final String MAVEN_DEPENDENCY_FORMAT = "%s:%s:%s%s";
    public static final String MAVEN_FILE_NAME_FORMAT = "%s-%s%s.jar";
    public static final String MAVEN_FILE_URL_FORMAT = "%s/%s/%s/%s";

    @NotNull
    private final String group;
    @NotNull
    private final String artifact;
    @NotNull
    private final String version;

    @Nullable
    private final String classifier;

    public MavenDependency(@NotNull String dependencyNotation) {
        String[] part = dependencyNotation.split(":");

        if (part.length < 3 || part.length > 4) {
            throw new IllegalArgumentException("Invalid maven dependency!");
        }

        this.group = part[0];
        this.artifact = part[1];
        this.version = part[2];
        this.classifier = (part.length == 4) ? part[3] : null;
    }

    public String getDependencyNotation() {
        return String.format(MAVEN_DEPENDENCY_FORMAT, this.group, this.artifact, this.version, (this.classifier == null) ? "" : "-" + this.classifier);
    }

    public String getMavenFileName() {
        return String.format(MAVEN_FILE_NAME_FORMAT, this.artifact, this.version, (this.classifier == null) ? "" : "-" + this.classifier);
    }

    public String getDependencyURL() {
        return String.format(MAVEN_FILE_URL_FORMAT, this.group.replaceAll("\\.", "/"), this.artifact, this.version, this.getMavenFileName());
    }
}
