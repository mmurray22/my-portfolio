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

package com.google.sps.servlets;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class DeleteCommentsTest {
  private final HttpServletRequest postRequest = mock(HttpServletRequest.class);
  private final HttpServletResponse postResponse = mock(HttpServletResponse.class);
  private final DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
  private final DataServlet dataServlet = new DataServlet();
  private final DataServletTest dataServletTest = new DataServletTest();
  private final DeleteComments deleteComments = new DeleteComments();
  private static final String COMMENT_ONE = "Test Comment #1";
  private static final String COMMENT_TWO = "Test Comment #2";
  private static final long TIMESTAMP_ONE = 0L;
  private static final long TIMESTAMP_TWO = 1L;
  private static final String NUM_COMMENTS_TO_DELETE_PARAM = "delete-num";
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
  
//   private int getNumberOfEntriesInDatastore() {
//     return ds.prepare(new Query(dataServlet.COMMENT_TABLE_NAME)).countEntities(withLimit(10));
//   }

//   private void addEntityToDatastore(String commentText, long timestamp) {
//     Entity comment = new Entity(dataServlet.COMMENT_TABLE_NAME);
//     comment.setProperty(dataServlet.COMMENT_COLUMN_NAME, commentText);
//     comment.setProperty(dataServlet.TIMESTAMP_COLUMN_NAME, timestamp);
//     ds.put(comment);
//   }

  @Test
  public void testDeleteSomeComments() throws Exception {
    dataServletTest.addEntityToDatastore(COMMENT_ONE, TIMESTAMP_ONE);
    dataServletTest.addEntityToDatastore(COMMENT_TWO, TIMESTAMP_TWO);

    when(postRequest.getParameter(NUM_COMMENTS_TO_DELETE_PARAM))
        .thenReturn("1");

    deleteComments.doPost(postRequest, postResponse);
    assertThat(dataServletTest.getNumberOfEntriesInDatastore()).isEqualTo(1);
  }

  @Test
  public void testDeleteAllComments() throws Exception {
    dataServletTest.addEntityToDatastore(COMMENT_ONE, TIMESTAMP_ONE);
    dataServletTest.addEntityToDatastore(COMMENT_TWO, TIMESTAMP_TWO);

    when(postRequest.getParameter(NUM_COMMENTS_TO_DELETE_PARAM))
        .thenReturn("2");

    deleteComments.doPost(postRequest, postResponse);

    assertThat(dataServletTest.getNumberOfEntriesInDatastore()).isEqualTo(0);
  }

  @Test
  public void testDeleteNoComments() throws Exception {
    dataServletTest.addEntityToDatastore(COMMENT_ONE, TIMESTAMP_ONE);
    dataServletTest.addEntityToDatastore(COMMENT_TWO, TIMESTAMP_TWO);

    when(postRequest.getParameter(NUM_COMMENTS_TO_DELETE_PARAM))
        .thenReturn("0");

    deleteComments.doPost(postRequest, postResponse);

    assertThat(dataServletTest.getNumberOfEntriesInDatastore()).isEqualTo(2);
  }
  
  @Test
  public void testDeleteNullComments() throws Exception{
    dataServletTest.addEntityToDatastore(COMMENT_ONE, TIMESTAMP_ONE);
    dataServletTest.addEntityToDatastore(COMMENT_TWO, TIMESTAMP_TWO);

    when(postRequest.getParameter(NUM_COMMENTS_TO_DELETE_PARAM))
        .thenReturn(null);

    deleteComments.doPost(postRequest, postResponse);

    assertThat(dataServletTest.getNumberOfEntriesInDatastore()).isEqualTo(2);
  }
}
