import java.util.ArrayList;
import java.util.TreeSet;

public class UrlCache {
   
    public UrlCache() {
        gettingFromCache = true;
        unique = 0;
        NUM_VALID_CHARS = 64;
        searchStringBuffer = new StringBuffer();
        urls = new ArrayList<TreeSet<String>>();
        for (int i = 0; i < NUM_VALID_CHARS; ++i) {
            urls.add(new TreeSet<String>());
        }
    }

    public void addToCache(final String url) throws Exception {
        this.storeUrl(url);
    }

    public void initGetFromCache() {
        this.reset();
    }

    public final String getFromCache(char c) throws Exception {
        this.searchStringBuffer.append(c);

        String searchString = searchStringBuffer.toString();
        if (this.gettingFromCache) {
            String urlMatch = this.getUrlMatch(searchString);
            if (urlMatch.isEmpty()) {
                this.gettingFromCache = false;
                return searchString;
            }
            return urlMatch;
        } else {
            return searchString;
        }
    }

    private void reset() {
        this.gettingFromCache = true;
        this.searchStringBuffer.setLength(0);
    }

    private boolean storeUrl(final String urlToCache) throws Exception {
        if (urlToCache.length() == 0) {
            return false;
        }
        int urlsIndex = getVecIndex(urlToCache.charAt(0));
        TreeSet<String> urlSet = this.urls.get(urlsIndex);

        if (urlSet.contains(urlToCache)) {
            System.out.println(urlToCache + " already exists");
            return false;
        }

        urlSet.add(urlToCache);
        ++this.unique;
        return true;
    }

    private String getUrlMatch(final String targetSubstr) throws Exception {
        int urlsIndex = getVecIndex(targetSubstr.charAt(0));
        TreeSet<String> urlSet = this.urls.get(urlsIndex);

        for (String url: urlSet) {
            if (url.length() < targetSubstr.length() ) {
                continue;
            }
            String urlTargetSubstr = url.substring(0, targetSubstr.length());

            if (urlTargetSubstr.compareTo(targetSubstr) == 0) {
                return url;
            }
        }
        return new String();
    }

    private int getVecIndex(char c) throws Exception {

        final int NUM_NUMBERS = 10;
        final int NUM_LETTERS = 26;

        int pos = 0;
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

        if (c == '-') {
            return 0;
        }

        throw new Exception("invalid character");
    }

    private ArrayList<TreeSet<String>> urls;
    private StringBuffer    searchStringBuffer;
    private boolean         gettingFromCache;
    public int              unique;
    private final int       NUM_VALID_CHARS;
};

