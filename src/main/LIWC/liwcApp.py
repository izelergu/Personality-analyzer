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
mycol2 = mydb["Detail"]

'''
categoryList = list()
categoryList = ["funct","pronoun","ppron","i","we","you","3rdPersonSingular","they","ipron","verb","past","present","future","adverb","prepositions","conjunction","negation","quantifier",
                "number","swear","social","family","friend","humans","affect","posemo","negemo","anxiety","anger","sad","cogmech","insight","cause","discrepancy",
                "tentative","certain","inhibition","include","exclusive","percept","see","hear","feel","bio","body","health","sexual","ingestion","relative","motion",
                "space","time","politic","work","achieve","leisure","home","money","religion","death","assent","nonfluencies","words > 6 letter"]
'''
data = pd.read_csv("src/main/LIWC/Dataset_2.csv", index_col="index")
global data2
categoryCounts = dict()
global index

for i in range(0, len(data)):
    if((data['username'] == sys.argv[1])[i] == True):
        index = i
        break

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
                    break;
    catList = list()
    sum = 0
    data2 = data
    for cat in sorted(categoryCounts.keys()):
        print(cat,":",str(categoryCounts[cat]))
        sum = sum + categoryCounts[cat]
    print("sum: " + str(sum))
    for cat in sorted(categoryCounts.keys()):
        normalized = categoryCounts[cat]/sum
        print(cat,":",str(round(normalized,3)))
        data2[cat][index] = round(normalized,3)
        catList.append(cat + "," + str(round(normalized,3)))

    for i in range(0, len(category_names)):
        print(data2[category_names[i]][index])
        if(pd.isnull(data2[category_names[i]][index])):
            data2[category_names[i]][index] = 0.0
    # data2 = data2.drop(columns = "i")
    # data2 = data2.drop(columns = "anger")
    # data2 = data2.drop(columns = "past")
    # data2 = data2.drop(columns = "present")
    # data2 = data2.drop(columns = "future")
    # data2 = data2.drop(columns = "cogmech")
    # data2 = data2.drop(columns = "affect")
    # data2 = data2.drop(columns = "relative")
    # data2 = data2.drop(columns = "percept")

    if usr is not None:
        updateDoc = {"username": sys.argv[1]}
        liwcGroups = {"$set": {"groups": catList}}
        numberofWordsUsed = {"$set": {"numberofWordsUsed": sum }}
        numberofWordsAnalyzed = {"$set": {"numberofWordsAnalyzed": totalWords}}
        doc1 = mycol1.update_one(updateDoc, liwcGroups)
        doc2 = mycol2.update_one(updateDoc, numberofWordsUsed)
        doc3 = mycol2.update_one(updateDoc, numberofWordsAnalyzed)
        print("Updated: " + str(updateDoc))

        data2.to_csv(r'src/main/LIWC/Dataset_2.csv')
        data2.to_excel(r'src/main/LIWC/Dataset_2.xlsx')
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



