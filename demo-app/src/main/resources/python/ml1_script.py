# Required imports
import pandas as pd
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import classification_report
from sklearn.model_selection import train_test_split

# Input parameters from Java via SpEL expressions
request = spel{#request}
data = request['data']
labels = request['labels']
test_size = request['testSize']
random_state = request['randomState']
max_depth = request['maxDepth']
n_estimators = request['nEstimators']

X = pd.DataFrame(data)
y = pd.Series(labels)

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=test_size, random_state=random_state)

model = RandomForestClassifier(max_depth=max_depth, n_estimators=n_estimators, random_state=random_state)
model.fit(X_train, y_train)

predictions = model.predict(X_test)
report = classification_report(y_test, predictions, output_dict=True)

# Output the result back to Java
result = {
    'accuracy': report['accuracy'],
    'macro_avg': report['macro avg'],
    'model_info': {
        'type': 'RandomForestClassifier',
        'max_depth': max_depth,
        'n_estimators': n_estimators
    }
}

o4java{result}