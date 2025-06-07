import pandas as pd
import numpy as np
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.preprocessing import LabelEncoder
import joblib

# Load dataset
data = pd.read_csv('my_dataset.csv')

data['Type'] = data['Type'].fillna('Other')
data['City'] = data['City'].fillna('Unknown')
data['State'] = data['State'].fillna('Unknown')

# Encode categories
label_enc = LabelEncoder()
data['Type_encoded'] = label_enc.fit_transform(data['Type'])

data['combined_features'] = data['Type'] + " " + data['City'] + " " + data['State']
vectorizer = TfidfVectorizer()
feature_matrix = vectorizer.fit_transform(data['combined_features'])

similarity_matrix = cosine_similarity(feature_matrix)

# Save the vectorizer and the data for later use
joblib.dump(vectorizer, 'vectorizer.pkl')
data.to_csv('processed_data.csv', index=False)


def recommend_places(user_data):
    user_preferences = user_data.get('preferences', [])
    user_history = user_data.get('history', [])
    
    # Create a preference-based search string
    preference_query = ' '.join(user_preferences + user_history)
    
    # Load vectorizer
    vectorizer = joblib.load('vectorizer.pkl')
    data = pd.read_csv('processed_data.csv')

    # Convert preferences to vector
    preference_vector = vectorizer.transform([preference_query])
    
    # Compute similarity scores with all places
    scores = cosine_similarity(preference_vector, feature_matrix)[0]
    
    # Rank places by similarity
    ranked_indices = np.argsort(scores)[::-1]
    top_indices = ranked_indices[:15]

    recommendations = data.iloc[top_indices][['Name', 'City', 'State', 'Type']].to_dict(orient='records')

    return recommendations

print('Model and vectorizer saved successfully.')
