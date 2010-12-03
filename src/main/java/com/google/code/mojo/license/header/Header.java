/**
 * Copyright (C) 2008 http://code.google.com/p/maven-license-plugin/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.code.mojo.license.header;

import com.google.code.mojo.license.util.StringUtils;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.google.code.mojo.license.util.FileUtils.read;
import static com.google.code.mojo.license.util.FileUtils.remove;

/**
 * The <code>Header</code> class wraps the license template file, the one which have to be outputted inside the other
 * files.
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Header {
    private final URL location;
    private final String headerContent;
    private final String headerContentOneLine;
    private String[] lines;

    private static final Map<String, String> EMPTY_PROPERTIES = new HashMap<String, String>(0);

    /**
     * Constructs a <code>Header</code> object pointing to a license template file. In case of the template contains
     * replaceable values (declared as ${<em>valuename</em>}), you can set the map of this values.
     *
     * @param location   The license template file location.
     * @param properties The map of values to replace.
     * @throws IllegalArgumentException If the header file location is null or if an error occurred while reading the
     *                                  file content.
     */
    public Header(URL location, Map<String, String> properties) {
        if (location == null) {
            throw new IllegalArgumentException("Cannot read license template header file with a null location");
        }
        if (properties == null) {
            properties = EMPTY_PROPERTIES;
        }
        this.location = location;
        try {
            this.headerContent = read(location, properties);
            lines = headerContent.replace("\r", "").split("\n");
            headerContentOneLine = remove(headerContent, " ", "\t", "\r", "\n");
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Cannot read header document " + location + ". Cause: " + e.getMessage(), e);
        }
    }

    public String asString() {
        return headerContent;
    }

    public String asOneLineString() {
        return headerContentOneLine;
    }

    public int getLineCount() {
        return lines.length;
    }

    /**
     * Returns the location of license template file.
     *
     * @return The URL location.
     */
    public URL getLocation() {
        return location;
    }

    public String eol(boolean unix) {
        return unix ? "\n" : "\r\n";
    }

    public String buildForDefinition(HeaderDefinition type, boolean unix) {
        StringBuilder newHeader = new StringBuilder();
        if (notEmpty(type.getFirstLine())) {
            newHeader.append(type.getFirstLine().replace("EOL", eol(unix)));
            newHeader.append(eol(unix));
        }
        for (String line : getLines()) {
            final String str = type.getBeforeEachLine().replace("EOL", eol(unix)) + line;
            newHeader.append(StringUtils.rtrim(str));
            newHeader.append(eol(unix));
        }
        if (notEmpty(type.getEndLine())) {
            newHeader.append(type.getEndLine().replace("EOL", eol(unix)));
            newHeader.append(eol(unix));
        }
        return newHeader.toString();
    }

    @Override
    public String toString() {
        return asString();
    }

    public String[] getLines() {
        return lines;
    }

    private boolean notEmpty(String str) {
        return str != null && str.length() > 0;
    }

}
