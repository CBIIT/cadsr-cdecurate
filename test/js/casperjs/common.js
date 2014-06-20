'use strict';
exports.init = function(caspermod, utils, user) {
    var x = caspermod.selectXPath;
    casper.options.viewportSize = {width: 1366, height: 643};
    casper.on('page.error', function(msg, trace) {
        this.echo('Error: ' + msg, 'ERROR');
        for(var i=0; i<trace.length; i++) {
            var step = trace[i];
            this.echo('   ' + step.file + ' (line ' + step.line + ')', 'ERROR');
        }
    });
    //===c.f. http://casperjs.readthedocs.org/en/latest/cli.html
    casper.echo("Casper CLI passed args:");
    utils.dump(casper.cli.args);

    casper.echo("Casper CLI passed options:");
    utils.dump(casper.cli.options);

    // removing default options passed by the Python executable
    casper.cli.drop("cli");
    casper.cli.drop("casper-path");
//    if (casper.cli.args.length === 3 && Object.keys(casper.cli.options).length === 3) {
//        casper.echo("No arg/option passed").exit();
//    } else {
    user.id = casper.cli.get("u");
    user.password = casper.cli.get("p");
    user.tier = casper.cli.get("t");
//    }

    var host, uri;
    if(typeof user.tier !== 'undefined') {
        if(user.tier !== "local") {
            host = "http://" + "cdecurate-" + user.tier + ".nci.nih.gov";
            uri = "/cdecurate/NCICurationServlet?reqType=logout";
        } else {
            host = "http://localhost:8080";
            uri = "/cdecurate";
        }
    }
    else
    if(typeof user.tier === 'undefined') {
        user.tier = '*** production ***';
        host = 'https://' + "cdecurate.nci.nih.gov";
        uri = "/cdecurate/NCICurationServlet?reqType=logout'";
    }
    casper.start(host + uri);
    casper.echo("TARGETED HOST = [" + host + uri + "] tier [" + user.tier + "]");
}

exports.connect = function(caspermod, user) {
    var x = caspermod.selectXPath;
    /* Begin of login steps */
    casper.waitForSelector(x("//a[normalize-space(text())='Login']"),
        function success() {
            casper.captureSelector("v.png", "html");
            casper.test.assertExists(x("//a[normalize-space(text())='Login']"), "Login STEP 1");
            this.click(x("//a[normalize-space(text())='Login']"));
        },
        function fail() {
            casper.test.assertExists(x("//a[normalize-space(text())='Login']"));
        });
    casper.waitForSelector("form[name=LoginForm] input[name='Username']",
        function success() {
            casper.test.assertExists("form[name=LoginForm] input[name='Username']", "Login STEP 2");
            this.click("form[name=LoginForm] input[name='Username']");
        },
        function fail() {
            casper.test.assertExists("form[name=LoginForm] input[name='Username']");
        });
    casper.waitForSelector("form[name=LoginForm] input[name='Username']",
        function success() {
            casper.test.assertExists("form[name=LoginForm] input[name='Username']", "Login STEP 3");
            this.click("form[name=LoginForm] input[name='Username']");
        },
        function fail() {
            casper.test.assertExists("form[name=LoginForm] input[name='Username']");
        });
    casper.waitForSelector("input[name='Username']",
        function success() {
            this.sendKeys("input[name='Username']", user.id);
        },
        function fail() {
            casper.test.assertExists("input[name='Username']");
        });
    casper.waitForSelector("input[name='Password']",
        function success() {
            this.sendKeys("input[name='Password']", user.password + "\r");
        },
        function fail() {
            casper.test.assertExists("input[name='Password']");
        });
    casper.waitForSelector("form[name=LoginForm] input[type=submit][value='Login']",
        function success() {
            casper.test.assertExists("form[name=LoginForm] input[type=submit][value='Login']", "Login STEP 4 Logged in!");
            this.click("form[name=LoginForm] input[type=submit][value='Login']");
        },
        function fail() {
            casper.test.assertExists("form[name=LoginForm] input[type=submit][value='Login']");
        });
    /* End of login steps */
}