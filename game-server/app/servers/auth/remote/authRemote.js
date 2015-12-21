module.exports = function(app) {
    return new AuthRemote(app);
}

var AuthRemote = function(app) {
    this.app = app;
}

var authRemote = AuthRemote.prototype;

var mongoose = require('mongoose');

var db = mongoose.createConnection("localhost", "VirtualPlanet");
db.on("error", console.error.bind(console, "数据库连接错误"));
db.once("open", function(){
    console.log("数据库打开了");
});

var userSchema = new mongoose.Schema({
    username: String,
    password: String,
    name: String,
    create_time: Number,
    last_signin_time: Number
})

var UserModel = db.model("User", userSchema);

authRemote.signin = function(username, password, success, fail) {
    var userEntity = new UserModel();
    userEntity.findOne({"username": username, "password": password}, "some select", function(err, user){
        console.log("user when sign in: " + user);
        if (user) {
            console.log("sign in successfully");
            user.last_signin_time = Date.parse(new Date());
            user.update();
            success();
        } else {
            console.log("sign in fails");
            fail();
        }
    })

    return;
}

authRemote.signout = function(session, uid) {
    session.unbind(uid);
}

authRemote.checkDuplicates = function(username, fail) {
    var userEntity = new UserModel();
    userEntity.findOne({"username": username}, "some select", function(err, user){
        if (!user) {
            fail();
        }
    });
}

authRemote.signup = function(username, password, name, success, fail) {
    var userEntity = new UserModel({
        username: username,
        password: password,
        name: name,
        create_time: Date.parse(new Date())
    });
    userEntity.save(function(err){
        if (!err) {
            success();
        } else {
            fail();
        }
    });
}