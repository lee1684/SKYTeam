import os
import json
import urllib3
from openai import OpenAI
from pymongo.mongo_client import MongoClient
from pymongo.server_api import ServerApi

mongo_uri = os.environ.get("MONGO_URI")

def lambda_handler(event, context):
    # Fetch user's profile data
    user_id = int(event['queryStringParameters']['userID'])
    
    # Create a new client and connect to the server
    client = MongoClient(mongo_uri, server_api=ServerApi('1'))
    
    # Name of database
    db = client.UserEmbedding

    # Name of collection
    collection = db.Embedding

    query = { "user_id": user_id }
    projection = { "embedding": 1 }
    
    # Insert document
    result = collection.find_one(query, projection)
    if result == None:
        return {
            "isBase64Encoded": False,
            "statusCode": 400,
            "headers": {},
            "multiValueHeaders": {},
            "body": "There's no such user in DB."
        }
    
    query_embedding = result['embedding']
    
    pipeline = [
        {
            '$vectorSearch': {
                'index': 'CategoryVectorIndex',
                'path': 'embedding',
                'queryVector': query_embedding,
                'numCandidates': 100,
                'limit': 100
            }
        }, {
            '$project': {
                '_id': 0,
                'prompt': 0,
                'embedding': 0
            }    
        }
    ]
        
    results = client['CategoryEmbedding']['Embedding'].aggregate(pipeline)
    
    moim_list = []
    for search_result in results:
        moim_list.append(search_result['category_id'])
    
    if (len(moim_list) > 0):
        return {
            "isBase64Encoded": False,
            "statusCode": 200,
            "headers": {},
            "multiValueHeaders": {},
            "body": "[" + ','.join(str(e) for e in moim_list) + "]"
        }
    else:
        return {
            "isBase64Encoded": False,
            "statusCode": 502,
            "headers": {},
            "multiValueHeaders": {},
            "body": "Error occured during insertion."
        }
