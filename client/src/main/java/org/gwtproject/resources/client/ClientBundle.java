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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The use of this interface is similar to that of ImageBundle. Declare
 * no-argument functions that return subclasses of {@link ResourcePrototype},
 * which are annotated with {@link ClientBundle.Source} annotations specifying
 * the classpath location of the resource to include in the output. At runtime,
 * the functions will return an object that can be used to access the data in
 * the original resource.
 * 
 * @deprecated use {@link ResourceBundle} annotation instead
 */
@Deprecated // (since = "gwt-3.0.0", forRemoval = true)
@ResourceGeneratorType(className = "org.gwtproject.resource.rg.BundleResourceGenerator")
public interface ClientBundle {
  /**
   * Specifies the classpath location of the resource or resources associated
   * with the {@link ResourcePrototype}.
   * 
   * @deprecated use {@link Path} instead
   */
  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  @Repeatable(Source.List.class)
  @Deprecated // (since = "gwt-3.0.0", forRemoval = true)
  public @interface Source {
    String[] value();
    
    /**
     * List of {@link Source}s
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @Deprecated // (since = "gwt-3.0.0", forRemoval = true)
    public @interface List {
      Source[] value();
    }
  }
}
