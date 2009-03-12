namespace java edu.washington.cs.cse490h.donut.service
namespace py donut.service

include "exceptions.thrift"

service HashService {
	
	void put(string key, binary value)
	
	binary get(string key) throws (exceptions.DataNotFoundException e)
	
	void remove(string key)
	
}