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
import java.io.IOException;
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
  private final DeleteComments deleteComments = new DeleteComments();
  private static final String COMMENT_ONE = "Test Comment #1";
  private static final String COMMENT_TWO = "Test Comment #2";
  private static final long TIMESTAMP_ONE = 0;
  private static final long TIMESTAMP_TWO = 1;
  private static final String NUM_COMMENTS_TO_DELETE = "delete-num";
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
  
  private int getNumberOfEntriesInDatastore() {
    return ds.prepare(new Query(dataServlet.COMMENT_TABLE_NAME)).countEntities(withLimit(10));
  }

  private void addEntityToDatastore(String commentText, long timestamp) {
    Entity comment = new Entity(dataServlet.COMMENT_TABLE_NAME);
    comment.setProperty(dataServlet.COMMENT_COLUMN_NAME, commentText);
    comment.setProperty(dataServlet.TIMESTAMP_COLUMN_NAME, timestamp);
    ds.put(comment);
  }

  @Test
  public void testDeleteSomeComments() throws IOException {
    int numCommentsToDelete = 1;
    addEntityToDatastore(COMMENT_ONE, TIMESTAMP_ONE);
    addEntityToDatastore(COMMENT_TWO, TIMESTAMP_TWO);

    when(postRequest.getParameter(NUM_COMMENTS_TO_DELETE))
        .thenReturn(Integer.toString(numCommentsToDelete));

    deleteComments.doPost(postRequest, postResponse);
    assertThat(getNumberOfEntriesInDatastore()).isEqualTo(1);
  }

  @Test
  public void testDeleteAllComments() throws IOException {
    int numCommentsToDelete = 2;
    addEntityToDatastore(COMMENT_ONE, TIMESTAMP_ONE);
    addEntityToDatastore(COMMENT_TWO, TIMESTAMP_TWO);

    when(postRequest.getParameter(NUM_COMMENTS_TO_DELETE))
        .thenReturn(Integer.toString(numCommentsToDelete));

    deleteComments.doPost(postRequest, postResponse);

    assertThat(getNumberOfEntriesInDatastore()).isEqualTo(0);
  }

  @Test
  public void testDeleteNoComments() throws IOException {
    int numCommentsToDelete = 0;
    addEntityToDatastore(COMMENT_ONE, TIMESTAMP_ONE);
    addEntityToDatastore(COMMENT_TWO, TIMESTAMP_TWO);

    when(postRequest.getParameter(NUM_COMMENTS_TO_DELETE))
        .thenReturn(Integer.toString(numCommentsToDelete));

    deleteComments.doPost(postRequest, postResponse);

    assertThat(getNumberOfEntriesInDatastore()).isEqualTo(2);
  }
  
  @Test
  public void testDeleteNullComments() throws IOException{
    addEntityToDatastore(COMMENT_ONE, TIMESTAMP_ONE);
    addEntityToDatastore(COMMENT_TWO, TIMESTAMP_TWO);

    when(postRequest.getParameter(NUM_COMMENTS_TO_DELETE))
        .thenReturn(null);

    deleteComments.doPost(postRequest, postResponse);

    assertThat(getNumberOfEntriesInDatastore()).isEqualTo(2);
  }
}
