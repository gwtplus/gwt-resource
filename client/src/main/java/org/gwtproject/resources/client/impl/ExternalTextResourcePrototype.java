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
package org.gwtproject.resources.client.impl;

import static elemental2.core.Global.JSON;

import org.gwtproject.http.client.Request;
import org.gwtproject.http.client.RequestBuilder;
import org.gwtproject.http.client.RequestCallback;
import org.gwtproject.http.client.RequestException;
import org.gwtproject.http.client.Response;
import org.gwtproject.jsonp.client.JsonpRequestBuilder;
import org.gwtproject.resources.client.ExternalTextResource;
import org.gwtproject.resources.client.ResourceCallback;
import org.gwtproject.resources.client.ResourceException;
import org.gwtproject.resources.client.TextResource;
import org.gwtproject.safehtml.shared.SafeUri;
import org.gwtproject.safehtml.shared.annotations.SuppressIsTrustedResourceUriCastCheck;
import org.gwtproject.user.client.rpc.AsyncCallback;

import elemental2.core.JsArray;

/**
 * Implements external resource fetching of TextResources.
 */
public class ExternalTextResourcePrototype implements ExternalTextResource {

  /**
   * Maps the HTTP callback onto the ResourceCallback.
   */
  private class ETRCallback implements RequestCallback, AsyncCallback<JsArray<String>> {
    final ResourceCallback<TextResource> callback;

    ETRCallback(ResourceCallback<TextResource> callback) {
      this.callback = callback;
    }

    // For RequestCallback
    public void onError(Request request, Throwable exception) {
      onFailure(exception);
    }

    // For AsyncCallback
    public void onFailure(Throwable exception) {
      callback.onError(new ResourceException(ExternalTextResourcePrototype.this,
          "Unable to retrieve external resource", exception));
    }

    // For RequestCallback
    public void onResponseReceived(Request request, final Response response) {
      String responseText = response.getText();

      // Using JSON.parse instead of eval since it should be fast enough
      // In case of bigger files both will be similarly slow
      @SuppressWarnings("unchecked")
      JsArray<String> jsArray = (JsArray<String>) JSON.parse(responseText);
      onSuccess(jsArray);
     }

    // For AsyncCallback
    public void onSuccess(JsArray<String> jsArray) {
      if (jsArray == null) {
        callback.onError(new ResourceException(ExternalTextResourcePrototype.this, 
            "JSON.parse returned null"));
        return;
      }

      // Populate the TextResponse cache array
      final String resourceText = jsArray.getAt(index);
      cache[index] = new TextResource() {

        public String getName() {
          return name;
        }

        public String getText() {
          return resourceText;
        }

      };

      // Finish by invoking the callback
      callback.onSuccess(cache[index]);
    }
  }

  /**
   * This is a reference to an array nominally created in the IRB that contains
   * the ExternalTextResource. It is intended to be shared between all instances
   * of the ETR that have a common parent IRB.
   */
  private final TextResource[] cache;
  private final int index;
  private final String md5Hash;
  private final String name;
  private final SafeUri url;

  public ExternalTextResourcePrototype(String name, SafeUri url,
      TextResource[] cache, int index) {
    this.name = name;
    this.url = url;
    this.cache = cache;
    this.index = index;
    this.md5Hash = null;
  }

  public ExternalTextResourcePrototype(String name, SafeUri url,
      TextResource[] cache, int index, String md5Hash) {
    this.name = name;
    this.url = url;
    this.cache = cache;
    this.index = index;
    this.md5Hash = md5Hash;
  }

  public String getName() {
    return name;
  }

  /**
   * Possibly fire off an HTTPRequest for the text resource.
   */
  @SuppressIsTrustedResourceUriCastCheck
  public void getText(ResourceCallback<TextResource> callback) throws ResourceException {

    // If we've already parsed the JSON bundle, short-circuit.
    if (cache[index] != null) {
      callback.onSuccess(cache[index]);
      return;
    }

    if (md5Hash != null) {
      // If we have an md5Hash, we should be using JSONP
      JsonpRequestBuilder rb = new JsonpRequestBuilder();
      rb.setPredeterminedId(md5Hash);
      rb.requestObject(url.asString(), new ETRCallback(callback));
    } else {
      RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, url.asString());
      try {
        rb.sendRequest("", new ETRCallback(callback));
      } catch (RequestException e) {
        throw new ResourceException(this, "Unable to initiate request for external resource", e);
      }
    }
  }
}
