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
