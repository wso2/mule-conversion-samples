/*
 *  Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.wso2.ei.tools.Constants.CDATA_TAG;
import static org.wso2.ei.tools.Constants.CLOSE_CURLY_BRACKET;
import static org.wso2.ei.tools.Constants.CODE_SEGMENT_BEGIN;
import static org.wso2.ei.tools.Constants.CODE_SEGMENT_END;
import static org.wso2.ei.tools.Constants.COMMA;
import static org.wso2.ei.tools.Constants.COMMENT_END;
import static org.wso2.ei.tools.Constants.COMMENT_START;
import static org.wso2.ei.tools.Constants.EMPTY_STRING;
import static org.wso2.ei.tools.Constants.GIT_PROPERTIES_FILE;
import static org.wso2.ei.tools.Constants.HASH;
import static org.wso2.ei.tools.Constants.IMG_TAG;
import static org.wso2.ei.tools.Constants.INCLUDE_CODE_SEGMENT_TAG;
import static org.wso2.ei.tools.Constants.INCLUDE_CODE_TAG;
import static org.wso2.ei.tools.Constants.INCLUDE_MD_TAG;
import static org.wso2.ei.tools.Constants.MARKDOWN_FILE_EXT;
import static org.wso2.ei.tools.Constants.NEW_LINE;
import static org.wso2.ei.tools.Constants.OPEN_CURLY_BRACKET;

/**
 * This tool processes the integration studio example projects and the corresponding readme files to generate the
 * doc files with relevant zipped project files ready to be pushed to the docs-ei repository.
 */
public class DocsGenerator {

    private static final Logger log = LoggerFactory.getLogger(DocsGenerator.class);
    // Current commit hash.
    private static String commitHash = null;

    private static Config config;

    public static void main(String[] args) {
        log.info("Docs generating process started...");

        config = loadConfig(args);
        DirRegistry dirRegistry = setupDirectories(config);
        commitHash = getCommitHashByReadingGitProperties();
        processDirectory(dirRegistry.getTempIncludesDir(), dirRegistry, DocsGenerator::processIncludesDirectory);
        processDirectory(dirRegistry.getTempSourcesDir(), dirRegistry, DocsGenerator::processSourceDirectory);
        log.info("Docs generating process finished...");
    }

    private static Config loadConfig(String[] args) {
        String configFileName = args[1];
        Yaml yaml = new Yaml(new Constructor(Config.class));
        InputStream inputStream = DocsGenerator.class.getClassLoader().getResourceAsStream(configFileName);
        Config config = yaml.load(inputStream);
        config.setAbsoluteToolPath(args[0]);
        log.info("Doc Generator Tool Directory: {}", config.getAbsoluteToolPath());
        return config;
    }

    private static DirRegistry setupDirectories(Config config) {
        DirRegistry dirRegistry = new DirRegistry(config);
        cleanupOldDirs(dirRegistry);
        createDirectories(dirRegistry);
        copySourcesToTempLocation(dirRegistry);
        return dirRegistry;
    }

    private static void cleanupOldDirs(DirRegistry dirRegistry) {
        // First delete already created mkdocs-content directory.
        Util.deleteDirectory(dirRegistry.getMkdocsContentDir());
        Util.deleteDirectory(dirRegistry.getTempDir());
    }

    private static void createDirectories(DirRegistry dirRegistry) {
        Util.mkdirs(dirRegistry.getTempDir());
        Util.mkdirs(dirRegistry.getZipOutDir());
        Util.mkdirs(dirRegistry.getReadmeImagesOutDir());
        Util.mkdirs(dirRegistry.getReadmeOutDir());
        Util.mkdirs(dirRegistry.getMkdocsContentDir());
        Util.createFile(dirRegistry.getTempDir() + File.separator + "temp.txt");
    }

    private static void copySourcesToTempLocation(DirRegistry dirRegistry) {
        Util.copyDirectoryContent(dirRegistry.getSourceDir(), dirRegistry.getTempSourcesDir());
        Util.copyDirectoryContent(dirRegistry.getIncludesDir(), dirRegistry.getTempIncludesDir());
        Util.copyDirectoryContent(dirRegistry.getSourceImagesDir(), dirRegistry.getReadmeImagesOutDir());
        Util.copyDirectoryContent(dirRegistry.getIncludesImagesDir(), dirRegistry.getIncludesImagesOutDir());
    }

    /**
     * Process files inside given directory. Traverse the directory structure in a breath first manner processing each
     * directory with a project file
     *
     * @param dirRegistry {@link DirRegistry} object
     */
    private static void processDirectory(Path directory, DirRegistry dirRegistry,
                                         DirectoryProcessor directoryProcessor) {
        File folder = directory.toFile();
        ArrayDeque<List<File>> queue = new ArrayDeque<>();
        addDirectoriesToQueue(queue, folder);
        while (!queue.isEmpty()) {
            List<File> files = queue.poll();
            boolean processingComplete = directoryProcessor.process(dirRegistry, files);

            if (!processingComplete) {
                for (File file: files) {
                    addDirectoriesToQueue(queue, file);
                }
            }
        }
    }

    private static boolean processSourceDirectory(DirRegistry dirRegistry, List<File> files) {
        boolean projectFileFound = false;
        Path relativeImageOutDirPath = dirRegistry.getReadmeOutDir().relativize(dirRegistry.getReadmeImagesOutDir());
        for (File file: files) {
            if (file.isFile() && (config.getProjectFileName().equals(file.getName()))) {
                projectFileFound = true;
                File readmeFile = Paths.get(file.getParent(), config.getReadmeFileName()).toFile();
                try {
                    if (readmeFile.exists()) {
                        processReadmeFile(readmeFile, relativeImageOutDirPath, true);
                        Files.copy(readmeFile.toPath(),
                                   dirRegistry.getReadmeOutDir().resolve(
                                           file.getParentFile().getName() + MARKDOWN_FILE_EXT));
                    } else {
                        throw new ServiceException(config.getReadmeFileName()
                                                   + " Not found. Path: " + file.toPath());
                    }

                    new ZipFile(Util.getZipFile(dirRegistry.getZipOutDir(), file))
                            .addFolder(new File(file.getParentFile().getPath()));
                } catch (ZipException e) {
                    throw new ServiceException("Error when zipping the directory: "
                                               + file.getParentFile().getPath(), e);
                } catch (IOException e) {
                    throw new ServiceException("Error while copying " + config.getReadmeFileName() + " from directory "
                                               + file.getParent(), e);
                }
                break;
            }
        }
        return projectFileFound;
    }

    private static boolean processIncludesDirectory(DirRegistry dirRegistry, List<File> files) {
        boolean directoriesFound = false;
        Path relativeImageOutDir = dirRegistry.getReadmeOutDir().relativize(dirRegistry.getIncludesImagesOutDir());
        for (File file: files) {
            if (file.isFile() && file.getName().endsWith(MARKDOWN_FILE_EXT)) {
                processReadmeFile(file, relativeImageOutDir, false);
            } else if (!directoriesFound && file.isDirectory()) {
                directoriesFound = true;
            }
        }

        return directoriesFound;
    }

    /**
     * Process a given README.md by reading through lines.
     *
     * @param file README.md file
     */
    private static void processReadmeFile(File file, Path relativeImageOutDirPath, boolean addFrontMatter) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            StringBuilder processedLines = new StringBuilder();
            processFrontMatter(addFrontMatter, reader, processedLines);

            while ((line = reader.readLine()) != null) {
                if (line.contains(INCLUDE_CODE_TAG)) {
                    // Replace INCLUDE_CODE line with include code file.
                    processedLines.append(getIncludeCodeFile(file.getParent(), line));
                } else if (line.contains(INCLUDE_CODE_SEGMENT_TAG)) {
                    // Replace INCLUDE_CODE_SEGMENT line with include code segment.
                    processedLines.append(getIncludeCodeSegment(file.getParent(), line));
                } else if (line.contains(IMG_TAG)) {
                    processedLines.append(updateImageUri(line, relativeImageOutDirPath));
                } else if (line.contains(INCLUDE_MD_TAG)) {
                    processedLines.append(getIncludeMarkdownFile(file.getParent(), line));
                } else {
                    processedLines.append(line);
                }
                processedLines.append('\n');
            }
            IOUtils.write(processedLines, new FileOutputStream(file), String.valueOf(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new ServiceException("Could not find the README.md file: " + file.getPath(), e);
        }
    }

    private static void processFrontMatter(boolean addFrontMatter, BufferedReader reader,
                                           StringBuilder processedLines) throws IOException {
        String line;
        if (addFrontMatter && (line = reader.readLine()) != null) {
            String firstLine = line;
            if (line.contains(HASH)) {
                firstLine = Util.generateFrontMatter(line, commitHash);
            }
            processedLines.append(firstLine).append('\n');
        }
    }

    public static String updateImageUri(String line, Path relativeImageOutDirPath) {
        if (line.contains(CDATA_TAG)) { // ignore CDATA tags
            return line;
        }
        int altTextStartIdx = line.indexOf(IMG_TAG);
        int altTextEndIdx = line.indexOf(']', altTextStartIdx);
        int uriStartIdx = line.indexOf('(', altTextEndIdx) + 1;
        int uriEndIdx = line.indexOf(')', uriStartIdx) - 1;

        if (uriStartIdx >= uriEndIdx) {
            log.warn("Empty image uri found in line.  {}", line);
            return line;
        }

        String uri = line.substring(uriStartIdx, uriEndIdx);
        Path fileName = Paths.get(uri).getFileName();
        Path newUri = relativeImageOutDirPath.resolve(fileName);
        return line.replace(uri, newUri.toString());
    }

    /**
     * Get code file content should be included in the README.md file.
     *
     * @param readMeParentPath parent path of the README.md file
     * @param line             line having INCLUDE_CODE_TAG
     * @return code content of the code file should be included
     */
    private static String getIncludeCodeFile(String readMeParentPath, String line) {
        String fullPathOfIncludeCodeFile = readMeParentPath
                                           + getIncludeFilePathFromIncludeCodeLine(line, INCLUDE_CODE_TAG);
        File includeCodeFile = new File(fullPathOfIncludeCodeFile);
        String code = Util.removeLicenceHeader(Util.getCodeFile(includeCodeFile, readMeParentPath,
                                                      config.getReadmeFileName()), readMeParentPath).trim();
        return handleCodeAlignment(line, Util.getMarkdownCodeBlockWithCodeType(fullPathOfIncludeCodeFile, code));
    }

    /**
     * Get code segment should be included in the README.md file.
     *
     * @param readMeParentPath parent path of the README.md file
     * @param line             line having INCLUDE_CODE_SEGMENT_TAG
     * @return code segment content should be included
     */
    private static String getIncludeCodeSegment(String readMeParentPath, String line) {
        String includeLineData = line.replace(COMMENT_START, EMPTY_STRING).replace(COMMENT_END, EMPTY_STRING)
                .replace(INCLUDE_CODE_SEGMENT_TAG, EMPTY_STRING)
                .trim();

        String[] tempDataArr = includeLineData.replace(OPEN_CURLY_BRACKET, EMPTY_STRING)
                .replace(CLOSE_CURLY_BRACKET, EMPTY_STRING).split(COMMA);

        String fullPathOfIncludeCodeFile =
                readMeParentPath + File.separator + tempDataArr[0].replace("file:", EMPTY_STRING).trim();
        String segment = tempDataArr[1].replace("segment:", EMPTY_STRING).trim();

        File includeCodeFile = new File(fullPathOfIncludeCodeFile);
        String codeFileContent = Util.removeLicenceHeader(Util.getCodeFile(includeCodeFile, readMeParentPath,
                                                                 config.getReadmeFileName()), readMeParentPath);

        String code = getCodeSegment(codeFileContent, segment).trim();
        return handleCodeAlignment(line, Util.getMarkdownCodeBlockWithCodeType(fullPathOfIncludeCodeFile, code));
    }

    /**
     * Get code segment form code file content.
     *
     * @param codeFileContent code file content
     * @param segmentName     segment name used in the code file (eg: segment_1)
     * @return code segment as a string
     */
    private static String getCodeSegment(String codeFileContent, String segmentName) {
        try {
            return codeFileContent.split(CODE_SEGMENT_BEGIN
                                         + segmentName)[1].split(CODE_SEGMENT_END + segmentName)[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ServiceException("Invalid code segment including. segmentName: " + segmentName);
        }
    }

    /**
     * Handle alignment of the code inclusion. Leading whitespaces of the INCLUDE_CODE_TAG line should be added
     * to the beginning of each code line.
     * <WHITESPACES>INCLUDE_CODE_TAG => </WHITESPACES>{code}
     *
     * @param line INCLUDE_CODE_TAG line
     * @param code code content as a string
     * @return aligned code
     */
    private static String handleCodeAlignment(String line, String code) {
        String leadingSpaces = Util.getLeadingWhitespaces(line);
        return leadingSpaces + code.replace(NEW_LINE, NEW_LINE + leadingSpaces);
    }

    /**
     * get file path of the include line.
     *
     * @param line line having include tag
     * @return file path of the file should be included
     */
    private static String getIncludeFilePathFromIncludeCodeLine(String line, String includeTag) {
        return "/" + line.replace(COMMENT_START, EMPTY_STRING).replace(COMMENT_END, EMPTY_STRING)
                .replace(includeTag, EMPTY_STRING).trim();
    }

    /**
     * Get current commit hash by reading `git.properties` file.
     * `git.properties` file generated by `git-commit-id-plugin` maven plugin.
     *
     * @return current git commit hash
     */
    private static String getCommitHashByReadingGitProperties() {
        ClassLoader classLoader = DocsGenerator.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(GIT_PROPERTIES_FILE);
        if (inputStream != null) {
            try {
                String gitCommitHash = Util.getCommitHash(inputStream);
                if (gitCommitHash == null) {
                    throw new ServiceException("git commit id is null.");
                }
                log.info("Git commit hash: {}", commitHash);
                return gitCommitHash;
            } catch (ServiceException e) {
                throw new ServiceException("Version information could not be retrieved", e);
            }
        } else {
            throw new ServiceException("Error when reading " + GIT_PROPERTIES_FILE);
        }
    }

    private static void addDirectoriesToQueue(ArrayDeque<List<File>> queue, File folder) {
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null || listOfFiles.length == 0) {
            return;
        }

        List<File> fileList = new ArrayList<>();
        Collections.addAll(fileList, listOfFiles);
        queue.add(fileList);
    }

    /**
     * Get markdown file content should be included in the README.md file.
     *
     * @param readMeParentPath parent path of the README.md file
     * @param line             line having INCLUDE_MD_TAG
     * @return content of the markdown file should be included
     */
    private static String getIncludeMarkdownFile(String readMeParentPath, String line) {
        String fullPathOfIncludeMdFile = readMeParentPath + getIncludeFilePathFromIncludeCodeLine(line, INCLUDE_MD_TAG);
        File includeMdFile = new File(fullPathOfIncludeMdFile);
        String includeMdContent = Util.getCodeFile(includeMdFile, readMeParentPath, config.getReadmeFileName()).trim();
        // Check fullPathOfIncludeMdFile is `get-the-code.md`.
        if (fullPathOfIncludeMdFile.contains("get-the-code.md")) {
            String markdownWithZipName = setZipFileName(includeMdContent, readMeParentPath);
            return setModuleName(setGetTheCodeMdPaths(fullPathOfIncludeMdFile, markdownWithZipName), readMeParentPath);
        } else {
            return includeMdContent;
        }
    }

    /**
     * Set zip file name by replacing MD_FILE_NAME tag with zip file name.
     *
     * @param includeMdContent include markdown file content
     * @param readMeParentPath README.md parent path
     * @return include markdown file content after replacing MD_FILE_NAME tag
     */
    private static String setZipFileName(String includeMdContent, String readMeParentPath) {
        String zipName = readMeParentPath.substring(readMeParentPath.lastIndexOf('/') + 1).trim();
        return includeMdContent.replace("<<<MD_FILE_NAME>>>", zipName);
    }

    /**
     * Set paths in `tutorial-get-the-code.md` for non default cases.
     *
     * @param fullPathOfIncludeMdFile full path of include md file
     * @param includeMdContent        include md file content
     * @return `tutorial-get-the-code.md` content after changing `download-zip` path.
     */
    private static String setGetTheCodeMdPaths(String fullPathOfIncludeMdFile, String includeMdContent) {
        // Get the no of occurrences of `../`
        int occurrences = fullPathOfIncludeMdFile.split("\\../", -1).length - 1;
        if (occurrences == 4) { // Default case: `tutorial-get-the-code.md` has 4 `../`s.
            return includeMdContent;
        } else { // Need to set the `download-zip` path.
            // Set the `download-zip` image path.
            String mdImgPath = Util.getStringBetweenTwoStrings(includeMdContent, "<img src=\"",
                                                          "\" width=\"200\" alt=\"Download ZIP\">");
            String correctImgPath = Util.addPrevDirectorySyntax(mdImgPath, occurrences + 1 - 5);
            String replacedImgContent = includeMdContent.replace(mdImgPath, correctImgPath);
            // Set the `download-zip` anchor path.
            String zipAnchorPath = Util.getStringBetweenTwoStrings(replacedImgContent, "<a href=\"",
                                                              "\">\n" + "    <img src=\"");
            String correctZipAnchorPath = Util.addPrevDirectorySyntax(zipAnchorPath, occurrences + 1 - 5);
            return replacedImgContent.replace(zipAnchorPath, correctZipAnchorPath);
        }
    }

    /**
     * Set module name by replacing MODULE_NAME tag by module name of the project.
     *
     * @param includeMdContent include markdown file content
     * @param readMeParentPath README.md parent path
     * @return include markdown file content after replacing MODULE_NAME tag
     */
    private static String setModuleName(String includeMdContent, String readMeParentPath) {
        String moduleName = findModuleName(readMeParentPath);
        if (moduleName.isEmpty()) {
            throw new ServiceException("Module name not found. projectPath: " + readMeParentPath);
        } else {
            return includeMdContent.replace("<<<MODULE_NAME>>>", moduleName);
        }
    }

    /**
     * Find module name to set module name.
     *
     * @param readMeParentPath README.md parent path
     * @return module name
     */
    private static String findModuleName(String readMeParentPath) {
        boolean moduleFound = false;
        String moduleName = "";
        File moduleParent = new File(readMeParentPath + File.separator + "src");
        File[] listOfFiles = moduleParent.listFiles();

        if (listOfFiles == null) {
            throw new ServiceException("Cannot find module name. projectPath: " + moduleParent.getPath());
        } else {
            for (File child : listOfFiles) {
                if (child.isDirectory()) {
                    if (!moduleFound) {
                        moduleName = child.getName();
                        moduleFound = true;
                    } else {
                        throw new ServiceException(
                                "Module name already found, Please confirm this project contains only "
                                + "one module. projectPath: " + moduleParent.getPath());
                    }
                }
            }
        }
        return moduleName;
    }

    /**
     * Interface to write the processing logic for provided set of files when traversing the directory
     * structure.
     */
    private interface DirectoryProcessor {

        boolean process(DirRegistry dirRegistry, List<File> files);
    }
}
