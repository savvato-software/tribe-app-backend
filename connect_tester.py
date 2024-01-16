# This is a sample Python script.

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.

# !/usr/bin/env python
import requests
import random
import sys

AUTH_HEADERS = {}
CONNECTED = set()
AUTH_URL = 'http://localhost:8080/api/public/login'
CONNECT_URL = 'http://localhost:8080/api/connect/'
MIN_ID, MAX_ID = 1, 6
NUMBER_OF_CONNECTIONS_TO_MAKE = 0


def calculateMaxConnections(total, additional_connections):
    if additional_connections == 0:
        return total
    return calculateMaxConnections(total + additional_connections, additional_connections - 1)


def getNumberOfConnections(max_connections):
    global MAX_ID
    global MIN_ID
    global NUMBER_OF_CONNECTIONS_TO_MAKE


    if len(sys.argv) < 2:
        print('Please supply the number of connections you would like (maximum is %(max_connections)i).' % {
            "max_connections": max_connections})
        sys.exit(1)
    if not sys.argv[1].isnumeric():
        print('Please supply an integer number of connections you would like (maximum is %(max_connections)i).' % {
            "max_connections": max_connections})
        sys.exit(1)
    if int(sys.argv[1]) > max_connections:
        print('Too many connections requested; maximum is %(max_connections)i.' % {
            "max_connections": max_connections})
        sys.exit(1)
    NUMBER_OF_CONNECTIONS_TO_MAKE = int(sys.argv[1])


def authorize():
    global AUTH_HEADERS
    print("Authorizing...")
    credentials = {'email': 'admin@tribeapp.com', 'password': 'admin'}
    response = requests.post(AUTH_URL, json=credentials)
    if 'authorization' in response.headers:
        AUTH_HEADERS = {'Authorization': "Bearer " + response.headers.get('authorization')}
        print("Authorized successfully.")
    else:
        print("Failed to authorize. Exiting early.")
        sys.exit(1)


def welcome():
    global MIN_ID
    global MAX_ID
    max_connections = calculateMaxConnections(0, MAX_ID - MIN_ID)
    getNumberOfConnections(max_connections)
    info = {"min_id": MIN_ID, "max_id": MAX_ID, "auth_url": AUTH_URL, "connect_url": CONNECT_URL, "max_connections": max_connections}

    print("NOTE: at this time, the program uses the following assumptions:")
    print('1. Valid user IDs are between %(min_id)i and %(max_id)i, inclusive.' % info)
    print('2. The maximum number of unique connections that can be made is: %(max_connections)i ' % info)
    print('3. The authorization URL is %(auth_url)s' % info)
    print('4. The connection endpoint URL is %(connect_url)s' % info)
    print("If any of the above information is no longer accurate, please adjust the global variables at the start of "
          "the script.")
    print("-" * 90)
    authorize()


def generateQrCode(user_id):
    url = CONNECT_URL + str(user_id)
    response = requests.get(url, headers=AUTH_HEADERS)
    if response.status_code != 200:
        print("Failed to retrieve QR Code")
        sys.exit(1)
    return response.text


def generateIds():
    global CONNECTED
    global MIN_ID
    global MAX_ID
    requesting_user_id, to_be_connected_with_user_id = random.randint(MIN_ID, MAX_ID), random.randint(MIN_ID, MAX_ID)
    to_be_connected_with_user_id = random.randint(MIN_ID, MAX_ID)
    while (requesting_user_id == to_be_connected_with_user_id
           or (requesting_user_id, to_be_connected_with_user_id) in CONNECTED
           or (to_be_connected_with_user_id, requesting_user_id) in CONNECTED):
        requesting_user_id, to_be_connected_with_user_id = random.randint(MIN_ID, MAX_ID), random.randint(MIN_ID,
                                                                                                          MAX_ID)
    CONNECTED.add((requesting_user_id, to_be_connected_with_user_id))
    return requesting_user_id, to_be_connected_with_user_id


def createConnections():
    global AUTH_HEADERS
    global CONNECTED
    global NUMBER_OF_CONNECTIONS_TO_MAKE
    print("Will be generating", NUMBER_OF_CONNECTIONS_TO_MAKE, "connections.")
    while len(CONNECTED) < NUMBER_OF_CONNECTIONS_TO_MAKE:
        requesting_user_id, to_be_connected_with_user_id = generateIds()
        qr_code_phrase = generateQrCode(to_be_connected_with_user_id)
        connect_request = {"requestingUserId": requesting_user_id,
                           "toBeConnectedWithUserId": to_be_connected_with_user_id, "qrcodePhrase": qr_code_phrase}

        response = requests.post(CONNECT_URL, headers=AUTH_HEADERS, json=connect_request)
        if response.status_code != 200 or response.text == "false":
            print("Failed to make a connection")
            sys.exit(1)
        CONNECTED.add((requesting_user_id, to_be_connected_with_user_id))


def displayConnections():
    global CONNECTED
    print("Connections have been made. The connected user IDs are as follows:")
    while len(CONNECTED):
        requesting_user_id, to_be_connected_with_user_id = CONNECTED.pop()
        print('User ID %(requester)i successfully requested a connection with user ID %(target)i.' % {
            "requester": requesting_user_id, "target": to_be_connected_with_user_id})


if __name__ == '__main__':
    welcome()
    createConnections()
    displayConnections()
# See PyCharm help at https://www.jetbrains.com/help/pycharm/
