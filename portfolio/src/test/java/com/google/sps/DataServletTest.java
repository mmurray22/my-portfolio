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
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class DataServletTest {
  static final String COMMENT_TABLE_NAME = "Comment";
  static final String COMMENT_COLUMN_NAME = "text";
  static final String TIMESTAMP_COLUMN_NAME = "submit_time";
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

  private String[] convertStringToArray(String responseOutput) {
      //Gets rid of brackets
      responseOutput = responseOutput.replace("[", "")
                                     .replace("]", "")
                                     .replace("\"", "");
      //Parse by commas
      String[] comments = (responseOutput.trim()).split(",");
      return comments;
  }

  @Test 
  public void testPostSingleComment() throws Exception {
    assertThat(0).isEqualTo(ds.prepare(new Query(COMMENT_TABLE_NAME)).countEntities(withLimit(10)));

    when(postRequest.getParameter("text-input")).thenReturn("Comment1");
    dataServelet.doPost(postRequest, postResponse);
    
    assertThat(1).isEqualTo(ds.prepare(new Query(COMMENT_TABLE_NAME)).countEntities(withLimit(10)));
  }

  @Test 
  public void testPostMultipleComments() throws Exception {
    assertThat(0).isEqualTo(ds.prepare(new Query(COMMENT_TABLE_NAME)).countEntities(withLimit(10)));

    when(postRequest.getParameter("text-input")).thenReturn("Comment1");
    dataServelet.doPost(postRequest, postResponse);
    when(postRequest.getParameter("text-input")).thenReturn("Comment2");
    dataServelet.doPost(postRequest, postResponse);
    when(postRequest.getParameter("text-input")).thenReturn("Comment3");
    dataServelet.doPost(postRequest, postResponse);
    
    assertThat(3).isEqualTo(ds.prepare(new Query(COMMENT_TABLE_NAME)).countEntities(withLimit(10)));
  }

  @Test 
  public void testPostNull() throws Exception {
    assertThat(0).isEqualTo(ds.prepare(new Query(COMMENT_TABLE_NAME)).countEntities(withLimit(10)));

    when(postRequest.getParameter("text-input")).thenReturn(null);
    dataServelet.doPost(postRequest, postResponse);
    
    assertThat(0).isEqualTo(ds.prepare(new Query(COMMENT_TABLE_NAME)).countEntities(withLimit(10)));
  }

  @Test
  public void testGetSingleComment() throws Exception {
    long TIMESTAMP_ONE = 0;
    Entity comment = new Entity(COMMENT_TABLE_NAME);
    comment.setProperty(COMMENT_COLUMN_NAME, "Test Comment #1");
    comment.setProperty(TIMESTAMP_COLUMN_NAME, TIMESTAMP_ONE);
    ds.put(comment);

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(getResponse.getWriter()).thenReturn(printWriter);

    dataServelet.doPost(postRequest, postResponse);
    dataServelet.doGet(getRequest, getResponse);

    printWriter.flush(); //may not have flushed yet
    String[] comments = convertStringToArray(stringWriter.toString());
    assertThat(comments[0]).isEqualTo("Test Comment #1");
  }
  
  @Test
  public void testGetSomeComments() throws Exception {
    int NUM_COMMENTS = 2;
    long TIMESTAMP_ONE = 0;
    Entity comment = new Entity(COMMENT_TABLE_NAME);
    comment.setProperty(COMMENT_COLUMN_NAME, "Test Comment #1");
    comment.setProperty(TIMESTAMP_COLUMN_NAME, TIMESTAMP_ONE);
    ds.put(comment);

    long TIMESTAMP_TWO = 1;
    comment = new Entity(COMMENT_TABLE_NAME);
    comment.setProperty(COMMENT_COLUMN_NAME, "Test Comment #2");
    comment.setProperty(TIMESTAMP_COLUMN_NAME, TIMESTAMP_TWO);
    ds.put(comment);

    long TIMESTAMP_THREE = 2;
    comment = new Entity(COMMENT_TABLE_NAME);
    comment.setProperty(COMMENT_COLUMN_NAME, "Test Comment #3");
    comment.setProperty(TIMESTAMP_COLUMN_NAME, TIMESTAMP_THREE);
    ds.put(comment);

    long TIMESTAMP_FOUR = 3;
    comment = new Entity(COMMENT_TABLE_NAME);
    comment.setProperty(COMMENT_COLUMN_NAME, "Test Comment #4");
    comment.setProperty(TIMESTAMP_COLUMN_NAME, TIMESTAMP_FOUR);
    ds.put(comment);

    long TIMESTAMP_FIVE = 4;
    comment = new Entity(COMMENT_TABLE_NAME);
    comment.setProperty(COMMENT_COLUMN_NAME, "Test Comment #5");
    comment.setProperty(TIMESTAMP_COLUMN_NAME, TIMESTAMP_FIVE);
    ds.put(comment);

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(getResponse.getWriter()).thenReturn(printWriter);
    when(getRequest.getParameter("max-num")).thenReturn(Integer.toString(NUM_COMMENTS));
    dataServelet.doGet(getRequest, getResponse);

    printWriter.flush(); //may not have flushed yet
    String[] comments = convertStringToArray(stringWriter.toString());
    assertThat(comments[0]).isEqualTo("Test Comment #1");
    assertThat(comments[1]).isEqualTo("Test Comment #2");  
    assertThat(comments.length).isEqualTo(2);
  }

  @Test
  public void testGetAllComments() throws Exception {
    int NUM_COMMENTS = 3;
    long TIMESTAMP_ONE = 0;
    Entity comment = new Entity(COMMENT_TABLE_NAME);
    comment.setProperty(COMMENT_COLUMN_NAME, "Test Comment #1");
    comment.setProperty(TIMESTAMP_COLUMN_NAME, TIMESTAMP_ONE);
    ds.put(comment);

    long TIMESTAMP_TWO = 1;
    comment = new Entity(COMMENT_TABLE_NAME);
    comment.setProperty(COMMENT_COLUMN_NAME, "Test Comment #2");
    comment.setProperty(TIMESTAMP_COLUMN_NAME, TIMESTAMP_TWO);
    ds.put(comment);

    long TIMESTAMP_THREE = 2;
    comment = new Entity(COMMENT_TABLE_NAME);
    comment.setProperty(COMMENT_COLUMN_NAME, "Test Comment #3");
    comment.setProperty(TIMESTAMP_COLUMN_NAME, TIMESTAMP_THREE);
    ds.put(comment);

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(getResponse.getWriter()).thenReturn(printWriter);

    when(getRequest.getParameter("max-num")).thenReturn(Integer.toString(NUM_COMMENTS));
    dataServelet.doGet(getRequest, getResponse);

    printWriter.flush(); //may not have flushed yet
    String responseOutput = stringWriter.toString();
    String[] comments = convertStringToArray(responseOutput);
    assertThat(comments[0]).isEqualTo("Test Comment #1");
    assertThat(comments[1]).isEqualTo("Test Comment #2");
    assertThat(comments[2]).isEqualTo("Test Comment #3"); 
    assertThat(comments.length).isEqualTo(3);
  }

  @Test
  public void testGetEmptyDataStore() throws Exception {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(getResponse.getWriter()).thenReturn(printWriter);  

    dataServelet.doGet(getRequest, getResponse); 

    printWriter.flush(); //may not have flushed yet
    assertThat(stringWriter.toString().trim()).isEqualTo("[]");
  }

  @Test
  public void testPostAndGetSingleComment() throws Exception {
    when(postRequest.getParameter("text-input")).thenReturn("Test Comment #1");

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(getResponse.getWriter()).thenReturn(printWriter);

    dataServelet.doPost(postRequest, postResponse);
    dataServelet.doGet(getRequest, getResponse);

    printWriter.flush(); //may not have flushed yet
    String[] comments = convertStringToArray(stringWriter.toString());
    assertThat(comments[0]).isEqualTo("Test Comment #1");
    assertThat(comments.length).isEqualTo(1);
  }
  
  @Test
  public void testPostAndGetSomeComments() throws Exception {
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
    String[] comments = convertStringToArray(stringWriter.toString());
    assertThat(comments[0]).isEqualTo("Test Comment #1");
    assertThat(comments[1]).isEqualTo("Test Comment #2");
    assertThat(comments.length).isEqualTo(2);
  }

  @Test
  public void testPostAndGetAllComments() throws Exception {
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
    String[] comments = convertStringToArray(stringWriter.toString());
    assertThat(comments[0]).isEqualTo("Test Comment #1");
    assertThat(comments[1]).isEqualTo("Test Comment #2");
    assertThat(comments[2]).isEqualTo("Test Comment #3");
    assertThat(comments.length).isEqualTo(3);
  }
}
