/*
 * Copyright 2018 Google Inc.
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
package org.gwtproject.resource.processor;

import static java.lang.String.format;
import static javax.tools.Diagnostic.Kind.WARNING;

import org.gwtproject.resources.client.ResourceBundle;

import com.google.auto.common.BasicAnnotationProcessor.ProcessingStep;
import com.google.auto.common.MoreElements;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Generated;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

/**
 * 
 */
public class ResourceBundleStep implements ProcessingStep {
  
  private Messager messager;
  private Filer filer;
  private Types types;
  private Elements elements;

  public ResourceBundleStep(Messager messager, Filer filer, Types types, Elements elements) {
    this.messager = messager;
    this.filer = filer;
    this.types = types;
    this.elements = elements;
  }

  @Override
  public Set<? extends Class<? extends Annotation>> annotations() {
    return ImmutableSet.of(ResourceBundle.class);
  }

  @Override
  public Set<? extends Element> process(
      SetMultimap<Class<? extends Annotation>, Element> elementsByAnnotation) {
    
    Set<? extends Element> result = new HashSet<>();
    
    Set<Element> bundleCandidates = elementsByAnnotation.get(ResourceBundle.class);
    for (Element bundleCandidate : bundleCandidates) {
      switch(bundleCandidate.getKind()) {
      case INTERFACE:
        // correct placement
        break;
      case CLASS:
        String msg = format("%s applied on a class instead of an interface.", 
            ResourceBundle.class.getCanonicalName());
        AnnotationMirror am = MoreElements.getAnnotationMirror(bundleCandidate, 
            ResourceBundle.class).get();
        
        messager.printMessage(WARNING, msg, bundleCandidate, am);
        continue;
      default:
        // Error should be reported by the compiler when checking annotation's @Target.
        continue;
      }
      
      TypeElement type = (TypeElement) bundleCandidate;
      ClassName className = ClassName.get(type);
      
      AnnotationSpec generated = AnnotationSpec.builder(Generated.class)
          .addMember("value", "\"" + ResourceProcessor.class.getCanonicalName() + "\"")
          .build();
      
      TypeSpec.Builder builder = TypeSpec.classBuilder(
          bundleCandidate.getSimpleName() + "_" + ResourceBundle.class.getSimpleName());
      builder
        .addOriginatingElement(bundleCandidate)
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(generated)
        .addSuperinterface(className);
      
      try {
        JavaFile.builder(className.packageName(), builder.build()).build().writeTo(filer);
      } catch (IOException ioe) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println(
            "Error generating source file for type " + className);
        ioe.printStackTrace(pw);
        pw.close();
        messager.printMessage(Kind.ERROR, sw.toString());
      }
    }
    
    // We should only be here if there was no previous warning (to avoid errors on "unused"
    // annotations)

    return result;
  }

}
