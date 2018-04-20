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
package org.gwtproject.resource.shared;

import java.util.List;

/**
 * This is an extension of ClientBundle that allows for name-based lookup of
 * resources. Note that the use of the methods defined within this interface
 * will prevent the compiler from pruning any of the resources declared in the
 * ClientBundle.
 * 
 * @deprecated use {@link ResourceRegistry} instead
 */
@Deprecated // (since = "gwt-3.0.0", forRemoval = true)
public interface ClientBundleWithLookup extends ClientBundle, ResourceRegistry {

  /**
   * Find a resource by the name of the function in which it is declared.
   * 
   * @param name the name of the desired resource
   * @return the resource, or <code>null</code> if no such resource is defined.
   * 
   * @deprecated use {@link #get(String)} instead
   */
  @Deprecated
  default ResourcePrototype getResource(String name) {
    return get(name);
  }

  /**
   * A convenience method to iterate over all ResourcePrototypes contained in
   * the ClientBundle.
   * 
   * @deprecated use {@link #getAll()} instead
   */
  @Deprecated
  default ResourcePrototype[] getResources() {
    List<ResourcePrototype> all = getAll();
    return all.toArray(new ResourcePrototype[all.size()]);
  }
}