var dispatcher = require("../../../utils/dispatcher.js");

module.exports = function(app) {
    return new Handler(app);
}

var Handler = function(app) {
    this.app = app;
}

var handler = Handler.prototype;

handler.queryEntry = function(msg, session, next) {
    // get uid (username)
    var username = msg.username;
    if (!username) {
        next(null,{
            code: 500
        });
        return;
    }

    // get all connectors
    var connectors = this.app.getServersByType("connector");
    if (!connectors || connectors.length == 0) {
        next(null, {
            code: 500
        });
        return;
    }

    // choose connector
    var connector = dispatcher.dispatch(username, connectors);
    next(null, {
        code: 200,
        host: connector.host,
        port: connector.clientPort
    });
    return;
}