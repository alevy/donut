require File.dirname(__FILE__) + '/../../gen-rb/KeyLocator'
require File.dirname(__FILE__) + '/../../gen-rb/node_types'
require File.dirname(__FILE__) + '/../../gen-rb/node_constants'
require 'digest/sha2'

module Donut
  class HashClient

    def initialize(hostname = 'localhost', port = 8080)
      @socket = Thrift::Socket.new(hostname, port)
      @client = KeyLocator::Client.new(Thrift::BinaryProtocol.new(@socket))
    end

    def get_fingers
      @socket.open
      fingers = @client.getFingers
      @socket.close
      return fingers
    end

    def get_predecessor
      @socket.open
      predecessor = @client.getPredecessor
      @socket.close
      return predecessor
    end


    def get(key)
      key_id = gen_key(key)
      node = get_node(key_id)
      socket = Thrift::Socket.new(node.name, node.port)
      client = KeyLocator::Client.new(Thrift::BinaryProtocol.new(socket))
      socket.open if not socket.open?
      begin
        result = client.get(key_id)
      rescue
        result = nil
      end
      socket.close
      return result
    end

    def put(key, data)
      key_id = gen_key(key)
      node = get_node(key_id)
      socket = Thrift::Socket.new(node.name, node.port)
      client = KeyLocator::Client.new(Thrift::BinaryProtocol.new(socket))
      socket.open if not socket.open?
      client.put(key_id, data)
      socket.close
    end

    def remove(key)
      key_id = gen_key(key)
      node = get_node(key_id)
      socket = Thrift::Socket.new(node.name, node.port)
      client = KeyLocator::Client.new(Thrift::BinaryProtocol.new(socket))
      socket.open if not socket.open?
      client.remove(key_id)
      socket.close
    end

    def gen_key(key)
      result = EntryKey.new({:key => key, :id => KeyId.new({
        :id => Digest::SHA512.hexdigest(key).hex % 2**KEY_SPACE})})
      return result
    end

    def get_node(key)
      @socket.open if not @socket.open?
      result = @client.findSuccessor(key)
      @socket.close
      return result
    end
  end
end

