# Match onze churn met dat uit de LT?

Na het draaien van are-we-close-to-15%.sh kreeg ik de volgende uitkomst:

```bash
echo "Lines added and removed in the last year"
git log --since=31/12/2012 --numstat --pretty="%H" | awk '
    NF==3 {plus+=$1; minus+=$2;}
    END   {printf("+%d, -%d\n", plus, minus)}'

echo ""

echo "total lines of code in the application"
cloc .
```

```bash
Lines added and removed in the last year
+1159130, -971277

total lines of code in the application
   15259 text files.
   14342 unique files.
    3241 files ignored.

github.com/AlDanial/cloc v 2.04  T=68.70 s (208.8 files/s, 53435.3 lines/s)
--------------------------------------------------------------------------------------
Language                            files          blank        comment           code
--------------------------------------------------------------------------------------
XML                                  2852          11211            436        1550263
Java                                 6463         119783         164640         488532
CSV                                    90              4              0         354088
JavaScript                            385          12869          37481         175983
JSON                                  306              8              0         166846
XSD                                   172            522           1507         148600
Cucumber                              344           3469            353          77228
HTML                                  980            689            211          61188
Web Services Description               51            203              3          40543
YAML                                   51            348             57          38407
CSS                                   153           7892           4824          36163
Kotlin                                713           8863          24742          35816
XSLT                                  149           1721           1967          16942
TypeScript                            276           2622           1032          16575
Text                                  377            176              0          12399
Gradle                                364           2361            287          12363
SVG                                    23              7              4           8839
Properties                            239            976           1197           6117
SQL                                    44            158              2           2806
Markdown                               99            875              1           2084
SCSS                                   56            265             69           1529
TOML                                    3              5              0            727
DOS Batch                              87             87             14            644
Groovy                                  9             60             43            578
Bourne Shell                           11             64            151            299
Dockerfile                             33            157             20            297
Maven                                   1              6              1            210
Ant                                     2             37             17            187
PlantUML                                3             13              0             55
AsciiDoc                                1              9              0             54
INI                                     2              3              0             18
JSP                                     1              0              0             17
Bourne Again Shell                      2              3              0              8
--------------------------------------------------------------------------------------
SUM:                                14342         175466         239059        3256405
--------------------------------------------------------------------------------------
```

# Voorlopige conclusie

De eerste getallen zouden de hoeveelheid regels moeten zijn die zijn aangepast sinds 31/12/2023.
Dat is iets meer dan 1 jaar geleden, maar dat zou betekenen dat bijna 2 miljoen regels aangepast zouden zijn in die periode.
**Dat is bijna 65% van de codebase!**, dit kan niet kloppen, dus dit resultaat lijkt mij incorrect.
