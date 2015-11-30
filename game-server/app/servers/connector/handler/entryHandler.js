module.exports = function(app) {
    return new Handler(app);
};

var Handler = function(app) {
    this.app = app;
};

/**
 * New client entry.
 *
 * @param  {Object}   msg     request message
 * @param  {Object}   session current session object
 * @param  {Function} next    next step callback
 * @return {Void}
 */
Handler.prototype.entry = function(msg, session, next) {
    console.log("connector.entry begins");
    var self = this;
    var username = msg.username;
    var coordinate = msg.coordinate;
    var sessionService = self.app.get("sessionService");

    if (!sessionService.getByUid(username)) {
        session.bind(username);
    }

    next(null, {information: "正在查询中，请稍后..."});
    console.log("connector.entry ends");
};