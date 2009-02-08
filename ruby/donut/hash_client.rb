$:.unshift File.dirname(File.dirname(__FILE__))
require '../../gen-rb/KeyLocator'
require '../../gen-rb/node_types'
require 'digest/sha2'

module Donut
  class HashClient

    def initialize(hostname = 'localhost', port = 8080)
      @socket = Thrift::Socket.new(hostname, port)
      @client = KeyLocator::Client.new(Thrift::BinaryProtocol.new(@socket))
    end

    def get(key)
      key_id = KeyId.new({:id => Digest::SHA512.hexdigest(key).hex % 2**64})
      node = get_node(key_id)
      socket = Thrift::Socket.new(node.name, 8080)
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
      key_id = KeyId.new({:id => Digest::SHA512.hexdigest(key).hex % 2**64})
      node = get_node(key_id)
      socket = Thrift::Socket.new(node.name, 8080)
      client = KeyLocator::Client.new(Thrift::BinaryProtocol.new(socket))
      socket.open if not socket.open?
      client.put(key_id, DonutData.new({:exists => true, :data => data}))
      socket.close
    end
 
    private

    def get_node(key)
      @socket.open if not @socket.open?
      result = @client.findSuccessor(key)
      @socket.close
      return result
    end
  end
 
end

