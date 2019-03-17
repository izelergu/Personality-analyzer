import re
from collections import Counter
import liwc
import sys

def tokenize(text):
    # you may want to use a smarter tokenizer
    for match in re.finditer(r'\w+', text, re.UNICODE):
        yield match.group(0)
parse = None

parse, category_names = liwc.load_token_parser('src/main/LIWC2007_English100131.dic')

words = ""

for word in sys.argv[1]:
    words += word + " "

gettysburg_tokens = tokenize(words)
# now flatmap over all the categories in all of the tokens using a generator:
words_counts = Counter(category for token in gettysburg_tokens for category in parse(token))
# and print the results:
print(words_counts)
#database e nasıl yazdırcaz ?