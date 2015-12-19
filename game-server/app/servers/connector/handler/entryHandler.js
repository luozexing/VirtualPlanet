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
            error: true,
            message: "already sign in"
        });
        return;
    }

    self.app.rpc.auth.authRemote.signin(session, username, password, function(){
        session.bind(username);
        session.on('closed', onUserLeave(null, self.app));
        next(null, {
            code: 200,
            error: false,
            message: "sign in successfully"
        });
    }, function() {
        next(null, {
            code: 500,
            error: true,
            message: "username or password incorrect"
        });
    });

    console.log("connector.entry ends");
};

Handler.prototype.signup = function(msg, session, next) {
    var self = this;
    var username = msg.username;
    var password = msg.password;
    var name = msg.name;

    if (self.app.rpc.auth.authRemote.checkDuplicates(session, username)) {
        next(null, {
            code: 500,
            error: true,
            message: "this username already exists"
        });
        return;
    }

    if (self.app.rpc.auth.authRemote.signup(session, username, password, name)) {
        next(null, {
            code: 200,
            error: false,
            message: "sign up successfully"
        });
        return;
    } else {
        next(null, {
            code: 500,
            error: true,
            message: "sign up fails"
        });
        return;
    }
}

Handler.prototype.exit = function(msg, session, next) {
    onUserLeave(this.app, session);
    next(null, {
        code: 200,
        error: false,
        message: "sign out successfully"
    })
}

var onUserLeave = function(app, session) {
    console.log("on user leave");
    if (!session || !session.uid) {
        return;
    }
    self.app.rpc.auth.authRemote.signout(session, session.uid);
}