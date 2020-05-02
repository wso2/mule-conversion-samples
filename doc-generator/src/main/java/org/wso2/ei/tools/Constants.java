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

/**
 * Constants used in site builder.
 */
final class Constants {

    // Default files and directories
    static final String GIT_PROPERTIES_FILE = "git.properties";

    // Special syntax
    static final String INCLUDE_CODE_TAG = "INCLUDE_CODE:";
    static final String INCLUDE_CODE_SEGMENT_TAG = "INCLUDE_CODE_SEGMENT:";
    static final String MD_IMG_TAG = "![";
    static final String HTML_IMG_TAG = "<img ";
    static final String CDATA_TAG = "<![CDATA[";
    static final String INCLUDE_MD_TAG = "INCLUDE_MD:";
    public static final String EMPTY_STRING = "";
    static final String COMMENT_START = "<!--";
    static final String COMMENT_END = "-->";
    public static final String CODE = "{CODE}";
    public static final String NEW_LINE = "\n";
    private static final String THREE_BACK_TICKS = "```";
    public static final String FRONT_MATTER_SIGN = "---";
    public static final String LICENCE_LAST_LINE = "// under the License.";
    static final String OPEN_CURLY_BRACKET = "{";
    static final String CLOSE_CURLY_BRACKET = "}";
    static final String CODE_SEGMENT_BEGIN = "// CODE-SEGMENT-BEGIN: ";
    static final String CODE_SEGMENT_END = "// CODE-SEGMENT-END: ";
    public static final String CODE_MD_SYNTAX = THREE_BACK_TICKS + NEW_LINE + CODE + NEW_LINE + THREE_BACK_TICKS;
    public static final String FORWARD_SLASH = "/";
    static final String COMMA = ",";
    public static final String HASH = "#";
    public static final String EQUAL = "=";
    public static final String DOWNLOAD_ZIP_LOCATION_TAG = "${DOWNLOAD_ZIP_LOCATION}";
    static final String MARKDOWN_FILE_EXT = ".md";
    static final String ZIP_FILE_EXT = ".zip";
    public static final String GIT_COMMIT_ID = "git.commit.id";
    public static final String COMMIT_HASH = "commitHash: ";
    public static final String TITLE = "title: ";
    public static final String NOTE = "NOTE: This is an auto-generated file do not edit this, Edit content in "
            + "source repository";
    public static final String BALLERINA_CODE_MD_SYNTAX =
            THREE_BACK_TICKS + "ballerina" + NEW_LINE + CODE + NEW_LINE + THREE_BACK_TICKS;
    public static final String JAVA_CODE_MD_SYNTAX =
            THREE_BACK_TICKS + "java" + NEW_LINE + CODE + NEW_LINE + THREE_BACK_TICKS;
    public static final String SYNAPSE_CODE_MD_SYNTAX =
            THREE_BACK_TICKS + "synapse" + NEW_LINE + CODE + NEW_LINE + THREE_BACK_TICKS;

    private Constants() {}
}
