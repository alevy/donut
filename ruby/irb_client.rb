#!/usr/bin/env ruby
#
# Copyright 2009 Amit Levy, Jeff Prouty, Rylan Hawkins
# 
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
#      http://www.apache.org/licenses/LICENSE-2.0
#      
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

$:.unshift File.dirname(__FILE__)
require 'irb'
require 'irb/completion'
require 'donut/hash_client'

port = ARGV.pop || 8080 # Last argument
hostname = ARGV.pop || "localhost" # Second to last argument

ARGV.push("--simple-prompt")

@_client = Donut::HashClient.new(hostname, port)

def get(x)
  @_client.get(x)
end

def put(x, y)
  @_client.put(x,y)
end

def remove(x)
  @_client.remove(x)
end

def get_node(x)
  @_client.get_node(KeyId.new({ :id => x }))
end

def get_fingers()
  @_client.get_fingers()
end

puts "======================================================="
puts "Donut/HashTable Interactive Client"
puts "======================================================="
puts "Usage:"
puts "\tget(key)"
puts "\tput(key, value)"
puts "\tremove(key)"
puts "\tget_fingers()"
puts "All arguments are strings.\n"
puts "=====================Happy Hashing====================="

IRB.start
