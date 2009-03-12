require File.dirname(__FILE__) + '/../../gen-rb/HashService'
require 'digest/sha2'

module Donut
  class HashClient

    def initialize(hostname = 'localhost', port = 4000)
      @socket = Thrift::Socket.new(hostname, port)
      @client = HashService::Client.new(Thrift::BinaryProtocol.new(@socket))
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
      if not @socket.open?
        @socket.open
      end
      return @client.get(key)
    end

    def put(key, data)
      if not @socket.open?
        @socket.open
      end
      @client.put(key, data)
    end

    def remove(key)
      if not @socket.open?
        @socket.open
      end
      @client.remove(key)
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

