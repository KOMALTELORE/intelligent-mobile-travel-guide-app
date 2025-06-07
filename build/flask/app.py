from flask import Flask, request, jsonify
import firebase_admin
from firebase_admin import credentials, db
import sys

# Ensure Python can find model.py
sys.path.append(".")
from pre_model import recommend_places

app = Flask(__name__)

# Firebase Configuration (commented out for testing)
# cred = credentials.Certificate("path/to/your/firebase-credentials.json")
# firebase_admin.initialize_app(cred, {
#     'databaseURL': 'https://your-database-url.firebaseio.com/'
# })

@app.route('/', methods=['POST'])
def get_suggestions():
    data = request.get_json()
    # ... your existing logic ...
    
    recommended_places = recommend_places({
        "preferences": preferences,
        "history": history
    })
    
    # Convert to list of objects
    places = []
    for place in recommended_places:
        places.append({
            "name": place["name"],  # or however you access name
            "description": place.get("description", ""),
            "imageUrl": place.get("image_url", ""),
            # other fields...
        })
    
    return jsonify({
        "recommendations": places  # Note the exact field name must match
    })

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)