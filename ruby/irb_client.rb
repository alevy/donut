#!/usr/bin/env ruby

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

puts "======================================================="
puts "Donut/HashTable Interactive Client"
puts "======================================================="
puts "Usage:"
puts "\tget(key)"
puts "\tput(key, value)"
puts "\tremove(key)"
puts "All arguments are strings.\n"
puts "=====================Happy Hashing====================="

IRB.start
