import re
from collections import Counter
import liwc
import sys
import pymongo
from bson.objectid import ObjectId

myclient = pymongo.MongoClient(
    "mongodb+srv://ismailyankayis:2430zcbg@twitterpersonalityanalyzer-aeniz.mongodb.net/admin")
mydb = myclient["TwitterPersonalityAnalyzerDB"]
col_User = mydb["User"]

categoryCounts = dict()

global sum
global totalWords

def tokenize(text):
    # you may want to use a smarter tokenizer
    for match in re.finditer(r'\w+', text, re.UNICODE):
        yield match.group(0)

def addCategory(category):
    if category[0] not in categoryCounts:
        categoryCounts[category[0]] = category[1]
    else:
        categoryCounts[category[0]] = categoryCounts[category[0]] + category[1]

def determineCategories(usr):
    parse = None
    totalWords = 0
    UsedWords = 0
    parse, category_names = liwc.load_token_parser('src/main/LIWC/LIWC_Turkish.dic')
    gettysburg_counts = None
    tweets = usr['preprocessedTweets']
    #print(tweets)
    for tweet in tweets:
        #print(tweet)
        words = tweet.split(" ")
        for eachWord in words:  ## each word in a tweet
            totalWords = totalWords + 1
            optionalWords = eachWord.split("|")
            for eachOptionalWord in optionalWords:  #each options of a word. It will run till find a option which belongs to any category.
                category_tokens = tokenize(eachOptionalWord)    #categories of the option
                gettysburg_counts = Counter(category for token in category_tokens for category in parse(token))
                if len(gettysburg_counts) > 0:
                    for cat in gettysburg_counts.items():
                        addCategory(cat)
                    UsedWords += 1
                    break;
    catList = list()
    sum = 0
    for cat in sorted(categoryCounts.keys()):
        #print(cat,":",str(categoryCounts[cat]))
        sum = sum + categoryCounts[cat]
    print("sum: " + str(sum))
    for cat in sorted(categoryCounts.keys()):
        normalized = categoryCounts[cat]/sum
        catList.append(cat + "," + str(("%.3f" % normalized)))

    print(ObjectId(sys.argv[2]))

    if usr is not None:
        liwcGroups = {"$set": {"groups": catList}}
        doc1 = col_User.update_one(updateDoc, liwcGroups)
        print("Updated User: " + str(updateDoc))
    else:
        print("There is no user as " + sys.argv[1] )

def main():
    user = col_User.find_one({"username": sys.argv[1]})
    if user is not None:
        determineCategories(user)
    else:
        print("There is no user as " + sys.argv[1] )

if __name__ == '__main__':
    main()



