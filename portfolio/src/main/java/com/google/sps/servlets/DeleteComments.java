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
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. */
@WebServlet("/delete-data")
public class DeleteComments extends HttpServlet {
  private final DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    int numCommentsToDelete = 1;
    Query query =
        new Query(DataServlet.COMMENT_TABLE_NAME)
            .addSort(DataServlet.TIMESTAMP_COLUMN_NAME, SortDirection.ASCENDING);
    PreparedQuery results = dataStore.prepare(query);
    String inputNumDelete = request.getParameter("delete-num");
    if (inputNumDelete != null && !inputNumDelete.isEmpty()) {
      try {
        numCommentsToDelete = Integer.parseInt(inputNumDelete);
      } catch (NumberFormatException e) {
        numCommentsToDelete = 1;
      }
    }
    for (Entity entity : results.asList(FetchOptions.Builder.withLimit(numCommentsToDelete))) {
      dataStore.delete(entity.getKey());
    }
    response.sendRedirect("/index.html");
  }
}
