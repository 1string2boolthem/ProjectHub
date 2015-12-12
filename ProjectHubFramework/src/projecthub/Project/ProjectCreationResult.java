package projecthub.Project;

import java.util.ArrayList;

/**
 * Created by Chris on 12/11/2015.
 */
public class ProjectCreationResult {
   private boolean success;
   private int projectID;
   private ArrayList<String> errors;
   public ProjectCreationResult(boolean success, int projectID){
      this.success = success;
      this.projectID = projectID;
      this.errors = new ArrayList<String>();
   }
   public void addError(String error){
      this.errors.add(error);
   }
   public int getErrorCount(){
      return this.errors.size();
   }
   public String getError(int index){
      return this.errors.get(index);
   }
   public boolean wasSuccessful() {
      return success;
   }

   public int getProjectID() {
      return projectID;
   }
}
