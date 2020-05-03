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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.wso2.ei.tools.Constants.ZIP_FILE_EXT;

/**
 * Util functions used for site builder.
 */
public class Util {
    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    private Util() {}

    /**
     * Create a directory.
     *
     * @param directoryPath path of the directory
     */
    public static void mkdirs(Path directoryPath) {
        File file = directoryPath.toFile();
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new ServiceException("Error occurred when creating directory: " + directoryPath);
            }
        } else {
            if (logger.isInfoEnabled()) {
                logger.info("Directory already exists: {}", directoryPath);
            }
        }
    }

    /**
     * Create a file.
     *
     * @param filePath path of the file
     */
    public static void createFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new ServiceException("Could not create the file: " + filePath);
                }
            } catch (IOException e) {
                throw new ServiceException("Error when creating the file: " + filePath, e);
            }
        } else {
            if (logger.isInfoEnabled()) {
                logger.info("File already exists: {}", filePath);
            }
        }
    }

    /**
     * Delete a directory.
     *
     * @param directory path of the directory
     */
    public static void deleteDirectory(Path directory) {
        try {
            FileUtils.deleteDirectory(directory.toFile());
        } catch (IOException e) {
            throw new ServiceException("Error occurred while deleting temporary directory " + directory, e);
        }
    }

    /**
     * Delete a file.
     *
     * @param file file should be deleted
     */
    public static void deleteFile(File file) {
        try {
            Files.delete(Paths.get(file.getPath()));
        } catch (IOException e) {
            throw new ServiceException("Error occurred when deleting file. file:" + file.getPath(), e);
        }
    }

    /**
     * Copy directory content to another directory.
     *
     * @param src  path of the source directory
     * @param dest path of the destination directory
     */
    public static void copyDirectoryContent(Path src, Path dest) {
        try {
            FileUtils.copyDirectory(src.toFile(), dest.toFile());
        } catch (IOException e) {
            throw new ServiceException("Error when copying directory content. src: " + src + ", dest: " + dest, e);
        }
    }

    /**
     * Get file content as a string.
     *
     * @param file file which want to content as string
     * @return file content as a string
     */
    public static String getCodeFile(File file, File readmeParentFile, String readmeName) {
        try {
            if (file.exists()) {
                return IOUtils.toString(new FileInputStream(file), String.valueOf(StandardCharsets.UTF_8));
            } else {
                throw new ServiceException("Invalid file path in INCLUDE_CODE tag. Mentioned file does not exists in "
                        + "the project. Please mention the correct file path and try again.\n\tInclude file path\t:"
                        + file.getPath() + "\n\tREADME file path\t:"
                                           + readmeParentFile.toPath().resolve(readmeName));
            }
        } catch (IOException e) {
            throw new ServiceException("Error occurred when converting file content to string. file: "
                                       + file.getPath(), e);
        }
    }

    /**
     * Remove licence header of the code.
     *
     * @param code code file content
     * @param file code file
     * @return code without licence header
     */
    public static String removeLicenceHeader(String code, File file) {
        if (code.contains(Constants.LICENCE_LAST_LINE)) {
            String[] temp = code.split(Constants.LICENCE_LAST_LINE);
            return temp[1].trim();
        } else {
            throw new ServiceException("Licence header is not in the correct format.\nGuide\t: " + file + "\nCode\t:\n"
                    + code);
        }
    }

    /**
     * Get markdown code block with associated type of the code file.
     *
     * @param includeCodeFilePath code file path of the particular code block
     * @param code                      code content
     * @return code block in markdown format
     */
    public static String getMarkdownCodeBlockWithCodeType(Path includeCodeFilePath, String code) {
        String type = FilenameUtils.getExtension(includeCodeFilePath.toString());

        String codeSyntax;
        switch (type) {
            case "bal":
                codeSyntax = Constants.BALLERINA_CODE_MD_SYNTAX;
                break;
            case "java":
                codeSyntax = Constants.JAVA_CODE_MD_SYNTAX;
                break;
            case "synapse":
                codeSyntax = Constants.SYNAPSE_CODE_MD_SYNTAX;
                break;
            default:
                codeSyntax = Constants.CODE_MD_SYNTAX;
                break;
        }
        return codeSyntax.replace(Constants.CODE, code);
    }

    /**
     * Add default front matter for posts.
     *
     * @param title heading title of the md file.
     * @return default front matter for posts
     */
    public static String generateFrontMatter(String title, String commitHash) {
        title = title.substring(1).trim();
        return Constants.FRONT_MATTER_SIGN + Constants.NEW_LINE
                + Constants.TITLE + title + Constants.NEW_LINE
                + Constants.COMMIT_HASH + commitHash + Constants.NEW_LINE
                + Constants.NOTE + Constants.NEW_LINE
                + Constants.FRONT_MATTER_SIGN;
    }

    /**
     * Get commit hash from `git.properties` file input stream.
     *
     * @param inputStream `git.properties` file input stream
     * @return commit hash
     */
    public static String getCommitHash(InputStream inputStream) {
        String commitHash = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(Constants.GIT_COMMIT_ID + Constants.EQUAL)) {
                    commitHash = line.replace(Constants.GIT_COMMIT_ID + Constants.EQUAL, Constants.EMPTY_STRING);
                }
            }
            return commitHash;
        } catch (IOException e) {
            throw new ServiceException("Error occurred when reading input stream.", e);
        }
    }

    /**
     * Remove leading whitespaces of a given string.
     *
     * @param param string want to remove leading whitespaces
     * @return string without leading whitespaces
     */
    private static String removeLeadingSpaces(String param) {
        return param.replaceAll("^\\s+", Constants.EMPTY_STRING);
    }

    /**
     * Get leading whitespaces of a given string.
     *
     * @param param string want to get leading whitespaces
     * @return leading whitespaces of the string
     */
    public static String getLeadingWhitespaces(String param) {
        return param.replace(removeLeadingSpaces(param), Constants.EMPTY_STRING);
    }

    /**
     * Get zip file name using the project file path.
     *
     * @param projectFile toml file
     * @return zip file name
     */
    public static File getZipFile(Path directory, File projectFile) {
        return directory.resolve(projectFile.getParentFile().getName() + ZIP_FILE_EXT).toFile();
    }
}
