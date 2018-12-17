# Estimation: before looking at the old code:
### how long do you think it will take you to complete this new feature?
Probably 30 min.
### How many files will you need to add or update? Why?
I have to add two methods to the Direct class that calls a new method on the TurtleManager. I probably have to edit the TurtleManager as well.



# Review: after completing the feature:
### how long did it take you to complete this new feature?
One and half hours.
### how many files did you need to add or update? Why?
I updated six or seven files. I did not realize most of the changes occur in the front end and not back end. Now I think about it, back then we did decide to remove any graphical components from the backend. Therefore our TurtleManager does not have any information regarding the images of the turtles. I had to read through the frontend code and updated a few event listeners and made a lot of changes to maintain the encapsulation of backend from graphical stuff.
### did you get it completely right on the first try?
No. I was not familiar with this part of the code at all.
### Analysis: what do you feel this exercise reveals about your project's design and documentation?
Documentation at frontend is almost absent, so... I still like our design though. It's good that after my modification, backend and frontend are still well encapsulated.
### was it as good (or bad) as you remembered?
Around the same.
### what could be improved?
A more consistent event handling system. A better controller module that could pass information between frontend and backend.
### what would it have been like if you were not familiar with the code at all?
Indeed I am not familiar with the code at all, so... Reading through others' code after such a long time indeed made me see the necessity of documentation.