/**
 * Created by Chris on 12/11/2015.
 *
 * This class represents a list of project resources
 * for use in project management.
 */

package projecthub;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class ResourceList {
   private Map<String, String> resources = new HashMap<String, String>();
   public void add(String resourceID, String resourceName){
      resources.put(resourceID, resourceName);
   }
   public Set<String> getKeys(){
      return resources.keySet();
   }
   public String getValue(String key){
      return resources.get(key);
   }
}
