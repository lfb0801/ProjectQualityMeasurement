echo "Lines added and removed in the last year"
git log --since=31/12/2012 --numstat --pretty="%H" | awk '
    NF==3 {plus+=$1; minus+=$2;}
    END   {printf("+%d, -%d\n", plus, minus)}'

echo ""

echo "total lines of code in the application"
cloc .
