#!/usr/bin/env bash

set -x

#get highest tag number
VERSION=`git describe --abbrev=0 --tags`

#replace . with space so can split into an array
VERSION_BITS=(${VERSION//./ })

#get number parts and increase last one by 1
VNUM1=${VERSION_BITS[0]}
VNUM2=${VERSION_BITS[1]}
VNUM3=${VERSION_BITS[2]}
VNUM1=`echo $VNUM1 | sed 's/v//'`

echo "Updating patch version"
VNUM3=$((VNUM3+1))

#create new tag
NEW_TAG="v$VNUM1.$VNUM2.$VNUM3"

echo "Updating $VERSION to $NEW_TAG"

#get current hash and see if it already has a tag
GIT_COMMIT=`git rev-parse HEAD`
NEEDS_TAG=`git describe --contains $GIT_COMMIT`

#only tag if no tag already (would be better if the git describe command above could have a silent option)
if [ -z "$NEEDS_TAG" ]; then
    echo "Tagged with $NEW_TAG (Ignoring fatal:cannot describe - this means commit is untagged) "
    git tag -a -m "release- $NEW_TAG" $NEW_TAG
    URL=https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/joveo/eqr-test-sdk.git
    git push $URL $NEW_TAG
    git checkout $NEW_TAG
else
    echo "Already a tag on this commit"
    exit 1
fi
