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

import com.google.sps.servlets.*;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.After;
import org.junit.Before;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.mockito.Mockito.*;
import java.io.StringWriter;
import java.io.PrintWriter;
import org.junit.Assert;
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
    HttpServletRequest requestPost = mock(HttpServletRequest.class);       
    HttpServletResponse responsePost = mock(HttpServletResponse.class);
    HttpServletRequest requestGet = mock(HttpServletRequest.class);       
    HttpServletResponse responseGet = mock(HttpServletResponse.class);
    
    when(requestPost.getParameter("text-input")).thenReturn("Test Comment #1");

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(responseGet.getWriter()).thenReturn(writer);

    new DataServlet().doPost(requestPost, responsePost);
    new DataServlet().doGet(requestGet, responseGet);

    // verify(requestPost, atLeast(1)).getParameter("text-input");
    writer.flush(); // it may not have been flushed yet...
    Assert.assertTrue(stringWriter.toString().contains("Test Comment #1"));
  }
}
