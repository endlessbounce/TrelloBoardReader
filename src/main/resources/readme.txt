Trello data reader by endlessbouce. V1.3-2019/05/08

The program requires from you to provide a *.properties file.
It fetches cards (names, descriptions and comments) of all lists from boards listed in the file,
and writes it into files (.txt and .json) or into a zip file, destination of which you specify.
The file must contain your key and token to access necessary account, and a list of boards' IDs.

Required properties: 
 - app_output_path (e.g. app_output_path:d:/some/pack.zip)
 - app_token
 - app_key
 - board names (e.g. board1=***)

Change log:
 - 1.3 (05.08.19) - added support to pack the result into a zip file
--------------------------
 - 1.2 (02.02.19) - added support to also write boards into .json files
--------------------------
 - 1.1 - read boards to .txt files
 
 