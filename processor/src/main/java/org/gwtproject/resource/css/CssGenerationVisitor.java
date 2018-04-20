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
package org.gwtproject.resource.css;

import org.gwtproject.resource.css.ast.Context;
import org.gwtproject.resource.css.ast.CssDef;
import org.gwtproject.resource.css.ast.CssEval;
import org.gwtproject.resource.css.ast.CssExternalSelectors;
import org.gwtproject.resource.css.ast.CssFontFace;
import org.gwtproject.resource.css.ast.CssIf;
import org.gwtproject.resource.css.ast.CssMediaRule;
import org.gwtproject.resource.css.ast.CssNoFlip;
import org.gwtproject.resource.css.ast.CssNode;
import org.gwtproject.resource.css.ast.CssPageRule;
import org.gwtproject.resource.css.ast.CssProperty;
import org.gwtproject.resource.css.ast.CssRule;
import org.gwtproject.resource.css.ast.CssSelector;
import org.gwtproject.resource.css.ast.CssSprite;
import org.gwtproject.resource.css.ast.CssSubstitution;
import org.gwtproject.resource.css.ast.CssUnknownAtRule;
import org.gwtproject.resource.css.ast.CssUrl;
import org.gwtproject.resource.css.ast.CssVisitor;

import com.google.gwt.dev.util.TextOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Generates a static CSS template string and provides information on where to
 * inject dynamic expressions.
 */
public class CssGenerationVisitor extends CssVisitor {
  private boolean needsOpenBrace;
  private boolean needsComma;

  private final TextOutput out;

  private final boolean substituteDots;
  private final SortedMap<Integer, List<CssSubstitution>> substitutionPositions = new TreeMap<Integer, List<CssSubstitution>>();

  /**
   * Constructor.
   * 
   * @param out the output hondler
   */
  public CssGenerationVisitor(TextOutput out) {
    this(out, false);
  }

  /**
   * Constructor for producing an abbreviated form of the template for use with
   * {@link CssNode#toString()}.
   * 
   * @param out the output handler
   * @param substituteDots if <code>true</code> locations in the text output
   *          where expression substitutions would normally occur are replaced
   *          with a textual placeholder
   */
  public CssGenerationVisitor(TextOutput out, boolean substituteDots) {
    this.out = out;
    this.substituteDots = substituteDots;
  }

  @Override
  public void endVisit(CssFontFace x, Context ctx) {
    closeBrace();
  }

  @Override
  public void endVisit(CssIf x, Context ctx) {
    // Match up an explanatory comment
    out.indentOut();
    out.printOpt("/* } */");
    out.newlineOpt();
  }

  @Override
  public void endVisit(CssMediaRule x, Context ctx) {
    out.indentOut();
    out.print("}");
    out.newlineOpt();
  }

  @Override
  public void endVisit(CssNoFlip x, Context ctx) {
    out.printOpt("/*} @noflip */");
    out.newlineOpt();
  }

  @Override
  public void endVisit(CssPageRule x, Context ctx) {
    out.indentOut();
    out.print("}");
    out.newlineOpt();
  }

  @Override
  public void endVisit(CssRule x, Context ctx) {
    if (!x.getProperties().isEmpty()) {
      // Don't print empty rule blocks
      closeBrace();
    }
  }

  @Override
  public void endVisit(CssUnknownAtRule x, Context ctx) {
    out.printOpt("/* Unknown at-rule */\n");
    out.print(x.getRule());
  }

  public SortedMap<Integer, List<CssSubstitution>> getSubstitutionPositions() {
    return substitutionPositions;
  }

  @Override
  public boolean visit(CssFontFace x, Context ctx) {
    out.print("@font-face");
    openBrace();
    return true;
  }

  @Override
  public boolean visit(CssDef x, Context ctx) {
    // These are not valid CSS
    out.printOpt("/* CssDef */");
    out.newlineOpt();
    return false;
  }

  @Override
  public boolean visit(CssEval x, Context ctx) {
    // These are not valid CSS
    out.printOpt("/* CssEval */");
    out.newlineOpt();
    return false;
  }

  @Override
  public boolean visit(CssExternalSelectors x, Context ctx) {
    // These are not valid CSS
    out.printOpt("/* @external");
    for (String className : x.getClasses()) {
      out.printOpt(" ");
      out.printOpt(className);
    }
    out.printOpt("; */");
    out.newlineOpt();
    return false;
  }

  @Override
  public boolean visit(CssIf x, Context ctx) {
    // Record where the contents of the if block should be inserted
    StringBuilder expr = new StringBuilder("/* @if ");
    if (x.getExpression() != null) {
      expr.append(x.getExpression()).append(" ");
    } else {
      expr.append(x.getPropertyName()).append(" ");
      for (String v : x.getPropertyValues()) {
        expr.append(v).append(" ");
      }
    }
    expr.append("{ */");
    out.printOpt(expr.toString());
    out.newlineOpt();
    out.indentIn();
    addSubstitition(x);
    return false;
  }

  @Override
  public boolean visit(CssMediaRule x, Context ctx) {
    out.print("@media");
    boolean isFirst = true;
    for (String m : x.getMedias()) {
      if (isFirst) {
        out.print(" ");
        isFirst = false;
      } else {
        comma();
      }
      out.print(m);
    }
    spaceOpt();
    out.print("{");
    out.newlineOpt();
    out.indentIn();
    return true;
  }

  @Override
  public boolean visit(CssNoFlip x, Context ctx) {
    out.printOpt("/*@noflip { */");
    out.newlineOpt();
    return true;
  }

  @Override
  public boolean visit(CssPageRule x, Context ctx) {
    out.print("@page");
    if (x.getPseudoPage() != null) {
      out.print(" :");
      out.print(x.getPseudoPage());
    }
    spaceOpt();
    out.print("{");
    out.newlineOpt();
    out.indentIn();
    return true;
  }

  @Override
  public boolean visit(CssProperty x, Context ctx) {
    if (needsOpenBrace) {
      openBrace();
      needsOpenBrace = false;
    }

    out.print(x.getName());
    colon();
    addSubstitition(x);

    if (x.isImportant()) {
      important();
    }

    semi();

    return true;
  }

  @Override
  public boolean visit(CssRule x, Context ctx) {
    if (x.getProperties().isEmpty()) {
      // Don't print empty rule blocks
      return false;
    }

    needsOpenBrace = true;
    needsComma = false;
    return true;
  }

  @Override
  public boolean visit(CssSelector x, Context ctx) {
    if (needsComma) {
      comma();
    }
    needsComma = true;
    out.print(x.getSelector());
    return true;
  }

  @Override
  public boolean visit(CssSprite x, Context ctx) {
    // These are not valid CSS
    out.printOpt("/* CssSprite */");
    out.newlineOpt();
    addSubstitition(x);
    return false;
  }

  @Override
  public boolean visit(CssUrl x, Context ctx) {
    // These are not valid CSS
    out.printOpt("/* CssUrl */");
    out.newlineOpt();
    return false;
  }

  private <T extends CssNode & CssSubstitution> void addSubstitition(T node) {
    if (substituteDots) {
      out.printOpt(".....");
      out.newlineOpt();
    } else {
      int position = out.toString().length();
      if (substitutionPositions.containsKey(position)) {
        substitutionPositions.get(position).add(node);
      } else {
        List<CssSubstitution> nodes = new ArrayList<CssSubstitution>();
        nodes.add(node);
        substitutionPositions.put(position, nodes);
      }
    }
  }

  private void closeBrace() {
    out.indentOut();
    out.print('}');
    out.newlineOpt();
  }

  private void colon() {
    spaceOpt();
    out.print(':');
    spaceOpt();
  }

  private void comma() {
    out.print(',');
    spaceOpt();
  }

  private void important() {
    out.print(" !important");
  }

  private void openBrace() {
    spaceOpt();
    out.print('{');
    out.newlineOpt();
    out.indentIn();
  }

  private void semi() {
    out.print(';');
    out.newlineOpt();
  }

  private void spaceOpt() {
    out.printOpt(' ');
  }
}
