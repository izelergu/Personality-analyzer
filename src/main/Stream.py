import tweepy
import json
import pymongo

myclient = pymongo.MongoClient("mongodb+srv://ismailyankayis:2430zcbg@twitterpersonalityanalyzer-aeniz.mongodb.net/admin")
mydb = myclient["TwitterPersonalityAnalyzerDB"]
mycol1 = mydb["Tweets"]
# Specify the account credentials in the following variables:
consumer_key = '5EoGuierDVOdYhvbEZVDsN9jO'
consumer_secret = 'eA227eYOJ8T7hmK46sbZ5ONPQYxxYC3FKyrRM6Lfl3ym4wZAUG'
access_token = '1022656334-bVOQBtPzSHVfaru7mQf2l9FuwmTOlynGdEEx49P'
access_token_secret = 'N5G428M0NxMDwvv0Iv3C8cZgsblTBJhsaVW0uzrfz65dy'

file_object= open("C:\\Users\\izele\\Desktop\\inserted_id.txt", "w+", encoding = "utf-8")
# This listener will print out all Tweets it receives
class PrintListener(tweepy.StreamListener):
    def on_data(self, data):
        # Decode the JSON data
        tweet = json.loads(data)

        # Print out the Tweet
        #file_object.write('@%s: %s' (tweet['user']['screen_name'], tweet['text']))
        if '"lang":"tr"' in data:
            tweet = json.loads(data.encode('iso-8859-1'))
            #file_object.write(str(tweet))
            tweets= mycol1.insert_one(tweet)
            file_object.write(str(tweets.inserted_id))

    def on_error(self, status):
        print('error code: %s' % status)

if __name__ == '__main__':
    listener = PrintListener()

    # Authenticate
    auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
    auth.set_access_token(access_token, access_token_secret)

    # Connect the stream to our listener
    stream = tweepy.Stream(auth, listener)
    stream.filter(track=['ben', 'sen', 'o', 'biz', 'siz', 'onlar', 'acı', 'gerçek', 'mutlu', 'hüzün', 'sevinç', 'sevgi'])