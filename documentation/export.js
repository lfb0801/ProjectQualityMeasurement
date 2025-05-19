const puppeteer = require('puppeteer');
const fs = require('fs');
const path = require('path');

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

  const outputDir = path.join(__dirname, 'output');
  const outputPath = path.join(outputDir, 'documentation.pdf');

  if (!fs.existsSync(outputDir)) {
    fs.mkdirSync(outputDir);
  }

  console.log(" - Generating PDF...");
  await page.pdf({
    path: outputPath,
    format: 'A4',
    printBackground: true,
    margin: {
      top: '20mm',
      bottom: '20mm',
      left: '15mm',
      right: '15mm'
    }
  });

  console.log(" - PDF written to " + outputPath);
  await browser.close();
})();
