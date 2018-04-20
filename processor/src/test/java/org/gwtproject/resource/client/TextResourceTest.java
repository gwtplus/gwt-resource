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
package org.gwtproject.resource.client;

import org.gwtproject.resource.shared.ExternalTextResource;
import org.gwtproject.resource.shared.ResourcePrototype;
import org.gwtproject.resource.shared.Source;
import org.gwtproject.resource.shared.TextResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

/**
 * Tests for TextResource assembly and use.
 */
public class TextResourceTest extends GWTTestCase {

  static interface Resources extends ClientBundleWithLookup {
    @Source("bigtextresource.txt")
    TextResource bigTextResource();

    @Source("org/gwtproject/resource/client/hello.txt")
    TextResource helloWorldAbsolute();

    @Source("hello.txt")
    ExternalTextResource helloWorldExternal();

    @Source("org/gwtproject/resource/server/outside_resource_oracle.txt")
    TextResource helloWorldOutsideResourceOracle();

    @Source("hello.txt")
    TextResource helloWorldRelative();
  }

  private static final String HELLO = "Hello World!";

  @Override
  public String getModuleName() {
    return "org.gwtproject.resource.ResourceGeneratorTest";
  }

  /**
   * Test fix for problem where large text files caused out of memory errors
   * when run in Development Mode.
   */
  public void testBigTextResource() {
    final Resources r = GWT.create(Resources.class);
    String result = r.bigTextResource().getText();
    int length = result.length();
    assertEquals(12737792, length);
  }

  public void testInline() {
    Resources r = GWT.create(Resources.class);
    assertEquals(HELLO, r.helloWorldRelative().getText());
    assertEquals(HELLO, r.helloWorldAbsolute().getText());
  }

  public void testMeta() {
    Resources r = GWT.create(Resources.class);
    assertEquals("helloWorldAbsolute", r.helloWorldAbsolute().getName());
    assertEquals("helloWorldRelative", r.helloWorldRelative().getName());
    assertEquals("helloWorldExternal", r.helloWorldExternal().getName());

    ResourcePrototype[] resources = r.getResources();
    assertEquals(5, resources.length);
  }

  public void testOutsideResourceOracle() {
    Resources r = GWT.create(Resources.class);
    assertTrue(r.helloWorldOutsideResourceOracle().getText().startsWith(HELLO));
  }
}
