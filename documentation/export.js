const puppeteer = require('puppeteer');
const fs = require('fs');
const path = require('path');

const IGNORE_HTTPS_ERRORS = true;
const HEADLESS = true;

const baseUrl = 'http://localhost:9797'

const urls = new Map([
  ['ontwerp', ''],
  ['implementatie', '/workspace/documentation/Project%20Quality%20Measurement/Quality%20Measurement%20Server'],
  ['research-on-churn', '/workspace/documentation/research/research%20on%20Churn'],
  ['literature-study', '/workspace/documentation/research/literature%20study'],
  ['regels-code-per-developer', '/workspace/documentation/research/regels%20code%20per%20developer'],
  ['scope-voor-meetpunten', '/workspace/documentation/research/scope%voor%meetpunten'],
  ['expert-interview', '/workspace/documentation/research/expert%20interview']
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
    await page.goto(baseUrl + url, {waitUntil: 'domcontentloaded'});

    try {
      await page.waitForFunction(
          'structurizr.scripting && structurizr.scripting.isDocumentationRendered() === true',
          { timeout: 60000 }
      );
    } catch (error) {
      console.error(`Failed to render documentation for ${title}:`, error);
      continue; // Skip to the next URL in the loop
    }
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
