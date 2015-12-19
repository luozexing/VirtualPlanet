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
    var password = msg.password;
    var sessionService = self.app.get("sessionService");

    //already sign in
    if (sessionService.getByUid(username)) {
        next(null, {
            code: 500,
            error: true
        });
        return;
    }

    var signinStatus = self.app.rpc.auth.authRemote.signin(username, password);
    if (signinStatus) {
        session.bind(username);
        session.on('closed', onUserLeave(null, self.app));
    } else {
        next(null, {
            code: 500,
            error: true
        });
        return;
    }

    next(null, {
        code: 200,
        error: false
    });
    console.log("connector.entry ends");
};

Handler.prototype.exit = function(msg, session, next) {
    onUserLeave(this.app, session);
    next(null, {
        code: 200
    })
}

var onUserLeave = function(app, session) {
    if (!session || !session.uid) {
        return;
    }
    self.app.rpc.auth.authRemote.signout(session, session.uid);
}