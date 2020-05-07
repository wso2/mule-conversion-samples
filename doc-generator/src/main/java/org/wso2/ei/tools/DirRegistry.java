/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package org.wso2.ei.tools;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Data holder for directory paths used by the tool.
 */
public class DirRegistry {

    private final Path targetDir;
    private final Path tempDir;
    private final Path mkdocsContentDir;
    private final Path zipOutDir;
    private final Path readmeOutDir;
    private final Path readmeImagesOutDir;
    private final Path includesImagesOutDir;
    private final Path sourceDir;
    private final Path sourceImagesDir;
    private final Path includesDir;
    private final Path includesImagesDir;
    private final Path tempSourcesDir;
    private final Path tempIncludesDir;
    private final Path toolDir;
    private final Path baseDir;
    private final Path htmlImageAbsoluteDir;
    private final Path htmlCommonImageAbsoluteDir;
    private final Path htmlZipAbsoluteDir;

    public DirRegistry(Config config) {
        toolDir = Paths.get(config.getAbsoluteToolPath());
        baseDir = toolDir.resolve(config.getRelativeBasePath());
        targetDir = Paths.get(config.getAbsoluteToolPath(), "target");
        tempDir = targetDir.resolve("tempDirectory");
        mkdocsContentDir = targetDir.resolve("mkdocs-content");
        zipOutDir = mkdocsContentDir.resolve(config.getZipOutputPath());
        readmeImagesOutDir = mkdocsContentDir.resolve(config.getReadmeImagesOutputPath());
        includesImagesOutDir = mkdocsContentDir.resolve(config.getIncludesImagesOutputPath());

        readmeOutDir = mkdocsContentDir.resolve(config.getReadmeOutputPath());
        sourceDir = Paths.get(config.getAbsoluteToolPath(), config.getSourcePath());
        sourceImagesDir = Paths.get(config.getAbsoluteToolPath(), config.getSourceImagesPath());
        includesDir = Paths.get(config.getAbsoluteToolPath(), config.getIncludesPath());
        includesImagesDir = Paths.get(config.getAbsoluteToolPath(), config.getIncludesImagesPath());
        tempSourcesDir = tempDir.resolve(baseDir.relativize(sourceDir));
        tempIncludesDir = tempDir.resolve(baseDir.relativize(includesDir));

        htmlImageAbsoluteDir = Paths.get(config.getHtmlReadmeImagesAbsolutePath());
        htmlCommonImageAbsoluteDir = Paths.get(config.getHtmlIncludesImagesAbsolutePath());
        htmlZipAbsoluteDir = Paths.get(config.getHtmlZipAbsolutePath());
    }

    public Path getTargetDir() {
        return targetDir;
    }

    public Path getTempDir() {
        return tempDir;
    }

    public Path getMkdocsContentDir() {
        return mkdocsContentDir;
    }

    public Path getZipOutDir() {
        return zipOutDir;
    }

    public Path getReadmeImagesOutDir() {
        return readmeImagesOutDir;
    }

    public Path getReadmeOutDir() {
        return readmeOutDir;
    }

    public Path getSourceDir() {
        return sourceDir;
    }

    public Path getIncludesDir() {
        return includesDir;
    }

    public Path getTempSourcesDir() {
        return tempSourcesDir;
    }

    public Path getTempIncludesDir() {
        return tempIncludesDir;
    }

    public Path getToolDir() {
        return toolDir;
    }

    public Path getBaseDir() {
        return baseDir;
    }

    public Path getSourceImagesDir() {
        return sourceImagesDir;
    }

    public Path getIncludesImagesDir() {
        return includesImagesDir;
    }

    public Path getIncludesImagesOutDir() {
        return includesImagesOutDir;
    }

    public Path getHtmlImageAbsolutePath() {
        return htmlImageAbsoluteDir;
    }

    public Path getHtmlCommonImageAbsoluteDir() {
        return htmlCommonImageAbsoluteDir;
    }

    public Path getHtmlZipAbsolutePath() {
        return htmlZipAbsoluteDir;
    }
}
