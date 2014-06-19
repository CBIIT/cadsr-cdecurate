/*==============================================================================*/
/* Casper generated Wed Jun 18 2014 09:18:21 GMT-0400 (Eastern Daylight Time) */
/*==============================================================================*/

"use strict";
var userid;
var password;
var tier;
var x = require('casper').selectXPath;
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
require("utils").dump(casper.cli.args);

casper.echo("Casper CLI passed options:");
require("utils").dump(casper.cli.options);

casper.test.begin('JR1036 test', function(test) {
    // removing default options passed by the Python executable
    casper.cli.drop("cli");
    casper.cli.drop("casper-path");
//    if (casper.cli.args.length === 3 && Object.keys(casper.cli.options).length === 3) {
//        casper.echo("No arg/option passed").exit();
//    } else {
        userid = casper.cli.get("u");
        password = casper.cli.get("p");
        tier = casper.cli.get("t");
//    }

    var host, uri;
    if(typeof tier !== 'undefined') {
        if(tier !== "local") {
            host = "http://" + "cdecurate-" + tier + ".nci.nih.gov";
            uri = "/cdecurate/NCICurationServlet?reqType=logout";
        } else {
            host = "http://localhost:8080";
            uri = "/cdecurate";
        }
    }
    else
    if(typeof tier === 'undefined') {
        tier = '*** production ***';
        host = 'https://' + "cdecurate.nci.nih.gov";
        uri = "/cdecurate/NCICurationServlet?reqType=logout'";
    }
    casper.echo("TARGETED HOST = [" + host + uri + "] tier [" + tier + "]");

//    casper.start('https://cdecurate.nci.nih.gov/cdecurate/NCICurationServlet?reqType=homePage');
    /* Begin of login steps */
    casper.start(host + uri);
    casper.waitForSelector(x("//a[normalize-space(text())='Login']"),
        function success() {
            casper.captureSelector("v.png", "html");
            test.assertExists(x("//a[normalize-space(text())='Login']"), "Login STEP 1");
            this.click(x("//a[normalize-space(text())='Login']"));
        },
        function fail() {
            test.assertExists(x("//a[normalize-space(text())='Login']"));
        });
    casper.waitForSelector("form[name=LoginForm] input[name='Username']",
        function success() {
            test.assertExists("form[name=LoginForm] input[name='Username']", "Login STEP 2");
            this.click("form[name=LoginForm] input[name='Username']");
        },
        function fail() {
            test.assertExists("form[name=LoginForm] input[name='Username']");
        });
    casper.waitForSelector("form[name=LoginForm] input[name='Username']",
        function success() {
            test.assertExists("form[name=LoginForm] input[name='Username']", "Login STEP 3");
            this.click("form[name=LoginForm] input[name='Username']");
        },
        function fail() {
            test.assertExists("form[name=LoginForm] input[name='Username']");
        });
    casper.waitForSelector("input[name='Username']",
        function success() {
            this.sendKeys("input[name='Username']", userid);
        },
        function fail() {
            test.assertExists("input[name='Username']");
        });
    casper.waitForSelector("input[name='Password']",
        function success() {
            this.sendKeys("input[name='Password']", password + "\r");
        },
        function fail() {
            test.assertExists("input[name='Password']");
        });
    casper.waitForSelector("form[name=LoginForm] input[type=submit][value='Login']",
        function success() {
            test.assertExists("form[name=LoginForm] input[type=submit][value='Login']", "Login STEP 4");
            this.click("form[name=LoginForm] input[type=submit][value='Login']");
        },
        function fail() {
            test.assertExists("form[name=LoginForm] input[type=submit][value='Login']");
        });
    /* End of login steps */

//    casper.wait(1000);
//    casper.echo("clicking on the menu ...");
    casper
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
            this.click(x('//*[@id="createMenu"]/dl[2]/dt[2]'));
        })
    ;

//    casper.echo("performing real test ...");
    casper.waitForSelector("form[name=newDECForm] input[name='rNameConv']",
        function success() {
            casper.captureSelector("v.png", "html");
            test.assertExists("form[name=newDECForm] input[name='rNameConv']", "Clicking system generated button of 'Select Short Name Naming Standard' radio button ...");
            this.click("form[name=newDECForm] input[name='rNameConv']");
        },
        function fail() {
            test.assertExists("form[name=newDECForm] input[name='rNameConv']");
        });
    casper.waitForSelector("form[name=newDECForm] input[type=button][value='Validate']",
        function success() {
            test.assertExists("form[name=newDECForm] input[type=button][value='Validate']", "Clicking validate button ...");
            this.click("form[name=newDECForm] input[type=button][value='Validate']");
        },
        function fail() {
            test.assertExists("form[name=newDECForm] input[type=button][value='Validate']");
        });
    casper.waitForSelector("form[name=validateDECForm] input[type=button][value='Back']",
        function success() {
            this.captureSelector("1036.png", "html");
            var text = x("//form/table[2]/tbody/tr[5]/td[2]/text()");
//            if(typeof text !== 'undefined') {
//                var text = text.trim();
//            }
            casper.echo("Target text = [" + text + "]")
            test.assertTextDoesntExist("(Generated by the System)", text, "Asserting fix is good");
        },
        function fail() {
            this.captureSelector("1036.png", "html");
        });
    casper.waitForSelector("form[name=validateDECForm] input[type=button][value='Back']",
        function success() {
            test.assertExists("form[name=validateDECForm] input[type=button][value='Back']", "Clicking back button ...");
            this.click("form[name=validateDECForm] input[type=button][value='Back']");
        },
        function fail() {
            test.assertExists("form[name=validateDECForm] input[type=button][value='Back']");
        });

    casper.run(function() {
        test.done();
        console.log('TESTED URL [' +this.getCurrentUrl()+']','info');
        this.captureSelector("done.png", "html");
//        this.exit();
    });
});