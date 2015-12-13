/**
 * Created by Chris on 12/11/2015.
 *
 * This class is an abstraction of an instance of a username
 * attempting to create a new project.
 */

package projecthub.Project;

import java.util.ArrayList;

public class ProjectCreationAttempt {
   private String name;
   private String description;
   private String ownerUsername;
   private ArrayList<String> participantUsernames = new ArrayList<String>();
   private ArrayList<Integer> resourceIDs = new ArrayList<Integer>();

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getOwnerUsername() {
      return ownerUsername;
   }

   public void setOwnerUsername(String ownerUsername) {
      this.ownerUsername = ownerUsername;
   }
   public void addParticipant(String username){
      this.participantUsernames.add(username);
   }
   public String getParticipant(int index){
      return this.participantUsernames.get(index);
   }
   public int getParticipantCount(){
      return this.participantUsernames.size();
   }
   public void addResourceID(int resourceID){
      this.resourceIDs.add(resourceID);
   }
   public int getResourceID(int index){
      return this.resourceIDs.get(index).intValue();
   }
   public int getResourceIDCount(){
      return this.resourceIDs.size();
   }
}
