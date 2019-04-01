import re
from collections import Counter
import liwc
import sys

categoryCounts = dict()


def tokenize(text):
    # you may want to use a smarter tokenizer
    for match in re.finditer(r'\w+', text, re.UNICODE):
        yield match.group(0)

def addCategory(category):

    if category[0] not in categoryCounts:
        categoryCounts[category[0]] = category[1]
    else:
        categoryCounts[category[0]] = categoryCounts[category[0]] + category[1]



print("girdim")
parse = None

parse, category_names = liwc.load_token_parser('src/main/LIWC_Turkish.dic')
gettysburg_counts = None

print("girdim")
for tweet in sys.argv[1]:
    words = tweet.split()
    for eachWord in words:  ## each word in a tweet
        optionalWords = eachWord.split("|")
        for eachOptionalWord in optionalWords:  #each options of a word. It will run till find a option which belongs to any category.
            category_tokens = tokenize(eachOptionalWord)    #categories of the option
            gettysburg_counts = Counter(category for token in category_tokens for category in parse(token))
            if len(gettysburg_counts) > 0:
                for cat in gettysburg_counts.items():
                    addCategory(cat)
                break;

print(categoryCounts)

