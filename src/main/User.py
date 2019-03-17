import tweepy
import re
import sys
import datetime
import pymongo

myclient = pymongo.MongoClient("mongodb+srv://ismailyankayis:2430zcbg@twitterpersonalityanalyzer-aeniz.mongodb.net/admin")
mydb = myclient["TwitterPersonalityAnalyzerDB"]
mycol1 = mydb["User"]

def clearTweet(cleanedTweet):
    prepTweet = list()
    cleanedTweet = re.split('; |, |\.|\n|\\s+|\"|”',cleanedTweet)
    #print(cleanedTweet)
    for i in range(0,len(cleanedTweet)):
        clstr = cleanedTweet[i]
        if((clstr.isalpha() == 0 or clstr == 'RT') == 0):
            prepTweet.append(clstr)
    return prepTweet

consumer_key = '5EoGuierDVOdYhvbEZVDsN9jO'
consumer_secret = 'eA227eYOJ8T7hmK46sbZ5ONPQYxxYC3FKyrRM6Lfl3ym4wZAUG'
access_token = '1022656334-bVOQBtPzSHVfaru7mQf2l9FuwmTOlynGdEEx49P'
access_token_secret = 'N5G428M0NxMDwvv0Iv3C8cZgsblTBJhsaVW0uzrfz65dy'

auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
auth.set_access_token(access_token, access_token_secret)
cleanedTweets = ""
api = tweepy.API(auth)


tweets = api.user_timeline(sys.argv[1], count=100)
for tweet in tweets:
    cleanedTweet = clearTweet(tweet.text)
    for cleaned in cleanedTweet:
        print(cleaned + "\n")
        cleanedTweets+=cleaned + " "

now = datetime.datetime.now()
now = str(now)
user1 = {'username':sys.argv[1], 'last_analysis':now, 'tweets':cleanedTweets}
mycol1.insert_one(user1)