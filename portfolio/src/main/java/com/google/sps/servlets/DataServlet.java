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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.io.IOException;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private static final String COMMENT_TABLE_NAME = "Comment";
  private static final String COMMENT_COLUMN_NAME = "text";
  private static final String TIMESTAMP_COLUMN_NAME = "submit_time";
  private final DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query(COMMENT_TABLE_NAME).addSort(TIMESTAMP_COLUMN_NAME, SortDirection.ASCENDING);
    PreparedQuery results = datastore.prepare(query);
    List<String> myComments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
        myComments.add((String) entity.getProperty(COMMENT_COLUMN_NAME));
    }
    String commentJSON = convertToJson(myComments);

    // Send JSON as the response
    response.setContentType("application/json;");
    response.getWriter().println(commentJSON);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get response from the form
    String text = request.getParameter("text-input");
    if (text != null && !text.isEmpty()) {
        long timestamp = System.currentTimeMillis();
        Entity comment = new Entity(COMMENT_TABLE_NAME);
        comment.setProperty(COMMENT_COLUMN_NAME, text);
        comment.setProperty(TIMESTAMP_COLUMN_NAME, timestamp);
        dataStore.put(comment);
    }
    response.sendRedirect("/index.html");
  }

  private static String convertToJson(List<String> myComments) {
    Gson gson = new Gson();
    String json = gson.toJson(myComments);
    return json;
  }
}
