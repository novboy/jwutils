import threading
import socket
from collections import deque

class recvThread (threading.Thread):
    def __init__(self, socket, callback, callback_err=None):
        threading.Thread.__init__(self)
        self.socket = socket
        self.callback = callback
        self.callback_err = callback_err or callback
        self.buffer = ""
        self.queue = deque() # Thankfully these are apparently thread-safe... apparently.
        self.active = False

    def pop(self):
        if len(self.queue) > 0:
            return self.queue.popleft()
        else:
            return None
    
    def run(self):
        self.active = True
        while self.active:
            try:
                buffer = self.socket.recv(8192)
            except:
                self.active = False
                #raise
            if buffer == '':
                if self.callback_err is not None:
                    self.callback_err()
                break
            self.buffer += buffer
            while "\r" in self.buffer:
                line, _, restbuffer = self.buffer.partition("\r")
                if restbuffer[0] == '\n':
                    restbuffer = restbuffer[1:]
                self.buffer = restbuffer
                self.queue.append(line)
                self.callback()

class sendThread(threading.Thread):
    def __init__(self, socket):
        threading.Thread.__init__(self)
        self.socket = socket
        self.queue = deque() # Thankfully these are apparently thread-safe... apparently.
        self.active = False
    
    def append(self, line):
        self.queue.append(line)
    
    def run(self):
        self.active = True
        while self.active:
            if self.queue:
                line = str(self.queue.popleft()) + '\r\n'
                try:
                    sent=self.socket.send(line)
                except:
                    self.active = False
                    #raise
                if sent > 0:
                    self.queue.appendleft(line[sent:])

class notificationServer (object):
    def __init__(self, msnconnection, addr, user):
        self.msnconnection = msnconnection
        self.addr = addr
        self.username = user
        self.socket = None
        
        self.__recv = None
        self.__send = None
        self.__trid = 0
        return
    
    def conn_close(self):
        return False
    
    def connect(self):
        self.socket = socket.socket()
        self.__recv = recvThread(self.socket, self.recv_callback, self.conn_close)
        self.__send = sendThread(self.socket)
        
        self._send(True, 'VER', 'MSNP8')
        
        self.socket.connect(self.addr)
        self.__recv.start()
        self.__send.start()
        return True
    
    def recv_callback(self):
        line = self._recv()
        if not line:
            return
        words = line.split(' ')
        if words[0] == 'VER':
            versions = words[2:]
            assert '0' not in versions
            assert 'MSNP8' in versions
            
        print 'MSN Recv', line

    def _send(self, trid=False, *args):
        "Send a protocol line to the remote server."
        if trid:
            self.__trid += 1
            raw_line = '%s %d %s' % (args[0], self.__trid, ' '.join(args[1:]))
        else:
            raw_line = ' '.join(args[1:])
        print 'MSN Send', raw_line
        self.__send.queue.append(raw_line)
        return
    
    def _recv(self):
        "Receive a protocol line from the remote server."
        return self.__recv.queue.popleft()
    
    def disconnect(self):
        "Disconects you from the remote server."
        return