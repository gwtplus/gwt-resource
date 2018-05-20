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

import org.gwtproject.resources.client.ResourceBundle;

import com.google.auto.common.BasicAnnotationProcessor.ProcessingStep;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.lang.model.element.Element;

/**
 * 
 */
public class ResourceBundleStep implements ProcessingStep {

  @Override
  public Set<? extends Class<? extends Annotation>> annotations() {
    return ImmutableSet.of(ResourceBundle.class);
  }

  @Override
  public Set<? extends Element> process(SetMultimap<Class<? extends Annotation>, 
      Element> elementsByAnnotation) {
    
    // TODO Auto-generated method stub
    return null;
  }

}
