/**
 * Copyright © 2016 Collaborne B.V. (opensource@collaborne.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.collaborne.operations.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Request;
import org.apache.catalina.core.StandardContext;

import java.util.*;

public class MockRequest extends Request {

    private HashMap<String, List<String>> headers = new HashMap<>();
    private String scheme = "http";
    private Context context = new StandardContext();

    MockRequest() {
        setCoyoteRequest(new org.apache.coyote.Request());
    }

    public MockRequest addHeader(String key, String value) {
        List<String> list = this.headers.get(key);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(value);
        this.headers.put(key, list);
        return this;
    }

    @Override
    public String getHeader(String name) {
        List<String> values = this.headers.get(name);
        if (values == null) return null;
        return values.get(0);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        List<String> values = this.headers.get(name);
        return Collections.enumeration(values);
    }

    @Override
    public String getScheme() {
        return  scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    @Override
    public Context getContext() {
        return this.context;
    }

}
