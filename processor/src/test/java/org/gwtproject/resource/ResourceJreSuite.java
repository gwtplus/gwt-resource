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

import org.gwtproject.resource.converter.AlternateAnnotationCreatorVisitorTest;
import org.gwtproject.resource.converter.Css2GssTest;
import org.gwtproject.resource.converter.DefCollectorVisitorTest;
import org.gwtproject.resource.converter.ElseNodeCreatorTest;
import org.gwtproject.resource.converter.UndefinedConstantVisitorTest;
import org.gwtproject.resource.css.CssExternalTest;
import org.gwtproject.resource.css.CssNodeClonerTest;
import org.gwtproject.resource.css.CssReorderTest;
import org.gwtproject.resource.css.CssRtlTest;
import org.gwtproject.resource.css.ExtractClassNamesVisitorTest;
import org.gwtproject.resource.css.UnknownAtRuleTest;
import org.gwtproject.resource.ext.ResourceGeneratorUtilTest;
import org.gwtproject.resource.gss.BooleanConditionCollectorTest;
import org.gwtproject.resource.gss.ClassNamesCollectorTest;
import org.gwtproject.resource.gss.CssPrinterTest;
import org.gwtproject.resource.gss.ExtendedEliminateConditionalNodesTest;
import org.gwtproject.resource.gss.ExternalClassesCollectorTest;
import org.gwtproject.resource.gss.ImageSpriteCreatorTest;
import org.gwtproject.resource.gss.PermutationsCollectorTest;
import org.gwtproject.resource.gss.RecordingBidiFlipperTest;
import org.gwtproject.resource.gss.RenamingSubstitutionMapTest;
import org.gwtproject.resource.gss.ResourceUrlFunctionTest;
import org.gwtproject.resource.gss.RuntimeConditionalBlockCollectorTest;
import org.gwtproject.resource.gss.ValidateRuntimeConditionalNodeTest;
import org.gwtproject.resource.gss.ValueFunctionTest;
import org.gwtproject.resource.rg.CssClassNamesTestCase;
import org.gwtproject.resource.rg.CssOutputTestCase;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * JRE tests of the ClientBundle framework.
 */
public class ResourceJreSuite {
  public static Test suite() {

    TestSuite suite = new TestSuite("JRE test for org.gwtproject.resource");
    suite.addTestSuite(CssClassNamesTestCase.class);
    suite.addTestSuite(CssExternalTest.class);
    suite.addTestSuite(CssNodeClonerTest.class);
    suite.addTestSuite(CssReorderTest.class);
    suite.addTestSuite(CssRtlTest.class);
    suite.addTestSuite(ExtractClassNamesVisitorTest.class);
    suite.addTestSuite(ResourceGeneratorUtilTest.class);
    suite.addTestSuite(UnknownAtRuleTest.class);

    // GSS tests
    suite.addTestSuite(ExternalClassesCollectorTest.class);
    suite.addTestSuite(RenamingSubstitutionMapTest.class);
    suite.addTestSuite(ImageSpriteCreatorTest.class);
    suite.addTestSuite(ClassNamesCollectorTest.class);
    suite.addTestSuite(CssPrinterTest.class);
    suite.addTestSuite(PermutationsCollectorTest.class);
    suite.addTestSuite(RecordingBidiFlipperTest.class);
    suite.addTestSuite(ResourceUrlFunctionTest.class);
    suite.addTestSuite(ExtendedEliminateConditionalNodesTest.class);
    suite.addTestSuite(PermutationsCollectorTest.class);
    suite.addTestSuite(ResourceUrlFunctionTest.class);
    suite.addTestSuite(RuntimeConditionalBlockCollectorTest.class);
    suite.addTestSuite(ValidateRuntimeConditionalNodeTest.class);
    suite.addTestSuite(ValueFunctionTest.class);
    suite.addTestSuite(BooleanConditionCollectorTest.class);

    // CSS to GSS converter tests
    suite.addTestSuite(Css2GssTest.class);
    suite.addTestSuite(CssOutputTestCase.class);
    suite.addTestSuite(DefCollectorVisitorTest.class);
    suite.addTestSuite(ElseNodeCreatorTest.class);
    suite.addTestSuite(AlternateAnnotationCreatorVisitorTest.class);
    suite.addTestSuite(UndefinedConstantVisitorTest.class);

    return suite;
  }
}
