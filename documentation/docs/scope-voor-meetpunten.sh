AANTAL_COMMITS=$(git log --oneline | wc -l)
AANTAL_TAGS=$(git tag --list | wc -l)

echo "iBurgerzaken bestaat uit $AANTAL_COMMITS"

echo "iBurgerzaken heeft op dit moment $AANTAL_TAGS"
