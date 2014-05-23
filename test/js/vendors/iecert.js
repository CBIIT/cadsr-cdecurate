/** courtesy of https://github.com/dalekjs/dalek/issues/91 */

module.exports = function ieCert() {
    var IE = (function () {
        var ret, isTheBrowser,
            actualVersion,
            jscriptMap, jscriptVersion;

        isTheBrowser = false;
        jscriptMap = {
            "5.6": 6,
            "5.7": 7,
            "5.8": 8,
            "9": 9,
            "10": 10
        };
        jscriptVersion = new Function("/*@cc_on return @_jscript_version; @*/")();

        if (jscriptVersion !== undefined) {
            isTheBrowser = true;
            actualVersion = jscriptMap[jscriptVersion];
        }

        ret = {
            isTheBrowser: isTheBrowser,
            actualVersion: actualVersion
        };

        return ret;
    }());

    if (IE.actualVersion === 10 && document.title.search("Certificate") !== -1) {
        var $link = document.getElementById('overridelink');
        var doc;

        if (node.ownerDocument) {
            doc = $link.ownerDocument;
        } else {
            doc = $link;
        }

        var event = doc.createEventObject();
        node.fireEvent("onclick", event);
    }
};