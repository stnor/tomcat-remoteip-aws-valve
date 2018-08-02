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

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;
import org.junit.jupiter.api.Assertions;

import javax.servlet.ServletException;
import java.io.IOException;

public class AssertingNoOpValve extends ValveBase {

    private String expectedRemoteAddr;

    public AssertingNoOpValve(String expectedRemoteAddr) {
        super();
        this.expectedRemoteAddr = expectedRemoteAddr;
    }

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        Assertions.assertEquals(expectedRemoteAddr, request.getRemoteAddr());
    }
}
