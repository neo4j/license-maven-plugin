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

import com.google.code.mojo.license.util.FileUtils;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class HeaderTest {
    @Test
    public void test() throws Exception {
        Map<String, String> props = new HashMap<String, String>();
        props.put("year", "2008");
        Header header = new Header(getClass().getResource("/test-header1.txt"), props);
        assertEquals(header.getLineCount(), 13);
        assertTrue(header.asOneLineString().contains("2008"));
        assertEquals(header.getLocation(), getClass().getResource("/test-header1.txt"));

        //FileUtils.write(new File("src/test/resources/test-header2.txt"), header.buildForDefinition(HeaderType.ASP.getDefinition(), false));

        final File file = new File("src/test/resources/test-header2.txt");
        final String content = FileUtils.read(file, System.getProperty("file.encoding"));
        assertEquals(content, header.buildForDefinition(HeaderType.ASP.getDefinition(), content.indexOf("\n") == -1));
    }
}
