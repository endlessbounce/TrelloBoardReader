# Trello Board Reader

## Description
This application is a console utility program which uses Trello API. You might make use of it if you want to create a local copy 
of your Trello data. It fetches names, descriptions and comments of cards from boards listed in the properties file,
then writes them into .json files and zips them to the destination you specify.

## Requirements
The program requires from you to provide a .properties file.
The file must contain your Developer API key, a token to access necessary Trello account, and a list of boards' IDs
```
#access data
app_key=someKey
app_token=someToken
app_output_path:d:/some/pack.zip

#boards' IDs
board1=someID1
board2=someID2
```

## Documentation
* [Trello API-Docs](https://developers.trello.com/) - here you can find out how to get your Developer API key and token, 
and learn more about URLs used to access Trello web recourses.
