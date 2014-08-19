var u = require('./logininfo.js');
module.exports = {
        'DalekJS Hi Res Test for cdecurate': function (test) {
                  test.open('https://cdecurate-dev.nci.nih.gov').wait(1000).click('body > div:nth-child(11) > table > tbody > tr:nth-child(2) > td:nth-child(2) > a').waitForElement('input[value=login]', 3000).click('#Username').type('#Username',u().id).click('#Password').type('#Password',u().password).sendKeys('#Password','\uE007').waitForElement('a[href=javascript:callLogout();]',60000).screenshot().done();
        }
};