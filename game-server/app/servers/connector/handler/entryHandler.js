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
    
    console.log("connector.entry ends");
};
