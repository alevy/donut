namespace java edu.washington.edu.cs.cse490h.donut.service
 
struct KeyId {
  1:i64 id
}
 
struct MetaData {
 
}
 
service KeyLocator {
  
  /** @param entryId the id we are looking for
   * @return the name of the node that holds the entryId
   *  		 or null if not found.
   */
  string findSuccessor(1:KeyId entryId),
}
