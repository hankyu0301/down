"use client";

import React from "react";
import { useRef, useEffect, useState, createContext, useContext } from "react";
import SockJs from "sockjs-client";

type SocketContextType = {
  socket: any | null;
  isConnected: boolean;
};

const SocketContext = createContext<SocketContextType>({
  socket: null,
  isConnected: false,
});

export const useSocket = () => {
  return useContext(SocketContext);
};

const SocketProvider = ({ children }: { children: React.ReactNode }) => {
  const [socket, setSocket] = useState(null);
  const [isConnected, setIsConnected] = useState(false);
  const sockJs = useRef(null);

  useEffect(() => {
    sockJs.current = new SockJs("http://localhost:9999/sock");
    setSocket(sockJs.current);
  }, []);

  useEffect(() => {
    if (!sockJs.current) return;
    sockJs.current.onopen = () => {
      console.log("open", sockJs.current.protocol);
      setIsConnected(true);
    };

    sockJs.current.onclose = () => {
      console.log("close");
      setIsConnected(false);
    };
  });
  return (
    <SocketContext.Provider value={{ socket, isConnected }}>
      {children}
    </SocketContext.Provider>
  );
};

export default SocketProvider;
