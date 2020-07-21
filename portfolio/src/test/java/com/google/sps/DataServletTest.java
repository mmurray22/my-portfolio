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
  private final HttpServletRequest postRequest = mock(HttpServletRequest.class);
  private final HttpServletResponse postResponse = mock(HttpServletResponse.class);
  private final HttpServletRequest getRequest = mock(HttpServletRequest.class);
  private final HttpServletResponse getResponse = mock(HttpServletResponse.class);
  private final DataServlet dataServlet = new DataServlet();
  private final DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
  private static final String COMMENT_ONE = "Test Comment #1";
  private static final String COMMENT_TWO = "Test Comment #2";
  private static final String COMMENT_THREE = "Test Comment #3";
  private static final String COMMENT_FOUR = "Test Comment #4";
  private static final String COMMENT_FIVE = "Test Comment #5";
  private static final long TIMESTAMP_ONE = 0;
  private static final long TIMESTAMP_TWO = 1;
  private static final long TIMESTAMP_THREE = 2;
  private static final long TIMESTAMP_FOUR = 3;
  private static final long TIMESTAMP_FIVE = 4;
  private static final String COMMENT_PARAMETER = "text-input";
  private static final String NUMBER_COMMENTS_DISPLAYED_PARAMETER = "max-num";
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
      // Gets rid of brackets
      responseOutput = responseOutput.replace("[", "")
                                     .replace("]", "")
                                     .replace("\"", "");
      // Parse by commas
      String[] comments = (responseOutput.trim()).split(",");
      return comments;
  }
  
  private int getNumberOfEntiresInDatastore() {
      return ds.prepare(new Query(dataServlet.COMMENT_TABLE_NAME)).countEntities(withLimit(10));
  }

  private void addEntityToDatastore(String comment_text, long timestamp) {
    Entity comment = new Entity(dataServlet.COMMENT_TABLE_NAME);
    comment.setProperty(dataServlet.COMMENT_COLUMN_NAME, comment_text);
    comment.setProperty(dataServlet.TIMESTAMP_COLUMN_NAME, timestamp);
    ds.put(comment);
  }

  @Test 
  public void testPostSingleComment() throws Exception {
    assertThat(getNumberOfEntiresInDatastore()).isEqualTo(0);

    when(postRequest.getParameter(COMMENT_PARAMETER)).thenReturn(COMMENT_ONE);
    dataServlet.doPost(postRequest, postResponse);
    
    assertThat(getNumberOfEntiresInDatastore()).isEqualTo(1);
  }

  @Test 
  public void testPostMultipleComments() throws Exception {
    assertThat(getNumberOfEntiresInDatastore()).isEqualTo(0);

    when(postRequest.getParameter(COMMENT_PARAMETER)).thenReturn(COMMENT_ONE);
    dataServlet.doPost(postRequest, postResponse);
    when(postRequest.getParameter(COMMENT_PARAMETER)).thenReturn(COMMENT_TWO);
    dataServlet.doPost(postRequest, postResponse);
    when(postRequest.getParameter(COMMENT_PARAMETER)).thenReturn(COMMENT_THREE);
    dataServlet.doPost(postRequest, postResponse);
    
    assertThat(getNumberOfEntiresInDatastore()).isEqualTo(3);
  }

  @Test 
  public void testPostNull() throws Exception {
    assertThat(getNumberOfEntiresInDatastore()).isEqualTo(0);

    when(postRequest.getParameter(COMMENT_PARAMETER)).thenReturn(null);
    dataServlet.doPost(postRequest, postResponse);
    
    assertThat(getNumberOfEntiresInDatastore()).isEqualTo(0);
  }

  @Test
  public void testGetSingleComment() throws Exception {
    addEntityToDatastore(COMMENT_ONE, TIMESTAMP_ONE);

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(getResponse.getWriter()).thenReturn(printWriter);

    dataServlet.doGet(getRequest, getResponse);

    String[] comments = convertStringToArray(stringWriter.toString());
    assertThat(comments[0]).isEqualTo(COMMENT_ONE);
  }
  
  @Test
  public void testGetSomeComments() throws Exception {
    int NUM_COMMENTS_TO_DISPLAY = 2;

    addEntityToDatastore(COMMENT_ONE, TIMESTAMP_ONE);
    addEntityToDatastore(COMMENT_TWO, TIMESTAMP_TWO);
    addEntityToDatastore(COMMENT_THREE, TIMESTAMP_THREE);
    addEntityToDatastore(COMMENT_FOUR, TIMESTAMP_FOUR);
    addEntityToDatastore(COMMENT_FIVE, TIMESTAMP_FIVE);

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(getResponse.getWriter()).thenReturn(printWriter);
    when(getRequest.getParameter(NUMBER_COMMENTS_DISPLAYED_PARAMETER)).thenReturn(Integer.toString(NUM_COMMENTS_TO_DISPLAY));
    dataServlet.doGet(getRequest, getResponse);

    String[] comments = convertStringToArray(stringWriter.toString());
    assertThat(comments[0]).isEqualTo(COMMENT_ONE);
    assertThat(comments[1]).isEqualTo(COMMENT_TWO);  
    assertThat(comments.length).isEqualTo(NUM_COMMENTS_TO_DISPLAY);
  }

  @Test
  public void testGetAllComments() throws Exception {
    int NUM_COMMENTS_TO_DISPLAY = 3;
    addEntityToDatastore(COMMENT_ONE, TIMESTAMP_ONE);
    addEntityToDatastore(COMMENT_TWO, TIMESTAMP_TWO);
    addEntityToDatastore(COMMENT_THREE, TIMESTAMP_THREE);

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(getResponse.getWriter()).thenReturn(printWriter);

    when(getRequest.getParameter(NUMBER_COMMENTS_DISPLAYED_PARAMETER)).thenReturn(Integer.toString(NUM_COMMENTS_TO_DISPLAY));
    dataServlet.doGet(getRequest, getResponse);

    String responseOutput = stringWriter.toString();
    String[] comments = convertStringToArray(responseOutput);
    assertThat(comments[0]).isEqualTo(COMMENT_ONE);
    assertThat(comments[1]).isEqualTo(COMMENT_TWO);
    assertThat(comments[2]).isEqualTo(COMMENT_THREE); 
    assertThat(comments.length).isEqualTo(NUM_COMMENTS_TO_DISPLAY);
  }

  @Test
  public void testGetEmptyDataStore() throws Exception {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(getResponse.getWriter()).thenReturn(printWriter);  

    dataServlet.doGet(getRequest, getResponse); 

    assertThat(stringWriter.toString().trim()).isEqualTo("[]");
  }

  @Test
  public void testPostAndGetSingleComment() throws Exception {
    when(postRequest.getParameter(COMMENT_PARAMETER)).thenReturn(COMMENT_ONE);

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(getResponse.getWriter()).thenReturn(printWriter);

    dataServlet.doPost(postRequest, postResponse);
    dataServlet.doGet(getRequest, getResponse);

    String[] comments = convertStringToArray(stringWriter.toString());
    assertThat(comments[0]).isEqualTo(COMMENT_ONE);
    assertThat(comments.length).isEqualTo(1);
  }
  
  @Test
  public void testPostAndGetSomeComments() throws Exception {
    int NUM_COMMENTS = 2;

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(getResponse.getWriter()).thenReturn(printWriter);

    when(postRequest.getParameter(COMMENT_PARAMETER)).thenReturn(COMMENT_ONE);
    dataServlet.doPost(postRequest, postResponse);
    when(postRequest.getParameter(COMMENT_PARAMETER)).thenReturn(COMMENT_TWO);
    dataServlet.doPost(postRequest, postResponse);
    when(postRequest.getParameter(COMMENT_PARAMETER)).thenReturn(COMMENT_THREE);
    dataServlet.doPost(postRequest, postResponse);
    when(postRequest.getParameter(COMMENT_PARAMETER)).thenReturn(COMMENT_FOUR);
    dataServlet.doPost(postRequest, postResponse);
    when(postRequest.getParameter(COMMENT_PARAMETER)).thenReturn(COMMENT_FIVE);
    dataServlet.doPost(postRequest, postResponse);
    
    when(getRequest.getParameter(NUMBER_COMMENTS_DISPLAYED_PARAMETER)).thenReturn(Integer.toString(NUM_COMMENTS));
    dataServlet.doGet(getRequest, getResponse);

    String[] comments = convertStringToArray(stringWriter.toString());
    assertThat(comments[0]).isEqualTo(COMMENT_ONE);
    assertThat(comments[1]).isEqualTo(COMMENT_TWO);
    assertThat(comments.length).isEqualTo(2);
  }

  @Test
  public void testPostAndGetAllComments() throws Exception {
    int NUM_COMMENTS = 3;

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(getResponse.getWriter()).thenReturn(printWriter);

    when(postRequest.getParameter(COMMENT_PARAMETER)).thenReturn(COMMENT_ONE);
    dataServlet.doPost(postRequest, postResponse);
    when(postRequest.getParameter(COMMENT_PARAMETER)).thenReturn(COMMENT_TWO);
    dataServlet.doPost(postRequest, postResponse);
    when(postRequest.getParameter(COMMENT_PARAMETER)).thenReturn(COMMENT_THREE);
    dataServlet.doPost(postRequest, postResponse);
    when(getRequest.getParameter(NUMBER_COMMENTS_DISPLAYED_PARAMETER)).thenReturn(Integer.toString(NUM_COMMENTS));
    dataServlet.doGet(getRequest, getResponse);

    String[] comments = convertStringToArray(stringWriter.toString());
    assertThat(comments[0]).isEqualTo(COMMENT_ONE);
    assertThat(comments[1]).isEqualTo(COMMENT_TWO);
    assertThat(comments[2]).isEqualTo(COMMENT_THREE);
    assertThat(comments.length).isEqualTo(3);
  }
}
