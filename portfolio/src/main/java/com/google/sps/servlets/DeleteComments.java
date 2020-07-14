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
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.servlets.DataServlet;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Servlet that returns some example content. */
@WebServlet("/delete-data")
public class DeleteComments extends HttpServlet {
  private final DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    int numDeleteComments = 1;
    Query query = new Query(DataServlet.COMMENT_TABLE_NAME)
                            .addSort(DataServlet.TIMESTAMP_COLUMN_NAME, SortDirection.ASCENDING);
    PreparedQuery results = dataStore.prepare(query);
    String inputNumDelete = request.getParameter("delete-num");
    if (inputNumDelete != null && !inputNumDelete.isEmpty()) {
        numDeleteComments = Integer.parseInt(inputNumDelete);
    }
    for (Entity entity : results.asList(FetchOptions.Builder.withLimit(numDeleteComments))) {
        dataStore.delete(entity.getKey());
    }
    response.sendRedirect("/index.html");
  }
}
