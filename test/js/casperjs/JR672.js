/*==============================================================================*/
/* Casper generated Wed Jun 18 2014 14:23:07 GMT-0400 (Eastern Daylight Time) */
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
casper.test.begin('JR672 test', function(test) {
    casper.start('https://cdecurate.nci.nih.gov/cdecurate/NCICurationServlet?reqType=login');
    casper.waitForSelector(".xyz:nth-child(5) > table > tbody > tr > td > div > select",
        function success() {
            test.assertExists(".xyz:nth-child(5) > table > tbody > tr > td > div > select");
            this.click(".xyz:nth-child(5) > table > tbody > tr > td > div > select");
        },
        function fail() {
            test.assertExists(".xyz:nth-child(5) > table > tbody > tr > td > div > select");
        });
    casper.waitForSelector(".xyz:nth-child(5) > table > tbody > tr > td > div > select > option",
        function success() {
            test.assertExists(".xyz:nth-child(5) > table > tbody > tr > td > div > select > option");
            this.click(".xyz:nth-child(5) > table > tbody > tr > td > div > select > option");
        },
        function fail() {
            test.assertExists(".xyz:nth-child(5) > table > tbody > tr > td > div > select > option");
        });
    casper.waitForSelector("select[name='listMultiContextFilter']",
        function success() {
            this.sendKeys("select[name='listMultiContextFilter']", "\r");
        },
        function fail() {
            test.assertExists("select[name='listMultiContextFilter']");
        });
    casper.waitForSelector(".xyz:nth-child(5) .stripe:nth-child(2) .stripe",
        function success() {
            test.assertExists(".xyz:nth-child(5) .stripe:nth-child(2) .stripe");
            this.click(".xyz:nth-child(5) .stripe:nth-child(2) .stripe");
        },
        function fail() {
            test.assertExists(".xyz:nth-child(5) .stripe:nth-child(2) .stripe");
        });
    casper.waitForSelector("#objMenu tr:nth-child(3) .cell:nth-child(1) img",
        function success() {
            test.assertExists("#objMenu tr:nth-child(3) .cell:nth-child(1) img");
            this.click("#objMenu tr:nth-child(3) .cell:nth-child(1) img");
        },
        function fail() {
            test.assertExists("#objMenu tr:nth-child(3) .cell:nth-child(1) img");
        });
    casper.waitForSelector("body tr:nth-child(21) select",
        function success() {
            test.assertExists("body tr:nth-child(21) select");
            this.click("body tr:nth-child(21) select");
        },
        function fail() {
            test.assertExists("body tr:nth-child(21) select");
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
    casper.waitForSelector(".xyz:nth-child(5) .stripe:nth-child(2) .stripe",
        function success() {
            test.assertExists(".xyz:nth-child(5) .stripe:nth-child(2) .stripe");
            this.click(".xyz:nth-child(5) .stripe:nth-child(2) .stripe");
        },
        function fail() {
            test.assertExists(".xyz:nth-child(5) .stripe:nth-child(2) .stripe");
        });
    casper.waitForSelector("#objMenu tr:nth-child(3) .cell:nth-child(1)",
        function success() {
            test.assertExists("#objMenu tr:nth-child(3) .cell:nth-child(1)");
            this.click("#objMenu tr:nth-child(3) .cell:nth-child(1)");
        },
        function fail() {
            test.assertExists("#objMenu tr:nth-child(3) .cell:nth-child(1)");
        });
    casper.waitForSelector("body table:nth-child(2) > tbody > tr:nth-child(3) td:nth-child(2)",
        function success() {
            test.assertExists("body table:nth-child(2) > tbody > tr:nth-child(3) td:nth-child(2)");
            this.click("body table:nth-child(2) > tbody > tr:nth-child(3) td:nth-child(2)");
        },
        function fail() {
            test.assertExists("body table:nth-child(2) > tbody > tr:nth-child(3) td:nth-child(2)");
        });
    casper.waitForSelector("body",
        function success() {
            this.sendKeys("body", "         ");
        },
        function fail() {
            test.assertExists("body");
        });
    casper.waitForSelector("form[name=newDECForm] input[name='rNameConv']",
        function success() {
            test.assertExists("form[name=newDECForm] input[name='rNameConv']");
            this.click("form[name=newDECForm] input[name='rNameConv']");
        },
        function fail() {
            test.assertExists("form[name=newDECForm] input[name='rNameConv']");
        });
    casper.waitForSelector("form[name=gbqf] input[name='q']",
        function success() {
            test.assertExists("form[name=gbqf] input[name='q']");
            this.click("form[name=gbqf] input[name='q']");
        },
        function fail() {
            test.assertExists("form[name=gbqf] input[name='q']");
        });
    casper.waitForSelector("input[name='q']",
        function success() {
            this.sendKeys("input[name='q']", "C DE");
        },
        function fail() {
            test.assertExists("input[name='q']");
        });
    casper.waitForSelector(x("//a[@href='/spreadsheet/ccc?key=0Aov1RWgHNTwLdGRuVUo1dEVaVkR5d25ld2h0LWM5Y0E&usp=drive_web']"),
        function success() {
            test.assertExists(x("//a[@href='/spreadsheet/ccc?key=0Aov1RWgHNTwLdGRuVUo1dEVaVkR5d25ld2h0LWM5Y0E&usp=drive_web']"));
            this.click(x("//a[@href='/spreadsheet/ccc?key=0Aov1RWgHNTwLdGRuVUo1dEVaVkR5d25ld2h0LWM5Y0E&usp=drive_web']"));
        },
        function fail() {
            test.assertExists(x("//a[@href='/spreadsheet/ccc?key=0Aov1RWgHNTwLdGRuVUo1dEVaVkR5d25ld2h0LWM5Y0E&usp=drive_web']"));
        });
    casper.waitForSelector("#docs-insert-menu",
        function success() {
            test.assertExists("#docs-insert-menu");
            this.click("#docs-insert-menu");
        },
        function fail() {
            test.assertExists("#docs-insert-menu");
        });
    casper.waitForSelector("body > div:nth-child(13) textarea",
        function success() {
            this.sendKeys("body > div:nth-child(13) textarea", "6/18Assgined to ika");
        },
        function fail() {
            test.assertExists("body > div:nth-child(13) textarea");
        });
    casper.waitForSelector("#docs-insert-menu",
        function success() {
            test.assertExists("#docs-insert-menu");
            this.click("#docs-insert-menu");
        },
        function fail() {
            test.assertExists("#docs-insert-menu");
        });
    casper.waitForSelector("#docs-insert-menu",
        function success() {
            test.assertExists("#docs-insert-menu");
            this.click("#docs-insert-menu");
        },
        function fail() {
            test.assertExists("#docs-insert-menu");
        });
    casper.waitForSelector("body > div:nth-child(13) textarea",
        function success() {
            this.sendKeys("body > div:nth-child(13) textarea", "6/18");
        },
        function fail() {
            test.assertExists("body > div:nth-child(13) textarea");
        });
    casper.waitForSelector("#summary-val",
        function success() {
            test.assertExists("#summary-val");
            this.click("#summary-val");
        },
        function fail() {
            test.assertExists("#summary-val");
        });
    casper.waitForSelector("#summary-val",
        function success() {
            test.assertExists("#summary-val");
            this.click("#summary-val");
        },
        function fail() {
            test.assertExists("#summary-val");
        });
    casper.waitForSelector("body > div:nth-child(13) textarea",
        function success() {
            this.sendKeys("body > div:nth-child(13) textarea", "6/18Fix seems to affect  / \r");
        },
        function fail() {
            test.assertExists("body > div:nth-child(13) textarea");
        });
    casper.waitForSelector(x("//a[normalize-space(text())='Comment']"),
        function success() {
            test.assertExists(x("//a[normalize-space(text())='Comment']"));
            this.click(x("//a[normalize-space(text())='Comment']"));
        },
        function fail() {
            test.assertExists(x("//a[normalize-space(text())='Comment']"));
        });
    casper.waitForSelector("#rowForcustomfield_10010 .wrap",
        function success() {
            test.assertExists("#rowForcustomfield_10010 .wrap");
            this.click("#rowForcustomfield_10010 .wrap");
        },
        function fail() {
            test.assertExists("#rowForcustomfield_10010 .wrap");
        });
    casper.waitForSelector("body#jira",
        function success() {
            this.sendKeys("body#jira", "   ");
        },
        function fail() {
            test.assertExists("body#jira");
        });
    casper.waitForSelector(".toolbar-trigger.js-default-dropdown.active .dropdown-text",
        function success() {
            test.assertExists(".toolbar-trigger.js-default-dropdown.active .dropdown-text");
            this.click(".toolbar-trigger.js-default-dropdown.active .dropdown-text");
        },
        function fail() {
            test.assertExists(".toolbar-trigger.js-default-dropdown.active .dropdown-text");
        });
    casper.waitForSelector(".issue-header-content",
        function success() {
            test.assertExists(".issue-header-content");
            this.click(".issue-header-content");
        },
        function fail() {
            test.assertExists(".issue-header-content");
        });
    casper.waitForSelector("#opsbar-operations_more .dropdown-text",
        function success() {
            test.assertExists("#opsbar-operations_more .dropdown-text");
            this.click("#opsbar-operations_more .dropdown-text");
        },
        function fail() {
            test.assertExists("#opsbar-operations_more .dropdown-text");
        });
    casper.waitForSelector(".ops-menus.aui-toolbar",
        function success() {
            test.assertExists(".ops-menus.aui-toolbar");
            this.click(".ops-menus.aui-toolbar");
        },
        function fail() {
            test.assertExists(".ops-menus.aui-toolbar");
        });
    casper.waitForSelector("div#divCc",
        function success() {
            this.sendKeys("div#divCc", "ha");
        },
        function fail() {
            test.assertExists("div#divCc");
        });
    casper.waitForSelector(x("//a[normalize-space(text())='Assign To Me']"),
        function success() {
            test.assertExists(x("//a[normalize-space(text())='Assign To Me']"));
            this.click(x("//a[normalize-space(text())='Assign To Me']"));
        },
        function fail() {
            test.assertExists(x("//a[normalize-space(text())='Assign To Me']"));
        });
    casper.waitForSelector("#opsbar-operations_more .dropdown-text",
        function success() {
            test.assertExists("#opsbar-operations_more .dropdown-text");
            this.click("#opsbar-operations_more .dropdown-text");
        },
        function fail() {
            test.assertExists("#opsbar-operations_more .dropdown-text");
        });
    casper.waitForSelector(".ops-cont",
        function success() {
            test.assertExists(".ops-cont");
            this.click(".ops-cont");
        },
        function fail() {
            test.assertExists(".ops-cont");
        });
    casper.waitForSelector(x("//a[normalize-space(text())='Assign']"),
        function success() {
            test.assertExists(x("//a[normalize-space(text())='Assign']"));
            this.click(x("//a[normalize-space(text())='Assign']"));
        },
        function fail() {
            test.assertExists(x("//a[normalize-space(text())='Assign']"));
        });
    casper.waitForSelector(x("//a[normalize-space(text())='Cancel']"),
        function success() {
            test.assertExists(x("//a[normalize-space(text())='Cancel']"));
            this.click(x("//a[normalize-space(text())='Cancel']"));
        },
        function fail() {
            test.assertExists(x("//a[normalize-space(text())='Cancel']"));
        });
    casper.waitForSelector("#opsbar-operations_more .icon.drop-menu",
        function success() {
            test.assertExists("#opsbar-operations_more .icon.drop-menu");
            this.click("#opsbar-operations_more .icon.drop-menu");
        },
        function fail() {
            test.assertExists("#opsbar-operations_more .icon.drop-menu");
        });
    casper.waitForSelector(".ops-cont",
        function success() {
            test.assertExists(".ops-cont");
            this.click(".ops-cont");
        },
        function fail() {
            test.assertExists(".ops-cont");
        });
    casper.waitForSelector("#edit-issue .trigger-text",
        function success() {
            test.assertExists("#edit-issue .trigger-text");
            this.click("#edit-issue .trigger-text");
        },
        function fail() {
            test.assertExists("#edit-issue .trigger-text");
        });
    casper.waitForSelector(x("//a[normalize-space(text())='Cancel']"),
        function success() {
            test.assertExists(x("//a[normalize-space(text())='Cancel']"));
            this.click(x("//a[normalize-space(text())='Cancel']"));
        },
        function fail() {
            test.assertExists(x("//a[normalize-space(text())='Cancel']"));
        });
    casper.waitForSelector("#rowForcustomfield_13651 .wrap",
        function success() {
            test.assertExists("#rowForcustomfield_13651 .wrap");
            this.click("#rowForcustomfield_13651 .wrap");
        },
        function fail() {
            test.assertExists("#rowForcustomfield_13651 .wrap");
        });
    casper.waitForSelector("body#jira",
        function success() {
            this.sendKeys("body#jira", "    ");
        },
        function fail() {
            test.assertExists("body#jira");
        });
    casper.waitForSelector("#opsbar-operations_more .dropdown-text",
        function success() {
            test.assertExists("#opsbar-operations_more .dropdown-text");
            this.click("#opsbar-operations_more .dropdown-text");
        },
        function fail() {
            test.assertExists("#opsbar-operations_more .dropdown-text");
        });
    casper.waitForSelector(".ops-cont",
        function success() {
            test.assertExists(".ops-cont");
            this.click(".ops-cont");
        },
        function fail() {
            test.assertExists(".ops-cont");
        });
    casper.waitForSelector(x("//a[normalize-space(text())='Assign']"),
        function success() {
            test.assertExists(x("//a[normalize-space(text())='Assign']"));
            this.click(x("//a[normalize-space(text())='Assign']"));
        },
        function fail() {
            test.assertExists(x("//a[normalize-space(text())='Assign']"));
        });
    casper.waitForSelector(x("//a[normalize-space(text())='Cancel']"),
        function success() {
            test.assertExists(x("//a[normalize-space(text())='Cancel']"));
            this.click(x("//a[normalize-space(text())='Cancel']"));
        },
        function fail() {
            test.assertExists(x("//a[normalize-space(text())='Cancel']"));
        });
    casper.waitForSelector(x("//a[normalize-space(text())='Comment']"),
        function success() {
            test.assertExists(x("//a[normalize-space(text())='Comment']"));
            this.click(x("//a[normalize-space(text())='Comment']"));
        },
        function fail() {
            test.assertExists(x("//a[normalize-space(text())='Comment']"));
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
    casper.waitForSelector("form[name=validateDECForm] input[type=button][value='Submit']",
        function success() {
            test.assertExists("form[name=validateDECForm] input[type=button][value='Submit']");
            this.click("form[name=validateDECForm] input[type=button][value='Submit']");
        },
        function fail() {
            test.assertExists("form[name=validateDECForm] input[type=button][value='Submit']");
        });
    casper.waitForSelector("form[name=validateDECForm] input[type=button][value='Submit']",
        function success() {
            test.assertExists("form[name=validateDECForm] input[type=button][value='Submit']");
            this.click("form[name=validateDECForm] input[type=button][value='Submit']");
        },
        function fail() {
            test.assertExists("form[name=validateDECForm] input[type=button][value='Submit']");
        });
    casper.waitForSelector("form[name=validateDECForm] input[type=button][value='Submit']",
        function success() {
            test.assertExists("form[name=validateDECForm] input[type=button][value='Submit']");
            this.click("form[name=validateDECForm] input[type=button][value='Submit']");
        },
        function fail() {
            test.assertExists("form[name=validateDECForm] input[type=button][value='Submit']");
        });
    casper.waitForSelector("form[name=validateDECForm] input[type=button][value='Submit']",
        function success() {
            test.assertExists("form[name=validateDECForm] input[type=button][value='Submit']");
            this.click("form[name=validateDECForm] input[type=button][value='Submit']");
        },
        function fail() {
            test.assertExists("form[name=validateDECForm] input[type=button][value='Submit']");
        });
    casper.waitForSelector("form[name=validateDECForm] input[type=button][value='Submit']",
        function success() {
            test.assertExists("form[name=validateDECForm] input[type=button][value='Submit']");
            this.click("form[name=validateDECForm] input[type=button][value='Submit']");
        },
        function fail() {
            test.assertExists("form[name=validateDECForm] input[type=button][value='Submit']");
        });
    casper.waitForSelector("form[name=validateDECForm] input[type=button][value='Submit']",
        function success() {
            test.assertExists("form[name=validateDECForm] input[type=button][value='Submit']");
            this.click("form[name=validateDECForm] input[type=button][value='Submit']");
        },
        function fail() {
            test.assertExists("form[name=validateDECForm] input[type=button][value='Submit']");
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