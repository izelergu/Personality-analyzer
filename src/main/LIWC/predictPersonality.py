import pandas as pd
from sklearn.externals import joblib
import pymongo
import sys


def predictExtraversion(groups):
    extraversion_nn = joblib.load('src/main/LIWC/models/extraversion_nn.mdl')
    df = pd.read_csv("src/main/LIWC//models/empty_extraversion.csv")

    for group in groups:
        splited = group.split(',')
        if any(splited[0] is col for col in df.columns):
            df[splited[0]] = splited[1]
    return extraversion_nn.predict(df)


def predictAggreableness(groups):
    aggreeableness_adaboost = joblib.load('src/main/LIWC/models/aggreableness_adaboost.mdl')
    df = pd.read_csv("src/main/LIWC//models/empty_aggreableness.csv")

    for group in groups:
        splited = group.split(',')
        if any(splited[0] is col for col in df.columns):
            df[splited[0]] = splited[1]
    return aggreeableness_adaboost.predict(df)


def predictConscientiousness(groups):
    concientiousnes_sgd = joblib.load('src/main/LIWC/models/conscientiousness_SGD.mdl')
    df = pd.read_csv("src/main/LIWC//models/empty_conscientiousness.csv")

    for group in groups:
        splited = group.split(',')
        if any(splited[0] is col for col in df.columns):
            df[splited[0]] = splited[1]
    return concientiousnes_sgd.predict(df)


def predictOpenness(groups):
    opennes_gbc = joblib.load('src/main/LIWC/models/openness_GBC.mdl')
    df = pd.read_csv("src/main/LIWC//models/empty_opennes.csv")

    for group in groups:
        splited = group.split(',')
        if any(splited[0] is col for col in df.columns):
            df[splited[0]] = splited[1]
    return opennes_gbc.predict(df)


def predictNeuroticism(groups):
    neuroticism_sgd = joblib.load('src/main/LIWC/models/Neuroticism_SGD.mdl')
    df = pd.read_csv("src/main/LIWC//models/empty_opennes.csv")

    for group in groups:
        splited = group.split(',')
        if any(splited[0] is col for col in df.columns):
            df[splited[0]] = splited[1]
    return neuroticism_sgd.predict(df)


def main():
    myclient = pymongo.MongoClient(
        "mongodb+srv://ismailyankayis:2430zcbg@twitterpersonalityanalyzer-aeniz.mongodb.net/admin")
    mydb = myclient["TwitterPersonalityAnalyzerDB"]
    col_user = mydb["User"]
    col_result = mydb["Result"]
    user = col_user.find_one({"username": sys.argv[1]})
    if user is not None:
        # Get groups of the user and insert them into a data frame
        groups = user['groups']

        # Predicted Personalities are updated on DB
        result = col_result.find({'username': sys.argv[1]})
        results = {"username": sys.argv[1], "opennes": predictOpenness(groups)[0], "extraversion": predictExtraversion(groups)[0],
                            "neuroticism": predictNeuroticism(groups)[0], "conscientiousness": predictConscientiousness(groups)[0],
                            "aggreeableness": predictConscientiousness(groups)[0]}
        resuld_id = col_result.insert_one(results)
        print("result_id:" + str(resuld_id.inserted_id))
        print("Result Inserted: " + str(results))
    else:
        print("There is no user as " + sys.argv[1])


if __name__ == '__main__':
    main()