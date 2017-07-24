#include <iostream>
#include <stdio.h>
#include "urlCache.h"

using std::cout;
using std::endl;

    const unsigned NUM_VALID_CHARS = 64;

    UrlCache::UrlCache() :
        urls(NUM_VALID_CHARS),
        gettingFromCache(true),
        unique(0)
    {}

    void UrlCache::addToCache(const string& url) {
        this->storeUrl(url);
    }

    void UrlCache::initGetFromCache() {
        this->reset();
    }

    const string UrlCache::getFromCache(char c) {
        this->searchString += c;

        if (this->gettingFromCache) {
            string urlMatch;
            if (this->getUrlMatch(searchString, urlMatch)) {
                return urlMatch;
            } else {
                this->gettingFromCache = false;
                return this->searchString;
            }
        } else {
            return this->searchString;
        }
    }

    void UrlCache::reset() {
        this->gettingFromCache = true;
        searchString.clear();
    }

    bool UrlCache::storeUrl(const string& urlToCache) {
        if (urlToCache.length() == 0) {
            return false;
        }
        unsigned urlsIndex = this->getVecIndex(urlToCache[0]);
        set<string, UrlsComp>& urlSet = this->urls[urlsIndex];

        auto findIter = urlSet.find(urlToCache);
        if (findIter != urlSet.end()) {
            cout << urlToCache << " already exists" << endl;
            return false;
        }

        urlSet.insert(urlToCache);
        ++this->unique;
        return true;
    }

    bool UrlCache::getUrlMatch(const string& targetSubstr, string& urlMatch) {
        unsigned urlsIndex = this->getVecIndex(targetSubstr[0]);
        set<string, UrlsComp>& urlSet = this->urls[urlsIndex];

        for (const string& url: urlSet) {
            if (url.length() < targetSubstr.length() ) {
                continue;
            }

            string urlTargetSubstr = url.substr(0, targetSubstr.length());

            if (urlTargetSubstr.compare(targetSubstr) == 0) {
                urlMatch = url;
                return true;
            }
        }
        return false;
    }

    unsigned UrlCache::getVecIndex(char c) {

        const int NUM_NUMBERS = 10;
        const int NUM_LETTERS = 26;

        unsigned pos = 0;
        if (c == '-') {
            return pos;
        }

        ++pos;
        if (c == '.') {
            return pos;
        }

        ++pos;
        if (c >= '0' && c <= '9') {
            return pos + c - '0';
        }

        pos += (NUM_NUMBERS - 1);
        if (c >= 'A' && c <= 'Z') {
            return pos + c - 'A';
        }

        pos += (NUM_LETTERS - 1);
        if (c >= 'a' && c <= 'z') {
            return pos + c - 'a';
        }

        throw string("invalid character");
    }

    unsigned UrlCache::getUniqueUrls() {
        return this->unique;
    }

