#include <fstream>
#include <iostream>
#include "urlCache.h"
using std::cin;
using std::cout;
using std::endl;
using std::ifstream;

int main(int argc, char *argv[]) {

    if (argc != 2) {
        cout << "usage: <filename containing urls>" << endl;
        return 0;
    }

    const char ADD_URL = ';';
    const char RESET_GET_FROM_CACHE = ':';
    const char QUIT_CHAR = '-';
    UrlCache   urlCache;

    cout << "populating lookup data structures..." << endl;

    char line[255];
    ifstream ifs (argv[1], ifstream::in);
    while (ifs.getline(line, 255)) {
        string url(line);
        urlCache.addToCache(url);
    }

    cout << "finished populating lookup data structures - number of unique url: " << urlCache.getUniqueUrls() << endl;

    system("stty raw");
    urlCache.initGetFromCache();
    bool done = false;

    while (!done) {
        char c = getchar();
        switch(c) {
            case ADD_URL: {
                system("stty cooked");
                string url;
                cout << "url: ";
                cin >> url;
                urlCache.addToCache(url);
                system("stty raw");
                getchar(); // Remove the newline from the input stream

                // Fall through....
            }
            case RESET_GET_FROM_CACHE:
                cout << "...resetting... ";
                urlCache.initGetFromCache();
                break;

            default:
                if (c <= QUIT_CHAR) {
                    cout << endl;
                    done = true;
                } else {
                    cout << urlCache.getFromCache(c) << ", ";
                }
                break;
        } 
    }

    system("stty cooked");
    return 0;
}

