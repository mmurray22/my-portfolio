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

import com.google.sps.data.MyComments;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.io.IOException;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  MyComments myComments = new MyComments();
  int maxNumComments = 1;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query("Comment");
    MyComments myComments = new MyComments();
    PreparedQuery results = datastore.prepare(query);
    // String maxNumComments = request.getParameter("max-num");
    int index = 0;
    for (Entity entity : results.asIterable()) {
        if (maxNumComments == index) {
            break;
        }
        myComments.addComment((String) entity.getProperty("text"));
        index++;
    }
    // myComments.addComment(Integer.toString(maxNumComments));
    String commentJSON = convertToJson(myComments);

    //Send JSON as the response
    response.setContentType("application/json;");
    response.getWriter().println(commentJSON);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    //Get response from the form
    String text = request.getParameter("text-input");
    String maxNumCommentsParam = request.getParameter("max-num");
    if (maxNumCommentsParam != null && !maxNumCommentsParam.isEmpty()) {
        maxNumComments = Integer.parseInt(request.getParameter("max-num"));
    }
    Entity comment = new Entity("Comment");
    comment.setProperty("text", text);
    DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
    dataStore.put(comment);
    response.sendRedirect("/index.html");
  }

  private static String convertToJson(MyComments myComments) {
    Gson gson = new Gson();
    String json = gson.toJson(myComments);
    return json;
  }
}
