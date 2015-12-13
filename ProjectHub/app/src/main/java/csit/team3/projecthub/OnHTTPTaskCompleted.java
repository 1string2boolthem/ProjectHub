/**
 * Created by Chris on 12/9/2015.
 *
 * This interface helps ensure consistent behavior 
 * throughout the project with regard to the confirmation
 * of APP-DB messages:
 */

package csit.team3.projecthub;

public interface OnHTTPTaskCompleted {
    void onHTTPTaskCompleted(HTTPResult result);
}
