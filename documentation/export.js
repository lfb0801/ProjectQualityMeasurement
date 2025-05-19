const path = require('path');
const fs = require('fs');
const puppeteer = require('puppeteer');

const IGNORE_HTTPS_ERRORS = true;
const HEADLESS = true;

const url = 'http://localhost:9797';

(async () => {
  const browser = await puppeteer.launch({
    ignoreHTTPSErrors: IGNORE_HTTPS_ERRORS,
    headless: HEADLESS,
    args: ['--no-sandbox', '--disable-setuid-sandbox']
  });

  const page = await browser.newPage();

  console.log(" - Opening " + url);
  await page.goto(url, { waitUntil: 'domcontentloaded' });

  await page.waitForFunction('structurizr.scripting && structurizr.scripting.isDocumentationRendered() === true');

  await page.exposeFunction('saveHtml', (content) => {
    const outputDir = path.join(__dirname, 'output');
    const filename = path.join(outputDir, 'documentation.html');

    if (!fs.existsSync(outputDir)) {
      fs.mkdirSync(outputDir);
    }

    console.log(" - Writing " + filename);
    fs.writeFileSync(filename, content, 'utf8');

    console.log(" - Finished");
    browser.close();
  });

  await page.evaluate(() => {
    return structurizr.scripting.exportDocumentationToOfflineHtmlPage(function(html) {
      saveHtml(html);
    });
  });
})();
