import tweepy
import sys
import datetime
import pymongo

myclient = pymongo.MongoClient(
    "mongodb+srv://ismailyankayis:2430zcbg@twitterpersonalityanalyzer-aeniz.mongodb.net/admin")
mydb = myclient["TwitterPersonalityAnalyzerDB"]
mycol1 = mydb["User"]

consumer_key = '5EoGuierDVOdYhvbEZVDsN9jO'
consumer_secret = 'eA227eYOJ8T7hmK46sbZ5ONPQYxxYC3FKyrRM6Lfl3ym4wZAUG'
access_token = '1022656334-bVOQBtPzSHVfaru7mQf2l9FuwmTOlynGdEEx49P'
access_token_secret = 'N5G428M0NxMDwvv0Iv3C8cZgsblTBJhsaVW0uzrfz65dy'

auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
auth.set_access_token(access_token, access_token_secret)
cleanedTweets = ""
api = tweepy.API(auth)

tweetList = list()
tweets = api.user_timeline(sys.argv[1], count=100)

for tweet in tweets:
    tweetList.append(tweet.text)

now = datetime.datetime.now()
now = str(now)
user1 = {'username': sys.argv[1], 'last_analysis': now, 'tweets': tweetList }

user = mycol1.find({"username": sys.argv[1]})
if user.count() is not 0:
    updateDoc = {"username": sys.argv[1]}
    newTweets = {"$set": {"tweets": tweetList}}
    doc = mycol1.update_one(updateDoc, newTweets)
    print("Updated: " + str(updateDoc))
else:

    mycol1.insert_one(user1)
    print("Inserted: " + str(user1))
