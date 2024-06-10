import os
import json
import urllib3
from openai import OpenAI
from pymongo.mongo_client import MongoClient
from pymongo.server_api import ServerApi

mongo_uri = os.environ.get("MONGO_URI")

def lambda_handler(event, context):
    # Fetch user's profile data
    request_event = json.loads(event["body"])
    category_id = request_event["category_id"]
    category = request_event["category"]
    prompt = request_event["prompt"]
    
    # Create a new client and connect to the server
    client = MongoClient(mongo_uri, server_api=ServerApi('1'))
    
    # Name of database
    db = client.CategoryEmbedding

    # Name of collection
    collection = db.Embedding

    embedding = getEmbedding(prompt)
    document = {"category": category, "prompt": prompt, "embedding": embedding}
    
    # Insert document
    result = collection.update_one({"category_id": category_id}, {"$set": document}, upsert=True)

    if result.acknowledged:
        return {
            "isBase64Encoded": False,
            "statusCode": 200,
            "headers": {},
            "multiValueHeaders": {},
            "body": "Successfully added the category."
        }
    else:
        return {
            "isBase64Encoded": False,
            "statusCode": 502,
            "headers": {},
            "multiValueHeaders": {},
            "body": "There was error during the insertion."
        }
    
    
def getEmbedding(document):
    open_ai_url = os.environ.get("OPENAI_URI")
    open_ai_key = os.environ.get("OPENAI_KEY")
    
    header = {
        'Content-Type': 'application/json',
        'Authorization': f'Bearer {open_ai_key}'
    }
    
    body = json.dumps({
        'input': f'{document}',
        'model': "text-embedding-3-large",
        'format': "float"
    })
    
    http = urllib3.PoolManager()
    response = http.request('POST', open_ai_url, body=body, headers=header)
    
    data = json.loads(response.data)
    
    return data['data'][0]['embedding']