import tweepy
import re
import sys

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

api = tweepy.API(auth)

f = open("src/main/normalizedWords.txt", "w",  encoding='utf-8')

tweets = api.user_timeline(sys.argv[1], count=10)
for tweet in tweets:
    cleanedTweet = clearTweet(tweet.text)
    #print(cleanedTweet)
    for cleaned in cleanedTweet:
        f.write(cleaned + "\n")