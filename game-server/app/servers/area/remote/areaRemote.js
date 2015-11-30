/**
 * Created by suyhuai on 2015/11/29.
 */
module.exports = function(app) {
    return new AreaRemote(app);
};

var AreaRemote = function(app) {
    this.app = app;
    this.channelService = app.get('channelService');
};

/**
 * get map information.
 */

AreaRemote.prototype.map = function(username, sid, flag, cb) {
    /*var channelService = this.channelService;
    channelService.pushMessageByUids("onMap","pushed message",uid,{},function(){
        console.log("Message is pushed.");
    });
    if( !! channelService) {
        channelService.add(uid, sid);
    }*/
    cb("test success");
};
