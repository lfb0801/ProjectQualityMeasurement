const puppeteer = require('puppeteer');
const fs = require('fs');
const path = require('path');

const IGNORE_HTTPS_ERRORS = true;
const HEADLESS = true;

const baseUrl = 'http://localhost:9797'

const urls = new Map([
  ['overview', ''],
  ['documentation', '/workspace/documentation/Project%20Quality%20Measurement']
]);

(async () => {
  const browser = await puppeteer.launch({
    ignoreHTTPSErrors: IGNORE_HTTPS_ERRORS,
    headless: HEADLESS,
    args: ['--no-sandbox', '--disable-setuid-sandbox']
  });

  const outputDir = path.join(__dirname, 'output');
  if (!fs.existsSync(outputDir)) {
    fs.mkdirSync(outputDir);
  }

  for (const [title, url] of urls.entries()) {
    const page = await browser.newPage();

    console.log(` - Opening ${baseUrl}${url}`);
    await page.goto(baseUrl+url, { waitUntil: 'domcontentloaded' });

    await page.waitForFunction('structurizr.scripting && structurizr.scripting.isDocumentationRendered() === true');

    const outputPath = path.join(outputDir, `${title}.pdf`);

    console.log(` - Generating PDF for ${title}...`);
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

    console.log(` - PDF written to ${outputPath}`);
    await page.close();
  }

  await browser.close();
})();
