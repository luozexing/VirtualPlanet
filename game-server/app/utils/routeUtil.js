var dispathcer = require("./dispatcher.js");

module.exports.area = function(session, msg, app, next) {
    var areaServers = app.getServersByType("area");

    if (!areaServers || areaServers.length == 0) {
        next(new Error("can't find area servers."));
        return;
    }

    // choose area server according to the coordinate of this user
    var areaServer = areaServers[0];

    return areaServer;
}