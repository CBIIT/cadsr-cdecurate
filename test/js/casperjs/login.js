/*==============================================================================*/
/* Casper generated Wed Jun 18 2014 09:33:52 GMT-0400 (Eastern Daylight Time) */
/*==============================================================================*/

var x = require('casper').selectXPath;
casper.options.viewportSize = {width: 1366, height: 643};
casper.on('page.error', function(msg, trace) {
    this.echo('Error: ' + msg, 'ERROR');
    for(var i=0; i<trace.length; i++) {
        var step = trace[i];
        this.echo('   ' + step.file + ' (line ' + step.line + ')', 'ERROR');
    }
});
casper.test.begin('Resurrectio test', function(test) {
    casper.start('https://cdecurate.nci.nih.gov/cdecurate/NCICurationServlet?reqType=logout');
    casper.waitForSelector(".xyz:nth-child(5) > table > tbody > tr:nth-child(3) td:nth-child(2)",
        function success() {
            test.assertExists(".xyz:nth-child(5) > table > tbody > tr:nth-child(3) td:nth-child(2)");
            this.click(".xyz:nth-child(5) > table > tbody > tr:nth-child(3) td:nth-child(2)");
        },
        function fail() {
            test.assertExists(".xyz:nth-child(5) > table > tbody > tr:nth-child(3) td:nth-child(2)");
        });
    casper.waitForSelector(x("//a[normalize-space(text())='Login']"),
        function success() {
            test.assertExists(x("//a[normalize-space(text())='Login']"));
            this.click(x("//a[normalize-space(text())='Login']"));
        },
        function fail() {
            test.assertExists(x("//a[normalize-space(text())='Login']"));
        });
    casper.waitForSelector("form[name=LoginForm] input[name='Username']",
        function success() {
            test.assertExists("form[name=LoginForm] input[name='Username']");
            this.click("form[name=LoginForm] input[name='Username']");
        },
        function fail() {
            test.assertExists("form[name=LoginForm] input[name='Username']");
        });
    casper.waitForSelector("form[name=LoginForm] input[name='Username']",
        function success() {
            test.assertExists("form[name=LoginForm] input[name='Username']");
            this.click("form[name=LoginForm] input[name='Username']");
        },
        function fail() {
            test.assertExists("form[name=LoginForm] input[name='Username']");
        });
    casper.waitForSelector("input[name='Username']",
        function success() {
            this.sendKeys("input[name='Username']", "tanj");
        },
        function fail() {
            test.assertExists("input[name='Username']");
        });
    casper.waitForSelector("input[name='Password']",
        function success() {
            this.sendKeys("input[name='Password']", "Te$t1247\r");
        },
        function fail() {
            test.assertExists("input[name='Password']");
        });
    casper.waitForSelector("form[name=LoginForm] input[type=submit][value='Login']",
        function success() {
            test.assertExists("form[name=LoginForm] input[type=submit][value='Login']");
            this.click("form[name=LoginForm] input[type=submit][value='Login']");
        },
        function fail() {
            test.assertExists("form[name=LoginForm] input[type=submit][value='Login']");
        });
    /* submit form */
    casper.wait(1000);
    casper.then(function() {
        this.captureSelector("login.png", "html");
    });

    casper.run(function() {test.done();});
});