/*
 * Copyright 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.gwtproject.resource;

import com.google.gwt.junit.tools.GWTTestSuite;
import org.gwtproject.resource.client.CSSResourceTest;
import org.gwtproject.resource.client.CSSResourceWithGSSTest;
import org.gwtproject.resource.client.DataResourceDoNotEmbedTest;
import org.gwtproject.resource.client.DataResourceMimeTypeTest;
import org.gwtproject.resource.client.ExternalTextResourceJsonpTest;
import org.gwtproject.resource.client.ExternalTextResourceTest;
import org.gwtproject.resource.client.ImageResourceNoInliningTest;
import org.gwtproject.resource.client.ImageResourceTest;
import org.gwtproject.resource.client.NestedBundleTest;
import org.gwtproject.resource.client.TextResourceTest;
import org.gwtproject.resource.client.gss.AutoConversionTest;
import org.gwtproject.resource.client.gss.DebugObfuscationStyleTest;
import org.gwtproject.resource.client.gss.GssResourceTest;
import org.gwtproject.resource.client.gss.PrettyObfuscationStyleTest;
import org.gwtproject.resource.client.gss.StableNoTypeObfuscationStyleTest;
import org.gwtproject.resource.client.gss.StableObfuscationStyleTest;
import org.gwtproject.resource.client.gss.StableShortTypeObfuscationStyleTest;

import junit.framework.Test;

/**
 * Tests the ClientBundle framework.
 */
public class ResourceGwtSuite {
  public static Test suite() {

    GWTTestSuite suite = new GWTTestSuite("Test for org.gwtproject.resource");
    suite.addTestSuite(DataResourceDoNotEmbedTest.class);
    suite.addTestSuite(DataResourceMimeTypeTest.class);
    suite.addTestSuite(ExternalTextResourceJsonpTest.class);
    /* TODO: check xhr! suite.addTestSuite(ExternalTextResourceTest.class);*/
    suite.addTestSuite(ImageResourceNoInliningTest.class);
    suite.addTestSuite(ImageResourceTest.class);
    suite.addTestSuite(NestedBundleTest.class);
    suite.addTestSuite(TextResourceTest.class);
    suite.addTestSuite(CSSResourceTest.class);
    suite.addTestSuite(CSSResourceWithGSSTest.class);

    // GSS
    suite.addTestSuite(AutoConversionTest.class);
    suite.addTestSuite(GssResourceTest.class);
    suite.addTestSuite(DebugObfuscationStyleTest.class);
    suite.addTestSuite(PrettyObfuscationStyleTest.class);
    suite.addTestSuite(StableShortTypeObfuscationStyleTest.class);
    suite.addTestSuite(StableNoTypeObfuscationStyleTest.class);
    suite.addTestSuite(StableObfuscationStyleTest.class);
    return suite;
  }
}
