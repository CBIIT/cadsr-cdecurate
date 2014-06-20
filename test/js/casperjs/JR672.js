/*==============================================================================*/
/* Casper generated Thu Jun 19 2014 15:12:48 GMT-0400 (Eastern Daylight Time) */
/*==============================================================================*/

var host, uri;
var user = {
    id: '',
    password: '',
    tier: ''
};
var caspermod = require('casper');
var utils = require("utils");
var cdecurate = require("./common.js");

cdecurate.init(caspermod, utils, user);

casper.test.begin('Resurrectio test', function(test) {
    casper
        .then(function() {
            cdecurate.connect(caspermod, user);
        })
        .then(function() {
            this.capture('login.png', {
                top: 0,
                left: 0,
                width: casper.options.viewportSize.width,
                height: casper.options.viewportSize.height
            });
            //===click on the "Create" menu
            //this.click(x("/html/body/div[3]/table/tbody/tr[1]/td[2]/table/tbody/tr/td[2]"));  //===#deprecated - only for production as at 6/19/2014
        })
        .then(function() {
            //===click on the "Data Element Concept" menu item
//            this.click(x('//*[@id="createMenu"]/dl[2]/dt[2]'));
        })
    ;

//    casper.start('https://cdecurate-dev.nci.nih.gov/cdecurate/NCICurationServlet?reqType=homePage');
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
            this.capture('v.png');
            this.sendKeys("input[name='keyword']", "*\r");
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