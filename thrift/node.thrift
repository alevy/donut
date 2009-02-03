namespace java edu.washington.edu.cs.cse490h.donut.service

struct KeyId {
  1:i64 id
}

struct MetaData {

}

service KeyLocator {
  async void lookup(1:KeyId id, 2:string caller),
}

service LocatorCallback {
  void lookup(1:KeyId id, 2:MetaData data),
}
