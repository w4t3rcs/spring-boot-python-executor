# Required imports
import numpy as np
from sklearn.feature_extraction.text import CountVectorizer

# Simple sentiment analysis using predefined positive and negative words
positive_words = ['good', 'great', 'excellent', 'amazing', 'wonderful', 'best', 'love']
negative_words = ['bad', 'terrible', 'awful', 'worst', 'hate', 'poor', 'disappointing']

# Get the input text from Java
text = spel{#text}.lower()

# Count positive and negative words
positive_count = sum(1 for word in positive_words if word in text)
negative_count = sum(1 for word in negative_words if word in text)

# Calculate sentiment score (-1 to 1)
total = positive_count + negative_count
if total == 0:
    sentiment = 0
else:
    sentiment = (positive_count - negative_count) / total

# Return the sentiment score to Java
o4java{sentiment}