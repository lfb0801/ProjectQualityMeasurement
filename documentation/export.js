const puppeteer = require('puppeteer');
const fs = require('fs');
const path = require('path');

const IGNORE_HTTPS_ERRORS = true;
const HEADLESS = true;

const baseUrl = 'http://localhost:9797'

const urls = new Map([
  ['ontwerp', '/workspace/documentation/Project%20Quality%20Measurement'],
  ['implementatie',
    '/workspace/documentation/Project%20Quality%20Measurement/Quality%20Measurement%20Server'],
  ['Research on Churn',
    '/workspace/documentation/research/research%20on%20Churn'],
  ['Literature study',
    '/workspace/documentation/research/Literature%20study'],
  ['regels code per developer',
    '/workspace/documentation/research/regels%20code%20per%20developer'],
  ['Scope voor meetpunten',
    '/workspace/documentation/research/scope%voor%meetpunten'],
  ['Expert interview',
    '/workspace/documentation/research/expert%20interview']
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

    await page.waitForFunction(
        'structurizr.scripting && structurizr.scripting.isDocumentationRendered() === true');

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
