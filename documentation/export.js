const puppeteer = require('puppeteer');
const fs = require('fs');

const IGNORE_HTTPS_ERRORS = true;
const HEADLESS = true;

// URL is now hardcoded
const url = 'http://localhost:9797';

(async () => {
  const browser = await puppeteer.launch({ignoreHTTPSErrors: IGNORE_HTTPS_ERRORS, headless: HEADLESS});
  const page = await browser.newPage();

  // visit the documentation page
  console.log(" - Opening " + url);
  await page.goto(url, { waitUntil: 'domcontentloaded' });
  await page.waitForFunction('structurizr.scripting && structurizr.scripting.isDocumentationRendered() === true');

  await page.exposeFunction('saveHtml', (content) => {
    const filename = 'documentation.html';
    console.log(" - Writing " + filename);
    fs.writeFile(filename, content, 'utf8', function (err) {
      if (err) throw err;
    });

    console.log(" - Finished");
    browser.close();
  });

  await page.evaluate(() => {
    return structurizr.scripting.exportDocumentationToOfflineHtmlPage(function(html) {
      saveHtml(html);
    });
  });

})();
