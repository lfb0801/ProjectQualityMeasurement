const puppeteer = require('puppeteer');
const fs = require('fs');
const path = require('path');
const cheerio = require('cheerio');

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

  const htmlContent = await page.evaluate(() => {
    return new Promise((resolve) => {
      structurizr.scripting.exportDocumentationToOfflineHtmlPage(function(html) {
        resolve(html);
      });
    });
  });

  await browser.close();

  const $ = cheerio.load(htmlContent);

  // Inline local images as base64
  $('img').each(function () {
    const src = $(this).attr('src');
    if (!src || src.startsWith('http') || src.startsWith('data:')) return;

    const imagePath = path.resolve(__dirname, src);
    if (!fs.existsSync(imagePath)) {
      console.warn(` ⚠️  Image file not found: ${imagePath}`);
      return;
    }

    const mimeType = getMimeType(imagePath);
    const imageData = fs.readFileSync(imagePath);
    const base64 = imageData.toString('base64');
    const dataUri = `data:${mimeType};base64,${base64}`;

    $(this).attr('src', dataUri);
    console.log(` - Inlined image: ${src}`);
  });

  const outputDir = path.join(__dirname, 'output');
  if (!fs.existsSync(outputDir)) {
    fs.mkdirSync(outputDir);
  }

  const outputPath = path.join(outputDir, 'documentation.html');
  fs.writeFileSync(outputPath, $.html(), 'utf8');
  console.log(` - Exported HTML with inline images: ${outputPath}`);
})();

// Simple MIME type detection by file extension
function getMimeType(filePath) {
  const ext = path.extname(filePath).toLowerCase();
  switch (ext) {
    case '.png': return 'image/png';
    case '.jpg':
    case '.jpeg': return 'image/jpeg';
    case '.gif': return 'image/gif';
    case '.svg': return 'image/svg+xml';
    case '.webp': return 'image/webp';
    default: return 'application/octet-stream';
  }
}
