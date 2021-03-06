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
package org.gwtproject.resources.client;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Specifies the implementation of ResourceGenerator to use for a type of
 * {@link ResourcePrototype}.
 * 
 * @deprecated with no direct replacement
 */

//TODO: Revert the relationship, the generator should specify which resource type it supports
//TODO: generators should register using SPI

@Deprecated
@Target(ElementType.TYPE)
public @interface ResourceGeneratorType {
  
  /**
   * Default value. When used the generator will use {@link ResourceGeneratorType#className()}
   */
  final class Default { 
    private Default() { }
  }
  
  /**
   * @deprecated use {@link #className()}
   */
  @Deprecated
  Class<?> value() default Default.class;
  
  /**
   * @return fully qualified name of the generator class implementing {@link ResourceGenerator}
   */
  String className() default "org.gwtproject.resources.client.ResourceGeneratorType.Default";
}
