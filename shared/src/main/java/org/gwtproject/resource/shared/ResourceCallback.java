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
package org.gwtproject.resource.shared;

import org.gwtproject.callback.shared.Callback;

/**
 * A callback interface for asynchronous operations on resources.
 * 
 * @param <R> the type of resource
 */
public interface ResourceCallback<R extends ResourcePrototype> extends 
    Callback<R, ResourceException> {
  
  /**
   * Invoked if the asynchronous operation failed.
   * @param reason an exception describing the failure
   */
  @Override
  default void onFailure(ResourceException reason) {
    onError(reason);
  }

  /**
   * @deprecated implement {@link #onFailure(ResourceException)} instead
   */
  @Deprecated // (since="gwt-3.0.0", forRemoval=true)
  default void onError(ResourceException e) { };

  /**
   * Invoked if the asynchronous operation was successfully completed.
   * @param resource the resource on which the operation was performed
   */
  @Override
  void onSuccess(R resource);
}
