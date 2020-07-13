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

class AWSRemoteIpValveRegexCidr32Test {

  public static final String REMOTE_ADDR = "1.2.3.4";
  public static final String[] REAL_CLOUDFRONT_ADDRESSES = {
      "52.73.10.190",
  };
  private static final String A_LOAD_BALANCER = "192.168.1.1";
  private static AWSRemoteIpValve valve = new AWSRemoteIpLastValve(new AssertingNoOpValve(REMOTE_ADDR));

  @BeforeAll
  public static void init() throws IOException {
    valve.setIpRangesUrl("file:test-range-with-cidr-32.json");
    valve.updateIpRanges();
  }

  @Test
  public void testAddresses() throws Exception {
    for (String cloudfrontAddress : REAL_CLOUDFRONT_ADDRESSES) {
      MockRequest request = createMockRequest(cloudfrontAddress);
      valve.invoke(request, null);
    }
  }

  private MockRequest createMockRequest(String cloudfrontAddress) {
    MockRequest request = new MockRequest();
    request.setRemoteAddr(A_LOAD_BALANCER);
    request.setRemoteHost(A_LOAD_BALANCER);
    request.setServerPort(8080);
    request.setScheme("https");
    request.addHeader("X-Forwarded-For", REMOTE_ADDR+","+cloudfrontAddress);
    return request;
  }

}