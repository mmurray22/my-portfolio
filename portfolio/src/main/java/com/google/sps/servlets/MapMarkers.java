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
import com.google.gson.Gson;
import com.google.sps.data.MapMarker;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that adds MapMarkers to a database */
@WebServlet("/markers")
public class MapMarkers extends HttpServlet {
    private static final String MARKER_TABLE_NAME = "MARKER_LOCATION";
    private static final String MARKER_LAT_COLUMN_NAME = "LATITUDE";
    private static final String MARKER_LNG_COLUMN_NAME = "LONGITUDE";
    private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<MapMarker> markersToDisplay = new ArrayList<>();
        Query query = new Query(MARKER_TABLE_NAME);
        PreparedQuery results = datastore.prepare(query);
        for (Entity entity : results.asIterable()) {
            MapMarker marker = new MapMarker((double) entity.getProperty(MARKER_LAT_COLUMN_NAME),
                                             (double) entity.getProperty(MARKER_LNG_COLUMN_NAME)); 
            markersToDisplay.add(marker);
        }
        String markerJSON = convertToJson(markersToDisplay);
        
        // Send JSON response 
        response.setContentType("application/json;");
        response.getWriter().println(markerJSON);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
         String lat_str = request.getParameter("lat");
         String lng_str = request.getParameter("lng");

         if (lat_str != null && !lat_str.isEmpty() && lng_str != null && !lng_str.isEmpty()) {
                double lat = Double.parseDouble(lat_str);
                double lng = Double.parseDouble(lng_str);
                Entity coordinates = new Entity(MARKER_TABLE_NAME);
                coordinates.setProperty(MARKER_LAT_COLUMN_NAME, lat);
                coordinates.setProperty(MARKER_LNG_COLUMN_NAME, lng);
                datastore.put(coordinates);
            }
         response.sendRedirect("/index.html");
    }

    private static String convertToJson(List<MapMarker> markersToDisplay) {
        Gson gson = new Gson();
        String json = gson.toJson(markersToDisplay);
        return json;
    } 
}
