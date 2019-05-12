import pandas as pd
import numpy as np
from sklearn.model_selection import LeaveOneOut
from sklearn.neighbors import KNeighborsClassifier
from sklearn.ensemble import RandomForestClassifier
from sklearn import tree
from sklearn.externals import joblib
import pickle


def trainKNN(x=None, y=None, n=None, metric=None, weight=None, output_filename='output_knn.mdl'):
    if x is None:
        print("x values cannot be null!")
        return -1
    if y is None:
        print("y values cannot be null!")
        return -1
    if metric is None:
        print("metric cannot be null!")
        return -1
    if weight is None:
        print("weight type cannot be null!")
        return -1

    knn = KNeighborsClassifier(n_neighbors=n, metric=metric, weights=weight)

    loo = LeaveOneOut()

    maxScore = -1.0
    maxX_train = []
    maxY_train = []
    sum_score = 0
    for train_index, test_index in loo.split(x):
        # Get train and test indexes
        X_train, X_test = x.iloc[train_index], x.iloc[test_index]
        y_train, y_test = y.iloc[train_index], y.iloc[test_index]
        knn.fit(X_train, y_train)
        score = knn.score(X_test, y_test)
        sum_score += score
        if score >= maxScore:
            maxScore = score
            maxX_train = X_train
            maxY_train = y_train

    knn.fit(maxX_train, maxY_train)
    print(knn)
    print("Mean Accuracy: " + str(sum_score/y.size))
    #pickle.dump(knn, open(output_filename, 'wb'))
    joblib.dump(knn, output_filename)
    return sum_score/y.size


def trainDecisionTree(x=None, y=None, min_sample_in_leaf=None, output_filename='output_decision_tree.mdl'):
    dt = tree.DecisionTreeClassifier(min_samples_leaf=min_sample_in_leaf)
    loo = LeaveOneOut()

    maxScore = -1.0
    maxX_train = []
    maxY_train = []
    sum_score = 0
    for train_index, test_index in loo.split(x):
        # Get train and test indexes
        X_train, X_test = x.iloc[train_index], x.iloc[test_index]
        y_train, y_test = y.iloc[train_index], y.iloc[test_index]
        dt.fit(X_train, y_train)
        score = dt.score(X_test, y_test)
        sum_score += score
        if score >= maxScore:
            maxScore = score
            maxX_train = X_train
            maxY_train = y_train

    dt.fit(maxX_train, maxY_train)
    print(dt)
    print("Mean Accuracy: " + str(sum_score / y.size))

    #tree.export_graphviz(dt, out_file='tree.dot', feature_names=x.columns)
    #pickle.dump(dt, open(output_filename, 'wb'))
    joblib.dump(dt, output_filename)
    return sum_score / y.size)


def trainRandomForest(x=None, y=None, number_of_trees=None, random_seed=None, output_filename='random_forest.mdl'):
    randomForest = RandomForestClassifier(n_estimators=number_of_trees, random_state=random_seed)
    loo = LeaveOneOut()

    maxScore = -1.0
    maxX_train = []
    maxY_train = []
    sum_score = 0
    for train_index, test_index in loo.split(x):
        # Get train and test indexes
        X_train, X_test = x.iloc[train_index], x.iloc[test_index]
        y_train, y_test = y.iloc[train_index], y.iloc[test_index]
        randomForest.fit(X_train, y_train)
        score = randomForest.score(X_test, y_test)
        sum_score += score
        if score >= maxScore:
            maxScore = score
            maxX_train = X_train
            maxY_train = y_train

    randomForest.fit(maxX_train, maxY_train)
    print(randomForest)
    print("Mean Accuracy: " + str(sum_score / y.size))

    #pickle.dump(randomForest, open(output_filename, 'wb'))
    joblib.dump(randomForest, output_filename)
    return sum_score / y.size


def main():
    data = pd.read_csv("src/main/LIWC/models/dataset.csv")
    X = data.iloc[:, 2:-5] # 0,1 are index and username columns, last five columns are targets
    trainKNN(x=X, y=data['Aggreeableness'], n=1, metric='chebyshev', weight='distance', output_filename='src/main/LIWC/models/aggreeableness_knn_model.mdl')
    trainDecisionTree(x=X, y=data['Extraversion'], min_sample_in_leaf=13, output_filename='src/main/LIWC/models/extraversion_tree_model.mdl')
    trainRandomForest(x=X, y=data['Concientiousnes'], number_of_trees=9, random_seed=13, output_filename='src/main/LIWC/models/concientiousnes_random_forest_model.mdl')
    trainKNN(x=X, y=data['Neuroticism'], n=2, metric='chebyshev', weight='distance', output_filename='src/main/LIWC/models/neuroticism_knn_model.mdl')
    trainDecisionTree(x=X, y=data['Opennes'], min_sample_in_leaf=6, output_filename='src/main/LIWC/models/opennes_tree_model.mdl')
    print('SUCCESSFUL!')


if __name__ == '__main__':
    main()