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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AWSRemoteIpValveTest {

    public static final int ITERATIONS = 100000;
    public static final String REMOTE_ADDR = "1.2.3.4";
    public static final String A_REAL_CLOUDFRONT_ADDRESS = "143.204.194.179";
    private static final String A_LOAD_BALANCER = "192.168.1.1";
    private static AWSRemoteIpValve valve = new AWSRemoteIpLastValve(new AssertingNoOpValve(REMOTE_ADDR));

    @BeforeAll
    public static void init() throws IOException {
        valve.updateIpRanges();
    }

    @Test
    public void testLatency() throws Exception {
        MockRequest request = createMockRequest();
        valve.invoke(request, null); // Get rid of overhead for the first request
        long t1 = System.currentTimeMillis();
        for(int i = 0; i< ITERATIONS; i++) {
            valve.invoke(request, null);
        }
        long t2 = System.currentTimeMillis();

        Long duration = t2 - t1;
        Float avgDuration = (duration.floatValue()/ITERATIONS);

        System.out.println("Duration: " + duration + "ms for " + ITERATIONS + " iterations");
        System.out.println("Average: " + avgDuration + "ms");

        assertTrue(avgDuration<0.01);
    }

    private MockRequest createMockRequest() {
        MockRequest request = new MockRequest();
        request.setRemoteAddr(A_LOAD_BALANCER);
        request.setRemoteHost(A_LOAD_BALANCER);
        request.setServerPort(8080);
        request.setScheme("https");
        request.addHeader("X-Forwarded-By", A_REAL_CLOUDFRONT_ADDRESS);
        request.addHeader("X-Forwarded-By", A_LOAD_BALANCER);
        request.addHeader("X-Forwarded-For", REMOTE_ADDR);
        return request;
    }

}