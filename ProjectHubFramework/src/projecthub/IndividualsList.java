/**
 * Created by Chris on 12/11/2015.
 *
 * This project represents a data structure used to hold
 * user ("individual") ID/name pairs:
 */

package projecthub;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class IndividualsList {
   private Map<String, String> individuals = new HashMap<String, String>();
   public void add(String individualID, String individualName){
      individuals.put(individualID, individualName);
   }
   public Set<String> getKeys(){
      return individuals.keySet();
   }
   public String getValue(String key){
      return individuals.get(key);
   }
}
