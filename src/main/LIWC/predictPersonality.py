import pandas as pd
from sklearn.externals import joblib
import pymongo
import sys


def main():
    myclient = pymongo.MongoClient(
        "mongodb+srv://ismailyankayis:2430zcbg@twitterpersonalityanalyzer-aeniz.mongodb.net/admin")
    mydb = myclient["TwitterPersonalityAnalyzerDB"]
    col_user = mydb["User"]
    col_result = mydb["Result"]
    user = col_user.find_one({"username": sys.argv[1]})
    if user is not None:
        # Load Prediction Models
        extraversion_tree = joblib.load('src/main/LIWC/models/extraversion_tree_model.mdl')
        aggreeableness_knn = joblib.load('src/main/LIWC/models/aggreeableness_knn_model.mdl')
        concientiousnes_random_forest = joblib.load('src/main/LIWC/models/concientiousnes_random_forest_model.mdl')
        opennes_tree = joblib.load('src/main/LIWC/models/opennes_tree_model.mdl')
        neuroticism_knn = joblib.load('src/main/LIWC/models/neuroticism_knn_model.mdl')

        # Get groups of the user and insert them into a data frame
        groups = user['groups']
        df = pd.read_csv("src/main/LIWC//models/empty_data.csv", index_col='index')
        for group in groups:
            splited = group.split(',')
            df[splited[0]] = float(splited[1])

        # Prediction of Personalities
        print (len(df.columns))
        extraversion_value = extraversion_tree.predict(df)
        aggreeableness_value = aggreeableness_knn.predict(df)
        concientiousnes_value = concientiousnes_random_forest.predict(df)
        opennes_value = opennes_tree.predict(df)
        neuroticism_value = neuroticism_knn.predict(df)

        # Predicted Personalities are updated on DB
        result = col_result.find({'username': sys.argv[1]})
        if result.count() is not 0:
            updateDoc = {"username": sys.argv[1]}
            results = {"$set": {"opennes": opennes_value[0], "extraversion": extraversion_value[0], "neuroticism": neuroticism_value[0], "conscientiousness": concientiousnes_value[0], "aggreeableness": aggreeableness_value[0]}}
            doc = col_result.update_one(updateDoc, results)
            print("Result Updated: " + str(updateDoc))
        else:
            results = {"username": sys.argv[1], "opennes": opennes_value[0], "extraversion": extraversion_value[0],
                                "neuroticism": neuroticism_value[0], "conscientiousness": concientiousnes_value[0],
                                "aggreeableness": aggreeableness_value[0]}
            col_result.insert_one(results)
            print("Result Inserted: " + str(results))
    else:
        print("There is no user as " + sys.argv[1])


if __name__ == '__main__':
    main()