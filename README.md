# Secret Santa :santa: :gift:
### client-server console app

=> `register <username> <password>` # introduces a new user to the system and automatically logs the user in

=> `login <username> <password>` # the command will be successful for already existing (registered) users, witch have previously logged themselves out;
it is an important prerequisite for a user to be logged in, to be able to post and get wishes;

=> `logout <username>` # changes the status of the given user

=> `post-wish <usernameReciver> <wish> <usernameCreator>` # for this command to be successful, there are some conditions, which need to be satisfied:
1. Both the "Receiver" of the gift and the Creator of the wish need to be registered in the system.
2. The Creator of the wish has to be logged in to obtain permission for posting wishes.
3. The Creator of the wish can post wishes for himself as well as for other existing users.
4. Only one wish at a time. If a user has multiple added wishes, they will be printed as a list afterwords.

=> `get-wish <usernameRequestor>` #  returns a user and all added wishes of that user at a random manner;
the only condition is that the user must be different from the one making the get-request;
if this condition cannot be satisfied (that is the only user registered), the command will not be successful and will return an appropriate explanation;

=> `disconnect` # breaks the connection of the client to the server
