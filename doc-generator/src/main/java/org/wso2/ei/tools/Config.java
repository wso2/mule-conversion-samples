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

/**
 * Doc generation configurations object used by Yaml reader.
 */
public class Config {

    private String projectFileName = ".project";
    private String readmeOutputPath        = "out/readme";
    private String readmeImagesOutputPath   = "out/assets/zip";
    private String includesImagesOutputPath = "out/assets/img/common";
    private String zipOutputPath            = "out/assets/img";
    private String readmeFileName = "README.md";
    private String sourcePath = "sources";
    private String sourceImagesPath = "in/assets/sources/img";
    private String includesPath = "includes";
    private String includesImagesPath = "in/assets/includes/img";
    private String absoluteToolPath;
    private String relativeBasePath = "../";

    public String getProjectFileName() {
        return projectFileName;
    }

    public void setProjectFileName(String projectFileName) {
        this.projectFileName = projectFileName;
    }

    public String getReadmeOutputPath() {
        return readmeOutputPath;
    }

    public void setReadmeOutputPath(String readmeOutputPath) {
        this.readmeOutputPath = readmeOutputPath;
    }

    public String getReadmeImagesOutputPath() {
        return readmeImagesOutputPath;
    }

    public void setReadmeImagesOutputPath(String readmeImagesOutputPath) {
        this.readmeImagesOutputPath = readmeImagesOutputPath;
    }

    public String getZipOutputPath() {
        return zipOutputPath;
    }

    public void setZipOutputPath(String zipOutputPath) {
        this.zipOutputPath = zipOutputPath;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getReadmeFileName() {
        return readmeFileName;
    }

    public void setReadmeFileName(String readmeFileName) {
        this.readmeFileName = readmeFileName;
    }

    public String getAbsoluteToolPath() {
        return absoluteToolPath;
    }

    public void setAbsoluteToolPath(String absoluteToolPath) {
        this.absoluteToolPath = absoluteToolPath;
    }

    public String getRelativeBasePath() {
        return relativeBasePath;
    }

    public void setRelativeBasePath(String relativeBasePath) {
        this.relativeBasePath = relativeBasePath;
    }

    public String getIncludesPath() {
        return includesPath;
    }

    public void setIncludesPath(String includesPath) {
        this.includesPath = includesPath;
    }

    public String getIncludesImagesOutputPath() {
        return includesImagesOutputPath;
    }

    public void setIncludesImagesOutputPath(String includesImagesOutputPath) {
        this.includesImagesOutputPath = includesImagesOutputPath;
    }

    public String getSourceImagesPath() {
        return sourceImagesPath;
    }

    public void setSourceImagesPath(String sourceImagesPath) {
        this.sourceImagesPath = sourceImagesPath;
    }

    public String getIncludesImagesPath() {
        return includesImagesPath;
    }

    public void setIncludesImagesPath(String includesImagesPath) {
        this.includesImagesPath = includesImagesPath;
    }
}
