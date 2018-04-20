/*
 * Copyright 2009 Google Inc.
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
package org.gwtproject.resource.css;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import org.gwtproject.resource.css.ast.CssNode;
import org.gwtproject.resource.css.ast.CssNodeCloner;
import org.gwtproject.resource.css.ast.CssProperty;
import org.gwtproject.resource.css.ast.CssSelector;
import org.gwtproject.resource.css.ast.CssStylesheet;
import org.gwtproject.resource.rg.CssTestCase;

import java.util.List;

/**
 * Tests the CssNodeCloner utility class.
 */
public class CssNodeClonerTest extends CssTestCase {

  public void testClone() throws UnableToCompleteException {
    CssStylesheet sheet = GenerateCssAst.exec(TreeLogger.NULL,
        getClass().getClassLoader().getResource(
            "org/gwtproject/resource/client/test.css"));

    CssStylesheet cloned = CssNodeCloner.clone(CssStylesheet.class, sheet);

    assertNotSame(sheet, cloned);
    assertNoAliasing(cloned);
  }

  public void testCloneList() throws UnableToCompleteException {
    CssStylesheet sheet = GenerateCssAst.exec(TreeLogger.NULL,
        getClass().getClassLoader().getResource(
            "org/gwtproject/resource/client/test.css"));

    List<CssNode> cloned = CssNodeCloner.clone(CssNode.class, sheet.getNodes());

    assertEquals(sheet.getNodes().size(), cloned.size());

    for (CssNode node : cloned) {
      assertNoAliasing(node);
    }
  }

  public void testCloneMedia() throws UnableToCompleteException {
    CssStylesheet sheet = GenerateCssAst.exec(TreeLogger.NULL,
        getClass().getClassLoader().getResource(
            "org/gwtproject/resource/css/media.css"));

    CssStylesheet cloned = CssNodeCloner.clone(CssStylesheet.class, sheet);

    assertEquals("@media print, standard {\n}\n", cloned.toString());
  }

  public void testCloneProperty() {
    CssProperty.IdentValue value = new CssProperty.IdentValue("value");
    CssProperty p = new CssProperty("name", value, true);

    CssProperty clone = CssNodeCloner.clone(CssProperty.class, p);

    assertNotSame(p, clone);
    assertEquals(p.getName(), clone.getName());
    assertEquals(value.getIdent(),
        clone.getValues().getValues().get(0).isIdentValue().getIdent());
  }

  public void testCloneSelector() {
    CssSelector sel = new CssSelector("a , b");

    CssSelector clone = CssNodeCloner.clone(CssSelector.class, sel);

    assertNotSame(sel, clone);
    assertEquals(sel.getSelector(), clone.getSelector());
  }
}
