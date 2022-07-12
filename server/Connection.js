const blacklist = require('./AutoBlock');

class Connection {
    request; resposne; 
    ip; token; url;
    headers; body;

    constructor(request, response) {
        this.request = request;
        this.response = response;
        this.ip = this.getIpAddress(request);
        this.token = request.headers.key;
        this.headers = request.headers;
        this.body = request.body;
        this.url = request.originalUrl;
    }

    getIpAddress() {
        var ip = this.request.connection.remoteAddress;
        ip.replace("::ffff:192.168.0.", "localhost ");
        if (ip.startsWith("::ffff:")) ip = ip.slice(7);
        return ip;
    }

    send(object) {
        this.response.json(object);
    }

    internalError() {
        this.response.statusCode = 500;
        this.response.send("INTERNAL ERROR");
    }

    unauthorize() {
        this.response.statusCode = 403;
        this.response.send("UNAUTHORIZED");
        
        blacklist.addIntoBlacklist(this.getIpAddress());
    }
}

module.exports = Connection;