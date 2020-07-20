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

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static com.google.common.truth.Truth.*;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.mockito.Mockito.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
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
  static final String COMMENT_TABLE_NAME = "Comment";
  HttpServletRequest postRequest = mock(HttpServletRequest.class);
  HttpServletResponse postResponse = mock(HttpServletResponse.class);
  HttpServletRequest getRequest = mock(HttpServletRequest.class);
  HttpServletResponse getResponse = mock(HttpServletResponse.class);
  DataServlet dataServelet = new DataServlet();
  DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

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
  public void testPostSingleComment() throws Exception {
    Assert.assertEquals(0, ds.prepare(new Query(COMMENT_TABLE_NAME)).countEntities(withLimit(10)));

    when(postRequest.getParameter("text-input")).thenReturn("Comment1");
    dataServelet.doPost(postRequest, postResponse);
    
    Assert.assertEquals(1, ds.prepare(new Query(COMMENT_TABLE_NAME)).countEntities(withLimit(10)));
  }

  @Test 
  public void testPostMultipleComments() throws Exception {
    Assert.assertEquals(0, ds.prepare(new Query(COMMENT_TABLE_NAME)).countEntities(withLimit(10)));

    when(postRequest.getParameter("text-input")).thenReturn("Comment1");
    dataServelet.doPost(postRequest, postResponse);
    when(postRequest.getParameter("text-input")).thenReturn("Comment2");
    dataServelet.doPost(postRequest, postResponse);
    when(postRequest.getParameter("text-input")).thenReturn("Comment3");
    dataServelet.doPost(postRequest, postResponse);
    
    Assert.assertEquals(3, ds.prepare(new Query(COMMENT_TABLE_NAME)).countEntities(withLimit(10)));
  }

  @Test 
  public void testPostNull() throws Exception {
    Assert.assertEquals(0, ds.prepare(new Query(COMMENT_TABLE_NAME)).countEntities(withLimit(10)));

    when(postRequest.getParameter("text-input")).thenReturn(null);
    dataServelet.doPost(postRequest, postResponse);
    
    Assert.assertEquals(0, ds.prepare(new Query(COMMENT_TABLE_NAME)).countEntities(withLimit(10)));
  }

  @Test
  public void testGetSingleComment() throws Exception {
    when(postRequest.getParameter("text-input")).thenReturn("Test Comment #1");

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(getResponse.getWriter()).thenReturn(printWriter);

    dataServelet.doPost(postRequest, postResponse);
    dataServelet.doGet(getRequest, getResponse);

    printWriter.flush(); //may not have flushed yet
    assertThat(stringWriter.toString()).contains("Test Comment #1");
  }
  
  @Test
  public void testGetSomeComments() throws Exception {
    int NUM_COMMENTS = 2;

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(getResponse.getWriter()).thenReturn(printWriter);

    when(postRequest.getParameter("text-input")).thenReturn("Test Comment #1");
    dataServelet.doPost(postRequest, postResponse);
    when(postRequest.getParameter("text-input")).thenReturn("Test Comment #2");
    dataServelet.doPost(postRequest, postResponse);
    when(postRequest.getParameter("text-input")).thenReturn("Test Comment #3");
    dataServelet.doPost(postRequest, postResponse);
    when(postRequest.getParameter("text-input")).thenReturn("Test Comment #4");
    dataServelet.doPost(postRequest, postResponse);
    when(postRequest.getParameter("text-input")).thenReturn("Test Comment #5");
    dataServelet.doPost(postRequest, postResponse);
    when(getRequest.getParameter("max-num")).thenReturn(Integer.toString(NUM_COMMENTS));
    dataServelet.doGet(getRequest, getResponse);

    printWriter.flush(); //may not have flushed yet
    assertThat(stringWriter.toString()).contains("Test Comment #1");
    assertThat(stringWriter.toString()).contains("Test Comment #2");  
  }

  @Test
  public void testGetAllComments() throws Exception {
    int NUM_COMMENTS = 3;

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(getResponse.getWriter()).thenReturn(printWriter);

    when(postRequest.getParameter("text-input")).thenReturn("Test Comment #1");
    dataServelet.doPost(postRequest, postResponse);
    when(postRequest.getParameter("text-input")).thenReturn("Test Comment #2");
    dataServelet.doPost(postRequest, postResponse);
    when(postRequest.getParameter("text-input")).thenReturn("Test Comment #3");
    dataServelet.doPost(postRequest, postResponse);
    when(getRequest.getParameter("max-num")).thenReturn(Integer.toString(NUM_COMMENTS));
    dataServelet.doGet(getRequest, getResponse);

    printWriter.flush(); //may not have flushed yet
    assertThat(stringWriter.toString()).contains("Test Comment #1");
    assertThat(stringWriter.toString()).contains("Test Comment #2");
    assertThat(stringWriter.toString()).contains("Test Comment #3");
  }

  @Test
  public void testGetEmptyDataStore() throws Exception {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(getResponse.getWriter()).thenReturn(printWriter);  

    dataServelet.doGet(getRequest, getResponse); 

    printWriter.flush(); //may not have flushed yets
    assertThat(stringWriter.toString().trim()).isEqualTo("[]");
  }

//   @Test 
//   public void testGETContentType() throws Exception {
//     HttpServletRequest getRequest = mock(HttpServletRequest.class);
//     HttpServletResponse getResponse = mock(HttpServletResponse.class);

//     StringWriter stringWriter = new StringWriter();
//     PrintWriter printWriter = new PrintWriter(stringWriter);
      
//     dataServelet.doGet(getRequest, getResponse); 
//     when(getResponse.getWriter()).thenReturn(printWriter);
//     printWriter.flush(); //may not have flushed yets
//     String responseContentType = getResponse.getContentType();
//     assertThat(responseContentType).isEqualTo("application/json");  
//   }
}
