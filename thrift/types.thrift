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
