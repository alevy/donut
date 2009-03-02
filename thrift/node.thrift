namespace java edu.washington.edu.cs.cse490h.donut.service

const i32 KEY_SPACE = 64;
const i32 SUCCESSOR_LIST_SIZE = 3;

const i32 FIX_FINGERS_INTERVAL = 10;
const i32 STABILIZE_INTERVAL = 100;
const i32 CHECK_PREDECESSOR_INTERVAL = 150;

struct KeyId {
  i64 id
}

struct TNode {
  string name
  i32 port
  KeyId nodeId
}

exception NodeNotFoundException {

}

exception DataNotFoundException {

}

service KeyLocator {
  
  /** 
   * @param entryId the id we are looking for
   * @return the name of the node that holds the entryId
   *  		 or null if not found.
   */
  TNode findSuccessor(1:KeyId entryId),
  
  TNode getPredecessor() throws (NodeNotFoundException e),
  
  void ping(),
  
  binary get(KeyId entryId) throws (DataNotFoundException e),
  
  void put(KeyId entryId, binary data, i32 numReplicas),
  
  void remove(KeyId entryId, i32 numReplicas),
  
  void notify(TNode n)
  
  list<TNode> getFingers()
}
