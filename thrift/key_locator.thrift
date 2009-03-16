/*
 * Copyright 2009 Amit Levy, Jeff Prouty, Rylan Hawkins
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *      
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

namespace java edu.washington.cs.cse490h.donut.service
namespace py donut.service

include "types.thrift"
include "exceptions.thrift"
include "constants.thrift"

service KeyLocator {
  
  /** 
   * @param entryId the id we are looking for
   * @return the name of the node that holds the entryId
   *  		 or null if not found.
   */
  types.TNode findSuccessor(types.KeyId entryId),
  
  types.TNode getPredecessor() throws (exceptions.NodeNotFoundException e),
  
  void ping(),
  
  binary get(types.EntryKey key) throws (exceptions.DataNotFoundException e)
  
  void put(types.EntryKey key, binary data) throws (exceptions.NotResponsibleForId e)
  
  void replicatePut(types.EntryKey key, binary data, i32 numReplicas)
  
  void remove(types.EntryKey key) throws (exceptions.NotResponsibleForId e)
  
  void replicateRemove(types.EntryKey key, i32 numReplicas)
  
  set<types.EntryKey> getDataRange(types.KeyId startVal, types.KeyId endVal)
  
  list<types.TNode> notify(types.TNode n)
  
  list<types.TNode> getFingers()
}
