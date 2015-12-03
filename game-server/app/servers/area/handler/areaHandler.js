module.exports = function(app) {
    return new Handler(app);
};

var Handler = function(app) {
    this.app = app;
}

var handler = Handler.prototype;

handler.map = function(msg,session,next){
    var coordinate = msg.coordinate;
    var result = "done!";
    next(null,{
        coordinate:coordinate,
        result:result
    });
}