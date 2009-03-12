namespace java edu.washington.cs.cse490h.donut.service
namespace py donut.service

include "types.thrift"

exception NodeNotFoundException {
 
}
 
exception DataNotFoundException {
 
}
 
exception NotResponsibleForId {
  types.KeyId id
}