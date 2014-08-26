var u = require('./logininfo.js');
module.exports = {
        'DalekJS Hi Res Test for cdecurate': function (test) {
                  test//.open('https://cdecurate.nci.nih.gov')
//.open('https://cdecurate-qa.nci.nih.gov')
.open('https://cdecurate-dev.nci.nih.gov')
.wait(1000).click('a[href*=Login]').waitForElement('input[value=login]', 3000).click('input[name=Username]').type('input[name=Username]',u().id).click('input[name=Password]').type('input[name=Password]',u().password).sendKeys('input[name=Password]','\uE007').waitForElement('a[href=javascript:callLogout();]',60000).screenshot().type('input[name=keyword]','Anaplastic*\n').assert.exists('body','Anaplastic Thyroid Cancer American Joint Committee on Cancer (AJCC) Edition 7 Group Stage').click('body > div:nth-child(14) > table > tbody > tr:nth-child(3) > td:nth-child(2) > form:nth-child(1) > table:nth-child(10) > tbody > tr:nth-child(2) > td:nth-child(2) > img')
//.click('body > div:nth-child(5) > table > tbody > tr:nth-child(3) > td:nth-child(2) > form:nth-child(1) > table:nth-child(10) > tbody > tr:nth-child(2) > td:nth-child(2) > img')
.assert.visible('#objMenu > table', 'Menu action popup is visible').click('#objMenu > table > tbody > tr:nth-child(3) > td:nth-child(1)').click('img[src=/cdecurate/images/edit_16.gif]').assert.chain()
//.text('body > form:nth-child(16) > b > font').is('Not Authorized for Edits in this Context.', 'Not authorized text found')
.end()
.screenshot('done.png').assert.exists('body','Logout').click('img[src*=biztech_button_b7]').click('a[href*=Logout]').accept().screenshot('logout.png').assert.exists('body','Anaplastic Thyroid Cancer American Joint Committee on Cancer (AJCC) Edition 7 Group Stage').done();
        }
};