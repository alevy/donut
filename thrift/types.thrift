namespace java edu.washington.cs.cse490h.donut.business
namespace py donut.types

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
