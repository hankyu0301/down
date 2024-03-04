const http = require("http");
const sockjs = require("sockjs");

const sock = sockjs.createServer();

const clients = new Map();

sock.on("connection", function (conn) {
  let myId = "";
  conn.on("data", function (message) {
    console.log(message);

    const { data, type, id } = JSON.parse(message);
    // 6
    switch (type) {
      case "id":
        myId = data;
        console.log(myId, "마이아이디");
        clients.set(data, conn);
        break;
      case "msg":
        clients.forEach((value, key, map) => {
          if (key !== myId) {
            console.log(key, myId);
            value.write(JSON.stringify({ data: data, id: id }));
          }
          console.log(key, myId);
        });
        break;
      default:
        break;
    }
  });
  conn.on("close", function () {
    clients.delete(myId);
  });
});

// 7
const server = http.createServer();
sock.installHandlers(server, { prefix: "/sock" });
server.listen(9999, "localhost");
