/*==============================================================================*/
/* Casper generated Thu Jun 19 2014 15:12:48 GMT-0400 (Eastern Daylight Time) */
/*==============================================================================*/

var x = require('casper').selectXPath;
casper.options.viewportSize = {width: 1517, height: 714};
casper.on('page.error', function(msg, trace) {
    this.echo('Error: ' + msg, 'ERROR');
    for(var i=0; i<trace.length; i++) {
        var step = trace[i];
        this.echo('   ' + step.file + ' (line ' + step.line + ')', 'ERROR');
    }
});
casper.test.begin('Resurrectio test', function(test) {
    casper.start('https://cdecurate-dev.nci.nih.gov/cdecurate/NCICurationServlet?reqType=homePage');
    casper.waitForSelector("#listSearchFor",
        function success() {
            test.assertExists("#listSearchFor");
            this.click("#listSearchFor");
        },
        function fail() {
            test.assertExists("#listSearchFor");
        });
    casper.waitForSelector("#listStatusFilter option:nth-child(8)",
        function success() {
            test.assertExists("#listStatusFilter option:nth-child(8)");
            this.click("#listStatusFilter option:nth-child(8)");
        },
        function fail() {
            test.assertExists("#listStatusFilter option:nth-child(8)");
        });
    casper.waitForSelector("#listMultiContextFilter option:nth-child(2)",
        function success() {
            test.assertExists("#listMultiContextFilter option:nth-child(2)");
            this.click("#listMultiContextFilter option:nth-child(2)");
        },
        function fail() {
            test.assertExists("#listMultiContextFilter option:nth-child(2)");
        });
    casper.waitForSelector("select[name='listMultiContextFilter']",
        function success() {
            this.sendKeys("select[name='listMultiContextFilter']", "\r");
        },
        function fail() {
            test.assertExists("select[name='listMultiContextFilter']");
        });
    casper.waitForSelector("form[name=searchParmsForm] input[name='keyword']",
        function success() {
            test.assertExists("form[name=searchParmsForm] input[name='keyword']");
            this.click("form[name=searchParmsForm] input[name='keyword']");
        },
        function fail() {
            test.assertExists("form[name=searchParmsForm] input[name='keyword']");
        });
    casper.waitForSelector("input[name='keyword']",
        function success() {
            this.sendKeys("input[name='keyword']", "*\r");
        },
        function fail() {
            test.assertExists("input[name='keyword']");
        });
    casper.waitForSelector("form[name=searchParmsForm] input[name='keyword']",
        function success() {
            test.assertExists("form[name=searchParmsForm] input[name='keyword']");
            this.click("form[name=searchParmsForm] input[name='keyword']");
        },
        function fail() {
            test.assertExists("form[name=searchParmsForm] input[name='keyword']");
        });
    casper.waitForSelector("form[name=searchParmsForm] input[name='keyword']",
        function success() {
            test.assertExists("form[name=searchParmsForm] input[name='keyword']");
            this.click("form[name=searchParmsForm] input[name='keyword']");
        },
        function fail() {
            test.assertExists("form[name=searchParmsForm] input[name='keyword']");
        });
    casper.waitForSelector("input[name='keyword']",
        function success() {
            this.sendKeys("input[name='keyword']", "\r");
        },
        function fail() {
            test.assertExists("input[name='keyword']");
        });
    casper.waitForSelector(".xyz:nth-child(14) .stripe:nth-child(2) .stripe",
        function success() {
            test.assertExists(".xyz:nth-child(14) .stripe:nth-child(2) .stripe");
            this.click(".xyz:nth-child(14) .stripe:nth-child(2) .stripe");
        },
        function fail() {
            test.assertExists(".xyz:nth-child(14) .stripe:nth-child(2) .stripe");
        });
    casper.waitForSelector("#objMenu tr:nth-child(3) .cell:nth-child(1) img",
        function success() {
            test.assertExists("#objMenu tr:nth-child(3) .cell:nth-child(1) img");
            this.click("#objMenu tr:nth-child(3) .cell:nth-child(1) img");
        },
        function fail() {
            test.assertExists("#objMenu tr:nth-child(3) .cell:nth-child(1) img");
        });
    casper.waitForSelector("form[name=newDECForm] input[type=button][value='Validate']",
        function success() {
            test.assertExists("form[name=newDECForm] input[type=button][value='Validate']");
            this.click("form[name=newDECForm] input[type=button][value='Validate']");
        },
        function fail() {
            test.assertExists("form[name=newDECForm] input[type=button][value='Validate']");
        });
    casper.waitForSelector("form[name=validateDECForm] input[type=button][value='Submit']",
        function success() {
            test.assertExists("form[name=validateDECForm] input[type=button][value='Submit']");
            this.click("form[name=validateDECForm] input[type=button][value='Submit']");
        },
        function fail() {
            test.assertExists("form[name=validateDECForm] input[type=button][value='Submit']");
        });
    casper.waitForSelector("form[name=validateDECForm] input[type=button][value='Back']",
        function success() {
            test.assertExists("form[name=validateDECForm] input[type=button][value='Back']");
            this.click("form[name=validateDECForm] input[type=button][value='Back']");
        },
        function fail() {
            test.assertExists("form[name=validateDECForm] input[type=button][value='Back']");
        });
    casper.waitForSelector("body tr:nth-child(22) select",
        function success() {
            test.assertExists("body tr:nth-child(22) select");
            this.click("body tr:nth-child(22) select");
        },
        function fail() {
            test.assertExists("body tr:nth-child(22) select");
        });
    casper.waitForSelector("form[name=newDECForm] input[type=button][value='Validate']",
        function success() {
            test.assertExists("form[name=newDECForm] input[type=button][value='Validate']");
            this.click("form[name=newDECForm] input[type=button][value='Validate']");
        },
        function fail() {
            test.assertExists("form[name=newDECForm] input[type=button][value='Validate']");
        });
    casper.waitForSelector("form[name=validateDECForm] input[type=button][value='Back']",
        function success() {
            test.assertExists("form[name=validateDECForm] input[type=button][value='Back']");
            this.click("form[name=validateDECForm] input[type=button][value='Back']");
        },
        function fail() {
            test.assertExists("form[name=validateDECForm] input[type=button][value='Back']");
        });
    casper.waitForSelector("form[name=newDECForm] input[name='rNameConv']",
        function success() {
            test.assertExists("form[name=newDECForm] input[name='rNameConv']");
            this.click("form[name=newDECForm] input[name='rNameConv']");
        },
        function fail() {
            test.assertExists("form[name=newDECForm] input[name='rNameConv']");
        });
    casper.waitForSelector("form[name=newDECForm] input[type=button][value='Validate']",
        function success() {
            test.assertExists("form[name=newDECForm] input[type=button][value='Validate']");
            this.click("form[name=newDECForm] input[type=button][value='Validate']");
        },
        function fail() {
            test.assertExists("form[name=newDECForm] input[type=button][value='Validate']");
        });
    casper.waitForSelector("form[name=validateDECForm] input[type=button][value='Submit']",
        function success() {
            test.assertExists("form[name=validateDECForm] input[type=button][value='Submit']");
            this.click("form[name=validateDECForm] input[type=button][value='Submit']");
        },
        function fail() {
            test.assertExists("form[name=validateDECForm] input[type=button][value='Submit']");
        });
    casper.waitForSelector("form[name=validateDECForm] input[type=button][value='Back']",
        function success() {
            test.assertExists("form[name=validateDECForm] input[type=button][value='Back']");
            this.click("form[name=validateDECForm] input[type=button][value='Back']");
        },
        function fail() {
            test.assertExists("form[name=validateDECForm] input[type=button][value='Back']");
        });
    casper.waitForSelector("form[name=newDECForm] input[name='rNameConv']",
        function success() {
            test.assertExists("form[name=newDECForm] input[name='rNameConv']");
            this.click("form[name=newDECForm] input[name='rNameConv']");
        },
        function fail() {
            test.assertExists("form[name=newDECForm] input[name='rNameConv']");
        });
    casper.waitForSelector("form[name=newDECForm] input[type=button][value='Validate']",
        function success() {
            test.assertExists("form[name=newDECForm] input[type=button][value='Validate']");
            this.click("form[name=newDECForm] input[type=button][value='Validate']");
        },
        function fail() {
            test.assertExists("form[name=newDECForm] input[type=button][value='Validate']");
        });
    casper.waitForSelector("form[name=validateDECForm] input[type=button][value='Submit']",
        function success() {
            test.assertExists("form[name=validateDECForm] input[type=button][value='Submit']");
            this.click("form[name=validateDECForm] input[type=button][value='Submit']");
        },
        function fail() {
            test.assertExists("form[name=validateDECForm] input[type=button][value='Submit']");
        });

    casper.run(function() {test.done();});
});