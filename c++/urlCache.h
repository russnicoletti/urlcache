#include <set>
#include <string>
#include <vector>
using std::set;
using std::string;
using std::vector;

struct UrlsComp {
    bool operator() (const string& lhs, const string& rhs) const {
        if (lhs.compare(rhs) < 0) {
            return true;
        }
        return false;
    }
};

class UrlCache {
    public:
        UrlCache();

        void addToCache(const string& url);
        void initGetFromCache();
        const string getFromCache(char c);
        unsigned getUniqueUrls();

    private:
        void reset();
        bool storeUrl(const string& urlToCache);
        unsigned getVecIndex(char c);
        bool getUrlMatch(const string& targetSubstr, string& urlMatch);

        vector<set<string, UrlsComp> > urls;
        string searchString;
        bool   gettingFromCache;
        unsigned unique;
};



