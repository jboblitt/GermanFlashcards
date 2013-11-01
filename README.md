GermanFlashcards
================

Android application that allows the user to create flashcards.  

Details
------

Version 1.0.0 is a skeleton, proof of concept release that provides a hardcoded set of decks for the user to navigate through.  This application uses the accelerometer to flip flash cards, the microphone to record a user's pronunciation of the words, and a camera to take pictures of objects to associate with the flashcards.

Webserver
------

This application uses a Jetty webserver that allows clients to save their decks.  Each client can also request all decks from the webserver.  Currently, a user may only see that other decks exist and the first level of those decks.  In later releases the user will be able to select a deck from the server pool and save the deck as if it were his/her own.   
