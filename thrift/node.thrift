namespace java edu.washington.edu.cs.cse490h.donut.service
 
struct KeyId {
  1:i64 id
}
 
struct MetaData {
 
}

struct Data {
	1:bool exists = 1,
	2:optional binary data
}
 
service KeyLocator {
  
  /** @param entryId the id we are looking for
   * @return the name of the node that holds the entryId
   *  		 or null if not found.
   */
  string findSuccessor(1:KeyId entryId),
  
  Data get(1:KeyId entryId),
  
  void put(1:KeyId entryId, Data data),
}
