namespace java edu.washington.cs.cse490h.donut.service.thrift

const i32 KEY_SPACE = 64;
const i32 SUCCESSOR_LIST_SIZE = 3;
const i32 FIX_FINGERS_INTERVAL = 10;
const i32 STABILIZE_INTERVAL = 100;
const i32 CHECK_PREDECESSOR_INTERVAL = 150;

struct KeyId {
  i64 id
}

struct EntryKey {
	KeyId id
	string key
}

struct TNode {
  string name
  i32 port
  KeyId nodeId
}

struct DataPair {
	binary data
	i32 replicas
}

exception NodeNotFoundException {

}

exception DataNotFoundException {

}

exception NotResponsibleForId {
	KeyId id
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
  
  binary get(EntryKey key) throws (DataNotFoundException e)
  
  void put(EntryKey key, binary data) throws (NotResponsibleForId e)
  
  void replicatePut(EntryKey key, binary data, i32 numReplicas)
  
  void remove(EntryKey key) throws (NotResponsibleForId e)
  
  void replicateRemove(EntryKey key, i32 numReplicas)
  
  set<EntryKey> getDataRange(KeyId start, KeyId end)
  
  void notify(TNode n)
  
  list<TNode> getFingers()
}
