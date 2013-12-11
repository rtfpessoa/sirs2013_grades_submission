(function ($, window) {
    var Utils;

    Utils = (function () {

        function Utils() {
        }

        Utils.prototype.post = function (url, variable, callback, errorCallback) {
            if (!errorCallback)
                errorCallback = function (e, m) {
                };
            $.ajax({
                url: url,
                type: 'POST',
                data: variable,
                success: function (response) {
                    callback(response);
                },
                error: function (e, m) {
                    errorCallback(e, m);
                },
                complete: function () {
                }
            });
        };

        var instance;
        return {
            getInstance: function () {
                if (instance == null) {
                    instance = new Utils();
                    // Hide the constructor so the returned objected can't be new'd...
                    instance.constructor = null;
                }
                return instance;
            }
        };
    })();

    return window.Utils = Utils;

})(jQuery, window);
