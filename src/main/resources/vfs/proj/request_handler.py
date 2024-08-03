import polyglot


import requests

class RequestHandler:
    def get(self):
        data = requests.get("https://jsonplaceholder.typicode.com/posts/1")
        print(data.json())

# We export the PyHello class to Java as our explicit interface with the Java side
polyglot.export_value("RequestHandler", RequestHandler)