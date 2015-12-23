module.exports = function(app) {
    return new AuthHandler(app);
}

var AuthHandler = function(app) {
    this.app = app;
}

var authHandler = AuthHandler.prototype;

//mongo db connection
var mongoose = require('mongoose');

var db = mongoose.createConnection("localhost", "VirtualPlanet");
db.on("error", console.error.bind(console, "数据库连接错误"));
db.once("open", function(){
    console.log("数据库打开了");
});

var userSchema = new mongoose.Schema({
    username: String,
    password: String,
    gamename: String,
    create_time: Number,
    last_signin_time: Number
})

var UserModel = db.model("User", userSchema);

//interfaces
authHandler.signin = function(msg, session, next) {
    console.log("auth handler sign in begins");
    var self = this;
    var username = msg.username;
    var password = msg.password;

    //already sign in
    if (session.uid) {
        session.unbind(session.uid);
    }

    //sign in
    UserModel.findOne({"username": username, "password": password}, "some select", function(err, user){
        console.log("user when sign in: " + user);
        if (user) {
            console.log("sign in successfully");
            user.last_signin_time = Date.parse(new Date());
            user.update();

            session.bind(username);
            //session.on('closed', onUserLeave.bind(null, self.app));

            next(null, {
                code: 200,
                error: false,
                message: "sign in successfully"
            });
        } else {
            console.log("sign in fails, err: " + err);
            next(null, {
                code: 500,
                error: true,
                message: "username or password incorrect"
            });
        }
        return;
    });

    console.log("auth handler sign in ends");
    return;
};

authHandler.signup = function(msg, session, next) {
    var username = msg.username;
    var password = msg.password;
    var gamename = msg.gamename;

    //check duplicates
    UserModel.findOne({"username": username}, "some select", function(err, user){
        console.log("check duplicates, err: " + err + ", user: " + user);
        if (user) {
            next(null, {
                code: 500,
                error: true,
                message: "this username already exists"
            });
        } else {
            var userEntity = new UserModel({
                username: username,
                password: password,
                gamename: gamename,
                create_time: Date.parse(new Date())
            });
            userEntity.save(function(err){
                console.log("sign up, err: " + err);
                if (!err) {
                    console.log("sign up success");
                    next(null, {
                        code: 200,
                        error: false,
                        message: "sign up successfully"
                    });
                } else {
                    console.log("sign up fail");
                    next(null, {
                        code: 500,
                        error: true,
                        message: "sign up fails"
                    });
                }
                return;
            });
        }
        return;
    });

    return;
}

authHandler.signout = function(msg, session, next) {
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

    session.unbind(session.uid);
    return;
}