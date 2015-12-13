/**
 * Created by Chris on 12/10/2015.
 *
 * This class represents a data structure used to 
 * hold projectID/project name:
 */

package projecthub.Project;

import java.util.*;


public class ProjectList {
   private Map<String, String> projects = new HashMap<String, String>();
   public void add(String projectID, String projectName){
      projects.put(projectID, projectName);
   }
   public Set<String> getKeys(){
      return projects.keySet();
   }
   public String getValue(String key){
      return projects.get(key);
   }
}
