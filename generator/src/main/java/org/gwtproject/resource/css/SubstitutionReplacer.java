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
import org.gwtproject.resource.css.ast.CssDef;
import org.gwtproject.resource.css.ast.CssProperty;
import org.gwtproject.resource.css.ast.CssProperty.DotPathValue;
import org.gwtproject.resource.css.ast.CssProperty.ExpressionValue;
import org.gwtproject.resource.css.ast.CssProperty.FunctionValue;
import org.gwtproject.resource.css.ast.CssProperty.IdentValue;
import org.gwtproject.resource.css.ast.CssProperty.ListValue;
import org.gwtproject.resource.css.ast.CssProperty.Value;
import org.gwtproject.resource.css.ast.CssUrl;
import org.gwtproject.resource.css.ast.CssVisitor;
import org.gwtproject.resource.ext.ResourceContext;
import org.gwtproject.resource.ext.ResourceGeneratorUtil;
import org.gwtproject.resource.shared.DataResource;
import org.gwtproject.resource.shared.ImageResource;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Substitute symbolic replacements into string values.
 */
public class SubstitutionReplacer extends CssVisitor {
  private final ResourceContext context;
  private final JClassType dataResourceType;
  private final JClassType imageResourceType;
  private final TreeLogger logger;
  private final Map<String, CssDef> substitutions;

  public SubstitutionReplacer(TreeLogger logger, ResourceContext context,
      Map<String, CssDef> substitutions) {
    this.context = context;
    this.dataResourceType = context.getGeneratorContext().getTypeOracle().findType(
        DataResource.class.getCanonicalName());
    this.imageResourceType = context.getGeneratorContext().getTypeOracle().findType(
        ImageResource.class.getCanonicalName());
    this.logger = logger;
    this.substitutions = substitutions;
  }

  @Override
  public void endVisit(CssProperty x, Context ctx) {
    if (x.getValues() == null) {
      // Nothing to do
      return;
    }
    x.setValue(substituteDefs(x.getValues()));
  }

  private ListValue substituteDefs(ListValue listValue) {
    List<Value> result = new ArrayList<Value>(listValue.getValues().size());
    for (Value val : listValue.getValues()) {
      if (val.isFunctionValue() != null) {
        // Recursively perform substitution on a function's values
        FunctionValue fnVal = val.isFunctionValue();
        ListValue newVals = substituteDefs(fnVal.getValues());
        result.add(new FunctionValue(fnVal.getName(), newVals));
        continue;
      }

      IdentValue maybeIdent = val.isIdentValue();
      if (maybeIdent == null) {
        // Not an ident, append as-is to result
        result.add(val);
        continue;
      }

      String identStr = maybeIdent.getIdent();
      CssDef def = substitutions.get(identStr);

      if (def == null) {
        // No substitution found, append as-is to result
        result.add(val);
        continue;
      } else if (def instanceof CssUrl) {
        assert def.getValues().size() == 1;
        assert def.getValues().get(0).isDotPathValue() != null;
        DotPathValue functionName = def.getValues().get(0).isDotPathValue();

        JType methodType = null;
        try {
          methodType = ResourceGeneratorUtil.getMethodByPath(context.getClientBundleType(),
              functionName.getParts(), null).getReturnType();
        } catch (NotFoundException e) {
          logger.log(TreeLogger.ERROR, e.getMessage());
          throw new CssCompilerException("Cannot find data method");
        }

        if (!(methodType instanceof JClassType) ||
            (!dataResourceType.isAssignableFrom((JClassType) methodType) &&
            !imageResourceType.isAssignableFrom((JClassType) methodType))) {
          String message = "Invalid method type for url substitution: " + methodType + ". " +
              "Only DataResource and ImageResource are supported.";
          logger.log(TreeLogger.ERROR, message);
          throw new CssCompilerException(message);
        }

        StringBuilder expression = new StringBuilder();
        expression.append("\"url('\" + ");
        expression.append(context.getImplementationSimpleSourceName());
        expression.append(".this.");
        expression.append(functionName.getExpression());
        expression.append(".getSafeUri().asString()");
        expression.append(" + \"')\"");
        result.add(new ExpressionValue(expression.toString()));
      } else {
        for (Value defValue : def.getValues()) {
          result.add(defValue);
        }
      }
    }
    return new ListValue(result);
  }
}