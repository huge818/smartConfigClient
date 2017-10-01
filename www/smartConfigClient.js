var exec = require('cordova/exec');

exports.initConfig = function(arg0, arg1, success, error) {
    exec(success, error, "smartConfigClient", "initConfig", [arg0,arg1]);
};

exports.startSendAndListen = function(arg0, arg1, arg2, success, error) {
    exec(success, error, "smartConfigClient", "startSendAndListen", [arg0,arg1,arg2]);
};

exports.stopListen = function(success, error) {
    exec(success, error, "smartConfigClient", "stopListen", []);
};
