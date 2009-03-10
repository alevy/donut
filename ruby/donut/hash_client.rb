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
      key_id = KeyId.new({:id => gen_key(key)})
      node = get_node(key_id)
      socket = Thrift::Socket.new(node.name, node.port)
      client = KeyLocator::Client.new(Thrift::BinaryProtocol.new(socket))
      socket.open if not socket.open?
      result = client.get(key_id)
      socket.close
      if result.exists
        return result.data
      else
        return nil
      end
    end

    def put(key, data)
      key_id = KeyId.new({:id => gen_key(key)})
      node = get_node(key_id)
      socket = Thrift::Socket.new(node.name, node.port)
      client = KeyLocator::Client.new(Thrift::BinaryProtocol.new(socket))
      socket.open if not socket.open?
      val = DonutData.new
      if data
        val.exists = true
        val.data = data
      else
        val.exists = false
      end
      client.put(key_id, val)
      socket.close
    end

    def remove(key)
      put(key, nil)
    end

    def gen_key(key)
      Digest::SHA512.hexdigest(key).hex % 2**KEY_SPACE
    end

    def get_node(key)
      @socket.open if not @socket.open?
      result = @client.findSuccessor(key)
      @socket.close
      return result
    end
  end
end

