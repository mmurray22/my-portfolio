// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import static org.mockito.Mockito.*;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.sps.servlets.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class DataServletTest {
  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void testJSONConverter() throws Exception {
    HttpServletRequest postRequest = mock(HttpServletRequest.class);
    HttpServletResponse postResponse = mock(HttpServletResponse.class);
    HttpServletRequest getRequest = mock(HttpServletRequest.class);
    HttpServletResponse getResponse = mock(HttpServletResponse.class);

    when(postRequest.getParameter("text-input")).thenReturn("Test Comment #1");

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(getResponse.getWriter()).thenReturn(printWriter);

    new DataServlet().doPost(postRequest, postResponse);
    new DataServlet().doGet(getRequest, getResponse);

    printWriter.flush(); //may not have flushed yet
    Assert.assertTrue(stringWriter.toString().contains("Test Comment #1"));
  }
}
