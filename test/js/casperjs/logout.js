/*==============================================================================*/
/* Casper generated Wed Jun 18 2014 09:31:26 GMT-0400 (Eastern Daylight Time) */
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
    casper.start('https://cdecurate.nci.nih.gov/cdecurate/NCICurationServlet?reqType=homePage');
    casper.waitForSelector(".xyz:nth-child(5) > table > tbody > tr:nth-child(3) td:nth-child(2)",
        function success() {
            test.assertExists(".xyz:nth-child(5) > table > tbody > tr:nth-child(3) td:nth-child(2)");
            this.click(".xyz:nth-child(5) > table > tbody > tr:nth-child(3) td:nth-child(2)");
        },
        function fail() {
            test.assertExists(".xyz:nth-child(5) > table > tbody > tr:nth-child(3) td:nth-child(2)");
        });
    casper.waitForSelector(x("//a[normalize-space(text())='Logout']"),
        function success() {
            test.assertExists(x("//a[normalize-space(text())='Logout']"));
            this.click(x("//a[normalize-space(text())='Logout']"));
        },
        function fail() {
            test.assertExists(x("//a[normalize-space(text())='Logout']"));
        });
    casper.wait(1000);
    casper.then(function() {
        this.captureSelector("logout.png", "html");
    });

    casper.run(function() {test.done();});
});