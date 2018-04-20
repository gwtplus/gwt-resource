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

import org.gwtproject.resource.css.ast.Context;
import org.gwtproject.resource.css.ast.CssCompilerException;
import org.gwtproject.resource.css.ast.CssIf;
import org.gwtproject.resource.css.ast.CssVisitor;
import org.gwtproject.resource.ext.ClientBundleRequirements;

import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.TreeLogger;

/**
 * Analyzes a stylesheet to update the ClientBundleRequirements interface.
 */
public class RequirementsCollector extends CssVisitor {
  private final TreeLogger logger;
  private final ClientBundleRequirements requirements;

  public RequirementsCollector(TreeLogger logger,
      ClientBundleRequirements requirements) {
    this.logger = logger.branch(TreeLogger.DEBUG,
        "Scanning CSS for requirements");
    this.requirements = requirements;
  }

  @Override
  public void endVisit(CssIf x, Context ctx) {
    String propertyName = x.getPropertyName();
    if (propertyName != null) {
      try {
        requirements.addPermutationAxis(propertyName);
      } catch (BadPropertyValueException e) {
        logger.log(TreeLogger.ERROR, "Unknown deferred-binding property "
            + propertyName, e);
        throw new CssCompilerException("Unknown deferred-binding property", e);
      }
    }
  }
}
