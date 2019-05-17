import tweepy
import sys
import datetime
import pymongo

myclient = pymongo.MongoClient(
    "mongodb+srv://ismailyankayis:2430zcbg@twitterpersonalityanalyzer-aeniz.mongodb.net/admin")
mydb = myclient["TwitterPersonalityAnalyzerDB"]
col_User = mydb["User"]

consumer_key = '5EoGuierDVOdYhvbEZVDsN9jO'
consumer_secret = 'eA227eYOJ8T7hmK46sbZ5ONPQYxxYC3FKyrRM6Lfl3ym4wZAUG'
access_token = '1022656334-bVOQBtPzSHVfaru7mQf2l9FuwmTOlynGdEEx49P'
access_token_secret = 'N5G428M0NxMDwvv0Iv3C8cZgsblTBJhsaVW0uzrfz65dy'

auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
auth.set_access_token(access_token, access_token_secret)
api = tweepy.API(auth)

def main():
    tweetList = list()
    tweets = {}
    try:
        # Tweepy allows to max 200 tweet
        tweets = api.user_timeline(sys.argv[1], count=100)
    except tweepy.error.TweepError as e:
        print(e.response.text)
        if e.response.text == '{"errors":[{"code":34,"message":"Sorry, that page does not exist."}]}':
            print ("UserNotFound")
        elif e.response.text == '{"request":"\/1.1\/statuses\/user_timeline.json","error":"Not authorized."}':
            print("NotAuthorized")
        else:
            print ("UnknownError")
        return -1;

    last_tweet_date = str(tweets[0].created_at)
    first_tweet_date = str(tweets[len(tweets) - 1].created_at)
    for i in range(0,len(tweets)):
        tweetList.append(tweets[i].text)
    #for tweet in tweets:
     #   tweetList.append(tweet.text)

    now = datetime.datetime.now()
    now = str(now)

    updateDoc = {"username": sys.argv[1]}

    #Insert or Update the User
    user = col_User.find({"username": sys.argv[1]})
    if user.count() is not 0:
        newTweets = {"$set": {"tweets": tweetList, "last_analysis": now}}
        doc1 = col_User.update_one(updateDoc, newTweets)
        print("User Updated: " + str(updateDoc))
    else:
        user = {'username': sys.argv[1], 'last_analysis': now, 'tweets': tweetList, 'groups': list(), 'preprocessedTweets': list() }
        col_User.insert_one(user)
        print("User Inserted: " + str(updateDoc))



    print("Authorized")


if __name__ == '__main__':
    main()