import re
from collections import Counter
import liwc
import sys
import pymongo
import pandas as pd

myclient = pymongo.MongoClient(
    "mongodb+srv://ismailyankayis:2430zcbg@twitterpersonalityanalyzer-aeniz.mongodb.net/admin")
mydb = myclient["TwitterPersonalityAnalyzerDB"]
mycol1 = mydb["User"]

categoryList = list()
categoryList = ["funct","pronoun","ppron","i","we","you","3rdPersonSingular","they","ipron","verb","past","present","future","adverb","prepositions","conjunction","negation","quantifier",
                "number","swear","social","family","friend","humans","affect","posemo","negemo","anxiety","anger","sad","cogmech","insight","cause","discrepancy",
                "tentative","certain","inhibition","include","exclusive","percept","see","hear","feel","bio","body","health","sexual","ingestion","relative","motion",
                "space","time","politic","work","achieve","leisure","home","money","religion","death","assent","nonfluencies","words > 6 letter"]
data = pd.read_csv("src/main/LIWC/Data2.csv")
data2 = data.copy()
categoryCounts = dict()
global index

for i in range(0, len(data)):
    if((data['username'] == sys.argv[1])[i] == True):
        index = i
        break

global sum

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

    parse, category_names = liwc.load_token_parser('src/main/LIWC/LIWC_Turkish.dic')
    gettysburg_counts = None
    tweets = usr['preprocessedTweets']
    #print(tweets)
    for tweet in tweets:
        #print(tweet)
        words = tweet.split(" ")
        for eachWord in words:  ## each word in a tweet
            optionalWords = eachWord.split("|")
            for eachOptionalWord in optionalWords:  #each options of a word. It will run till find a option which belongs to any category.
                category_tokens = tokenize(eachOptionalWord)    #categories of the option
                gettysburg_counts = Counter(category for token in category_tokens for category in parse(token))
                if len(gettysburg_counts) > 0:
                    for cat in gettysburg_counts.items():
                        addCategory(cat)
                    break;
    catList = list()
    sum = 0
    for cat in sorted(categoryCounts.keys()):
        print(cat,":",str(categoryCounts[cat]))
        sum = sum + categoryCounts[cat]
    for cat in sorted(categoryCounts.keys()):
        normalized = categoryCounts[cat]/sum
        print(cat,":",str(round(normalized,3)))
        data2[cat][index] = round(normalized,3)
        catList.append(cat + "," + str(round(normalized,3)))

    for i in range(0, len(categoryList)):
        print(data2[categoryList[i]][index])
        if(pd.isnull(data2[categoryList[i]][index])):
            data2[categoryList[i]][index] = 0.0

    if usr is not None:
        updateDoc = {"username": sys.argv[1]}
        liwcGroups = {"$set": {"groups": catList}}
        doc = mycol1.update_one(updateDoc, liwcGroups)
        print("Updated: " + str(updateDoc))
        data2.to_csv(r'src/main/LIWC/Data2.csv')
        data2.to_excel(r'src/main/LIWC/Data3.xlsx')
    else:
        print("There is no user as " + sys.argv[1] )

def main():
    user = mycol1.find_one({"username": sys.argv[1]})
    if user is not None:
        determineCategories(user)
    else:
        print("There is no user as " + sys.argv[1] )

if __name__ == '__main__':
    main()



