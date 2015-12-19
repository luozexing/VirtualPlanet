module.exports = function(app) {
    return new AuthRemote(app);
}

var AuthRemote = function(app) {
    this.app = app;
}

var authRemote = AuthRemote.prototype;

authRemote.signup = function() {

}

authRemote.signin = function(username, password, success, fail) {
    var userEntity = new UserModel("localhost", "VirtualPlanet", "User", userSchema);

    userEntity.findOne({"username": username, "password": password}, "some select", function(err, user){
        console.log("user when sign in: " + user);
        if (user) {
            console.log("sign in successfully");
            user.last_signin_time = Date.parse(new Date());
            user.save();
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

var mongoose = require('mongoose');

var userSchema = new mongoose.Schema({
    username: String,
    password: String,
    name: String,
    create_time: Number,
    last_signin_time: Number
})

var UserModel = function(host, dbName, modelName, schema) {
    var db = mongoose.createConnection(host, dbName);

    db.on("error", console.error.bind(console, "连接错误："));
    db.once("open", function(){
        console.log("打开了：");
    })

    var UserModel = db.model("modelName", schema);

    return UserModel;
}